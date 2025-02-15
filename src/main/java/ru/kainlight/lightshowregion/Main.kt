package ru.kainlight.lightshowregion

import me.clip.placeholderapi.PlaceholderAPI
import ru.kainlight.lightlibrary.LightConfig
import ru.kainlight.lightlibrary.LightPlugin
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.UTILS.Parser
import ru.kainlight.lightshowregion.COMMANDS.LSRCommand
import ru.kainlight.lightshowregion.HOOKS.PAPIExtension
import ru.kainlight.lightshowregion.LISTENERS.PlayerListener

class Main : LightPlugin() {

    internal lateinit var regionsConfig: LightConfig

    override fun onLoad() {
        this.saveDefaultConfig()
        configurationVersion = 1.5
        this.updateConfig()

        LightConfig.saveLanguages(this, "main-settings.lang")
        messageConfig.configurationVersion = 1.5
        this.messageConfig.updateConfig()

        regionsConfig = LightConfig(this, null,"regions.yml")
    }

    override fun onEnable() {
        instance = this
        setLightPluginInstance()

        this.reloadConfigs()

        createBukkitAudience()

        registerCommand("lightshowregion", LSRCommand(this))
        registerListener(PlayerListener())

        this.registerPlaceholder()

        checkUpdates()
        enableMessage()
    }

    override fun onDisable() {
        unregisterListeners()
        this.unregisterPlaceholder()
    }

    fun reloadConfigs() {
        this.saveDefaultConfig()
        this.reloadConfig()
        Parser.parseMode = this.config.getString("main-settings.parse_mode") ?: "MINIMESSAGE"
        DebugBukkit.isEnabled = this.config.getBoolean("debug")

        this.messageConfig.saveDefaultConfig()
        this.messageConfig.reloadLanguage("main-settings.lang")
        this.messageConfig.reloadConfig()

        this.regionsConfig.saveDefaultConfig()
        this.regionsConfig.reloadConfig()
    }

    private fun registerPlaceholder() {
        val description = this.description
        if (this.server.pluginManager.isPluginEnabled("PlaceholderAPI") && ! PlaceholderAPI.isRegistered(description.name.lowercase())) {
            PAPIExtension(description).register()
        }
    }

    private fun unregisterPlaceholder() {
        runCatching {
            val description = this.description
            if (this.server.pluginManager.isPluginEnabled("PlaceholderAPI") && PlaceholderAPI.isRegistered(description.name.lowercase())) {
                PAPIExtension(description).unregister()
            }
        }
    }

    companion object {
        private lateinit var instance: Main

        internal fun getInstance(): Main {
            return instance
        }
    }
}