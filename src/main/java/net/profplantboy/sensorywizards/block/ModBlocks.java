package net.profplantboy.sensorywizards.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.profplantboy.sensorywizards.SensoryWizards;
import net.profplantboy.sensorywizards.block.entity.WandCarverBlockEntity;

public final class ModBlocks {
    private ModBlocks() {}

    public static final WandCarverBlock WAND_CARVER = Registry.register(
            Registries.BLOCK,
            Identifier.of(SensoryWizards.MOD_ID, "wand_carver"),
            new WandCarverBlock(AbstractBlock.Settings.create()
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD))
    );

    public static final Item WAND_CARVER_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(SensoryWizards.MOD_ID, "wand_carver"),
            new BlockItem(WAND_CARVER, new Item.Settings())
    );

    public static final BlockEntityType<WandCarverBlockEntity> WAND_CARVER_ENTITY =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(SensoryWizards.MOD_ID, "wand_carver"),
                    FabricBlockEntityTypeBuilder.create(WandCarverBlockEntity::new, WAND_CARVER).build()
            );

    /** Call from your main mod initializer. */
    public static void register() {
        SensoryWizards.LOGGER.info("[sensorywizards] Registered Wand Carver block & BE.");
    }

    /** The block class (opens a screen, provides the BE). */
    public static class WandCarverBlock extends BlockWithEntity {
        public static final MapCodec<WandCarverBlock> CODEC = createCodec(WandCarverBlock::new);

        public WandCarverBlock(Settings settings) { super(settings); }

        // 1) Required by BlockWithEntity in 1.21+: provide a codec for state <-> json/nbt
        @Override
        protected MapCodec<? extends BlockWithEntity> getCodec() {
            return CODEC;
        }

        @Override
        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new WandCarverBlockEntity(pos, state);
        }

        @Override
        public BlockRenderType getRenderType(BlockState state) {
            // normal block model
            return BlockRenderType.MODEL;
        }

        @Override
        public ActionResult onUse(BlockState state, World world, BlockPos pos,
                                  PlayerEntity player, BlockHitResult hit) {
            if (!world.isClient) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof WandCarverBlockEntity carver) {
                    player.openHandledScreen(carver);
                }
            }
            return ActionResult.SUCCESS;
        }
        @Override
        protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
            if (state.getBlock() != newState.getBlock()) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof WandCarverBlockEntity carver) {
                    carver.dropContents(world, pos);
                }
                super.onStateReplaced(state, world, pos, newState, moved);
            }
        }

        // NOTE:
        // - Remove hasRandomTicks(...) — there is no overridable method with that signature here.
        // - Remove createBlockEntity(BlockPos, BlockState, BlockView) — not a valid override in 1.21.
    }
}
