package games.polarbearbytes.mobxp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.permission.PermissionPredicate;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Commands for changing the mob xp details
 */
public class MobXPCommands {
    private enum XPtype {
        PRIMARY,
        SECONDARY,
        BABY
    }

    private enum XPFlag {
        ENABLED,
        RANDOM,
        BABYUSESPRIMARY
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("mobxp")
                        .requires(source -> {
                            ServerPlayerEntity entity = source.getPlayer();
                            PermissionPredicate perms = source.getPermissions();
                            return CommandManager.OWNERS_CHECK.allows(perms) ||
                                    (entity != null && Permissions.check(entity, "mobxp.manageXP"));
                        })
                        .then(
                                CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE))
                                        .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
                                        .executes(
                                                context -> showDetails(
                                                        context.getSource(),
                                                        RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity")
                                                )
                                        )
                                        .then(
                                                CommandManager.literal("primaryXP")
                                                        .then(
                                                                CommandManager.argument("xp", IntegerArgumentType.integer())
                                                                        .executes(context->updateXP(
                                                                                    context.getSource(),
                                                                                    RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"),
                                                                                    XPtype.PRIMARY,
                                                                                    IntegerArgumentType.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                CommandManager.literal("secondaryXP")
                                                        .then(
                                                                CommandManager.argument("xp", IntegerArgumentType.integer())
                                                                        .executes(context->updateXP(
                                                                                    context.getSource(),
                                                                                    RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"),
                                                                                    XPtype.SECONDARY,
                                                                                    IntegerArgumentType.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                CommandManager.literal("babyXP")
                                                        .then(
                                                                CommandManager.argument("xp", IntegerArgumentType.integer())
                                                                        .executes(context->updateXP(
                                                                                        context.getSource(),
                                                                                        RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"),
                                                                                        XPtype.BABY,
                                                                                        IntegerArgumentType.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                CommandManager.literal("enabled")
                                                        .then(
                                                                CommandManager.argument("value", BoolArgumentType.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.ENABLED,
                                                                                        BoolArgumentType.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                CommandManager.literal("random")
                                                        .then(
                                                                CommandManager.argument("value", BoolArgumentType.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.RANDOM,
                                                                                        BoolArgumentType.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                CommandManager.literal("usePrimaryXPForBaby")
                                                        .then(
                                                                CommandManager.argument("value", BoolArgumentType.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.BABYUSESPRIMARY,
                                                                                        BoolArgumentType.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int showDetails(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType) throws CommandSyntaxException {
        MobXPData data = MobXPStateManager.get(source.getServer()).getMobData(entityType.getIdAsString());

        String primaryXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.primaryXP());
        String secondaryXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.secondaryXP());
        String babyXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.babyXP());

        source.sendFeedback(() -> Text.translatable("mobxp.commands.mobxp.details", data.getName(), primaryXP, secondaryXP, babyXP, data.enabled(), data.random(), data.usePrimaryXPForBaby()), false);
        return 1;
    }

    private static int updateXP(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType, XPtype xptype, int xp) throws CommandSyntaxException {
        MobXPData oData = MobXPStateManager.get(source.getServer()).getMobData(entityType.getIdAsString());

        MobXPData data = switch(xptype){
            case XPtype.PRIMARY -> new MobXPData(oData.id(),xp,oData.secondaryXP(),oData.babyXP(),oData.enabled(),oData.random(),oData.usePrimaryXPForBaby());
            case XPtype.SECONDARY -> new MobXPData(oData.id(),oData.primaryXP(),xp,oData.babyXP(),oData.enabled(),oData.random(),oData.usePrimaryXPForBaby());
            case XPtype.BABY -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),xp,oData.enabled(),oData.random(),oData.usePrimaryXPForBaby());
        };

        MobXPStateManager.get(source.getServer()).updateState(data);
        source.sendFeedback(() -> Text.translatable("mobxp.commands.mobxp.xpupdate", data.getName(), xptype.toString(), xp), false);
        return 1;
    }

    private static int updateFlag(ServerCommandSource source, RegistryEntry.Reference<EntityType<?>> entityType, XPFlag xpflag, boolean enabled) throws CommandSyntaxException {
        MobXPData oData = MobXPStateManager.get(source.getServer()).getMobData(entityType.getIdAsString());

        MobXPData data = switch(xpflag){
            case XPFlag.ENABLED -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),enabled,oData.random(),oData.usePrimaryXPForBaby());
            case XPFlag.RANDOM -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.enabled(),enabled,oData.usePrimaryXPForBaby());
            case XPFlag.BABYUSESPRIMARY -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.enabled(),oData.random(),enabled);
        };

        MobXPStateManager.get(source.getServer()).updateState(data);
        source.sendFeedback(() -> Text.translatable("mobxp.commands.mobxp.flagupdate", data.getName(), xpflag.toString(), enabled), false);
        return 1;
    }


}