package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Packet from client with mob xp details to update the config to
 * @param data
 */
public record MobXPUpdatePacket(MobXPData data) implements CustomPayload {
    public static final Id<MobXPUpdatePacket> ID = new Id<>(Identifier.of(MobXP.MOD_ID, "mob_xp_update_packet"));
    public static final CustomPayload.Id<MobXPUpdatePacket> PAYLOAD_ID = new CustomPayload.Id<>(Identifier.of(MobXP.MOD_ID, "mob_xp_update_packet"));

    public static final PacketCodec<RegistryByteBuf, MobXPUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
            MobXPData.PACKET_CODEC,
            MobXPUpdatePacket::data,
            MobXPUpdatePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
