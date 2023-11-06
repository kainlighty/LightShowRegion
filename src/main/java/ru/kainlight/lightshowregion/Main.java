package ru.kainlight.lightshowregion;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.kainlight.lightshowregion.COMMANDS.LightShowRegion;
import ru.kainlight.lightshowregion.COMMON.ActionbarManager;
import ru.kainlight.lightshowregion.COMMON.RegionManager;
import ru.kainlight.lightshowregion.COMMON.lightlibrary.CONFIGS.BukkitConfig;
import ru.kainlight.lightshowregion.COMMON.lightlibrary.LightPlugin;
import ru.kainlight.lightshowregion.COMMON.lightlibrary.UTILS.Initiators;
import ru.kainlight.lightshowregion.HOOKS.PlaceholderAPI.CustomRegionExpansion;
import ru.kainlight.lightshowregion.LISTENERS.PlayerListener;

import java.util.Collection;
import java.util.Map;

@Getter
@SuppressWarnings("all")
public final class Main extends LightPlugin {

    @Getter
    private static Main instance;

    private BukkitConfig regionsConfig;
    private ActionbarManager actionbarManager;
    private RegionManager regionManager;

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;

        BukkitConfig.saveLanguages(this, "main-settings.lang");
        updateConfig();
        messageConfig.updateConfig();
        regionsConfig = new BukkitConfig(this, "regions.yml");
        regionsConfig.updateConfig();

        actionbarManager = new ActionbarManager(this);
        regionManager = new RegionManager(this);

        registerCommand("lightshowregion", new LightShowRegion(this));
        registerListener(new PlayerListener(this));

        registerPlaceholders();

        Initiators.startPluginMessage(this);
    }

    @Override
    public void onDisable() {
        unregisterPlaceholders();

        this.getServer().getScheduler().cancelTasks(this);
    }

    private void registerPlaceholders() {
        if(this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && PlaceholderAPI.isRegistered(this.getDescription().getName().toLowerCase())) {
            new CustomRegionExpansion(this).register();
        }
    }

    private void unregisterPlaceholders() {
        if(this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && PlaceholderAPI.isRegistered(this.getDescription().getName().toLowerCase())) {
            new CustomRegionExpansion(this).unregister();
        }
    }

}
