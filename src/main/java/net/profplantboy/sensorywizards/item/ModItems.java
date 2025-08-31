package net.profplantboy.sensorywizards.item;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

import java.util.LinkedHashMap;
import java.util.Map;

import net.profplantboy.sensorywizards.config.SensoryWizardsConfig;
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellComponent;
import net.profplantboy.sensorywizards.spell.SpellIds;

public class ModItems {

    public static final Map<String, Item> WANDS = new LinkedHashMap<>();

    public static final Item SPELL_SCROLL = registerItem("spell_scroll", new SpellScroll(new Item.Settings()));

    // Add this near your other methods
    public static ItemStack makeSpellScrollStack(String spellId) {
        ItemStack stack = new ItemStack(SPELL_SCROLL);
        // same component you already use
        stack.set(net.profplantboy.sensorywizards.spell.ModComponents.SPELL_COMPONENT,
                new net.profplantboy.sensorywizards.spell.SpellComponent(spellId));
        // CustomModelData so the icon matches your textures
        Integer modelId = SPELL_MODEL_DATA.get(spellId);
        if (modelId != null) {
            stack.set(net.minecraft.component.DataComponentTypes.CUSTOM_MODEL_DATA,
                    new net.minecraft.component.type.CustomModelDataComponent(modelId));
        } else {
            stack.remove(net.minecraft.component.DataComponentTypes.CUSTOM_MODEL_DATA);
        }
        return stack;
    }

    // CustomModelData numbers used in your spell_scroll.json overrides (flat models)
    private static final Map<String, Integer> SPELL_MODEL_DATA = Map.ofEntries(
            Map.entry("aguamenti", 1001),
            Map.entry("alarte_ascendare", 1002),
            Map.entry("appare_vestigium", 1003),
            Map.entry("apparition", 1004),
            Map.entry("arania_exumai", 1005),
            Map.entry("aresto_momentum", 1006),
            Map.entry("ascendio", 1007),
            Map.entry("avada_kedavra", 1008),
            Map.entry("avifors", 1009),
            Map.entry("baubillious", 1010),
            Map.entry("bombarda_maxima", 1011),
            Map.entry("bombarda", 1012),
            Map.entry("cave_inimicum", 1013),
            Map.entry("circumrota", 1014),
            Map.entry("colovaria", 1015),
            Map.entry("confringo", 1016),
            Map.entry("confundo", 1017),
            Map.entry("crucio", 1018),
            Map.entry("depulso", 1019),
            Map.entry("evanesco", 1020),
            Map.entry("fireball", 1021),
            Map.entry("healing_touch", 1022),
            Map.entry("ice_bolt", 1023)
    );

    public static final ItemGroup SENSORY_WIZARDS_TAB = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(SensoryWizards.MOD_ID, "sensory_wizards_tab"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup." + SensoryWizards.MOD_ID + ".sensory_wizards_tab"))
                    .icon(() -> new ItemStack(WANDS.get("wooden_wand")))
                    .entries((context, entries) -> {
                        // Add wands
                        WANDS.values().forEach(entries::add);

                        // Add spell scroll variants ONLY for enabled spells (from config)
                        SensoryWizardsConfig cfg = AutoConfig.getConfigHolder(SensoryWizardsConfig.class).getConfig();
                        cfg.ensureDefaults(SpellIds.ALL);

                        for (String id : SpellIds.ALL) {
                            if (!cfg.isEnabled(id)) continue;  // hide disabled spells
                            entries.add(createSpellStack(id));
                        }
                    })
                    .build()
    );

    /** Builds one spell-scroll variant with your SpellComponent + CustomModelData. */
    private static ItemStack createSpellStack(String spellName) {
        ItemStack stack = new ItemStack(SPELL_SCROLL);
        stack.set(ModComponents.SPELL_COMPONENT, new SpellComponent(spellName));

        Integer modelId = SPELL_MODEL_DATA.get(spellName);
        if (modelId != null) {
            stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(modelId));
        } else {
            // No per-spell icon -> ensure default model by removing component
            stack.remove(DataComponentTypes.CUSTOM_MODEL_DATA);
        }
        return stack;
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SensoryWizards.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SensoryWizards.LOGGER.info("Registering Mod Items for " + SensoryWizards.MOD_ID);

        String[] wandTypes = {"wooden", "stone", "copper", "iron", "gold", "diamond", "netherite"};
        for (String type : wandTypes) {
            Item wand = Registry.register(Registries.ITEM, Identifier.of(SensoryWizards.MOD_ID, type + "_wand"),
                    new WandItem(new Item.Settings()));
            WANDS.put(type + "_wand", wand);
        }
    }
}
