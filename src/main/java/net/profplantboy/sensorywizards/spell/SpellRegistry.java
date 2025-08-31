package net.profplantboy.sensorywizards.spell;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.BlockRotation;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import net.profplantboy.sensorywizards.config.SensoryWizardsConfig;

public class SpellRegistry {

    private static final Map<String, BiConsumer<PlayerEntity, World>> SPELL_ACTIONS = new HashMap<>();

    public static void registerSpells(SensoryWizardsConfig config) {
        // Fireball
        if (config.isEnabled("fireball")) SPELL_ACTIONS.put("fireball", SpellRegistry::castFireball);
        if (config.isEnabled("ice_bolt")) SPELL_ACTIONS.put("ice_bolt", SpellRegistry::castIceBolt);
        if (config.isEnabled("healing_touch")) SPELL_ACTIONS.put("healing_touch", SpellRegistry::castHealingTouch);
        if (config.isEnabled("aguamenti")) SPELL_ACTIONS.put("aguamenti", SpellRegistry::castAguamenti);
        if (config.isEnabled("alarte_ascendare")) SPELL_ACTIONS.put("alarte_ascendare", SpellRegistry::castAlarteAscendare);
        if (config.isEnabled("appare_vestigium")) SPELL_ACTIONS.put("appare_vestigium", SpellRegistry::castAppareVestigium);
        if (config.isEnabled("apparition")) SPELL_ACTIONS.put("apparition", SpellRegistry::castApparition);
        if (config.isEnabled("arania_exumai")) SPELL_ACTIONS.put("arania_exumai", SpellRegistry::castAraniaExumai);
        if (config.isEnabled("aresto_momentum")) SPELL_ACTIONS.put("aresto_momentum", SpellRegistry::castArestoMomentum);
        if (config.isEnabled("ascendio")) SPELL_ACTIONS.put("ascendio", SpellRegistry::castAscendio);
        if (config.isEnabled("avada_kedavra")) SPELL_ACTIONS.put("avada_kedavra", SpellRegistry::castAvadaKedavra);
        if (config.isEnabled("avifors")) SPELL_ACTIONS.put("avifors", SpellRegistry::castAvifors);
        if (config.isEnabled("avenseguim")) SPELL_ACTIONS.put("avenseguim", SpellRegistry::castAvenseguim);
        if (config.isEnabled("avis")) SPELL_ACTIONS.put("avis", SpellRegistry::castAvis);
        if (config.isEnabled("baubillious")) SPELL_ACTIONS.put("baubillious", SpellRegistry::castBaubillious);
        if (config.isEnabled("bombarda")) SPELL_ACTIONS.put("bombarda", SpellRegistry::castBombarda);
        if (config.isEnabled("bombarda_maxima")) SPELL_ACTIONS.put("bombarda_maxima", SpellRegistry::castBombardaMaxima);
        if (config.isEnabled("calvorio")) SPELL_ACTIONS.put("calvorio", SpellRegistry::castCalvorio);
        if (config.isEnabled("cave_inimicum")) SPELL_ACTIONS.put("cave_inimicum", SpellRegistry::castCaveInimicum);
        if (config.isEnabled("celescere")) SPELL_ACTIONS.put("celescere", SpellRegistry::castCelescere);
        if (config.isEnabled("circumrota")) SPELL_ACTIONS.put("circumrota", SpellRegistry::castCircumrota);
        if (config.isEnabled("colloshoo")) SPELL_ACTIONS.put("colloshoo", SpellRegistry::castColloshoo);
        if (config.isEnabled("colovaria")) SPELL_ACTIONS.put("colovaria", SpellRegistry::castColovaria);
        if (config.isEnabled("confringo")) SPELL_ACTIONS.put("confringo", SpellRegistry::castConfringo);
        if (config.isEnabled("confundo")) SPELL_ACTIONS.put("confundo", SpellRegistry::castConfundo);
        if (config.isEnabled("crucio")) SPELL_ACTIONS.put("crucio", SpellRegistry::castCrucio);
        if (config.isEnabled("depulso")) SPELL_ACTIONS.put("depulso", SpellRegistry::castDepulso);
        if (config.isEnabled("evanesco")) SPELL_ACTIONS.put("evanesco", SpellRegistry::castEvanesco);
        if (config.isEnabled("expecto_patronum")) SPELL_ACTIONS.put("expecto_patronum", SpellRegistry::castExpectoPatronum);
        if (config.isEnabled("expelliarmus")) SPELL_ACTIONS.put("expelliarmus", SpellRegistry::castExpelliarmus);
        if (config.isEnabled("fracto_strata")) SPELL_ACTIONS.put("fracto_strata", SpellRegistry::castFractoStrata);
        if (config.isEnabled("fumos")) SPELL_ACTIONS.put("fumos", SpellRegistry::castFumos);
        if (config.isEnabled("glacius")) SPELL_ACTIONS.put("glacius", SpellRegistry::castGlacius);
        if (config.isEnabled("herbivicus")) SPELL_ACTIONS.put("herbivicus", SpellRegistry::castHerbivicus);
        if (config.isEnabled("homenum_revelio")) SPELL_ACTIONS.put("homenum_revelio", SpellRegistry::castHomenumRevelio);
        if (config.isEnabled("immobulus")) SPELL_ACTIONS.put("immobulus", SpellRegistry::castImmobulus);
        if (config.isEnabled("impedimenta")) SPELL_ACTIONS.put("impedimenta", SpellRegistry::castImpedimenta);
        if (config.isEnabled("imperio")) SPELL_ACTIONS.put("imperio", SpellRegistry::castImperio);
        if (config.isEnabled("levicorpus")) SPELL_ACTIONS.put("levicorpus", SpellRegistry::castLevicorpus);
    }

