package net.profplantboy.sensorywizards;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.profplantboy.sensorywizards.command.LearnSpellCommand;
import net.profplantboy.sensorywizards.command.UnlearnSpellCommand;
import net.profplantboy.sensorywizards.item.ModItems;
import net.profplantboy.sensorywizards.network.CastSpellPayload;
import net.profplantboy.sensorywizards.network.SelectSpellPayload;
import net.profplantboy.sensorywizards.spell.EquippedSpellComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensoryWizards implements ModInitializer {
    public static final String MOD_ID = "sensorywizards";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModComponents.registerComponents();
        ModItems.registerModItems();
        registerPayloads();
        registerPacketHandlers();

        // Register both of your commands here
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            UnlearnSpellCommand.register(dispatcher);
            LearnSpellCommand.register(dispatcher); // This was the missing line
        });
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
