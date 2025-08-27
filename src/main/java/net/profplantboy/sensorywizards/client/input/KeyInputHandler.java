package net.profplantboy.sensorywizards.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
// Import the new SpellWheelScreen
import net.profplantboy.sensorywizards.client.gui.SpellWheelScreen;
import net.profplantboy.sensorywizards.item.ModItems;
import net.profplantboy.sensorywizards.spell.LearnedSpellsComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

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

            boolean holdingWand = ModItems.WANDS.containsValue(client.player.getMainHandStack().getItem());

            // Use isPressed() for a hold-to-open mechanic, which feels best for wheel menus
            if (openSpellGuiKey.isPressed() && holdingWand) {
                // Open the wheel if it's not already open
                if (!(client.currentScreen instanceof SpellWheelScreen)) {
                    LearnedSpellsComponent component = ModComponents.LEARNED_SPELLS.get(client.player);
                    client.setScreen(new SpellWheelScreen(new ArrayList<>(component.getSpells())));
                }
            } else {
                // Close the wheel if the key is released
                if (client.currentScreen instanceof SpellWheelScreen) {
                    client.currentScreen.close();
                }
            }
        });
    }
}