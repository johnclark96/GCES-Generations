package com.lypaka.gces.gottacatchemsmall.Utils;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountHandler {


    /**--------------------------------Getters--------------------------------**/

    public static int getCatchTier (Player player) {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Levels", "Catching-Tier").getInt();

    }


    public static int getLevelTier (Player player) {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Leveling-Tier").getInt();

    }

    public static List<String> getPermissions (Player player) throws ObjectMappingException {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class));

    }

    /**----------------------------------Setters------------------------------**/


    public static void setCatchTier (Player player, int tier) {

        ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Catching-Tier").setValue(tier);

    }

    public static void setLevelTier (Player player, int number) {

        ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Leveling-Tier").setValue(number);

    }

    public static void addPermission (Player player, String permission) throws ObjectMappingException {


        ArrayList<String> list = new ArrayList<String>(ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class)));
        list.add(permission);
        ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").setValue(list);

    }

    public static void removePermission (Player player, String permission) throws ObjectMappingException, IOException {

        ArrayList<String> list = new ArrayList<String>(ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class)));
        list.remove(permission);
        ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").setValue(list);

    }

    /**----------------------------------Misc--------------------------------**/

    public static boolean hasPermission (Player player, String permission) throws ObjectMappingException {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class)).contains(permission);

    }

    public static void levelUpCatchingTier (Player player) throws ObjectMappingException {

        int level = ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Catching-Tier").getInt();
        if (level < TierHandler.getMaxTierLevel("Catching")) {
            ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Catching-Tier").setValue(level + 1);
            ConfigManager.savePlayer(player.getUniqueId());
        }
    }

    public static void levelUpLevelingTier (Player player) throws ObjectMappingException {

        int level = ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Leveling-Tier").getInt();
        if (level < TierHandler.getMaxTierLevel("Leveling")) {
            ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Leveling-Tier").setValue(level + 1);
            ConfigManager.savePlayer(player.getUniqueId());
        }


    }

    public static void levelUpEvolvingTier (Player player) throws ObjectMappingException, IOException {

        if (getPermissions(player).contains("gces.evolving.middle")) {
            removePermission(player, "gces.evolving.middle");
            addPermission(player, "gces.evolving.final");
        }

    }

    public static void levelUpCatchingEvoStage (Player player) throws ObjectMappingException, IOException {

        if (getPermissions(player).contains("gces.catching.firststage")) {
            removePermission(player, "gces.catching.firststage");
            addPermission(player, "gces.catching.middlestage");
        } else if (getPermissions(player).contains("gces.catching.middlestage")) {
            removePermission(player, "gces.catching.middlestage");
            addPermission(player, "gces.catching.finalstage");
        }

    }

}
