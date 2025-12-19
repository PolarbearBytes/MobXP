package games.polarbearbytes.mobxp.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Record for mob's xp details
 * @param id Minecraft entity id
 * @param experiencePoints the amount of experience points the mob should drop, if {@code enabled} is true but this is {@code null} Minecraft's default will be dropped
 * @param secondaryExperiencePoints the amount of experience points the mob should drop based on custom criteria, same as experiencePoints in regard to being null
 * @param enabled if enabled the custom experience points will be used, false the default will be used
 * @param random
 */
public record MobXPData(String id, @Nullable Integer experiencePoints, @Nullable Integer secondaryExperiencePoints, @Nullable Integer babyExperiencePoints, boolean enabled, boolean random, boolean useAdultXPForBaby) {
    public static final PacketCodec<RegistryByteBuf, MobXPData> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, MobXPData::id,
            PacketCodecs.optional(PacketCodecs.INTEGER)
                .xmap(
                        opt -> opt.orElse(null),
                        Optional::ofNullable
                ),
            MobXPData::experiencePoints,
            PacketCodecs.optional(PacketCodecs.INTEGER)
                    .xmap(
                            opt -> opt.orElse(null),
                            Optional::ofNullable
                    ),
            MobXPData::secondaryExperiencePoints,
            PacketCodecs.optional(PacketCodecs.INTEGER)
                    .xmap(
                            opt -> opt.orElse(null),
                            Optional::ofNullable
                    ),
            MobXPData::babyExperiencePoints,
            PacketCodecs.BOOLEAN, MobXPData::enabled,
            PacketCodecs.BOOLEAN, MobXPData::random,
            PacketCodecs.BOOLEAN, MobXPData::useAdultXPForBaby,
            MobXPData::new
    );

    /**
     * Helper function to get a readable name based on the Minecraft entity id, e.g. minecraft:ender_dragon becomes Ender Dragon
     * @return Name in readable format
     */
    public String getName(){
        return WordUtils.capitalizeFully(Identifier.of(id()).getPath().replace("_", " "));
    }
}