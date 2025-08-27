package net.profplantboy.sensorywizards;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.profplantboy.sensorywizards.item.ModItems;
import net.profplantboy.sensorywizards.network.SelectSpellPayload;
import net.profplantboy.sensorywizards.spell.EquippedSpellComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.profplantboy.sensorywizards.command.UnlearnSpellCommand;

public class SensoryWizards implements ModInitializer {
    public static final String MOD_ID = "sensorywizards";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModComponents.registerComponents();
        ModItems.registerModItems();
        registerPayloads();
        registerSpellSelectionPacket();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> UnlearnSpellCommand.register(dispatcher));
    }

    private void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(SelectSpellPayload.ID, SelectSpellPayload.CODEC);
    }

    private void registerSpellSelectionPacket() {
        ServerPlayNetworking.registerGlobalReceiver(SelectSpellPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ItemStack handStack = context.player().getMainHandStack();
                if (ModItems.WANDS.containsValue(handStack.getItem())) {
                    handStack.set(ModComponents.EQUIPPED_SPELL, new EquippedSpellComponent(payload.spellId()));
                }
            });
        });
    }
}