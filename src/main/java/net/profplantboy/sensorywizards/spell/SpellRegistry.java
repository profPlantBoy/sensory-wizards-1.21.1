package net.profplantboy.sensorywizards.spell;

import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SpellRegistry {

    private static final Map<String, BiConsumer<PlayerEntity, World>> SPELL_ACTIONS = new HashMap<>();

    static {
        SPELL_ACTIONS.put("fireball", SpellRegistry::castFireball);
        SPELL_ACTIONS.put("ice_bolt", SpellRegistry::castIceBolt);
        SPELL_ACTIONS.put("healing_touch", SpellRegistry::castHealingTouch);
        // Add the new spell to the registry
        SPELL_ACTIONS.put("aguamenti", SpellRegistry::castAguamenti);
    }

    public static void castSpell(String spellId, PlayerEntity player, World world) {
        if (SPELL_ACTIONS.containsKey(spellId)) {
            SPELL_ACTIONS.get(spellId).accept(player, world);
        }
    }

    /**
     * Casts a simple fireball spell.
     * This creates a small fireball entity and launches it in the direction the player is looking.
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castFireball(PlayerEntity player, World world) {
        if (!world.isClient) {
            Vec3d lookVec = player.getRotationVector();
            SmallFireballEntity fireball = new SmallFireballEntity(world, player, lookVec);
            fireball.setPosition(player.getX(), player.getEyeY(), player.getZ());
            world.spawnEntity(fireball);
            player.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        }
    }

    /**
     * Casts an ice bolt spell.
     * This gives the player a short burst of Speed.
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castIceBolt(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1));
            player.playSound(SoundEvents.ENTITY_PLAYER_HURT_FREEZE, 1.0F, 1.0F);
        }
    }

    /**
     * Casts a healing touch spell.
     * This gives the player a short burst of Regeneration.
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castHealingTouch(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1));
            player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
        }
    }

    /**
     * Casts Aguamenti, creating a water source block.
     * This spell raycasts up to 20 blocks to find a target block.
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAguamenti(PlayerEntity player, World world) {
        if (!world.isClient) {
            // Raycast to find the block the player is looking at
            BlockHitResult hitResult = world.raycast(new RaycastContext(
                    player.getEyePos(),
                    player.getEyePos().add(player.getRotationVector().multiply(20)), // 20 is the range
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    player
            ));

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                // Get the position adjacent to the face of the block that was hit
                var targetPos = hitResult.getBlockPos().offset(hitResult.getSide());
                // Place the water block
                world.setBlockState(targetPos, Blocks.WATER.getDefaultState());
                player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
            }
        }
    }
}
