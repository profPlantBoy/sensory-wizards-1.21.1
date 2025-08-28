package net.profplantboy.sensorywizards.spell;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class SpellRegistry {

    private static final Map<String, BiConsumer<PlayerEntity, World>> SPELL_ACTIONS = new HashMap<>();

    static {
        // Your existing spells
        SPELL_ACTIONS.put("fireball", SpellRegistry::castFireball);
        SPELL_ACTIONS.put("ice_bolt", SpellRegistry::castIceBolt);
        SPELL_ACTIONS.put("healing_touch", SpellRegistry::castHealingTouch);
        SPELL_ACTIONS.put("aguamenti", SpellRegistry::castAguamenti);

        // New spells
        SPELL_ACTIONS.put("alarte_ascendare", SpellRegistry::castAlarteAscendare);
        SPELL_ACTIONS.put("appare_vestigium", SpellRegistry::castAppareVestigium);
        SPELL_ACTIONS.put("apparition", SpellRegistry::castApparition);
        SPELL_ACTIONS.put("arania_exumai", SpellRegistry::castAraniaExumai);
        SPELL_ACTIONS.put("aresto_momentum", SpellRegistry::castArestoMomentum);
        SPELL_ACTIONS.put("ascendio", SpellRegistry::castAscendio);
        SPELL_ACTIONS.put("avada_kedavra", SpellRegistry::castAvadaKedavra);
        SPELL_ACTIONS.put("avifors", SpellRegistry::castAvifors);
        SPELL_ACTIONS.put("avenseguim", SpellRegistry::castAvenseguim);
        SPELL_ACTIONS.put("avis", SpellRegistry::castAvis);
        SPELL_ACTIONS.put("baubillious", SpellRegistry::castBaubillious);
        SPELL_ACTIONS.put("bombarda", SpellRegistry::castBombarda);
        SPELL_ACTIONS.put("bombarda_maxima", SpellRegistry::castBombardaMaxima);
        SPELL_ACTIONS.put("calvorio", SpellRegistry::castCalvorio);
        SPELL_ACTIONS.put("cave_inimicum", SpellRegistry::castCaveInimicum);
        SPELL_ACTIONS.put("celescere", SpellRegistry::castCelescere);
        SPELL_ACTIONS.put("circumrota", SpellRegistry::castCircumrota);
        SPELL_ACTIONS.put("colloshoo", SpellRegistry::castColloshoo);
        SPELL_ACTIONS.put("colovaria", SpellRegistry::castColovaria);
        SPELL_ACTIONS.put("confringo", SpellRegistry::castConfringo);
        SPELL_ACTIONS.put("confundo", SpellRegistry::castConfundo);
        SPELL_ACTIONS.put("crucio", SpellRegistry::castCrucio);
        SPELL_ACTIONS.put("depulso", SpellRegistry::castDepulso);
        SPELL_ACTIONS.put("evanesco", SpellRegistry::castEvanesco);
        SPELL_ACTIONS.put("expecto_patronum", SpellRegistry::castExpectoPatronum);
        SPELL_ACTIONS.put("expelliarmus", SpellRegistry::castExpelliarmus);
        SPELL_ACTIONS.put("fracto_strata", SpellRegistry::castFractoStrata);
        SPELL_ACTIONS.put("fumos", SpellRegistry::castFumos);
        SPELL_ACTIONS.put("glacius", SpellRegistry::castGlacius);
        SPELL_ACTIONS.put("herbivicus", SpellRegistry::castHerbivicus);
        SPELL_ACTIONS.put("homenum_revelio", SpellRegistry::castHomenumRevelio);
        SPELL_ACTIONS.put("immobulus", SpellRegistry::castImmobulus);
        SPELL_ACTIONS.put("impedimenta", SpellRegistry::castImpedimenta);
        SPELL_ACTIONS.put("imperio", SpellRegistry::castImperio);
        SPELL_ACTIONS.put("levicorpus", SpellRegistry::castLevicorpus);
    }

    public static void castSpell(String spellId, PlayerEntity player, World world) {
        if (SPELL_ACTIONS.containsKey(spellId)) {
            SPELL_ACTIONS.get(spellId).accept(player, world);
        }
    }

    // Utility method for raycasting to find an entity
    private static Optional<EntityHitResult> raycastForEntity(PlayerEntity player, World world, double maxDistance) {
        Vec3d start = player.getEyePos();
        Vec3d end = start.add(player.getRotationVector().multiply(maxDistance));
        Box box = new Box(start, end);
        EntityHitResult hit = net.minecraft.entity.projectile.ProjectileUtil.getEntityCollision(world, player, start, end, box, e -> !e.isSpectator() && e.isAlive() && e.canBeHit());
        return Optional.ofNullable(hit);
    }

    // Utility method for raycasting to find a block
    private static BlockHitResult raycastForBlock(PlayerEntity player, World world, double maxDistance) {
        return world.raycast(new RaycastContext(
                player.getEyePos(),
                player.getEyePos().add(player.getRotationVector().multiply(maxDistance)),
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));
    }


    // Your existing spells...
    private static void castFireball(PlayerEntity player, World world) { if (!world.isClient) { Vec3d lookVec = player.getRotationVector(); SmallFireballEntity fireball = new SmallFireballEntity(world, player, lookVec); fireball.setPosition(player.getX(), player.getEyeY(), player.getZ()); world.spawnEntity(fireball); player.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F); } }
    private static void castIceBolt(PlayerEntity player, World world) { if (!world.isClient) { player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1)); player.playSound(SoundEvents.ENTITY_PLAYER_HURT_FREEZE, 1.0F, 1.0F); } }
    private static void castHealingTouch(PlayerEntity player, World world) { if (!world.isClient) { player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1)); player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 1.0F); } }
    private static void castAguamenti(PlayerEntity player, World world) { if (!world.isClient) { BlockHitResult hitResult = raycastForBlock(player, world, 20); if (hitResult.getType() == HitResult.Type.BLOCK) { var targetPos = hitResult.getBlockPos().offset(hitResult.getSide()); world.setBlockState(targetPos, Blocks.WATER.getDefaultState()); player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F); } } }

    // New Spell Implementations

    /**
     * Blasts target away/up
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAlarteAscendare(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                target.addVelocity(0, 1.5, 0);
                player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, 1.0F);
            });
        }
    }

    /**
     * Tracking spell used on a mob to track it
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAppareVestigium(PlayerEntity player, World world) {
        if (!world.isClient) {
            // TODO: Implement a proper tracking system. For now, this makes the target glow.
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 300, 0));
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Teleports player
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castApparition(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 30);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos targetPos = hitResult.getBlockPos().offset(hitResult.getSide());
                player.teleport(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
                player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Expells spiders
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAraniaExumai(PlayerEntity player, World world) {
        if (!world.isClient) {
            Box area = new Box(player.getBlockPos()).expand(10);
            world.getEntitiesByClass(SpiderEntity.class, area, spider -> true).forEach(spider -> {
                spider.damage(player.getDamageSources().magic(), 10.0F);
                Vec3d knockback = spider.getPos().subtract(player.getPos()).normalize().multiply(2.0);
                spider.addVelocity(knockback.x, 0.5, knockback.z);
            });
            player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
        }
    }

    /**
     * Slows down target
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castArestoMomentum(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 4));
                    player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Lifts sender into the air
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAscendio(PlayerEntity player, World world) {
        if (!world.isClient) {
            player.addVelocity(0, 1.2, 0);
            player.playSound(SoundEvents.ITEM_ELYTRA_START, 1.0F, 1.0F);
        }
    }

    /**
     * Killing curse
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAvadaKedavra(PlayerEntity player, World world) {
        if (!world.isClient) {
            // TODO: Add a significant cost or cooldown to this spell.
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.kill();
                    player.playSound(SoundEvents.ENTITY_WITHER_DEATH, 0.5F, 1.5F);
                }
            });
        }
    }

    /**
     * Mutates a mob into a bird
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAvifors(PlayerEntity player, World world) {
        if (!world.isClient) {
            // TODO: This is complex. A proper implementation would need a mapping of mobs to transform.
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (!(target instanceof PlayerEntity)) {
                    ParrotEntity parrot = new ParrotEntity(EntityType.PARROT, world);
                    parrot.setPosition(target.getPos());
                    world.spawnEntity(parrot);
                    target.discard();
                    player.playSound(SoundEvents.ENTITY_PARROT_IMITATE_WITCH, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Turns an object into a tracking device
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAvenseguim(PlayerEntity player, World world) {
        if (!world.isClient) {
            // TODO: This is very complex and requires NBT data on items and a tracking system.
            player.sendMessage(Text.of("This spell is not yet implemented."), false);
        }
    }

    /**
     * Bird summoning charm
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castAvis(PlayerEntity player, World world) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 5; i++) {
                ParrotEntity parrot = new ParrotEntity(EntityType.PARROT, world);
                parrot.setPosition(player.getPos().add(world.random.nextGaussian(), 1, world.random.nextGaussian()));
                serverWorld.spawnEntity(parrot);
            }
            player.playSound(SoundEvents.ENTITY_PARROT_AMBIENT, 1.0F, 1.0F);
        }
    }

    /**
     * Produces a bolt of white sparks from the end of the tip of the wand
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castBaubillious(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2));
                    player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Creates a small explosion
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castBombarda(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 20);
            if (hitResult.getType() != HitResult.Type.MISS) {
                world.createExplosion(player, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, 2.0F, World.ExplosionSourceType.MOB);
            }
        }
    }

    /**
     * Creates a large explosion
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castBombardaMaxima(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 20);
            if (hitResult.getType() != HitResult.Type.MISS) {
                world.createExplosion(player, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, 4.0F, World.ExplosionSourceType.MOB);
            }
        }
    }

    /**
     * Shears sheep or milks cows
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castCalvorio(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 10).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                if (target instanceof SheepEntity sheep && sheep.isShearable()) {
                    sheep.sheared(SoundCategory.PLAYERS);
                    player.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
                } else if (target instanceof CowEntity cow) {
                    ItemStack bucket = new ItemStack(Items.BUCKET);
                    ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
                    if (player.getInventory().contains(bucket)) {
                        player.getInventory().removeStack(player.getInventory().getSlotWithStack(bucket), 1);
                        player.getInventory().offerOrDrop(milkBucket);
                        player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                    }
                }
            });
        }
    }

    /**
     * Makes caster invisible for a short period of time
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castCaveInimicum(PlayerEntity player, World world) {
        if (!world.isClient) {
            // Note: True complete invisibility of armor is very complex and requires packets.
            // The standard invisibility effect is the best we can do easily.
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 0));
            player.playSound(SoundEvents.ENTITY_WITCH_DRINK, 1.0F, 1.0F);
        }
    }

    /**
     * Makes plants around the player in a 9x9 grow at a much faster rate
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castCelescere(PlayerEntity player, World world) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            BlockPos center = player.getBlockPos();
            for (BlockPos pos : BlockPos.iterate(center.add(-4, -1, -4), center.add(4, 1, 4))) {
                BlockState state = world.getBlockState(pos);
                // TODO: Add more plant types
                if (state.getBlock() == Blocks.WHEAT && world.random.nextInt(3) == 0) {
                    serverWorld.setBlockState(pos, Blocks.WHEAT.getDefaultState().with(net.minecraft.state.property.Properties.AGE_7, 7));
                }
            }
            player.playSound(SoundEvents.ITEM_BONE_MEAL_USE, 1.0F, 1.0F);
        }
    }

    /**
     * Used to rotate the target block
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castCircumrota(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 10);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState state = world.getBlockState(pos);
                // This uses the built-in rotate function, which works for many vanilla blocks
                world.setBlockState(pos, state.rotate(net.minecraft.block.BlockRotation.CLOCKWISE_90));
                player.playSound(SoundEvents.BLOCK_GRINDSTONE_USE, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Causes targeted mob to become frozen and not able to move for a short period of time
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castColloshoo(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 20, false, false));
                    player.playSound(SoundEvents.ENTITY_PLAYER_HURT_FREEZE, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Changes color of the block a player is looking at
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castColovaria(PlayerEntity player, World world) {
        if (!world.isClient) {
            // TODO: This is complex. A full implementation needs maps of colorable blocks.
            // This is a simple example for wool.
            BlockHitResult hitResult = raycastForBlock(player, world, 10);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                if (world.getBlockState(pos).getBlock() == Blocks.WHITE_WOOL) {
                    world.setBlockState(pos, Blocks.RED_WOOL.getDefaultState());
                    player.playSound(SoundEvents.ITEM_DYE_USE, 1.0F, 1.0F);
                }
            }
        }
    }

    /**
     * Uses heat as an explosive force towards a mob
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castConfringo(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                world.createExplosion(player, target.getX(), target.getY(), target.getZ(), 2.5F, true, World.ExplosionSourceType.MOB);
            });
        }
    }

    /**
     * Causes the targeted mob to confused and befuddled
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castConfundo(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                    player.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Creates excruciating pain on the victim
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castCrucio(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1));
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
                    player.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Sends the targeted mob backwards away from the sender
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castDepulso(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                Entity target = hitResult.getEntity();
                Vec3d knockback = player.getRotationVector().multiply(-1, 0, -1).normalize().multiply(2.5);
                target.addVelocity(knockback.x, 0.5, knockback.z);
                player.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
            });
        }
    }

    /**
     * Turns the target invisible
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castEvanesco(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 0));
                    player.playSound(SoundEvents.ENTITY_WITCH_DRINK, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Creates a protective guardian
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castExpectoPatronum(PlayerEntity player, World world) {
        if (!world.isClient) {
            // TODO: This is very complex. A proper implementation would need a custom entity.
            // For now, it summons a temporary friendly wolf.
            net.minecraft.entity.passive.WolfEntity wolf = new net.minecraft.entity.passive.WolfEntity(EntityType.WOLF, world);
            wolf.setOwner(player);
            wolf.setPosition(player.getPos().add(player.getRotationVector().multiply(2)));
            world.spawnEntity(wolf);
            player.playSound(SoundEvents.ENTITY_WOLF_HOWL, 1.0F, 1.0F);
        }
    }

    /**
     * Disarms the target
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castExpelliarmus(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.dropStack(target.getMainHandStack());
                    target.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                    player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Breaks weak objects into smaller pieces
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castFractoStrata(PlayerEntity player, World world) {
        if (!world.isClient) {
            BlockHitResult hitResult = raycastForBlock(player, world, 10);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState state = world.getBlockState(pos);
                if (state.isOf(Blocks.STONE)) world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
                else if (state.isOf(Blocks.COBBLESTONE)) world.setBlockState(pos, Blocks.GRAVEL.getDefaultState());
                else if (state.isOf(Blocks.GRAVEL)) world.setBlockState(pos, Blocks.SAND.getDefaultState());
                player.playSound(SoundEvents.BLOCK_STONE_BREAK, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Creates smoke or fog that hinders visibility
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castFumos(PlayerEntity player, World world) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            BlockHitResult hitResult = raycastForBlock(player, world, 20);
            if (hitResult.getType() != HitResult.Type.MISS) {
                for (int i = 0; i < 100; i++) {
                    serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z,
                            1, world.random.nextGaussian() * 0.2, 0.2, world.random.nextGaussian() * 0.2, 0.05);
                }
                player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Freezing spell that will freeze a mob in its place
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castGlacius(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 255));
                    player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Causes plants to rapidly grow in a small area
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castHerbivicus(PlayerEntity player, World world) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            BlockHitResult hitResult = raycastForBlock(player, world, 20);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos center = hitResult.getBlockPos();
                for (BlockPos pos : BlockPos.iterate(center.add(-2, -1, -2), center.add(2, 1, 2))) {
                    net.minecraft.item.BoneMealItem.useOnFertilizable(new ItemStack(Items.BONE_MEAL), world, pos);
                }
                player.playSound(SoundEvents.ITEM_BONE_MEAL_USE, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Human revealing spell
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castHomenumRevelio(PlayerEntity player, World world) {
        if (!world.isClient) {
            Box area = new Box(player.getBlockPos()).expand(30);
            world.getPlayers().forEach(p -> {
                if (area.contains(p.getPos()) && p != player) {
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
                }
            });
            player.playSound(SoundEvents.BLOCK_BELL_RESONATE, 1.0F, 1.0F);
        }
    }

    /**
     * Freezing spell that will freeze a mob in its place permanently
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castImmobulus(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.setAiDisabled(true);
                    player.playSound(SoundEvents.ENTITY_ZOMBIE_FREEZE, 1.0F, 1.0F);
                    // TODO: Need a way to un-freeze them. Perhaps another spell or command.
                }
            });
        }
    }

    /**
     * Slows down the targeted mob
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castImpedimenta(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
                    player.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Places the targeted mob under the control of the caster
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castImperio(PlayerEntity player, World world) {
        if (!world.isClient) {
            // TODO: This is very complex and requires custom AI goals.
            // For now, it makes the target stop attacking the player.
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof net.minecraft.entity.mob.MobEntity mob) {
                    mob.setTarget(null);
                    player.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0F, 1.0F);
                }
            });
        }
    }

    /**
     * Hoists targeted mob into the air and floats them above the ground
     * @param player The player casting the spell.
     * @param world The world the player is in.
     */
    private static void castLevicorpus(PlayerEntity player, World world) {
        if (!world.isClient) {
            raycastForEntity(player, world, 20).ifPresent(hitResult -> {
                if (hitResult.getEntity() instanceof LivingEntity target) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 0));
                    player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                }
            });
        }
    }
}