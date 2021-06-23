package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmongenerations.api.events.battles.UseMoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class ZMovesListener {

    @SubscribeEvent
    public void onZMove (UseMoveEvent.UseZMoveEvent event) throws ObjectMappingException {

        Player player = (Player) event.getPixelmonWrapper().getPlayerOwner();
        if (!ConfigManager.getConfigNode(7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(7, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }

        if (TierHandler.restrictZMoves()) {

            if (!player.hasPermission(TierHandler.getZMovesPermission())) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFancyText(TierHandler.getZMovesMessage()));

            }

        }

    }

}
