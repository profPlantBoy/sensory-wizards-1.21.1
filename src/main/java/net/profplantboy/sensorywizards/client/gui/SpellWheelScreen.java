package net.profplantboy.sensorywizards.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.profplantboy.sensorywizards.config.SensoryWizardsConfig;
import net.profplantboy.sensorywizards.item.ModItems;
import net.profplantboy.sensorywizards.network.SelectSpellPayload;
import net.profplantboy.sensorywizards.spell.LearnedSpellsComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellCategories;
import net.profplantboy.sensorywizards.spell.SpellIds;

import java.util.*;

public class SpellWheelScreen extends Screen {
    private enum Mode { CATEGORY, SPELLS }
    private Mode mode = Mode.CATEGORY;
    private String currentCategory = null;

    // Layout
    private static final int PADDING = 10;
    private static final int ICON_SIZE = 20; // item drawn at 16px; cell is 20px
    private static final int GAP = 8;        // gap between icons
    private static final int COLS = 6;       // target columns (clamped)

    private final List<Entry> entries = new ArrayList<>();
    private Integer previousBlur = null; // Integer in your mappings (0..100)

    private static final class Entry {
        int x, y, w, h;
        Text label;
        Runnable onClick;
        ItemStack icon;
    }

    public SpellWheelScreen() {
        super(Text.translatable("screen.sensorywizards.spell_wheel"));
    }

    @Override
    protected void init() {
        super.init();
        // Disable menu background blur while this screen is open
        if (this.client != null && this.client.options != null && previousBlur == null) {
            previousBlur = this.client.options.getMenuBackgroundBlurriness().getValue(); // Integer
            this.client.options.getMenuBackgroundBlurriness().setValue(0); // no blur
        }
    }

    @Override
    public boolean shouldCloseOnEsc() { return true; }

    @Override
    public void close() {
        // Restore user's blur
        if (this.client != null && this.client.options != null && previousBlur != null) {
            this.client.options.getMenuBackgroundBlurriness().setValue(previousBlur);
            previousBlur = null;
        }
        if (this.client != null) this.client.setScreen(null);
    }

