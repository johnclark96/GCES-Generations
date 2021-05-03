package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmongenerations.api.events.ExperienceGainEvent;
import com.pixelmongenerations.api.events.LevelUpEvent;
import com.pixelmongenerations.api.events.RareCandyEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.config.PixelmonConfig;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;

public class LevelingListener {

    @SubscribeEvent
    public void onLevelUp (LevelUpEvent event) throws ObjectMappingException {

        Player player = (Player) event.getPlayer();
        int pokeLevel = event.getPokemon().getLevel();
        int level = AccountHandler.getLevelTier(player);

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (!AccountHandler.hasPermission(player, TierHandler.getLevelPermission())) {

                if (!TierHandler.getLevelPermission().equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(0)));

                }

            } else {

                if (TierHandler.isLevelingSystemEnabled()) {

                    if (pokeLevel >= TierHandler.getMaxLvlLevel(level)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(level)));

                    }

                }

            }

        }


    }


    @SubscribeEvent
    public void onRareCandy (RareCandyEvent event) throws ObjectMappingException {

        Player player = (Player) event.getPlayer();
        int pokeLevel = event.getPokemon().level.getLevel();
        int level = AccountHandler.getLevelTier(player);

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (!AccountHandler.hasPermission(player, TierHandler.getLevelPermission())) {

                if (!TierHandler.getLevelPermission().equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(0)));

                }

            } else {

                if (TierHandler.isLevelingSystemEnabled()) {

                    if (pokeLevel >= TierHandler.getMaxLvlLevel(level)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(level)));

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onEXPGain (ExperienceGainEvent event) {

        Player player = (Player) event.getPokemon().getPlayerOwner();
        int level = AccountHandler.getLevelTier(player);
        int pokeLevel = event.getPokemon().getLevel();

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (TierHandler.isLevelingSystemEnabled()) {

                if (pokeLevel >= TierHandler.getMaxLvlLevel(level)) {

                    event.setExperience(0);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(level).replace("Pokemon", event.getPokemon().getBaseStats().pixelmonName)));

                }

            }

        }

    }

}
