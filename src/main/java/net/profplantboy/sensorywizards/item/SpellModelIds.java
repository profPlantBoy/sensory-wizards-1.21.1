package net.profplantboy.sensorywizards.item;

import java.util.HashMap;
import java.util.Map;

public final class SpellModelIds {
    private SpellModelIds() {}

    private static final Map<String, Integer> MAP = new HashMap<>();

    static {
        // Assign a unique CustomModelData id for each spell WITH an icon
        MAP.put("aguamenti", 1);
        MAP.put("alarte_ascendare", 2);
        MAP.put("appare_vestigium", 3);
        MAP.put("apparition", 4);
        MAP.put("arania_exumai", 5);
        MAP.put("aresto_momentum", 6);
        MAP.put("ascendio", 7);
        MAP.put("avada_kedavra", 8);
        MAP.put("avifors", 9);
        MAP.put("baubillious", 10);
        MAP.put("bombarda_maxima", 11);
        MAP.put("bombarda", 12);
        MAP.put("cave_inimicum", 13);
        MAP.put("circumrota", 14);
        MAP.put("colovaria", 15);
        MAP.put("confringo", 16);
        MAP.put("confundo", 17);
        MAP.put("crucio", 18);
        MAP.put("depulso", 19);
        MAP.put("evanesco", 20);
        MAP.put("fireball", 21);
        MAP.put("healing_touch", 22);
        MAP.put("ice_bolt", 23);
    }

    public static Integer get(String spellId) {
        return MAP.get(spellId);
    }
}
