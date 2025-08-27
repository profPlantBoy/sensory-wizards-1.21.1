package net.profplantboy.sensorywizards.spell;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

public class ModComponents {

    // This component remains a vanilla one, for use on ItemStacks.
    public static ComponentType<SpellComponent> SPELL_COMPONENT;

    // This is now a ComponentKey from Cardinal Components, used to access data on entities.
    public static final ComponentKey<LearnedSpellsComponent> LEARNED_SPELLS =
            ComponentRegistry.getOrCreate(Identifier.of(SensoryWizards.MOD_ID, "learned_spells"), LearnedSpellsComponent.class);


    public static void registerComponents() {
        // We still register the SPELL_COMPONENT the vanilla way.
        SPELL_COMPONENT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(SensoryWizards.MOD_ID, "spell_component"),
                ComponentType.<SpellComponent>builder().codec(SpellComponent.CODEC).packetCodec(SpellComponent.PACKET_CODEC).build()
        );

        // We no longer register LEARNED_SPELLS here. The ComponentRegistry handles it automatically.
    }
}