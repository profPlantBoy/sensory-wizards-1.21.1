package net.profplantboy.sensorywizards;

import net.fabricmc.api.ModInitializer;

import net.profplantboy.sensorywizards.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensoryWizards implements ModInitializer {
	public static final String MOD_ID = "sensorywizards";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItems.registerModItems();
	}
}