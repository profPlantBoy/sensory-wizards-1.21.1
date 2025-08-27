package net.profplantboy.sensorywizards.client.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.profplantboy.sensorywizards.network.SelectSpellPayload;
import net.profplantboy.sensorywizards.spell.LearnedSpellsComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;

import java.util.ArrayList;
import java.util.List;

public class SpellSelectionScreen extends Screen {
    // Make this final but initialize it in the init method
    private final List<String> learnedSpells = new ArrayList<>();
    private String hoveredSpell = null;

    public SpellSelectionScreen() {
        super(Text.of("Spell Selection"));
        // We no longer get the spells here, we wait until the screen is initialized
    }

    @Override
    protected void init() {
        super.init();
        // Get the learned spells here, when the player data is guaranteed to be available
        LearnedSpellsComponent component = ModComponents.LEARNED_SPELLS.get(this.client.player);
        if (component != null) {
            this.learnedSpells.addAll(component.getSpells());
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Do nothing to keep the background transparent
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hoveredSpell = null;

        int centerX = this.width / 2;
        int startY = this.height / 2 - (learnedSpells.size() * 20) / 2;

        if (learnedSpells.isEmpty()) {
            String message = "You have not learned any spells.";
            int textWidth = this.textRenderer.getWidth(message);
            context.drawText(this.textRenderer, message, centerX - textWidth / 2, this.height / 2 - 4, 0xFFFFFF, true);
        } else {
            for (int i = 0; i < learnedSpells.size(); i++) {
                String spell = learnedSpells.get(i);
                int y = startY + i * 20;
                int textWidth = this.textRenderer.getWidth(spell);
                int x = centerX - textWidth / 2;

                boolean hovered = mouseX >= x && mouseX <= x + textWidth && mouseY >= y && mouseY <= y + 10;
                if (hovered) {
                    this.hoveredSpell = spell;
                    context.fill(x - 2, y - 2, x + textWidth + 2, y + 10, 0x55FFFFFF);
                }
                context.drawText(this.textRenderer, spell, x, y, 0xFFFFFF, true);
            }
        }
    }

    @Override
    public void close() {
        if (this.hoveredSpell != null) {
            ClientPlayNetworking.send(new SelectSpellPayload(this.hoveredSpell));
        }
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}