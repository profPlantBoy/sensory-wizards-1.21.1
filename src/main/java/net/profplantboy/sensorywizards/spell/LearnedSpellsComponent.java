package net.profplantboy.sensorywizards.spell;

public class LearnedSpellsComponent {
}
package net.profplantboy.sensorywizards.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import java.util.HashSet;
import java.util.Set;

public record LearnedSpellsComponent(Set<String> spells) {

    public static final Codec<LearnedSpellsComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().xmap(HashSet::new, (spells) -> spells.stream().toList()).fieldOf("spells").forGetter(LearnedSpellsComponent::spells)
    ).apply(instance, LearnedSpellsComponent::new));

    public static final PacketCodec<RegistryByteBuf, LearnedSpellsComponent> PACKET_CODEC = PacketCodecs.collection(HashSet::new, PacketCodecs.string()).xmap(LearnedSpellsComponent::new, LearnedSpellsComponent::spells);

    public boolean hasSpell(String spellId) {
        return this.spells.contains(spellId);
    }

    public void addSpell(String spellId) {
        this.spells.add(spellId);
    }
}