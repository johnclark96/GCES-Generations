package com.lypaka.gces.gottacatchemsmall.Listeners;

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

public class EvolutionListener {

    @SubscribeEvent
    public void onEvolution (EvolveEvent.PreEvolve event) throws ObjectMappingException {

        Player player = (Player) event.getPlayer();

        if (TierHandler.areEvolutionsRestricted()) {

            if (!TierHandler.getEvoPermission().equalsIgnoreCase("none")) {

                if (!AccountHandler.hasPermission(player, TierHandler.getEvoPermission())) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getEvoRestrictionMessage()));

                }

            } else {

                if (!AccountHandler.hasPermission(player, "gces.evolving." + TierHandler.getEvoStage(event.getPostEvolutionSpec().create((World) Sponge.getServer().getWorlds().iterator().next())).toLowerCase() + "stage")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getEvoRestrictionMessage()));

                }

            }

        }

    }

    @SubscribeEvent
    public void onMega (MegaEvolutionEvent event) throws ObjectMappingException {

        Player player = (Player) event.getPokemon().getPlayerOwner();

        if (TierHandler.areMegasRestricted()) {

            if (!AccountHandler.hasPermission(player, TierHandler.getMegaPerm())) {

                if (!TierHandler.getMegaPerm().equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getMegaMessage()));

                }

            }

        }

    }

}
