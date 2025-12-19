package games.polarbearbytes.mobxp.config;

import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mod's configuration settings class
 */
public class MobXPConfig {
    @Deprecated
    @Nullable
    public Integer dragonXP;
    @Deprecated
    @Nullable
    public Integer firstDragonXP;

    private final Map<String, MobXPData> xp = new HashMap<>();

    public MobXPConfig(){
        // Build mob xp details from all EntityType, using null for experience points to default to Minecraft's default amount
        Registries.ENTITY_TYPE.forEach(entityType->{
            try {
                MobXPData data;
                if(entityType == EntityType.ENDER_DRAGON) {
                    data = new MobXPData(Registries.ENTITY_TYPE.getId(entityType).toString(), 12000, 500, null,false, false, false);
                } else {
                    data = new MobXPData(Registries.ENTITY_TYPE.getId(entityType).toString(), null, null,null, false, false, false);
                }
                xp.put(data.id(), data);
            } catch(Exception ignored){

            }
        });
    }

    public void updateMobXP(MobXPData data){
        xp.put(data.id(), data);
        ConfigManager.saveConfig();
    }

    public MobXPData get(String id){
        return xp.get(id);
    }

    public List<MobXPData> toList(){
        return new ArrayList<>(xp.values());
    }

}