package net.profplantboy.sensorywizards.config;

import java.util.HashMap;
import java.util.Map;

public class SensoryWizardsConfig {

    // Map of spell ID to enabled/disabled
    public Map<String, Boolean> enabledSpells = new HashMap<>();

    public SensoryWizardsConfig() {
        // Default all spells to enabled
        String[] allSpells = {
                "fireball", "ice_bolt", "healing_touch", "aguamenti", "alarte_ascendare",
                "appare_vestigium", "apparition", "arania_exumai", "aresto_momentum", "ascendio",
                "avada_kedavra", "avifors", "avenseguim", "avis", "baubillious",
                "bombarda", "bombarda_maxima", "calvorio", "cave_inimicum", "celescere",
                "circumrota", "colloshoo", "colovaria", "confringo", "confundo",
                "crucio", "depulso", "evanesco", "expecto_patronum", "expelliarmus",
                "fracto_strata", "fumos", "glacius", "herbivicus", "homenum_revelio",
                "immobulus", "impedimenta", "imperio", "levicorpus"
        };

        for (String spell : allSpells) {
            enabledSpells.put(spell, true);
        }
    }

    // Helper: check if a spell is enabled
    public boolean isEnabled(String spellId) {
        return enabledSpells.getOrDefault(spellId, false);
    }
}
