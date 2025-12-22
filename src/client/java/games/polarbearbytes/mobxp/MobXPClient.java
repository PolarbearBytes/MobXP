package games.polarbearbytes.mobxp;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.input.KeyInputHandler;
import games.polarbearbytes.mobxp.networking.MobXPClientNetworking;
import games.polarbearbytes.mobxp.networking.MobXPUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class MobXPClient implements ClientModInitializer {
	public static final HashMap<String, MobXPData> mobXPData = new HashMap<>();
	@Override
	public void onInitializeClient() {
		MobXPClientNetworking.registerReceivers();
		KeyInputHandler.register();

		//On entering the world build the default mob list for the mob xp screen
		ClientEntityEvents.ENTITY_LOAD.register((entity,world)->{
			if( !entity.isPlayer() ) return;
			Registries.ENTITY_TYPE.forEach(entityType->{
				try {
					Identifier id = Registries.ENTITY_TYPE.getId(entityType);
					MobEntity mobEntity = (MobEntity) Registries.ENTITY_TYPE.get(id).create(world, SpawnReason.EVENT);
					if(mobEntity == null) return;

					MobXPData data = new MobXPData(id.toString(), -1, -1,-1, false, false, false);
					mobXPData.put(data.id(), data);
				} catch(Exception ignored){}
			});
		});
	}

	public static void updateData(MobXPData data){
		mobXPData.put( data.id(), data );
		ClientPlayNetworking.send(new MobXPUpdatePacket(data));
	}
}