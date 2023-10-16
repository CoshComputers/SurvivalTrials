package com.dsd.st.commands;

import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.SurvivalTrialsConfig;
import com.dsd.st.util.CustomLogger;
import com.dsd.st.util.EnumTypes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ModConfigCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> survivaltrialsCommand = Commands.literal("survivaltrials");

        LiteralArgumentBuilder<CommandSource> toggleCommand = Commands.literal("toggle");
        toggleCommand.then(Commands.argument("option", StringArgumentType.string())
                .suggests(new ModConfigSuggestionProvider())
                .executes(context -> {
                    return toggleOption(context, StringArgumentType.getString(context, "option"));
                }));

        survivaltrialsCommand.then(toggleCommand);

        dispatcher.register(survivaltrialsCommand);
    }


    private static int toggleOption(CommandContext<CommandSource> context, String option) {
        SurvivalTrialsConfig.MainConfig config = ConfigManager.getInstance().getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getSurvivalTrialsMainConfig();
        EnumTypes.ModConfigOption configOption = EnumTypes.ModConfigOption.fromOptionName(option);
        boolean currentValue = false;
        boolean newValue = false;
        switch (configOption) {
            case OVERRIDE_MOBS:
                currentValue = config.isoverrideMobs();
                config.setoverrideMobs(!config.isoverrideMobs());
                newValue = config.isoverrideMobs();
                break;
            case SPAWN_GIANTS:
                currentValue = config.isSpawnGiants();
                config.setSpawnGiants(!config.isSpawnGiants());
                newValue = config.isSpawnGiants();
                break;
            case GIVE_INITIAL_GEAR:
                currentValue = config.isGiveInitialGear();
                config.setGiveInitialGear(!config.isGiveInitialGear());
                newValue = config.isGiveInitialGear();
                break;
            case GIVE_SPECIAL_LOOT:
                currentValue = config.isGiveSpecialLoot();
                config.setGiveSpecialLoot(!config.isGiveSpecialLoot());
                newValue = config.isGiveInitialGear();
                break;
            case USE_PLAYER_HEADS:
                currentValue = config.isUsePlayerHeads();
                config.setUsePlayerHeads(!config.isUsePlayerHeads());
                newValue = config.isUsePlayerHeads();
                break;
            case DEBUG_ON:
                currentValue = config.isDebugOn();
                config.setDebugOn(!config.isDebugOn());
                newValue = config.isDebugOn();
                break;
            default:
                context.getSource().sendFailure(new StringTextComponent("Invalid option"));
                return 0;
        }
        context.getSource().sendSuccess(new StringTextComponent("Toggled option " + option + " From " + currentValue + " To "+ newValue), true);
        if (!ConfigManager.getInstance().saveSurvivalTrialsConfig()) {
            PlayerEntity player = null;
            try {
                player = context.getSource().getPlayerOrException();
                CustomLogger.getInstance().sendPlayerMessage(player, "Option updated but Config File not saved");
            } catch (CommandSyntaxException e) {
                CustomLogger.getInstance().error(String.format("Failed to Find Player during Command Processing - %s", e));
            }
        }
        return 1;
    }
}

