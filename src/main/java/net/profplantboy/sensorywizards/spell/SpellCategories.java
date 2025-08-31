package net.profplantboy.sensorywizards.spell;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SpellCategories {
    private SpellCategories() {}

    // Order matters (LinkedHashMap = stable display order)
    public static final Map<String, List<String>> MAP = new LinkedHashMap<>();

    static {
        // Put each spell ID in exactly one category.
        // Update however you like; these are just sensible buckets.
        MAP.put("Offense", List.of(
                "fireball", "confringo", "bombarda", "bombarda_maxima", "depulso",
                "baubillious", "crucio", "expelliarmus"
        ));
        MAP.put("Control", List.of(
                "glacius", "colloshoo", "aresto_momentum", "immobulus", "impedimenta",
                "levicorpus", "confundo"
        ));
        MAP.put("Utility", List.of(
                "apparition", "appare_vestigium", "homenum_revelio", "circumrota",
                "colovaria", "celescere", "herbivicus", "aguamenti"
        ));
        MAP.put("Summons/Creatures", List.of(
                "avifors", "avis", "expecto_patronum", "arania_exumai"
        ));
        MAP.put("Illusion/Stealth", List.of(
                "evanesco", "cave_inimicum"
        ));
        MAP.put("Forbidden", List.of(
                "avada_kedavra", "imperio"
        ));
        // Add any missing IDs to whichever category fits.
    }
}
