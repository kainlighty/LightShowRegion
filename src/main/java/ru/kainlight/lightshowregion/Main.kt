package ru.kainlight.lightshowregion

import me.clip.placeholderapi.PlaceholderAPI
import ru.kainlight.lightlibrary.LightConfig
import ru.kainlight.lightlibrary.LightPlugin
import ru.kainlight.lightlibrary.UTILS.Init
import ru.kainlight.lightshowregion.COMMANDS.LSRCommand
import ru.kainlight.lightshowregion.HOOKS.PAPIExtension
import ru.kainlight.lightshowregion.LISTENERS.PlayerListener
import ru.kainlight.lightshowregion.UTILS.ActionbarManager
import ru.kainlight.lightshowregion.UTILS.RegionManager

class Main : LightPlugin() {
    companion object { lateinit var INSTANCE: Main }

    private var regionsConfig: LightConfig? = null
    val actionbarManager: ActionbarManager = ActionbarManager()
    val regionManager: RegionManager = RegionManager()

    override fun onLoad() {
        this.saveDefaultConfig()
        LightConfig.saveLanguages(this, "main-settings.lang")
    }

    override fun onEnable() {
        INSTANCE = this

        configurationVersion = 1.3
        messageConfig.configurationVersion = 1.3

        this.updateConfig()
        this.messageConfig.updateConfig()
        regionsConfig = LightConfig(this, null,"regions.yml")

        registerCommand("lightshowregion", LSRCommand(this))
        registerListener(PlayerListener(this))
        this.registerPlaceholders()

        Init.start(this, true)
    }

    override fun onDisable() {
        this.unregisterPlaceholders()
        this.server.scheduler.cancelTasks(this)
    }

    private fun registerPlaceholders() {
        if (this.server.pluginManager.isPluginEnabled("PlaceholderAPI") && ! PlaceholderAPI.isRegistered(this.description.name.lowercase())) {
            PAPIExtension(this).register()
        }
    }

    private fun unregisterPlaceholders() {
        try {
            if (this.server.pluginManager.isPluginEnabled("PlaceholderAPI") && PlaceholderAPI.isRegistered(this.description.name.lowercase())) {
                PAPIExtension(this).unregister()
            }
        } catch (_: Exception) {}
    }

    fun getRegionsConfig(): LightConfig { return regionsConfig!! }
}