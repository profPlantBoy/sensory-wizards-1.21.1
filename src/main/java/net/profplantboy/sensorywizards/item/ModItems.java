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
import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {

    // Use a map to store and easily access your registered items.
    public static final Map<String, Item> WANDS = new LinkedHashMap<>();

    // Define and register the new custom creative tab
    public static final ItemGroup SENSORY_WIZARDS_TAB = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(SensoryWizards.MOD_ID, "sensory_wizards_tab"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup." + SensoryWizards.MOD_ID + ".sensory_wizards_tab"))
                    .icon(() -> new ItemStack(WANDS.get("wooden_wand"))) // Use the map to get the icon
                    .entries((context, entries) -> {
                        // Add all items from the map to the creative tab
                        WANDS.values().forEach(entries::add);
                    })
                    .build()
    );

    public static void registerModItems() {
        SensoryWizards.LOGGER.info("Registering Mod Items for " + SensoryWizards.MOD_ID);

        String[] wandTypes = {"wooden", "stone", "copper", "iron", "gold", "diamond", "netherite"};
        for (String type : wandTypes) {
            Item wand = Registry.register(Registries.ITEM, Identifier.of(SensoryWizards.MOD_ID, type + "_wand"),
                    new Item(new Item.Settings()));
            WANDS.put(type + "_wand", wand);
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            // Add all items from the map to the INGREDIENTS tab
            WANDS.values().forEach(fabricItemGroupEntries::add);
        });
    }
}