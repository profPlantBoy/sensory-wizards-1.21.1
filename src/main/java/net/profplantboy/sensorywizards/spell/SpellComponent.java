package net.profplantboy.sensorywizards.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record SpellComponent(String spellType) {
    public static final Codec<SpellComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("spell_type").forGetter(SpellComponent::spellType)
    ).apply(instance, SpellComponent::new));

    public static final PacketCodec<RegistryByteBuf, SpellComponent> PACKET_CODEC = new PacketCodec<RegistryByteBuf, SpellComponent>() {
        @Override
        public SpellComponent decode(RegistryByteBuf buf) {
            return new SpellComponent(buf.readString());
        }

        @Override
        public void encode(RegistryByteBuf buf, SpellComponent value) {
            buf.writeString(value.spellType());
        }
    };
}