package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmongenerations.api.events.EvolveEvent;
import com.pixelmongenerations.api.events.MegaEvolutionEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public class EvolutionListener {

    @SubscribeEvent
    public void onEvolution (EvolveEvent.PreEvolve event) throws ObjectMappingException {

        Player player = (Player) event.getPlayer();
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

        if (!ConfigManager.getConfigNode(index, 7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 7, "World-Blacklist").getList(TypeToken.of(String.class));
            org.spongepowered.api.world.World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        if (TierHandler.areEvolutionsRestricted(index)) {

            if (!TierHandler.getEvoPermission(index).equalsIgnoreCase("none")) {

                if (!AccountHandler.hasPermission(player, TierHandler.getEvoPermission(index), index)) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getEvoRestrictionMessage(index)));

                }

            } else {

                if (!AccountHandler.hasPermission(player, "gces.evolving." + TierHandler.getEvoStage(event.getPostEvolutionSpec().create((World) Sponge.getServer().getWorlds().iterator().next())).toLowerCase() + "stage", index)) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getEvoRestrictionMessage(index)));

                }

            }

        }

    }

    @SubscribeEvent
    public void onMega (MegaEvolutionEvent event) throws ObjectMappingException {

        Player player = (Player) event.getPokemon().getPlayerOwner();

        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

        if (!ConfigManager.getConfigNode(index, 7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 7, "World-Blacklist").getList(TypeToken.of(String.class));
            org.spongepowered.api.world.World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }

        if (TierHandler.areMegasRestricted(index)) {

            if (!AccountHandler.hasPermission(player, TierHandler.getMegaPerm(index), index)) {

                if (!TierHandler.getMegaPerm(index).equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getMegaMessage(index)));

                }

            }

        }

    }

}
