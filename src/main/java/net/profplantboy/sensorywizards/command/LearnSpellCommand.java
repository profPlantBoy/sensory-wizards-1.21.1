package net.profplantboy.sensorywizards.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.profplantboy.sensorywizards.spell.LearnedSpellsComponent;
import net.profplantboy.sensorywizards.spell.ModComponents;
import net.profplantboy.sensorywizards.spell.SpellRegistry;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LearnSpellCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("learn")
                // Require permission level 2 (operator) to use the command
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        // Subcommand for learning a specific spell
                        .then(argument("spell", StringArgumentType.string())
                                .suggests(allSpellsSuggestionProvider())
                                .executes(context -> {
                                    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                    String spellId = StringArgumentType.getString(context, "spell");
                                    LearnedSpellsComponent learnedSpells = ModComponents.LEARNED_SPELLS.get(player);

                                    if (SpellRegistry.getAllSpellIds().contains(spellId)) {
                                        learnedSpells.addSpell(spellId);
                                        ModComponents.LEARNED_SPELLS.sync(player);
                                        context.getSource().sendFeedback(() -> Text.literal("Taught " + spellId + " to " + player.getName().getString()), true);
                                    } else {
                                        context.getSource().sendError(Text.literal("Spell '" + spellId + "' not found."));
                                    }
                                    return 1;
                                })
                        )
                        // Subcommand for learning all spells
                        .then(literal("all")
                                .executes(context -> {
                                    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                    LearnedSpellsComponent learnedSpells = ModComponents.LEARNED_SPELLS.get(player);

                                    SpellRegistry.getAllSpellIds().forEach(learnedSpells::addSpell);
                                    ModComponents.LEARNED_SPELLS.sync(player);

                                    context.getSource().sendFeedback(() -> Text.literal("Taught all spells to " + player.getName().getString()), true);
                                    return 1;
                                })
                        )
                )
        );
    }

    // Provides suggestions for all available spells from the registry
    private static SuggestionProvider<ServerCommandSource> allSpellsSuggestionProvider() {
        return (context, builder) -> CommandSource.suggestMatching(SpellRegistry.getAllSpellIds(), builder);
    }
}