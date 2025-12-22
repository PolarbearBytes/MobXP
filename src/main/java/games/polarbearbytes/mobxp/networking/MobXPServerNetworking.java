package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

import static games.polarbearbytes.mobxp.MobXP.hasManageXPPermission;

public class MobXPServerNetworking {
    public static void register(){
        registerPackets();
        registerReceivers();
    }

    protected static void registerPackets(){
        PayloadTypeRegistry.playC2S().register(MobXPDataRequestPacket.PAYLOAD_ID, MobXPDataRequestPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(MobXPDataListPacket.PAYLOAD_ID, MobXPDataListPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(MobXPUpdatePacket.PAYLOAD_ID, MobXPUpdatePacket.PACKET_CODEC);
    }
    protected static void registerReceivers(){
        ServerPlayNetworking.registerGlobalReceiver(MobXPDataRequestPacket.PAYLOAD_ID, MobXPServerNetworking::onDataRequest);
        ServerPlayNetworking.registerGlobalReceiver(MobXPUpdatePacket.PAYLOAD_ID, MobXPServerNetworking::onUpdateRequest);
    }

    protected static void onDataRequest(MobXPDataRequestPacket payload, Context context){
        MinecraftServer server = context.server();
        ServerPlayerEntity player = context.player();

        if(!hasManageXPPermission(player, server)) {
            player.sendMessage(Text.of(
                    "You do not have permission to manage mob xp"
            ));
        } else {
            List<MobXPData> dataList = MobXPStateManager.get(server).getList();

            ServerPlayNetworking.send(player,new MobXPDataListPacket( dataList ));
        }
    }

    protected static void onUpdateRequest(MobXPUpdatePacket payload, Context context){
        MinecraftServer server = context.server();
        ServerPlayerEntity player = context.player();

        if(!hasManageXPPermission(player, server)) {
            player.sendMessage(Text.of(
                    "You do not have permission to manage mob xp"
            ));
        } else {
            MobXPData data = payload.data();
            MobXPStateManager.get(server).updateState(data);
        }
    }
}
