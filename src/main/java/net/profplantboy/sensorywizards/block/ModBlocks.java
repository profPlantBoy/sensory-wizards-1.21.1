package net.profplantboy.sensorywizards.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

public class ModBlocks {

    public static final Block TEST_BLOCK = registerBlock("test_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.BAMBOO_WOOD)));
// ABOVE IS A METHOD FOR CREATING A NEW BLOCK! THERE ARE TONS OF THINGS YOU CAN CALL ON LIKE HOW
    //WE CALLED .requiresTool() OR .sounds() YOU CAN MESS WITH THIS!

    // Helper Method
    // If you read my notes in my code I am learning to code so I mark things quite a bit
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(SensoryWizards.MOD_ID, name), block);
    }

    // Helper Method
    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(SensoryWizards.MOD_ID),
             new BlockItem(block, new Item.Settings()));
    }
    // Shows what is happening inside of log
    public static void registerModBlocks() {
        SensoryWizards.LOGGER.info("Registering Mod Blocks for " + SensoryWizards.MOD_ID);
// BELOW Adds the TEST_BLOCK to the BUILDING_BLOCKS creative tab!
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries ->
                entries.add(ModBlocks.TEST_BLOCK)
        );

    }

}
