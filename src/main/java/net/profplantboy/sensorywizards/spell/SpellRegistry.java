package net.profplantboy.sensorywizards.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SpellRegistry {

    private static final Map<String, BiConsumer<PlayerEntity, World>> SPELL_ACTIONS = new HashMap<>();

    static {
        SPELL_ACTIONS.put("fireball", SpellRegistry::castFireball);
        SPELL_ACTIONS.put("ice_bolt", SpellRegistry::castIceBolt);
        SPELL_ACTIONS.put("healing_touch", SpellRegistry::castHealingTouch);
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
            // Use the constructor that takes the owner (the player) and the velocity as a Vec3d
            SmallFireballEntity fireball = new SmallFireballEntity(world, player, lookVec);
            // Set the starting position to be in front of the player's eyes
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
}
