package net.profplantboy.sensorywizards.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.profplantboy.sensorywizards.SensoryWizards;
import net.profplantboy.sensorywizards.block.ModBlocks;

public final class ModItemGroups {
    private ModItemGroups() {}

    // Use a RegistryKey for the tab
    public static final RegistryKey<ItemGroup> SENSORYWIZARDS_KEY =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(SensoryWizards.MOD_ID, "sensorywizards"));

    public static void register() {
        // Register the creative tab itself
        Registry.register(
                Registries.ITEM_GROUP,
                SENSORYWIZARDS_KEY,
                ItemGroup.create(ItemGroup.Row.TOP, 0)
                        .icon(() -> new ItemStack(ModBlocks.WAND_CARVER_ITEM))
                        .displayName(Text.translatable("itemGroup.sensorywizards"))
                        // Option A: add entries right in the builder (no event needed)
                        .entries((ctx, entries) -> {
                            entries.add(ModBlocks.WAND_CARVER_ITEM);
                            // entries.add(ModItems.WANDS.get("wooden_wand")); // add more here
                        })
                        .build()
        );

        // Option B (alternative): add entries via the event API.
        // If you use this, you can remove the .entries(...) block above.
        // ItemGroupEvents.modifyEntriesEvent(SENSORYWIZARDS_KEY).register(entries -> {
        //     entries.add(ModBlocks.WAND_CARVER_ITEM);
        // });
    }
}
