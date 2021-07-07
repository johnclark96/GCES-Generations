package com.lypaka.gces.gottacatchemsmall;

import com.google.inject.Inject;
import com.lypaka.gces.gottacatchemsmall.Commands.AdminCommands;
import com.lypaka.gces.gottacatchemsmall.Commands.PlayerCommands;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Listeners.*;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "gottacatchemsmall",
        name = "GCS",
        version = "7.0.0-Generations"
)
public class GCES {

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    private PluginContainer container;

    @Inject
    public Logger logger;

    public static GCES instance;

    public static boolean isPixelSkillsLoaded = false;
    public static boolean isSpawnValidationLoaded = false;

    @Listener
    public void onPreInit (GamePreInitializationEvent event) throws IOException {

        logger.info("GCES is starting up! Let's hope this bitch doesn't set your server on fire.");
        instance = this;
        container = Sponge.getPluginManager().getPlugin("gottacatchemsmall").get();
        ConfigManager.setup(configDir);
        MinecraftForge.EVENT_BUS.register(new CatchListener());
        MinecraftForge.EVENT_BUS.register(new LevelingListener());
        MinecraftForge.EVENT_BUS.register(new TradeListener());
        MinecraftForge.EVENT_BUS.register(new EvolutionListener());
        MinecraftForge.EVENT_BUS.register(new ZMovesListener());
        MinecraftForge.EVENT_BUS.register(new BattleListener());
        MinecraftForge.EVENT_BUS.register(new SpawnsListener());
        Sponge.getEventManager().registerListeners(this, new TradeListener());
        Sponge.getEventManager().registerListeners(this, new JoinListener());
        registerCommands();
        if (Sponge.getPluginManager().getPlugin("pixelskills").isPresent()) {

            isPixelSkillsLoaded = true;
            logger.info("Detected PixelSkills! Activating Skill tier restrictions!");

            // If we load this with the other events, will probably throw error when PixelSkills is not present
            MinecraftForge.EVENT_BUS.register(new SkillsListener());

        }
        if (Sponge.getPluginManager().getPlugin("spawnvalidation").isPresent()) {

            isSpawnValidationLoaded = true;
            logger.info("Detected SpawnValidation, integrating...");

        }

    }

    private void registerCommands() {

        CommandSpec main = CommandSpec.builder()
                .child(AdminCommands.getReloadCommand(), "reload")
                .child(AdminCommands.getLevelUpCommand(), "lvlup", "levelup")
                .child(AdminCommands.getPermissionCommand(), "permission", "perm")
                .child(AdminCommands.getSetLevelCommand(), "setlvl", "setlevel")
                .child(AdminCommands.getGetLevelCommand(), "getlvl", "getlevel")
                .child(AdminCommands.getDifficultyCommands(), "difficulty", "diff")
                .child(PlayerCommands.getCheckCommand(), "check")
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

                }).build();

        Sponge.getCommandManager().register(this, main, "gces");

    }

    @Listener
    public void onReload (GameReloadEvent event) {

        ConfigManager.load();
        logger.info("GCES has reloaded!");

    }

    public static PluginContainer getContainer() {
        return instance.container;
    }

    public static Logger getLogger() {
        return instance.logger;
    }


}
