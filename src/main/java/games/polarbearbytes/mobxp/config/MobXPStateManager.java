package games.polarbearbytes.mobxp.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * State manager for maintaining the custom mob xp
 */
public class MobXPStateManager extends PersistentState {
    private HashMap<String, MobXPData> mobXPDataList = new HashMap<>();

    public static final Codec<MobXPStateManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(
                    Codec.STRING,
                    MobXPData.CODEC
            ).fieldOf("mobXPDataList").forGetter(MobXPStateManager::getRawMap)
    ).apply(instance, MobXPStateManager::new));

    public static final PersistentStateType<MobXPStateManager> TYPE = new PersistentStateType<>("mob_xp_data",MobXPStateManager::new, CODEC, DataFixTypes.PLAYER);

    public MobXPStateManager(){}

    public MobXPStateManager(Map<String, MobXPData> mobXPDataList){
        this.mobXPDataList = new HashMap<>();
        this.mobXPDataList.putAll(mobXPDataList);
    }

    public static MobXPStateManager get(MinecraftServer server){
        return server.getOverworld().getPersistentStateManager().getOrCreate(TYPE);
    }

    private Map<String, MobXPData> getRawMap(){
        return this.mobXPDataList;
    }

    /**
     * Updates the mob state data in our hashmap.
     *
     * @param mobData The mob xp details
     */
    public void updateState(MobXPData mobData){
        this.mobXPDataList.put(mobData.id(),mobData);
        markDirty();
    }

    /**
     * Get the xp data of a mob
     *
     * @param id The id of the mob
     * @return MobXPData the mob's xp details
     */
    public MobXPData getMobData(String id){
        return this.mobXPDataList.getOrDefault(id, MobXPData.EMPTY(id));
    }

    public List<MobXPData> getList(){
        return mobXPDataList.values().stream().toList();
    }
}

