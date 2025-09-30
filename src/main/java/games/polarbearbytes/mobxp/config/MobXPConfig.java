package games.polarbearbytes.mobxp.config;

import java.util.HashMap;
import java.util.Map;

public class MobXPConfig {
    public int dragonXP = 12000;
    public int firstDragonXP = 12000;

    public record MobXPData(boolean enabled, int experiencePoints, boolean random) {}

    public final Map<String, MobXPData> xp = new HashMap<>();
    public MobXPConfig(){
        xp.put("minecraft:allay", new MobXPData(false, 0, false));
        xp.put("minecraft:armadillo", new MobXPData(false, 3, false));
        xp.put("minecraft:axolotl", new MobXPData(false, 3, false));
        xp.put("minecraft:bat", new MobXPData(false, 0, false));
        xp.put("minecraft:camel", new MobXPData(false, 3, false));
        xp.put("minecraft:cat", new MobXPData(false, 3, false));
        xp.put("minecraft:chicken", new MobXPData(false, 3, false));
        xp.put("minecraft:cod", new MobXPData(false, 3, false));
        xp.put("minecraft:cow", new MobXPData(false, 3, false));
        xp.put("minecraft:donkey", new MobXPData(false, 3, false));
        xp.put("minecraft:frog", new MobXPData(false, 3, false));
        xp.put("minecraft:glow_squid", new MobXPData(false, 3, false));
        xp.put("minecraft:happy_ghast", new MobXPData(false, 3, false));
        xp.put("minecraft:horse", new MobXPData(false, 3, false));
        xp.put("minecraft:mooshroom", new MobXPData(false, 3, false));
        xp.put("minecraft:mule", new MobXPData(false, 3, false));
        xp.put("minecraft:ocelot", new MobXPData(false, 3, false));
        xp.put("minecraft:parrot", new MobXPData(false, 3, false));
        xp.put("minecraft:pig", new MobXPData(false, 3, false));
        xp.put("minecraft:pufferfish", new MobXPData(false, 3, false));
        xp.put("minecraft:rabbit", new MobXPData(false, 3, false));
        xp.put("minecraft:salmon", new MobXPData(false, 3, false));
        xp.put("minecraft:sheep", new MobXPData(false, 3, false));
        xp.put("minecraft:skeleton_horse", new MobXPData(false, 3, false));
        xp.put("minecraft:sniffer", new MobXPData(false, 3, false));
        xp.put("minecraft:snow_golem", new MobXPData(false, 0, false));
        xp.put("minecraft:squid", new MobXPData(false, 3, false));
        xp.put("minecraft:strider", new MobXPData(false, 3, false));
        xp.put("minecraft:tadpole", new MobXPData(false, 0, false));
        xp.put("minecraft:tropical_fish", new MobXPData(false, 3, false));
        xp.put("minecraft:turtle", new MobXPData(false, 3, false));
        xp.put("minecraft:villager", new MobXPData(false, 0, false));
        xp.put("minecraft:wandering_trader", new MobXPData(false, 0, false));

        xp.put("minecraft:bee", new MobXPData(false, 3, false));
        xp.put("minecraft:cave_spider", new MobXPData(false, 5, false));
        xp.put("minecraft:dolphin", new MobXPData(false, 3, false));
        xp.put("minecraft:drowned", new MobXPData(false, 5, false));
        xp.put("minecraft:enderman", new MobXPData(false, 5, false));
        xp.put("minecraft:fox", new MobXPData(false, 3, false));
        xp.put("minecraft:goat", new MobXPData(false, 3, false));
        xp.put("minecraft:iron_golem", new MobXPData(false, 0, false));
        xp.put("minecraft:llama", new MobXPData(false, 3, false));
        xp.put("minecraft:panda", new MobXPData(false, 3, false));
        xp.put("minecraft:piglin", new MobXPData(false, 5, false));
        xp.put("minecraft:polar_bear", new MobXPData(false, 3, false));
        xp.put("minecraft:spider", new MobXPData(false, 5, false));
        xp.put("minecraft:trader_llama", new MobXPData(false, 3, false));
        xp.put("minecraft:wolf", new MobXPData(false, 3, false));
        xp.put("minecraft:zombified_piglin", new MobXPData(false, 6, false));


        xp.put("minecraft:blaze", new MobXPData(false, 10, false));
        xp.put("minecraft:bogged", new MobXPData(false, 5, false));
        xp.put("minecraft:breeze", new MobXPData(false, 10, false));
        xp.put("minecraft:creaking", new MobXPData(false, 0, false));
        xp.put("minecraft:creeper", new MobXPData(false, 5, false));
        xp.put("minecraft:elder_guardian", new MobXPData(false, 5, false));
        xp.put("minecraft:endermite", new MobXPData(false, 3, false));
        xp.put("minecraft:ender_dragon", new MobXPData(false, 500, false));
        xp.put("minecraft:evoker", new MobXPData(false, 10, false));
        xp.put("minecraft:ghast", new MobXPData(false, 5, false));
        xp.put("minecraft:guardian", new MobXPData(false, 10, false));
        xp.put("minecraft:hoglin", new MobXPData(false, 5, false));
        xp.put("minecraft:husk", new MobXPData(false, 5, false));
        xp.put("minecraft:magma_cube", new MobXPData(false, 5, false));
        xp.put("minecraft:phantom", new MobXPData(false, 5, false));
        xp.put("minecraft:piglin_brute", new MobXPData(false, 20, false));
        xp.put("minecraft:pillager", new MobXPData(false, 5, false));
        xp.put("minecraft:ravager", new MobXPData(false, 20, false));
        xp.put("minecraft:shulker", new MobXPData(false, 5, false));
        xp.put("minecraft:silverfish", new MobXPData(false, 5, false));
        xp.put("minecraft:skeleton", new MobXPData(false, 5, false));
        xp.put("minecraft:slime", new MobXPData(false, 5, false));
        xp.put("minecraft:stray", new MobXPData(false, 5, false));
        xp.put("minecraft:vex", new MobXPData(false, 3, false));
        xp.put("minecraft:vindicator", new MobXPData(false, 5, false));
        xp.put("minecraft:warden", new MobXPData(false, 5, false));
        xp.put("minecraft:witch", new MobXPData(false, 5, false));
        xp.put("minecraft:wither", new MobXPData(false, 50, false));
        xp.put("minecraft:wither_skeleton", new MobXPData(false, 5, false));
        xp.put("minecraft:zoglin", new MobXPData(false, 5, false));
        xp.put("minecraft:zombie", new MobXPData(false, 5, false));
        xp.put("minecraft:zombie_villager", new MobXPData(false, 5, false));

        xp.put("minecraft:giant", new MobXPData(false, 5, false));
        xp.put("minecraft:zombie_horse", new MobXPData(false, 3, false));
        xp.put("minecraft:illusioner", new MobXPData(false, 5, false));
    }


}