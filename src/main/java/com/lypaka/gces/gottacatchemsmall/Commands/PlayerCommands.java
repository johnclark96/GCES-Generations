package com.lypaka.gces.gottacatchemsmall.Commands;

import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class PlayerCommands {

    public static CommandSpec getCheckCommand() {

        return CommandSpec.builder()
                .permission("gces.command.check")
                .arguments(
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .executor((sender, context) -> {

                    Player player;
                    if (context.getOne("player").isPresent() && sender.hasPermission("gces.command.check.others")) {

                        player = (Player) context.getOne("player").get();

                    } else {

                        player = (Player) sender;

                    }
                    int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
                    int catchLvl = 0;
                    try {
                        catchLvl = AccountHandler.getCatchTier(player, index);
                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }
                    int levelLvl = 0;
                    try {
                        levelLvl = AccountHandler.getLevelTier(player, index);
                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(Text.of(TextColors.YELLOW, "Catch level = " + catchLvl + ". Level level = " + levelLvl));

                    return CommandResult.success();
                })
                .build();

    }

    public static CommandSpec getHelpCommand() {

        return CommandSpec.builder()
                .permission("gces.command.help")
                .executor((sender, context) -> {

                    Text headerCommandText = Text.of(TextColors.YELLOW, TextStyles.BOLD, "======== [ GCES Help Command ] ========");
                    Text helpUserCommandText = Text.of(TextColors.RED, TextStyles.BOLD, "/gces check", TextColors.GREEN, " - Checks users stats", TextColors.RED, TextStyles.BOLD, "\n/gces levelup", TextColors.GREEN, " - Levels up the user", TextColors.RED, TextStyles.BOLD, "\n/gces setlvl", TextColors.GREEN, " - Sets the users Level", TextColors.RED, TextStyles.BOLD, "\n/gces getlevel", TextColors.GREEN, " - Gets users level", TextColors.RED, TextStyles.BOLD, "\n/gces difficulty", TextColors.GREEN, " - Difficulty Subcommand");
                    Text helpAdminCommandText = Text.of(TextColors.DARK_RED, TextStyles.BOLD, "/gces check <playername> ", TextColors.GREEN, " - Checks other users stats", TextColors.DARK_RED, TextStyles.BOLD, "\n/gces addperm", TextColors.GREEN, " - Adds User Permission" , TextColors.DARK_RED, TextStyles.BOLD, "\n/gces removeperm", TextColors.GREEN, " - Removes user permission", TextColors.DARK_RED, TextStyles.BOLD, "\n/gces reload", TextColors.GREEN, " - Reloads the GCES Plugin");
                    Text footerCommandText = Text.of(TextColors.YELLOW, TextStyles.BOLD, "=======================================");

                    sender.sendMessage(headerCommandText);
                    sender.sendMessage(helpUserCommandText);
                    sender.sendMessage(helpAdminCommandText);
                    sender.sendMessage(footerCommandText);

                    return CommandResult.success();
                })
                .build();

    }


}
