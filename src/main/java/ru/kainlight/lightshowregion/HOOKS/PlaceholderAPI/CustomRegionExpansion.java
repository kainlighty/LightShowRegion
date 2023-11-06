package ru.kainlight.lightshowregion.HOOKS.PlaceholderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.kainlight.lightshowregion.Main;

public final class CustomRegionExpansion extends PlaceholderExpansion {

    private final Main plugin;

    public CustomRegionExpansion(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String identifier) {
        if(offlinePlayer.hasPlayedBefore()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                if (identifier.equalsIgnoreCase("custom")) {
                    return plugin.getRegionManager().sendRegionName(player);
                }
            }
        }
        return identifier;
    }
}
