package net.profplantboy.sensorywizards.spell;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

public class ModComponents {

    public static ComponentType<SpellComponent> SPELL_COMPONENT;
    public static ComponentType<EquippedSpellComponent> EQUIPPED_SPELL;

    public static final ComponentKey<LearnedSpellsComponent> LEARNED_SPELLS =
            ComponentRegistry.getOrCreate(Identifier.of(SensoryWizards.MOD_ID, "learned_spells"), LearnedSpellsComponent.class);

    public static void registerComponents() {
        SPELL_COMPONENT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(SensoryWizards.MOD_ID, "spell_component"),
                ComponentType.<SpellComponent>builder().codec(SpellComponent.CODEC).packetCodec(SpellComponent.PACKET_CODEC).build()
        );

        EQUIPPED_SPELL = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(SensoryWizards.MOD_ID, "equipped_spell"),
                ComponentType.<EquippedSpellComponent>builder().codec(EquippedSpellComponent.CODEC).packetCodec(EquippedSpellComponent.PACKET_CODEC).build()
        );
    }
}