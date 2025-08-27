package net.profplantboy.sensorywizards.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.profplantboy.sensorywizards.client.gui.SpellSelectionScreen;
import net.profplantboy.sensorywizards.item.ModItems;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_SENSORYWIZARDS = "key.category.sensorywizards.main";
    public static final String KEY_OPEN_SPELL_GUI = "key.sensorywizards.open_spell_gui";

    public static KeyBinding openSpellGuiKey;
    private static boolean isGuiOpen = false; // Manages the state to prevent flickering

    public static void register() {
        openSpellGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_SPELL_GUI,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY_SENSORYWIZARDS
        ));
        registerKeyInputs();
    }

    private static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // This line is crucial: it syncs our flag if the screen is closed by other means (like ESC)
            if (!(client.currentScreen instanceof SpellSelectionScreen)) {
                isGuiOpen = false;
            }

            boolean holdingWand = ModItems.WANDS.containsValue(client.player.getMainHandStack().getItem());

            if (holdingWand && openSpellGuiKey.isPressed()) {
                if (!isGuiOpen) {
                    client.setScreen(new SpellSelectionScreen());
                    isGuiOpen = true;
                }
            } else {
                if (isGuiOpen) {
                    if (client.currentScreen instanceof SpellSelectionScreen) {
                        client.currentScreen.close();
                    }
                    // The flag will be reset on the next tick by our sync logic
                }
            }
        });
    }
}