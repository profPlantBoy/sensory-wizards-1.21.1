package net.profplantboy.sensorywizards.spell;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

public class ModComponents {

    public static ComponentType<SpellComponent> SPELL_COMPONENT;
    public static ComponentType<LearnedSpellsComponent> LEARNED_SPELLS;

    public static void registerComponents() {
        SPELL_COMPONENT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(SensoryWizards.MOD_ID, "spell_component"),
                ComponentType.<SpellComponent>builder().codec(SpellComponent.CODEC).packetCodec(SpellComponent.PACKET_CODEC).build()
        );

        LEARNED_SPELLS = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(SensoryWizards.MOD_ID, "learned_spells"),
                ComponentType.<LearnedSpellsComponent>builder().codec(LearnedSpellsComponent.CODEC).packetCodec(LearnedSpellsComponent.PACKET_CODEC).build()
        );
    }
}