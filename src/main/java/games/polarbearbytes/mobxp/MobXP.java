package games.polarbearbytes.mobxp;

import games.polarbearbytes.mobxp.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobXP implements ModInitializer {
	public static final String MOD_ID = "mobxp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();
	}
}