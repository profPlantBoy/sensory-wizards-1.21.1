package net.profplantboy.sensorywizards.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.ArrayList;
import java.util.HashSet;

public record LearnedSpellsComponent(HashSet<String> spells) {

    public static final Codec<LearnedSpellsComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().xmap(
                    HashSet::new,
                    ArrayList::new
            ).fieldOf("spells").forGetter(LearnedSpellsComponent::spells)
    ).apply(instance, LearnedSpellsComponent::new));

    public static final PacketCodec<RegistryByteBuf, LearnedSpellsComponent> PACKET_CODEC = PacketCodec.of(
            (value, buf) -> buf.writeCollection(value.spells(), (b, s) -> b.writeString(s)),
            (buf) -> new LearnedSpellsComponent(buf.readCollection(HashSet::new, b -> b.readString()))
    );

    public boolean hasSpell(String spellId) {
        return this.spells.contains(spellId);
    }

    public LearnedSpellsComponent addSpell(String spellId) {
        HashSet<String> newSpells = new HashSet<>(this.spells);
        newSpells.add(spellId);
        return new LearnedSpellsComponent(newSpells);
    }
}