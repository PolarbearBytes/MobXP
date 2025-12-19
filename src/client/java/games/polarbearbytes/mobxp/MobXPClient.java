package games.polarbearbytes.mobxp;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.input.KeyInputHandler;
import games.polarbearbytes.mobxp.networking.MobXPClientNetworking;
import games.polarbearbytes.mobxp.networking.MobXPUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import java.util.HashMap;

public class MobXPClient implements ClientModInitializer {
	public static HashMap<String, MobXPData> mobXPData = null;
	@Override
	public void onInitializeClient() {
		MobXPClientNetworking.registerReceivers();
		KeyInputHandler.register();
	}

	public static void updateData(MobXPData data){
		mobXPData.put( data.id(), data );
		ClientPlayNetworking.send(new MobXPUpdatePacket(data));
	}
}