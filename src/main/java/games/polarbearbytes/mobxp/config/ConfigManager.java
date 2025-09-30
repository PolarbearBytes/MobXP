package games.polarbearbytes.mobxp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import games.polarbearbytes.mobxp.MobXP;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "mobxp.json");

    private static MobXPConfig config = null;

    public static void loadConfig() {
        if (!CONFIG_FILE.exists()) {
            config = new MobXPConfig(); // default
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, MobXPConfig.class);
            } catch (IOException e) {
                MobXP.LOGGER.error(e.getMessage());
                config = new MobXPConfig(); // fallback
            }
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            MobXP.LOGGER.warn(e.getMessage());
        }
    }

    public static MobXPConfig getConfig() {
        if(config == null){
            ConfigManager.loadConfig();
        }
        return config;
    }
}
