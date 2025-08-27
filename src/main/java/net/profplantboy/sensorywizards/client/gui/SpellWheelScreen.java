package net.profplantboy.sensorywizards.client.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.profplantboy.sensorywizards.network.SelectSpellPayload;

import java.util.List;

public class SpellWheelScreen extends Screen {
    private final List<String> learnedSpells;
    private String selectedSpell = null;
    private static final int WHEEL_RADIUS = 80;

    public SpellWheelScreen(List<String> learnedSpells) {
        super(Text.of("Spell Wheel"));
        this.learnedSpells = learnedSpells;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render a semi-transparent dark background
        context.fill(0, 0, this.width, this.height, 0x80000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        if (learnedSpells.isEmpty()) {
            String message = "You have not learned any spells.";
            int textWidth = this.textRenderer.getWidth(message);
            context.drawTextWithShadow(this.textRenderer, message, centerX - textWidth / 2, centerY - 4, 0xFFFFFF);
            return;
        }

        int numSpells = learnedSpells.size();
        double angleSlice = 2 * Math.PI / numSpells;

        // --- Determine which spell is selected ---
        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        int selectedIndex = -1;
        if (distance > 20) { // Inner dead zone to prevent accidental selection
            double mouseAngle = Math.atan2(dy, dx);
            if (mouseAngle < 0) {
                mouseAngle += 2 * Math.PI;
            }
            selectedIndex = (int) Math.round(mouseAngle / angleSlice) % numSpells;
        }

        // --- Draw the wheel and text ---
        for (int i = 0; i < numSpells; i++) {
            double angle = i * angleSlice;
            int x = centerX + (int) (WHEEL_RADIUS * Math.cos(angle));
            int y = centerY + (int) (WHEEL_RADIUS * Math.sin(angle));

            Text spellText = Text.translatable("spell.sensorywizards." + learnedSpells.get(i));
            int textWidth = this.textRenderer.getWidth(spellText);

            if (i == selectedIndex) {
                this.selectedSpell = learnedSpells.get(i);
                // Draw a highlight behind the selected spell
                context.fill(x - textWidth / 2 - 2, y - 5, x + textWidth / 2 + 2, y + 10, 0x55FFFFFF);
            }

            context.drawTextWithShadow(this.textRenderer, spellText, x - textWidth / 2, y, 0xFFFFFF);
        }

        // Draw the name of the currently selected spell in the center
        if (this.selectedSpell != null) {
            Text selectedText = Text.translatable("spell.sensorywizards." + this.selectedSpell);
            int selectedWidth = this.textRenderer.getWidth(selectedText);
            context.drawTextWithShadow(this.textRenderer, selectedText, centerX - selectedWidth / 2, centerY - 4, 0xFFFF55);
        }
    }

    @Override
    public void close() {
        // Send the selected spell when the screen closes
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
