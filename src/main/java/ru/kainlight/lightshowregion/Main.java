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
import ru.kainlight.lightshowregion.HOOKS.PlaceholderAPI.CustomRegion;
import ru.kainlight.lightshowregion.LISTENERS.PlayerRegionListener;

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
        regionsConfig = new BukkitConfig(this, "regions.yml");
        regionsConfig.updateConfig();
    }

    @Override
    public void onEnable() {
        instance = this;

        actionbarManager = new ActionbarManager(this);
        regionManager = new RegionManager(this);

        getCommand("lightshowregion").setExecutor(new LightShowRegion(this));
        this.getServer().getPluginManager().registerEvents(new PlayerRegionListener(this),this);

        new CustomRegion(this).register();

        Initiators.startPluginMessage(this);
    }

    @Override
    public void onDisable() {
        unregisterPlaceholders();

        Map<Player, BukkitTask> taskList = this.getActionbarManager().getActionbarTask();
        Collection<BukkitTask> taskListValues = taskList.values();
        taskListValues.forEach(BukkitTask::cancel);
        taskList.clear();
    }

    private void unregisterPlaceholders() {
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && PlaceholderAPI.isRegistered(getDescription().getName().toLowerCase())) {
            new CustomRegion(this).unregister();
        }
    }

}
