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
            if (client.player == null) {
                return;
            }

            // Check if the player is holding a wand in their main hand
            boolean holdingWand = ModItems.WANDS.containsValue(client.player.getMainHandStack().getItem());

            // If the key is being pressed and we are holding a wand...
            if (openSpellGuiKey.isPressed() && holdingWand) {
                // ...and the GUI is not already open, open it.
                if (!(client.currentScreen instanceof SpellSelectionScreen)) {
                    client.setScreen(new SpellSelectionScreen());
                }
            }
            // Otherwise, if our GUI is open and the condition to keep it open is no longer true...
            else if (client.currentScreen instanceof SpellSelectionScreen) {
                // ...close it.
                client.currentScreen.close();
            }
        });
    }
}