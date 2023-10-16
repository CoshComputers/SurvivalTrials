package com.dsd.st.commands;

import com.dsd.st.util.EnumTypes;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class ModConfigSuggestionProvider implements SuggestionProvider {

    // This method provides suggestions for the toggle command
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext source, SuggestionsBuilder builder) throws CommandSyntaxException {
        String currentInput = builder.getRemaining().toLowerCase();
        for (EnumTypes.ModConfigOption option : EnumTypes.ModConfigOption.values()) {
            if (option.getOptionName().toLowerCase().startsWith(currentInput)) {
                builder.suggest(option.getOptionName());
            }
        }
        return builder.buildFuture();
    }
}
