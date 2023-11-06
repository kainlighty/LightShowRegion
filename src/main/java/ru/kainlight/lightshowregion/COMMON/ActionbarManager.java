package ru.kainlight.lightshowregion.COMMON;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import ru.kainlight.lightshowregion.COMMON.lightlibrary.LightPlayer;
import ru.kainlight.lightshowregion.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ActionbarManager {

    private final Main plugin;

    @Getter
    private final Map<Player, BukkitTask> actionbarTask;

    public ActionbarManager(Main plugin) {
        this.plugin = plugin;
        this.actionbarTask = new HashMap<>();
    }

    public void show(Player player) {
        hide(player);
        runTaskUpdateRegionName(player);
    }

    public boolean toggle(Player player) {
        if (hide(player)) {
            return true;
        } else {
            show(player);
            return false;
        }
    }

    public boolean hide(Player player) {
        boolean isShowen = actionbarTask.containsKey(player);
        if (isShowen) {
            actionbarTask.get(player).cancel();
            actionbarTask.remove(player);
        }

        return isShowen;
    }


    private void runTaskUpdateRegionName(Player player) {
        List<String> disabledWorlds = plugin.getConfig().getStringList("region-settings.disabled-worlds");
        String global = plugin.getMessageConfig().getConfig().getString("actionbar.global");

        actionbarTask.put(player, plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if(player == null) return;

            String playerWorldName = player.getWorld().getName();
            if (global == null || global.isEmpty() || disabledWorlds.contains(playerWorldName)) return;

            String regionName = plugin.getRegionManager().sendRegionName(player);
            LightPlayer.of(player).sendActionbar(regionName);
        }, 20L, 20L));
    }

}
