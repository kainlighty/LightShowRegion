package ru.kainlight.lightshowregion.library

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

open class LightPlugin : JavaPlugin() {

    private var configurationVersion = 1.2
    val isPaper: Boolean = isPaperPlugin()

    // -------------------- //
    fun registerListener(listener: Listener): LightPlugin {
        Bukkit.getPluginManager().registerEvents(listener, this)
        return this
    }

    fun registerCommand(command: String, executor: CommandExecutor, tabCompleter: TabCompleter? = null): LightPlugin {
            this.getCommand(command)?.let {
            it.setExecutor(executor)
            if (tabCompleter != null) it.tabCompleter = tabCompleter
        }
        return this
    }

    // ------------------------ //
    fun updateConfig() {
        // Загрузка текущей конфигурации
        val userConfig = config
        val version = userConfig.getDouble("config-version")
        if (version == configurationVersion) return

        // Чтение конфигурации по умолчанию из JAR-файла
        val defaultConfigStream = getResource("config.yml") ?: return
        val inputConfigReader = InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8)
        val defaultConfig = YamlConfiguration.loadConfiguration(inputConfigReader)

        // Добавление отсутствующих значений из конфигурации по умолчанию и удаление удаленных ключей
        defaultConfig.getKeys(true).forEach { key: String ->
            // Если у пользователя нет такого ключа или его значение является значением по умолчанию, добавляем его
            if (!userConfig.contains(key) || userConfig[key] == defaultConfig[key]) {
                userConfig[key] = defaultConfig[key]
            }
        }

        userConfig.getKeys(true).forEach { key: String ->
            if (!defaultConfig.contains(key)) {
                userConfig[key] = null
            }
        }

        /// logger.warning("config.yml updated")
        config["config-version"] = configurationVersion
        saveConfig()
    }

    private fun isPaperPlugin(): Boolean {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder")
            return true
        } catch (e: ClassNotFoundException) { return false }
    }

    fun command(command: String, sender: CommandSender = Bukkit.getConsoleSender()): Boolean {
        return Bukkit.dispatchCommand(sender, command)
    }
}