package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.pixelmongenerations.api.events.DynamaxEvent;
import com.pixelmongenerations.common.battle.controller.participants.PlayerParticipant;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.List;

public class DynamaxListener {

    @SubscribeEvent
    public void onDynamax (DynamaxEvent.EvolvingEvent event) throws ObjectMappingException {
//8
        if (event.getBattleParticipant() instanceof PlayerParticipant) {

            PlayerParticipant pp = (PlayerParticipant) event.getBattleParticipant();
            Player player = (Player) pp.player;

            if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

            int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

            boolean restrict = ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Restrict-Dynamaxing").getBoolean();
            if (!restrict) return;

            String permission = ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Unlock-Dynamaxing").getString();
            if (permission.equalsIgnoreCase("none")) return;


            if (!ConfigManager.getConfigNode(index, 7, "World-Blacklist").isEmpty()) {

                List<String> worlds = ConfigManager.getConfigNode(index, 7, "World-Blacklist").getList(TypeToken.of(String.class));
                World world = player.getWorld();
                if (worlds.contains(world.getName())) return;

            }
            if (!AccountHandler.hasPermission(player, permission, index)) {

                event.setCanceled(true);
                Text text = FancyText.getFancyText(ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Restriction-Message").getString());
                player.sendMessage(text);

            }

        }

    }

}
