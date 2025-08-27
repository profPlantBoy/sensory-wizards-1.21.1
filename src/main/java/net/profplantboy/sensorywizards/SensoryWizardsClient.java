package net.profplantboy.sensorywizards;

import net.fabricmc.api.ClientModInitializer;
import net.profplantboy.sensorywizards.client.input.KeyInputHandler;

public class SensoryWizardsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        // The manual packet registration and handling are no longer needed here
    }
}