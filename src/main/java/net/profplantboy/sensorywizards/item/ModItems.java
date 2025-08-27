package net.profplantboy.sensorywizards.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

public class ModItems {
                        //This is where we will start registering our items! YAY!

public static final Item WOODEN_WAND = registerItem("wooden_wand", new Item(new Item.Settings()));
// TO ADD ANOTHER ITEM YOU COPY THE LINE ABOVE BELOW THIS LINE AND CHANGE NAMES THROUGHOUT CODE!
    // ALSO MUST DO AN ENTRIES LINE AS WELL!
public static final Item STONE_WAND = registerItem("stone_wand", new Item(new Item.Settings()));
public static final Item COPPER_WAND = registerItem("copper_wand", new Item(new Item.Settings()));
public static final Item IRON_WAND = registerItem("iron_wand", new Item(new Item.Settings()));
public static final Item GOLD_WAND = registerItem("gold_wand", new Item(new Item.Settings()));
public static final Item DIAMOND_WAND = registerItem("diamond_wand", new Item(new Item.Settings()));
public static final Item NETHERITE_WAND = registerItem("netherite_wand", new Item(new Item.Settings()));

// Define and register the new custom creative tab
public static final ItemGroup SENSORY_WIZARDS_TAB = Registry.register(
        Registries.ITEM_GROUP,
        Identifier.of(SensoryWizards.MOD_ID, "sensory_wizards_tab"),
        FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup." + SensoryWizards.MOD_ID + ".sensory_wizards_tab"))
                .icon(() -> new ItemStack(WOODEN_WAND)) // Set the icon for the tab
                .entries((context, entries) -> {
                    // Add your items here
                    entries.add(WOODEN_WAND);
                })
                .build()
);


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SensoryWizards.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SensoryWizards.LOGGER.info("Registering Mod Items for " + SensoryWizards.MOD_ID);


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(WOODEN_WAND);
            fabricItemGroupEntries.add(STONE_WAND);
            fabricItemGroupEntries.add(COPPER_WAND);
            fabricItemGroupEntries.add(IRON_WAND);
            fabricItemGroupEntries.add(GOLD_WAND);
            fabricItemGroupEntries.add(DIAMOND_WAND);
            fabricItemGroupEntries.add(NETHERITE_WAND);
        });

    }
}
