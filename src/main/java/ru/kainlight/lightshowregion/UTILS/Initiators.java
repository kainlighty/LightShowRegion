package ru.kainlight.lightshowregion.UTILS;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.kainlight.lightshowregion.HOOKS.PlaceholderAPI.CustomRegion;
import ru.kainlight.lightshowregion.Main;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Initiators {

    public static void startPluginMessage(Main plugin) {
        new CustomRegion(plugin).register();

        Parser.get().logger("");
        Parser.get().logger("&c » &fLightShowRegion enabled");
        Parser.get().logger("&c » &fVersion: " + plugin.getDescription().getVersion());
        new GitHubUpdater(plugin).start();
        Parser.get().logger("&c » &fAuthor: kainlight");
        Parser.get().logger("");
    }

    public static void stopPluginMessage(Main plugin) {
        new CustomRegion(plugin).unregister();

        Map<Player, BukkitTask> taskList = plugin.getActionbarManager().getActionbarTask();
        Collection<BukkitTask> taskListValues = taskList.values();
        taskListValues.forEach(BukkitTask::cancel);
        taskList.clear();

        Parser.get().logger("&c » &fLightShowRegion disabled");
    }
}
