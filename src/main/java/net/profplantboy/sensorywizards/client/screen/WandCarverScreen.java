package net.profplantboy.sensorywizards.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import net.profplantboy.sensorywizards.screen.WandCarverScreenHandler;

public class WandCarverScreen extends HandledScreen<WandCarverScreenHandler> {
    public WandCarverScreen(WandCarverScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176; // vanilla container size
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        // center
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        // simple dark panel (no texture needed)
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Panel
        ctx.fill(x, y, x + backgroundWidth, y + backgroundHeight, 0xCC000000);
        ctx.drawBorder(x, y, backgroundWidth, backgroundHeight, 0x55FFFFFF);

        // Labels
        ctx.drawText(this.textRenderer, Text.literal("Wood"), x + 24, y + 8, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, Text.literal("Core"),  x + 44, y + 8, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, Text.literal("Focus"), x + 62, y + 8, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, Text.literal("Output"),x + 96, y + 8, 0xFFFFFF, false);

        // Button (simple rectangle; click logic is in mouseClicked)
        int bx = x + 120, by = y + 18, bw = 40, bh = 14;
        ctx.fill(bx, by, bx + bw, by + bh, 0xFF2E7D32); // green
        ctx.drawBorder(bx, by, bw, bh, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Carve"), bx + bw / 2, by + 3, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        int bx = x + 120, by = y + 18, bw = 40, bh = 14;
        if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh) {
            this.client.interactionManager.clickButton(this.handler.syncId, 0); // calls onButtonClick server-side
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
        ctx.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, this.playerInventoryTitle, 8, this.backgroundHeight - 96 + 2, 0xFFFFFF, false);
    }
}
