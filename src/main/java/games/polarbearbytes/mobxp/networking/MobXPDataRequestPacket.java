package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXP;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;


/**
 * Packet from client requesting the mob xp details list
 */
public record MobXPDataRequestPacket() implements CustomPayload {
    public static final Id<MobXPDataRequestPacket> ID = new Id<>(Identifier.of(MobXP.MOD_ID, "mob_xp_data_request_packet"));
    public static final CustomPayload.Id<MobXPDataRequestPacket> PAYLOAD_ID = new CustomPayload.Id<>(Identifier.of(MobXP.MOD_ID, "mob_xp_data_request_packet"));

    public static final MobXPDataRequestPacket INSTANCE = new MobXPDataRequestPacket();

    public static final PacketCodec<RegistryByteBuf, MobXPDataRequestPacket> PACKET_CODEC = PacketCodec.unit( INSTANCE );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}