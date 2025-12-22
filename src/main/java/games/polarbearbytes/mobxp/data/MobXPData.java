package games.polarbearbytes.mobxp.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import org.apache.commons.text.WordUtils;

/**
 * Record for mob's xp details
 * @param id Minecraft entity id
 * @param primaryXP the amount of experience points the mob should drop, if {@code enabled} is true but this is {@code null} Minecraft's default will be dropped
 * @param secondaryXP the amount of experience points the mob should drop based on custom criteria, same as experiencePoints in regard to being null
 * @param babyXP the amount of experience points the mob should drop based on custom criteria, same as experiencePoints in regard to being null
 * @param enabled if enabled the custom experience points will be used, false the default will be used
 * @param random if enabled the custom experience we be between 0 and the custom amount set
 * @param usePrimaryXPForBaby if enabled the primaryXP amount will be used instead of babyXP for baby variant of the mob
 */
public record MobXPData(String id, int primaryXP, int secondaryXP, int babyXP, boolean enabled, boolean random, boolean usePrimaryXPForBaby) {
    public static final Codec<MobXPData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(MobXPData::id),
            Codec.INT.fieldOf("primaryXP").forGetter(MobXPData::primaryXP),
            Codec.INT.fieldOf("secondaryXP").forGetter(MobXPData::secondaryXP),
            Codec.INT.fieldOf("babyXP").forGetter(MobXPData::babyXP),
            Codec.BOOL.fieldOf("enabled").forGetter(MobXPData::enabled),
            Codec.BOOL.fieldOf("random").forGetter(MobXPData::random),
            Codec.BOOL.fieldOf("usePrimaryXPForBaby").forGetter(MobXPData::usePrimaryXPForBaby)
        ).apply(instance, MobXPData::new));

    public static final PacketCodec<RegistryByteBuf, MobXPData> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, MobXPData::id,
            PacketCodecs.INTEGER, MobXPData::primaryXP,
            PacketCodecs.INTEGER, MobXPData::secondaryXP,
            PacketCodecs.INTEGER, MobXPData::babyXP,
            PacketCodecs.BOOLEAN, MobXPData::enabled,
            PacketCodecs.BOOLEAN, MobXPData::random,
            PacketCodecs.BOOLEAN, MobXPData::usePrimaryXPForBaby,
            MobXPData::new
    );

    public static MobXPData EMPTY(String id){
        return new MobXPData(id,-1,-1,-1,false,false,false);
    }

    /**
     * Helper function to get a readable name based on the Minecraft entity id, e.g. minecraft:ender_dragon becomes Ender Dragon
     * @return Name in readable format
     */
    public String getName(){
        return WordUtils.capitalizeFully(Identifier.of(id()).getPath().replace("_", " "));
    }
}