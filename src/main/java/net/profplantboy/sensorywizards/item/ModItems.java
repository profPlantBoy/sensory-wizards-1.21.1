package net.profplantboy.sensorywizards.item;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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

    // Your base “learn a spell” item stays the same
    public static final Item SPELL_SCROLL = registerItem("spell_scroll", new SpellScroll(new Item.Settings()));

    // Your custom creative tab
    public static final ItemGroup SENSORY_WIZARDS_TAB = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(SensoryWizards.MOD_ID, "sensory_wizards_tab"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup." + SensoryWizards.MOD_ID + ".sensory_wizards_tab"))
                    .icon(() -> new ItemStack(WANDS.get("wooden_wand")))
                    .entries((context, entries) -> {
                        // Add wands (unchanged)
                        WANDS.values().forEach(entries::add);

                        // Add spell scroll variants ONLY for enabled spells
                        SensoryWizardsConfig cfg = AutoConfig.getConfigHolder(SensoryWizardsConfig.class).getConfig();
                        // Ensure defaults exist (safe even if already set)
                        cfg.ensureDefaults(SpellIds.ALL);

                        for (String id : SpellIds.ALL) {
                            if (!cfg.isEnabled(id)) continue; // hide disabled spells from creative
                            entries.add(createSpellStack(id));
                        }
                    })
                    .build()
    );

    /** Builds one spell-scroll variant tagged with the spell id (your existing logic). */
    private static ItemStack createSpellStack(String spellName) {
        ItemStack stack = new ItemStack(SPELL_SCROLL);
        stack.set(ModComponents.SPELL_COMPONENT, new SpellComponent(spellName));

        // NEW: set CustomModelData if we’ve assigned a model for this spell
        Integer modelId = net.profplantboy.sensorywizards.item.SpellModelIds.get(spellName);
        if (modelId != null) {
            stack.getOrCreateNbt().putInt("CustomModelData", modelId);
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
            Item wand = Registry.register(
                    Registries.ITEM,
                    Identifier.of(SensoryWizards.MOD_ID, type + "_wand"),
                    new WandItem(new Item.Settings())
            );
            WANDS.put(type + "_wand", wand);
        }
    }
}
