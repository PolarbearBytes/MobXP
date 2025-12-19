package games.polarbearbytes.mobxp;

import games.polarbearbytes.mobxp.config.ConfigManager;
import games.polarbearbytes.mobxp.networking.MobXPServerNetworking;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobXP implements ModInitializer {
	public static final String MOD_ID = "mobxp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MobXPServerNetworking.register();
		ConfigManager.loadConfig();
	}

	public static boolean hasManageXPPermission(ServerPlayerEntity player, MinecraftServer server) {
		boolean singlePlayerHost = server.isSingleplayer() && server.getPlayerManager().isOperator(player.getPlayerConfigEntry());
		boolean serverPlayerOP = !server.isSingleplayer() && server.getPlayerManager().isOperator(player.getPlayerConfigEntry());

		return singlePlayerHost || serverPlayerOP || Permissions.check(player, MobXP.MOD_ID+".manageXP");
	}
}