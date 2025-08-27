package net.profplantboy.sensorywizards.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.profplantboy.sensorywizards.client.gui.SpellSelectionScreen;
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

            if (openSpellGuiKey.wasPressed()) {
                boolean holdingWand = ModItems.WANDS.containsValue(client.player.getMainHandStack().getItem());

                if (holdingWand) {
                    if (client.currentScreen instanceof SpellSelectionScreen) {
                        client.currentScreen.close();
                    } else {
                        // 1. Get the component and the spells *before* creating the screen.
                        LearnedSpellsComponent component = ModComponents.LEARNED_SPELLS.get(client.player);
                        // 2. Pass the list of spells directly to the new screen.
                        client.setScreen(new SpellSelectionScreen(new ArrayList<>(component.getSpells())));
                    }
                }
            }
        });
    }
}