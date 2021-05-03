package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmongenerations.api.enums.ReceiveType;
import com.pixelmongenerations.api.events.PixelmonReceivedEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.pixelmon.stats.Moveset;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;

public class TradeListener {

    @Listener
    public void onInteract (InteractBlockEvent.Secondary.MainHand event, @Root Player player) throws ObjectMappingException {

        if (event.getTargetBlock().getState().getType().getName().contains("trade_machine")) {

            if (!AccountHandler.hasPermission(player, TierHandler.getTradePerm())) {

                if (!TierHandler.getTradePerm().equalsIgnoreCase("none")) {

                    event.setCancelled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getTradeMessage()));

                }

            }

        }

    }

    // Could *technically* do this on the trade event but I figured it would be easier to just do it on the received event so don't have to get and apply both players at once
    // Plus you would have to code in more conditions for NPC trades and player trades and all that, so just easier to do it this way in my opinion

    @SubscribeEvent
    public void onTrade (PixelmonReceivedEvent event) throws ObjectMappingException {

        if (TierHandler.areTradesModified()) {

            if (event.getReceiveType().equals(ReceiveType.Trade)) {

                Player player = (Player) event.getPlayer();
                int lvl = event.getPokemon().getLvl().getLevel();
                EntityPixelmon pokemon = event.getPokemon();

                int playerLevel;
                if (TierHandler.getTierBase().equals("Catching")) {

                    playerLevel = AccountHandler.getCatchTier(player);

                } else {

                    playerLevel = AccountHandler.getLevelTier(player);

                }

                int lvlMax = getMaxLvl(TierHandler.getTierBase(), playerLevel);
                if (lvl > lvlMax) {

                    pokemon.level.setLevel(lvlMax);
                    pokemon.updateStats();
                    player.sendMessage(FancyText.getFancyText(TierHandler.getTradeMessage()));

                }

            }

        }

        if (TierHandler.restrictLegendaries()) {

            Player player = (Player) event.getPlayer();
            EntityPixelmon pokemon = event.getPokemon();

            if (!AccountHandler.hasPermission(player, TierHandler.getLegendaryPermission())) {

                for (String name : EnumSpecies.legendaries) {

                    if (name.equalsIgnoreCase(pokemon.getPokemonName())) {

                        event.getPokemon().setMoveset(new Moveset());
                        player.sendMessage(FancyText.getFancyText(TierHandler.getLegendaryMessage()));
                        break;

                    }

                }

            }

        }

    }

    private static int getMaxLvl (String value, int num) {

        if (value.equals("Catching")) {

            return TierHandler.getMaxCatchLevel(num);

        } else {

            return TierHandler.getMaxLvlLevel(num);

        }

    }

}
