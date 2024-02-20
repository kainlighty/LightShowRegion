package ru.kainlight.lightshowregion.library;

import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class LightConfig {
    private final LightPlugin plugin;
    private final String fileName;
    private final String subdirectory;
    private File configFile;
    private FileConfiguration fileConfiguration;

    @Setter
    private double configurationVersion = 1.2;

    public LightConfig(LightPlugin plugin, String fileName) {
        this(plugin, null, fileName);
    }

    public LightConfig(LightPlugin plugin, String subdirectory, String fileName) {
        this.plugin = plugin;
        this.subdirectory = subdirectory;
        this.fileName = fileName;
        saveDefaultConfig();
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            if (subdirectory == null) {
                configFile = new File(plugin.getDataFolder(), fileName);
            } else {
                File subdirectoryFile = new File(plugin.getDataFolder(), subdirectory);
                if (!subdirectoryFile.exists()) {
                    subdirectoryFile.mkdir();
                }
                configFile = new File(subdirectoryFile, fileName);
            }
        }

        if (!configFile.exists()) {
            if (subdirectory != null) {
                plugin.saveResource(subdirectory + File.separator + fileName, false);
            } else {
                plugin.saveResource(fileName, false);
            }
        }
    }

    public void reloadConfig() {
        if (configFile == null) {
            if (subdirectory != null) {
                configFile = new File(plugin.getDataFolder() + File.separator + subdirectory, fileName);
            } else {
                configFile = new File(plugin.getDataFolder(), fileName);
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null || configFile == null) return;

        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save config to " + configFile);
        }
    }

    @SuppressWarnings("all")
    public void updateConfig() {
        // Загрузка текущей конфигурации
        FileConfiguration userConfig = this.getConfig();
        // Загрузка текущей конфигурации
        double version = userConfig.getDouble("config-version");
        if(version == configurationVersion) return;

        InputStream defaultConfigStream;
        // Чтение конфигурации по умолчанию из JAR-файла
        if(subdirectory != null) {
            defaultConfigStream = plugin.getResource(subdirectory + "/" + fileName);
        } else {
            defaultConfigStream = plugin.getResource(fileName);
        }
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

        plugin.getLogger().warning(fileName + " updated");
        getConfig().set("config-version", configurationVersion);
        saveConfig();
    }

    public static LightConfig saveLanguages(LightPlugin plugin, String pathToLang) {
        LightConfig messageConfig;

        // Получить URL папки messages внутри JAR
        URL url = LightConfig.class.getResource("/messages");
        if (url != null) {
            try {
                // Создать подключение к этому URL
                URLConnection connection = url.openConnection();
                if (connection instanceof JarURLConnection) {
                    JarURLConnection jarConnection = (JarURLConnection) connection;
                    // Получить JAR файл
                    JarFile jar = jarConnection.getJarFile();

                    // Получить все элементы JAR файла
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        // Если элемент - файл в папке messages, то сохранить его
                        if (name.startsWith("messages/") && name.endsWith(".yml")) {
                            String fileName = name.substring("messages/".length());
                            messageConfig = new LightConfig(plugin, "messages", fileName);
                            return messageConfig;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String langFile = plugin.getConfig().getString(pathToLang) + ".yml";
        messageConfig = new LightConfig(plugin, "messages", langFile.toLowerCase());
        messageConfig.reloadConfig();

        return messageConfig;
    }

}