package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmongenerations.api.events.battles.UseMoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;

public class ZMovesListener {

    @SubscribeEvent
    public void onZMove (UseMoveEvent.UseZMoveEvent event) {

        Player player = (Player) event.getPixelmonWrapper().getPlayerOwner();

        if (TierHandler.restrictZMoves()) {

            if (!player.hasPermission(TierHandler.getZMovesPermission())) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFancyText(TierHandler.getZMovesMessage()));

            }

        }

    }

}
