package net.profplantboy.sensorywizards.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record EquippedSpellComponent(String spellId) {
    public static final Codec<EquippedSpellComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("spell_id").forGetter(EquippedSpellComponent::spellId)
    ).apply(instance, EquippedSpellComponent::new));

    public static final PacketCodec<ByteBuf, EquippedSpellComponent> PACKET_CODEC = PacketCodec.of(
            (value, buf) -> buf.writeCharSequence(value.spellId, java.nio.charset.StandardCharsets.UTF_8),
            (buf) -> new EquippedSpellComponent(buf.readCharSequence(buf.readableBytes(), java.nio.charset.StandardCharsets.UTF_8).toString())
    );

    public static final EquippedSpellComponent EMPTY = new EquippedSpellComponent("");
}