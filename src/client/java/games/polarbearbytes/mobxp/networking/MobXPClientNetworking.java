package games.polarbearbytes.mobxp.networking;

import games.polarbearbytes.mobxp.MobXPClient;
import games.polarbearbytes.mobxp.gui.screens.MobXPListScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;

/**
 * Class for handling all the networking requests and actions to take
 */
public class MobXPClientNetworking {
    /**
     * Register all the packets that the client might receive
     */
    public static void registerReceivers(){
        ClientPlayNetworking.registerGlobalReceiver(MobXPDataListPacket.PAYLOAD_ID, MobXPClientNetworking::onDataListPacket);
    }

    /**
     * Action for handling the incoming data list packet, filter the list by entities that are instances of LivingEntity (actual mobs)
     * Still doesn't filter out things like Armor Stands, might need a better filter down the road
     * @param packet {@link MobXPDataListPacket} packet containing the mob xp details list
     * @param context Client context
     */
    public static void onDataListPacket(MobXPDataListPacket packet, Context context){
        context.client().execute(()->{
            MobXPClient.mobXPData = new HashMap<>();
            packet.mobXPDataList().forEach(data->{
                try {
                    MobEntity entity = (MobEntity) Registries.ENTITY_TYPE.get(Identifier.of(data.id())).create(MinecraftClient.getInstance().world, SpawnReason.EVENT);
                    if(entity != null){
                        MobXPClient.mobXPData.put(data.id(),data);
                    }
                } catch(Exception ignored) {

                }
            });
            Screen currentScreen = context.client().currentScreen;
            if(currentScreen instanceof MobXPListScreen) {
                ((MobXPListScreen) currentScreen).updateList(MobXPClient.mobXPData);
            }
        });
    }
}
