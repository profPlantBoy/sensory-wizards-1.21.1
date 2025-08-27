package net.profplantboy.sensorywizards.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.profplantboy.sensorywizards.spell.LearnedSpellsComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class UnlearnSpellCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("unlearn")
                .then(argument("spell", StringArgumentType.string())
                        .suggests(learnedSpellsSuggestionProvider())
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();
                            String spellId = StringArgumentType.getString(context, "spell");
                            LearnedSpellsComponent learnedSpells = ModComponents.LEARNED_SPELLS.get(player);

                            if (learnedSpells.hasSpell(spellId)) {
                                learnedSpells.unlearnSpell(spellId);
                                ModComponents.LEARNED_SPELLS.sync(player); // Add this line
                                context.getSource().sendFeedback(() -> Text.of("You have unlearned the " + spellId + " spell."), false);
                            } else {
                                context.getSource().sendError(Text.of("You do not know the " + spellId + " spell."));
                            }
                            return 1;
                        })
                )
        );
    }

    private static SuggestionProvider<ServerCommandSource> learnedSpellsSuggestionProvider() {
        return (context, builder) -> {
            LearnedSpellsComponent learnedSpells = ModComponents.LEARNED_SPELLS.get(context.getSource().getPlayer());
            return CommandSource.suggestMatching(learnedSpells.getSpells(), builder);
        };
    }
}