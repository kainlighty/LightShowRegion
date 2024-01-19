package ru.kainlight.lightshowregion.COMMON;

import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.kainlight.lightshowregion.Main;

import java.util.ArrayList;
import java.util.List;

public final class RegionManager {

    private final Main plugin;

    public RegionManager(Main plugin) {
        this.plugin = plugin;
    }

    public String sendRegionName(Player player) {
        if(player == null) return "";
        List<String> names = new ArrayList<>();

        boolean hideGlobalRegion = plugin.getConfig().getBoolean("region-settings.hide-global-region");
        String globalBarMessage = plugin.getMessageConfig().getConfig().getString("actionbar.global");

        String region = getRegion(player);
        if (!region.equals("")) {
            String regionCustomName = getCustomRegionName(region);
            if (regionCustomName != null && !regionCustomName.isEmpty()) {

                List<String> blacklist = plugin.getConfig().getStringList("region-settings.blacklist");
                String blacklistBarMessage = plugin.getMessageConfig().getConfig().getString("actionbar.blacklisted");
                String regionBarMessage = plugin.getMessageConfig().getConfig().getString("actionbar.region");

                if (blacklist.contains(region)) {
                    return blacklistBarMessage;
                }

                names.add(regionCustomName);
                return regionCustomName.startsWith("!")
                        ? regionCustomName.substring(1)
                        : regionBarMessage.replace("<region>", regionCustomName);

            } else {
                return compareRegionPlayers(player, region);
            }
        }

        return names.isEmpty() && !hideGlobalRegion
                ? globalBarMessage
                : StringUtils.join(names, ", ");
    }


    private String compareRegionPlayers(Player player, String regionName) {
        String notYour = plugin.getMessageConfig().getConfig().getString("actionbar.not-your");
        String your = plugin.getMessageConfig().getConfig().getString("actionbar.your");

        String regionOwner = getRegionOwner(player);
        String regionMembers = getRegionMembers(player);

        if(regionOwner.contains(player.getName()) || regionMembers.contains(player.getName())) {
            return your.replace("<name>", regionName).replace("<owners>", regionOwner);
        } else {
            return notYour.replace("<name>", regionName).replace("<owners>", regionOwner);
        }
    }

    private String getCustomRegionName(String region) {
        ConfigurationSection customRegions = plugin.getRegionsConfig().getConfig().getConfigurationSection("custom.");
        if(customRegions == null) return null;

        if (customRegions.contains(region)) {
            String customRegion = customRegions.getString(region);
            return customRegion;
        } else {
            return null;
        }
    }


    private String getRegion(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%worldguard_region_name%");
    }

    private String getRegionOwner(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%worldguard_region_owner%");
    }

    private String getRegionMembers(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%worldguard_region_members%");
    }

}
