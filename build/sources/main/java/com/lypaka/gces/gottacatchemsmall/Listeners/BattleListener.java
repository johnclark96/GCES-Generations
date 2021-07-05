package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmongenerations.api.events.BattleStartEvent;
import com.pixelmongenerations.common.battle.controller.BattleControllerBase;
import com.pixelmongenerations.common.battle.controller.participants.PlayerParticipant;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class BattleListener {

    @SubscribeEvent
    public void onBattleStart (BattleStartEvent event) throws ObjectMappingException {

        BattleControllerBase bcb = event.getBattleController();

        if (bcb.participants.get(0) instanceof PlayerParticipant) {

            EntityPlayerMP fPlayer = (EntityPlayerMP) bcb.participants.get(0).getEntity();
            Player player = (Player) fPlayer;

            if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

            int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

            if (TierHandler.restrictBattles(index)) {

                if (!ConfigManager.getConfigNode(index, 7, "World-Blacklist").isEmpty()) {

                    List<String> worlds = ConfigManager.getConfigNode(index, 7, "World-Blacklist").getList(TypeToken.of(String.class));
                    World world = player.getWorld();
                    if (worlds.contains(world.getName())) return;

                }
                int level = AccountHandler.getLevelTier(player, index);
                int pokeLevel = TierHandler.getMaxLvlLevel(index, level);
                PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorage(fPlayer).get();

                for (int i = 0; i < party.count(); i++) {

                    EntityPixelmon pokemon = party.getPokemon(party.getIDFromPosition(i), fPlayer.getEntityWorld());

                    if (pokemon != null) {

                        int pokemonLevel = pokemon.level.getLevel();
                        if (pokemonLevel > pokeLevel) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getBattleMessage(index)));
                            break;

                        }

                    }

                }

            }

        }
        if (bcb.participants.get(1) instanceof PlayerParticipant) {

            EntityPlayerMP fPlayer = (EntityPlayerMP) bcb.participants.get(1).getEntity();
            Player player = (Player) fPlayer;

            if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

            int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

            if (TierHandler.restrictBattles(index)) {

                if (!ConfigManager.getConfigNode(index, 7, "World-Blacklist").isEmpty()) {

                    List<String> worlds = ConfigManager.getConfigNode(index, 7, "World-Blacklist").getList(TypeToken.of(String.class));
                    World world = player.getWorld();
                    if (worlds.contains(world.getName())) return;

                }
                int level = AccountHandler.getLevelTier(player, index);
                int pokeLevel = TierHandler.getMaxLvlLevel(index, level);

                PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorage(fPlayer).get();

                for (int i = 0; i < party.count(); i++) {

                    EntityPixelmon pokemon = party.getPokemon(party.getIDFromPosition(i), fPlayer.getEntityWorld());

                    if (pokemon != null) {

                        int pokemonLevel = pokemon.level.getLevel();
                        if (pokemonLevel > pokeLevel) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getBattleMessage(index)));
                            break;

                        }

                    }

                }

            }

        }

    }

}
