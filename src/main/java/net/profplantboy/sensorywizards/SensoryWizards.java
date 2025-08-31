package net.profplantboy.sensorywizards;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;

import net.profplantboy.sensorywizards.block.ModBlocks;
import net.profplantboy.sensorywizards.command.LearnSpellCommand;
import net.profplantboy.sensorywizards.command.UnlearnSpellCommand;
import net.profplantboy.sensorywizards.item.ModItemGroups;
import net.profplantboy.sensorywizards.item.ModItems;
import net.profplantboy.sensorywizards.network.CastSpellPayload;
import net.profplantboy.sensorywizards.network.SelectSpellPayload;
import net.profplantboy.sensorywizards.screen.WandCarverScreenHandler;
import net.profplantboy.sensorywizards.spell.EquippedSpellComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.profplantboy.sensorywizards.config.SensoryWizardsConfig;

public class SensoryWizards implements ModInitializer {
    public static final String MOD_ID = "sensorywizards";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Your existing init
        ModComponents.registerComponents();
        ModItems.registerModItems();
        ModBlocks.register();
        WandCarverScreenHandler.register();
        registerPayloads();
        registerPacketHandlers();
        ModItemGroups.register();
        // ---- Config setup (AutoConfig + defaults) ----
        AutoConfig.register(SensoryWizardsConfig.class, GsonConfigSerializer::new);
        SensoryWizardsConfig config = AutoConfig.getConfigHolder(SensoryWizardsConfig.class).getConfig();

        // Make sure all known spells exist in the map with default=true.
        // (Safe to call repeatedly.)
        config.ensureDefaults(
                "fireball", "ice_bolt", "healing_touch", "aguamenti",
                "alarte_ascendare", "appare_vestigium", "apparition", "arania_exumai",
                "aresto_momentum", "ascendio", "avada_kedavra", "avifors", "avenseguim",
                "avis", "baubillious", "bombarda", "bombarda_maxima", "calvorio",
                "cave_inimicum", "celescere", "circumrota", "colloshoo", "colovaria",
                "confringo", "confundo", "crucio", "depulso", "evanesco",
                "expecto_patronum", "expelliarmus", "fracto_strata", "fumos", "glacius",
                "herbivicus", "homenum_revelio", "immobulus", "impedimenta", "imperio",
                "levicorpus"
        );
        // Persist defaults if the config file was missing entries.
        AutoConfig.getConfigHolder(SensoryWizardsConfig.class).save();

        // Register spells using the config toggles
        SpellRegistry.registerSpells(config);

        // ---- Commands ----
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            UnlearnSpellCommand.register(dispatcher);
            LearnSpellCommand.register(dispatcher);
        });

        LOGGER.info("[{}] Initialized.", MOD_ID);
    }

    private void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(SelectSpellPayload.ID, SelectSpellPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CastSpellPayload.ID, CastSpellPayload.CODEC);
    }

    private void registerPacketHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(SelectSpellPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ItemStack handStack = context.player().getMainHandStack();
                if (ModItems.WANDS.containsValue(handStack.getItem())) {
                    handStack.set(ModComponents.EQUIPPED_SPELL, new EquippedSpellComponent(payload.spellId()));
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(CastSpellPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                SpellRegistry.castSpell(payload.spellId(), context.player(), context.player().getWorld());
            });
        });
    }
}
