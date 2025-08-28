package net.profplantboy.sensorywizards.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
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
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellComponent;

public class ModItems {

    public static final Map<String, Item> WANDS = new LinkedHashMap<>();

    public static final Item SPELL_SCROLL = registerItem("spell_scroll", new SpellScroll(new Item.Settings()));

    public static final ItemGroup SENSORY_WIZARDS_TAB = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(SensoryWizards.MOD_ID, "sensory_wizards_tab"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup." + SensoryWizards.MOD_ID + ".sensory_wizards_tab"))
                    .icon(() -> new ItemStack(WANDS.get("wooden_wand")))
                    .entries((context, entries) -> {
                        WANDS.values().forEach(entries::add);

                        // Add all spell scrolls to the creative tab
                        entries.add(createSpellStack("fireball"));
                        entries.add(createSpellStack("ice_bolt"));
                        entries.add(createSpellStack("healing_touch"));
                        entries.add(createSpellStack("aguamenti"));
                        entries.add(createSpellStack("alarte_ascendare"));
                        entries.add(createSpellStack("appare_vestigium"));
                        entries.add(createSpellStack("apparition"));
                        entries.add(createSpellStack("arania_exumai"));
                        entries.add(createSpellStack("aresto_momentum"));
                        entries.add(createSpellStack("ascendio"));
                        entries.add(createSpellStack("avada_kedavra"));
                        entries.add(createSpellStack("avifors"));
                        entries.add(createSpellStack("avenseguim"));
                        entries.add(createSpellStack("avis"));
                        entries.add(createSpellStack("baubillious"));
                        entries.add(createSpellStack("bombarda"));
                        entries.add(createSpellStack("bombarda_maxima"));
                        entries.add(createSpellStack("calvorio"));
                        entries.add(createSpellStack("cave_inimicum"));
                        entries.add(createSpellStack("celescere"));
                        entries.add(createSpellStack("circumrota"));
                        entries.add(createSpellStack("colloshoo"));
                        entries.add(createSpellStack("colovaria"));
                        entries.add(createSpellStack("confringo"));
                        entries.add(createSpellStack("confundo"));
                        entries.add(createSpellStack("crucio"));
                        entries.add(createSpellStack("depulso"));
                        entries.add(createSpellStack("evanesco"));
                        entries.add(createSpellStack("expecto_patronum"));
                        entries.add(createSpellStack("expelliarmus"));
                        entries.add(createSpellStack("fracto_strata"));
                        entries.add(createSpellStack("fumos"));
                        entries.add(createSpellStack("glacius"));
                        entries.add(createSpellStack("herbivicus"));
                        entries.add(createSpellStack("homenum_revelio"));
                        entries.add(createSpellStack("immobulus"));
                        entries.add(createSpellStack("impedimenta"));
                        entries.add(createSpellStack("imperio"));
                        entries.add(createSpellStack("levicorpus"));
                    })
                    .build()
    );

    private static ItemStack createSpellStack(String spellName) {
        ItemStack stack = new ItemStack(SPELL_SCROLL);
        stack.set(ModComponents.SPELL_COMPONENT, new SpellComponent(spellName));
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
