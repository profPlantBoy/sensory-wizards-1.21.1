package net.profplantboy.sensorywizards.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.profplantboy.sensorywizards.screen.WandCarverScreen;
import net.profplantboy.sensorywizards.screen.WandCarverScreenHandler;

public class SensoryWizardsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Hook the server-side handler to the client-side screen.
        HandledScreens.register(WandCarverScreenHandler.TYPE, WandCarverScreen::new);
    }
}
