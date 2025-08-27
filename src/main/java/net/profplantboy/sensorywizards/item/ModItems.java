package net.profplantboy.sensorywizards.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;
import java.util.LinkedHashMap;
import java.util.Map;
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellComponent;

public class ModItems {

    // Use a map to store and easily access your registered items.
    public static final Map<String, Item> WANDS = new LinkedHashMap<>();

    // Register the SpellScroll item.
    public static final Item SPELL_SCROLL = registerItem("spell_scroll", new SpellScroll(new Item.Settings()));

    // Define and register the new custom creative tab
    public static final ItemGroup SENSORY_WIZARDS_TAB = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(SensoryWizards.MOD_ID, "sensory_wizards_tab"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup." + SensoryWizards.MOD_ID + ".sensory_wizards_tab"))
                    .icon(() -> new ItemStack(WANDS.get("wooden_wand"))) // Use the map to get the icon
                    .entries((context, entries) -> {
                        // Add all wand items from the map to the creative tab
                        WANDS.values().forEach(entries::add);

                        // Add different versions of the spell scroll
                        entries.add(createSpellStack("fireball"));
                        entries.add(createSpellStack("ice_bolt"));
                        entries.add(createSpellStack("healing_touch"));
                    })
                    .build()
    );

    private static ItemStack createSpellStack(String spellName) {
        ItemStack stack = new ItemStack(SPELL_SCROLL);
        stack.set(ModComponents.SPELL_COMPONENT, new SpellComponent(spellName));
        return stack;
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SensoryWizards.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SensoryWizards.LOGGER.info("Registering Mod Items for " + SensoryWizards.MOD_ID);

        String[] wandTypes = {"wooden", "stone", "copper", "iron", "gold", "diamond", "netherite"};
        for (String type : wandTypes) {
            Item wand = Registry.register(Registries.ITEM, Identifier.of(SensoryWizards.MOD_ID, type + "_wand"),
                    new Item(new Item.Settings()));
            WANDS.put(type + "_wand", wand);
        }

    }
}