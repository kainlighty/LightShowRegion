package ru.kainlight.lightshowregion.COMMON.lightlibrary.UTILS;

import org.bukkit.plugin.Plugin;
import ru.kainlight.lightshowregion.COMMON.lightlibrary.LightLib;
import ru.kainlight.lightshowregion.Main;

@SuppressWarnings("deprecation")
public final class Initiators {

    public static void startPluginMessage(Plugin plugin) {
        LightLib.get()
                .logger("")
                .logger("&c » &7" + plugin.getDescription().getName() + " enabled")
                .logger("&c » &7Version: " + plugin.getDescription().getVersion());
        new GitHubUpdater(Main.getInstance()).start();
        LightLib.get().logger("");
    }
}
