// src/main/java/net/profplantboy/sensorywizards/screen/WandCarverScreen.java
package net.profplantboy.sensorywizards.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WandCarverScreen extends HandledScreen<WandCarverScreenHandler> {
    // use any vanilla texture for now; replace with your own later:
    private static final Identifier BG = Identifier.of("minecraft", "textures/gui/container/furnace.png");
    // furnace.png is 176x166. If you make your own, set these to its size.
    public WandCarverScreen(WandCarverScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        // place titles so they don't overlap
        this.titleX = 8;
        this.titleY = 6;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.y + this.backgroundHeight - 94 + 2; // typical layout
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        ctx.drawTexture(BG, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(ctx, mouseX, mouseY);
    }
}
