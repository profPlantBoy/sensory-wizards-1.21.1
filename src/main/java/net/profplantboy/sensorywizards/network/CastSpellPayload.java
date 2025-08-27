package net.profplantboy.sensorywizards.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

public record CastSpellPayload(String spellId) implements CustomPayload {
    public static final CustomPayload.Id<CastSpellPayload> ID = new CustomPayload.Id<>(Identifier.of(SensoryWizards.MOD_ID, "cast_spell"));
    public static final PacketCodec<RegistryByteBuf, CastSpellPayload> CODEC = PacketCodec.of(CastSpellPayload::write, CastSpellPayload::new);

    public CastSpellPayload(RegistryByteBuf buf) {
        this(buf.readString());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeString(this.spellId);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}