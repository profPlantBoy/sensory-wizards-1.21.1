// src/main/java/net/profplantboy/sensorywizards/item/ModItemGroups.java
package net.profplantboy.sensorywizards.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.profplantboy.sensorywizards.SensoryWizards;
import net.profplantboy.sensorywizards.block.ModBlocks;

public final class ModItemGroups {
    public static ItemGroup SENSORYWIZARDS;

    public static void register() {
        // Create the tab
        SENSORYWIZARDS = Registry.register(
                Registries.ITEM_GROUP,
                Identifier.of(SensoryWizards.MOD_ID, "sensorywizards"),
                ItemGroup.create(ItemGroup.Row.TOP, 0) // row+column in the tabs ribbon; tweak as you like
                        .icon(() -> new ItemStack(ModBlocks.WAND_CARVER_ITEM))
                        .displayName(Text.translatable("itemGroup.sensorywizards"))
                        .build()
        );

        // Add entries to it
        ItemGroupEvents.modifyEntriesEvent(SENSORYWIZARDS).register(entries -> {
            entries.add(ModBlocks.WAND_CARVER_ITEM);
            // add your other mod items/blocks here:
            // entries.add(ModItems.WANDS.get("wooden_wand"));
        });
    }
}