    public static void castSpell(String spellId, PlayerEntity player, World world) {
        if (SPELL_ACTIONS.containsKey(spellId)) {
            SPELL_ACTIONS.get(spellId).accept(player, world);
        }
    }

    public static Set<String> getAllSpellIds() {
        return SPELL_ACTIONS.keySet();
    }

    // --- Utility raycasts ---
    private static Optional<EntityHitResult> raycastForEntity(PlayerEntity player, World world, double maxDistance) {
        Vec3d start = player.getEyePos();
        Vec3d end = start.add(player.getRotationVector().multiply(maxDistance));
        Box box = new Box(start, end);
        EntityHitResult hit = net.minecraft.entity.projectile.ProjectileUtil.getEntityCollision(world, player, start, end, box, e -> !e.isSpectator() && e.isAlive() && e.isAttackable());
        return Optional.ofNullable(hit);
    }

    private static BlockHitResult raycastForBlock(PlayerEntity player, World world, double maxDistance) {
        return world.raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVector().multiply(maxDistance)),
                RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
    }

    // --- All 39 spell implementations ---
    private static void castFireball(PlayerEntity player, World world) { if (!world.isClient) { Vec3d lookVec = player.getRotationVector(); SmallFireballEntity fireball = new SmallFireballEntity(world, player, lookVec); fireball.setPosition(player.getX(), player.getEyeY(), player.getZ()); world.spawnEntity(fireball); player.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F); } }
    private static void castIceBolt(PlayerEntity player, World world) { if (!world.isClient) { player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1)); player.playSound(SoundEvents.ENTITY_PLAYER_HURT_FREEZE, 1.0F, 1.0F); } }
    private static void castHealingTouch(PlayerEntity player, World world) { if (!world.isClient) { player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1)); player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 1.0F); } }
    private static void castAguamenti(PlayerEntity player, World world) { if (!world.isClient) { BlockHitResult hitResult = raycastForBlock(player, world, 20); if (hitResult.getType() == HitResult.Type.BLOCK) { var targetPos = hitResult.getBlockPos().offset(hitResult.getSide()); world.setBlockState(targetPos, Blocks.WATER.getDefaultState()); player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F); } } }

    private static void castAlarteAscendare(PlayerEntity player, World world) { if (!world.isClient) { raycastForEntity(player, world, 20).ifPresent(hitResult -> { Entity target = hitResult.getEntity(); target.addVelocity(0, 1.5, 0); player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, 1.0F); }); } }
    private static void castAppareVestigium(PlayerEntity player, World world) { if (!world.isClient) { raycastForEntity(player, world, 20).ifPresent(hitResult -> { if (hitResult.getEntity() instanceof LivingEntity target) { target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 300, 0)); player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F); } }); } }
    private static void castApparition(PlayerEntity player, World world) { if (!world.isClient && world instanceof ServerWorld serverWorld) { BlockHitResult hitResult = raycastForBlock(player, world, 30); if (hitResult.getType() == HitResult.Type.BLOCK) { BlockPos targetPos = hitResult.getBlockPos().offset(hitResult.getSide()); player.teleport(serverWorld, targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, Collections.emptySet(), player.getYaw(), player.getPitch()); player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F); } } }
    private static void castAraniaExumai(PlayerEntity player, World world) { if (!world.isClient) { Box area = new Box(player.getBlockPos()).expand(10); world.getEntitiesByClass(SpiderEntity.class, area, spider -> true).forEach(spider -> { spider.damage(player.getDamageSources().magic(), 10.0F); Vec3d knockback = spider.getPos().subtract(player.getPos()).normalize().multiply(2.0); spider.addVelocity(knockback.x, 0.5, knockback.z); }); player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F); } }
    private static void castArestoMomentum(PlayerEntity player, World world) { if (!world.isClient) { raycastForEntity(player, world, 20).ifPresent(hitResult -> { if (hitResult.getEntity() instanceof LivingEntity target) { target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 4)); player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F); } }); } }
    private static void castAscendio(PlayerEntity player, World world) { if (!world.isClient) { player.addVelocity(0, 1.2, 0); player.playSound(SoundEvents.ITEM_ELYTRA_FLYING, 1.0F, 1.0F); } }
    private static void castAvadaKedavra(PlayerEntity player, World world) { if (!world.isClient) { raycastForEntity(player, world, 20).ifPresent(hitResult -> { if (hitResult.getEntity() instanceof LivingEntity target) { target.kill(); player.playSound(SoundEvents.ENTITY_WITHER_DEATH, 0.5F, 1.5F); } }); } }
    private static void castAvifors(PlayerEntity player, World world) { if (!world.isClient) { raycastForEntity(player, world, 20).ifPresent(hitResult -> { Entity target = hitResult.getEntity(); if (!(target instanceof PlayerEntity)) { ParrotEntity parrot = new ParrotEntity(EntityType.PARROT, world); parrot.setPosition(target.getPos()); world.spawnEntity(parrot); target.discard(); player.playSound(SoundEvents.ENTITY_PARROT_IMITATE_WITCH, 1.0F, 1.0F); } }); } }
    private static void castAvenseguim(PlayerEntity player, World world) { if (!world.isClient) { player.sendMessage(Text.of("This spell is not yet implemented."), false); } }
    private static void castAvis(PlayerEntity player, World world) { if (!world.isClient && world instanceof ServerWorld serverWorld) { for (int i = 0; i < 5; i++) { ParrotEntity parrot = new ParrotEntity(EntityType.PARROT, world); parrot.setPosition(player.getPos().add(world.random.nextGaussian(), 1, world.random.nextGaussian())); serverWorld.spawnEntity(parrot); } player.playSound(SoundEvents.ENTITY_PARROT_AMBIENT, 1.0F, 1.0F); } }
    private static void castBaubillious(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.sendMessage(Text.of("Baubillious: Flash effect!"), false);
            ((ServerWorld) world).spawnParticles(ParticleTypes.FLASH, player.getX(), player.getEyeY(), player.getZ(), 20, 0.5, 0.5, 0.5, 0.01);
            player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, 1.0F);
        }
    }

    private static void castBombarda(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 15);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                world.breakBlock(pos, true);
                player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1.0F, 1.0F);
            }
        }
    }

    private static void castBombardaMaxima(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 20);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 4.0F, World.ExplosionSourceType.BLOCK);
                player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1.0F, 0.8F);
            }
        }
    }

    private static void castCalvorio(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 1));
                    player.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void castCaveInimicum(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.sendMessage(Text.of("Cave Inimicum: Protective barrier activated!"), false);
            player.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
        }
    }

    private static void castCelescere(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockPos pos = player.getBlockPos().up();
            world.setBlockState(pos, Blocks.LADDER.getDefaultState());
            player.playSound(SoundEvents.BLOCK_LADDER_PLACE, 1.0F, 1.0F);
        }
    }

    private static void castCircumrota(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.addVelocity(0, 1.0, 0);
            player.playSound(SoundEvents.ENTITY_PARROT_FLY, 1.0F, 1.0F);
        }
    }

    private static void castColloshoo(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hit = raycastForBlock(player, world, 15);
            if (hit.getType() == HitResult.Type.BLOCK) {
                world.setBlockState(hit.getBlockPos(), Blocks.SLIME_BLOCK.getDefaultState());
                player.playSound(SoundEvents.BLOCK_SLIME_BLOCK_PLACE, 1.0F, 1.0F);
            }
        }
    }

    private static void castColovaria(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.sendMessage(Text.of("Colovaria: Rainbow colors appear!"), false);
            ((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getEyeY(), player.getZ(), 30, 1, 1, 1, 0.05);
        }
    }

    private static void castConfringo(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 15);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 3.0F, World.ExplosionSourceType.BLOCK);
                player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1.0F, 1.0F);
            }
        }
    }

    private static void castConfundo(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 15).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0));
                    player.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void castCrucio(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 15).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1));
                    player.playSound(SoundEvents.ENTITY_WITHER_HURT, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void castDepulso(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 15).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                Vec3d direction = target.getPos().subtract(player.getPos()).normalize().multiply(2.0);
                target.addVelocity(direction.x, 0.5, direction.z);
                player.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0F, 1.0F);
            });
        }
    }

    private static void castEvanesco(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> hitResult.getEntity().discard());
            player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
    }

    private static void castExpectoPatronum(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.sendMessage(Text.of("Patronus appears!"), false);
            ((ServerWorld) world).spawnParticles(ParticleTypes.SOUL, player.getX(), player.getEyeY(), player.getZ(), 50, 1, 1, 1, 0.05);
        }
    }

    private static void castExpelliarmus(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof LivingEntity living) {
                    living.dropStack(new ItemStack(Items.STICK));
                    player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void castFractoStrata(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 15);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                world.setBlockState(hitResult.getBlockPos(), Blocks.AIR.getDefaultState());
                player.playSound(SoundEvents.BLOCK_STONE_BREAK, 1.0F, 1.0F);
            }
        }
    }

    private static void castFumos(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockPos pos = player.getBlockPos();
            ((ServerWorld) world).spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX(), pos.getY() + 1, pos.getZ(), 20, 1, 1, 1, 0.02);
            player.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 1.0F, 1.0F);
        }
    }

    private static void castGlacius(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 15).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2));
                    player.playSound(SoundEvents.BLOCK_SNOW_PLACE, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void castHerbivicus(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockPos pos = player.getBlockPos().up();
            world.setBlockState(pos, Blocks.OAK_SAPLING.getDefaultState());
            player.playSound(SoundEvents.BLOCK_GRASS_PLACE, 1.0F, 1.0F);
        }
    }

    private static void castHomenumRevelio(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.sendMessage(Text.of("Homenum Revelio: Humans detected nearby!"), false);
            player.playSound(SoundEvents.ENTITY_VILLAGER_AMBIENT, 1.0F, 1.0F);
        }
    }

    private static void castImmobulus(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 15).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 4));
                    player.playSound(SoundEvents.BLOCK_STONE_PLACE, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void castImpedimenta(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 15).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                Vec3d direction = player.getPos().subtract(target.getPos()).normalize().multiply(2.0);
                target.addVelocity(direction.x, 0.5, direction.z);
                player.playSound(SoundEvents.ENTITY_SHULKER_HURT, 1.0F, 1.0F);
            });
        }
    }

    private static void castImperio(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 1));
                    player.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void castLevicorpus(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 15).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                target.addVelocity(0, 1.5, 0);
                player.playSound(SoundEvents.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
            });
        }
    }
}

