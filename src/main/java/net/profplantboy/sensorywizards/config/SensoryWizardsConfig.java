package net.profplantboy.sensorywizards.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.LinkedHashMap;
import java.util.Map;

@Config(name = "sensorywizards")
public class SensoryWizardsConfig implements ConfigData {

    /**
     * Dynamically generated toggles: spell id -> enabled
     * Using LinkedHashMap to keep insertion order (nicer in UI).
     */
    @ConfigEntry.Gui.TransitiveObject
    public Map<String, Boolean> enabledSpells = new LinkedHashMap<>();

    /** Helper: check if a spell is enabled (default to false if missing). */
    public boolean isEnabled(String id) {
        return enabledSpells.getOrDefault(id, false);
    }

    /**
     * Call this once (e.g., after registration) to ensure all known spells exist with defaults.
     * Safe to call multiple times.
     */
    public void ensureDefaults(String... spellIds) {
        for (String id : spellIds) {
            enabledSpells.putIfAbsent(id, true); // default ON
        }
    }
}
