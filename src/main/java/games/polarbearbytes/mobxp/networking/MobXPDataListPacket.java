package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Network packet for transmitting the mob xp details list to client
 * @param mobXPDataList
 */
public record MobXPDataListPacket(List<MobXPData> mobXPDataList) implements CustomPayload {
    public static final Id<MobXPDataListPacket> ID = new Id<>(Identifier.of(MobXP.MOD_ID, "mob_xp_data_packet"));
    public static final CustomPayload.Id<MobXPDataListPacket> PAYLOAD_ID = new CustomPayload.Id<>(Identifier.of(MobXP.MOD_ID, "mob_xp_data_packet"));

    public static final PacketCodec<RegistryByteBuf, MobXPDataListPacket> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.collection(
                    ArrayList::new,
                    MobXPData.PACKET_CODEC
            ), MobXPDataListPacket::mobXPDataList,
            MobXPDataListPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
