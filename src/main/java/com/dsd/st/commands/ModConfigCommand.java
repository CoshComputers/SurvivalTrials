package com.dsd.st.commands;

import com.dsd.st.config.ConfigManager;
import com.dsd.st.util.EnumTypes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
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
        EnumTypes.ModConfigOption configOption = EnumTypes.ModConfigOption.fromOptionName(option);
        if (configOption == null) {
            context.getSource().sendFailure(new StringTextComponent("Invalid option"));
            return 0;
        }

        String resultString = ConfigManager.getInstance().toggleMainConfigOption(configOption);
        if (resultString != null) {
            context.getSource().sendSuccess(new StringTextComponent(resultString), true);
            return 1;
        } else {
            context.getSource().sendFailure(new StringTextComponent("Failed to toggle option " + option));
            return  0;
        }
    }
}

