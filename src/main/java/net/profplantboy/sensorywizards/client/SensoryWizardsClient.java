// src/main/java/net/profplantboy/sensorywizards/client/SensoryWizardsClient.java
package net.profplantboy.sensorywizards.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import net.profplantboy.sensorywizards.client.gui.SpellWheelScreen;
import net.profplantboy.sensorywizards.item.ModItems;
import net.profplantboy.sensorywizards.screen.WandCarverScreen;
import net.profplantboy.sensorywizards.screen.WandCarverScreenHandler;

public class SensoryWizardsClient implements ClientModInitializer {
    private static KeyBinding OPEN_SPELL_WHEEL;

    @Override
    public void onInitializeClient() {
        // 1) Existing: hook server-side handler to client-side screen.
        HandledScreens.register(WandCarverScreenHandler.TYPE, WandCarverScreen::new);

        // 2) New: register keybind (default V) for spell wheel.
        OPEN_SPELL_WHEEL = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sensorywizards.open_spell_wheel",   // translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.sensorywizards"         // category
        ));

        // 3) New: open SpellWheelScreen when pressed and holding your wand.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_SPELL_WHEEL.wasPressed()) {
                if (client == null || client.player == null) continue;

                boolean holdingWand =
                        client.player.getMainHandStack().isOf(ModItems.WAND) ||
                                client.player.getOffHandStack().isOf(ModItems.WAND);

                boolean noOtherScreen = client.currentScreen == null;

                if (holdingWand && noOtherScreen) {
                    client.setScreen(new SpellWheelScreen());
                }
            }
        });
    }
}
