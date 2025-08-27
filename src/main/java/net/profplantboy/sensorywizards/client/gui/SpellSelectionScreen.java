package net.profplantboy.sensorywizards.client.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.profplantboy.sensorywizards.network.SelectSpellPayload;

import java.util.ArrayList;
import java.util.List;

public class SpellSelectionScreen extends Screen {
    private final List<String> learnedSpells;
    private String hoveredSpell = null;
    private String selectedSpell = null;

    // The constructor now accepts the list of spells from the KeyInputHandler
    public SpellSelectionScreen(List<String> learnedSpells) {
        super(Text.of("Spell Selection"));
        this.learnedSpells = learnedSpells;
    }

    @Override
    protected void init() {
        super.init();
        // We no longer need to get the spells here, as they are passed in the constructor.
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Keep the background transparent
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
                String spellId = learnedSpells.get(i);
                // Use a translatable text component to get the spell's proper name
                Text spellText = Text.translatable("spell.sensorywizards." + spellId);
                int y = startY + i * 20;
                int textWidth = this.textRenderer.getWidth(spellText);
                int x = centerX - textWidth / 2;

                boolean hovered = mouseX >= x && mouseX <= x + textWidth && mouseY >= y && mouseY <= y + 10;
                if (hovered) {
                    this.hoveredSpell = spellId;
                    context.fill(x - 2, y - 2, x + textWidth + 2, y + 10, 0x55FFFFFF);
                }
                context.drawText(this.textRenderer, spellText, x, y, 0xFFFFFF, true);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // This makes the spell names function as buttons
        if (this.hoveredSpell != null) {
            this.selectedSpell = this.hoveredSpell;
            this.close(); // Close the screen once a spell is selected
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        // Only send the packet if a spell was successfully selected by clicking
        if (this.selectedSpell != null) {
            ClientPlayNetworking.send(new SelectSpellPayload(this.selectedSpell));
        }
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}