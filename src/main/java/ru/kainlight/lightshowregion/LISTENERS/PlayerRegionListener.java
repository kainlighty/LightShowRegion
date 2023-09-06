package ru.kainlight.lightshowregion.LISTENERS;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.kainlight.lightshowregion.Main;

public final class PlayerRegionListener implements Listener {

    private final Main plugin;

    public PlayerRegionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getActionbarManager().toggle(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getActionbarManager().getActionbarTask().containsKey(player)) {
            plugin.getActionbarManager().hide(player);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (plugin.getActionbarManager().getActionbarTask().containsKey(player)) {
            plugin.getActionbarManager().hide(player);
        }
    }

/*    @EventHandler
    public static void regionBypass(PlayerMoveEvent event) {
        WG GetWG = new WG();
        Player player = event.getPlayer();
        Location loc = BukkitAdapter.adapt(player.getLocation());
        ApplicableRegionSet set = GetWG.getQuery().getApplicableRegions(loc);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        List<String> accessRegions = customConfigsManager.getPlayerAccessRegionsListConfig("accesses." + player.getName());

        for (ProtectedRegion region : set) {
            if (accessRegions.contains(region.getId())) {
                region.getOwners().addPlayer(localPlayer);
                return;
            } else {
                region.getOwners().removePlayer(localPlayer);
            }
        }
    }*/

}
