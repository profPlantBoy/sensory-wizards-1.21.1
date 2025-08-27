package net.profplantboy.sensorywizards.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.profplantboy.sensorywizards.SensoryWizards;

public record SelectSpellPayload(String spellId) implements CustomPayload {
    public static final CustomPayload.Id<SelectSpellPayload> ID = new CustomPayload.Id<>(Identifier.of(SensoryWizards.MOD_ID, "select_spell"));
    public static final PacketCodec<RegistryByteBuf, SelectSpellPayload> CODEC = PacketCodec.of(SelectSpellPayload::write, SelectSpellPayload::new);

    public SelectSpellPayload(RegistryByteBuf buf) {
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