    @Override
    public void removed() {
        super.removed();
        // Safety: restore blur if close() wasn’t called
        if (this.client != null && this.client.options != null && previousBlur != null) {
            this.client.options.getMenuBackgroundBlurriness().setValue(previousBlur);
            previousBlur = null;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Entry e : entries) {
            if (mouseX >= e.x && mouseX <= e.x + e.w && mouseY >= e.y && mouseY <= e.y + e.h) {
                if (e.onClick != null) { e.onClick.run(); return true; }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 1 && mode == Mode.SPELLS) { // right click = back
            mode = Mode.CATEGORY;
            currentCategory = null;
            playClick();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client != null && this.client.options != null &&
                this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            close(); return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // No blur/dim overlay — leave the world/UI untouched
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        entries.clear();

        // Title
        final Text titleText = (mode == Mode.CATEGORY)
                ? Text.literal("Choose a Category")
                : Text.literal("Category: " + currentCategory);
        ctx.drawCenteredTextWithShadow(this.textRenderer, titleText, width / 2, PADDING, 0xFFFFFF);

        // Build icons/labels/actions
        final List<ItemStack> icons = new ArrayList<>();
        final List<Text> labels = new ArrayList<>();
        final List<Runnable> actions = new ArrayList<>();

        final SensoryWizardsConfig cfg = AutoConfig.getConfigHolder(SensoryWizardsConfig.class).getConfig();
        cfg.ensureDefaults(SpellIds.ALL);

        final Set<String> learned = new HashSet<>();
        if (this.client != null && this.client.player != null) {
            LearnedSpellsComponent comp = ModComponents.LEARNED_SPELLS.get(this.client.player);
            learned.addAll(comp.getSpells());
        }

        if (mode == Mode.CATEGORY) {
            for (Map.Entry<String, List<String>> cat : SpellCategories.MAP.entrySet()) {
                final String catName = cat.getKey();
                ItemStack icon = null;
                boolean hasAny = false;
                for (String id : cat.getValue()) {
                    if (cfg.isEnabled(id) && learned.contains(id)) {
                        icon = ModItems.makeSpellScrollStack(id);
                        hasAny = true; break;
                    }
                }
                if (!hasAny) continue; // hide empty categories
                if (icon == null) icon = new ItemStack(ModItems.SPELL_SCROLL);

                icons.add(icon);
                labels.add(Text.literal(catName));
                actions.add(() -> {
                    currentCategory = catName;
                    mode = Mode.SPELLS;
                    playClick();
                });
            }
        } else {
            final List<String> ids = SpellCategories.MAP.getOrDefault(currentCategory, List.of());
            for (String id : ids) {
                if (!cfg.isEnabled(id) || !learned.contains(id)) continue;
                final ItemStack icon = ModItems.makeSpellScrollStack(id);
                icons.add(icon);
                labels.add(Text.literal(id));
                actions.add(() -> {
                    final MinecraftClient mc = this.client;
                    if (mc != null && mc.player != null) {
                        ClientPlayNetworking.send(new SelectSpellPayload(id));
                        playClick();
                    }
                    close();
                });
            }
        }

        // ----- Pixel-perfect grid calculation -----
        final int usableWidth = Math.max(0, width - PADDING * 2);
        final int maxPerRow = Math.max(1, Math.min(COLS, Math.max(1, usableWidth / (ICON_SIZE + GAP))));
        final int colsUsed = Math.min(maxPerRow, Math.max(1, icons.size()));
        final int rows = (int) Math.ceil(icons.size() / (double) colsUsed);

        // exact pixel width/height (icons + gaps, no trailing gap)
        final int gridWpx = colsUsed * ICON_SIZE + Math.max(0, colsUsed - 1) * GAP;
        final int gridHpx = rows * ICON_SIZE + Math.max(0, rows - 1) * GAP;

        final int startX = (width - gridWpx) / 2;
        final int startY = Math.max(PADDING + 18, (height - gridHpx) / 2);

        // Panel behind grid
        if (!icons.isEmpty()) {
            final int panelPad = 6;
            final int panelX0 = startX - panelPad;
            final int panelY0 = startY - panelPad;
            final int panelX1 = startX + gridWpx + panelPad;
            final int panelY1 = startY + gridHpx + panelPad;
            ctx.fill(panelX0, panelY0, panelX1, panelY1, 0x66000000); // translucent dark
            ctx.drawBorder(panelX0, panelY0, panelX1 - panelX0, panelY1 - panelY0, 0x55FFFFFF);
        }

        // Build/draw entries; track hovered for single label under title
        Entry hovered = null;

        for (int i = 0; i < icons.size(); i++) {
            final int row = i / colsUsed;
            final int col = i % colsUsed;

            final int x = startX + col * (ICON_SIZE + GAP);
            final int y = startY + row * (ICON_SIZE + GAP);

            final Entry e = new Entry();
            e.x = x; e.y = y; e.w = ICON_SIZE; e.h = ICON_SIZE;
            e.icon = icons.get(i);
            e.label = labels.get(i);
            e.onClick = actions.get(i);
            entries.add(e);

            final boolean hover = mouseX >= e.x && mouseX <= e.x + e.w &&
                    mouseY >= e.y && mouseY <= e.y + e.h;
            if (hover) hovered = e;

            // draw icon and a neat border (solid on hover)
            ctx.drawItem(e.icon, e.x + 2, e.y + 2);
            ctx.drawBorder(e.x - 1, e.y - 1, e.w + 2, e.h + 2, hover ? 0xFFFFFFFF : 0x55FFFFFF);
        }

        // Single hover label below title
        if (hovered != null) {
            ctx.drawCenteredTextWithShadow(this.textRenderer, hovered.label,
                    width / 2, PADDING + 14, 0xFFEEDD);
        }

        // Back hint
        if (mode == Mode.SPELLS) {
            ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal("[Right-click] Back"),
                    width / 2, height - PADDING - 12, 0xAAAAAA);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void playClick() {
        if (this.client != null) {
            this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
}
