package ru.kainlight.lightshowregion;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kainlight.lightshowregion.COMMON.RegionManager;
import ru.kainlight.lightshowregion.COMMANDS.LightShowRegion;
import ru.kainlight.lightshowregion.CONFIGS.CustomConfig;
import ru.kainlight.lightshowregion.LISTENERS.PlayerRegionListener;
import ru.kainlight.lightshowregion.COMMON.ActionbarManager;
import ru.kainlight.lightshowregion.UTILS.Initiators;
import ru.kainlight.lightshowregion.UTILS.Messenger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    public CustomConfig messageConfig;
    private CustomConfig regionsConfig;
    private Messenger messenger;
    private ActionbarManager actionbarManager;
    private RegionManager regionManager;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        regionsConfig = new CustomConfig(this, "regions.yml");
    }

    @Override
    public void onEnable() {
        instance = this;

        CustomConfig.saveLanguages();
        messenger = new Messenger(this);
        actionbarManager = new ActionbarManager(this);
        regionManager = new RegionManager(this);

        getCommand("lightshowregion").setExecutor(new LightShowRegion(this));
        this.getServer().getPluginManager().registerEvents(new PlayerRegionListener(this),this);

        Initiators.startPluginMessage(this);
    }

    @Override
    public void onDisable() {
        Initiators.stopPluginMessage(this);
    }

    @SuppressWarnings("all")
    public void updateConfig() {
        // Загрузка текущей конфигурации
        FileConfiguration userConfig = this.getConfig();

        // Чтение конфигурации по умолчанию из JAR-файла
        InputStream defaultConfigStream = this.getResource("config.yml");
        InputStreamReader inputConfigReader = new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8);
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(inputConfigReader);

        // Добавление отсутствующих значений из конфигурации по умолчанию и удаление удаленных ключей
        defaultConfig.getKeys(true).forEach(key -> {
            // Если у пользователя нет такого ключа или его значение является значением по умолчанию, добавляем его
            if (!userConfig.contains(key) || userConfig.get(key).equals(defaultConfig.get(key))) {
                userConfig.set(key, defaultConfig.get(key));
            }
        });

        userConfig.getKeys(true).forEach(key -> {
            if (!defaultConfig.contains(key)) {
                userConfig.set(key, null);
            }
        });
        saveConfig();
    }

}
