package ru.kainlight.lightshowregion

import me.clip.placeholderapi.PlaceholderAPI
import ru.kainlight.lightshowregion.COMMANDS.LSRCommand
import ru.kainlight.lightshowregion.HOOKS.PAPIExtension
import ru.kainlight.lightshowregion.LISTENERS.PlayerListener
import ru.kainlight.lightshowregion.UTILS.ActionbarManager
import ru.kainlight.lightshowregion.UTILS.RegionManager
import ru.kainlight.lightshowregion.library.LightConfig
import ru.kainlight.lightshowregion.library.LightPlugin
import ru.kainlight.lightshowregion.library.UTILS.Initiators

class Main : LightPlugin() {
    companion object { lateinit var INSTANCE: Main }

    private var messageConfig: LightConfig? = null
    private var regionsConfig: LightConfig? = null
    val actionbarManager: ActionbarManager = ActionbarManager()
    val regionManager: RegionManager = RegionManager()

    override fun onLoad() {
        this.saveDefaultConfig()
        messageConfig = LightConfig.saveLanguages(this, "main-settings.lang")
    }

    override fun onEnable() {
        INSTANCE = this

        this.updateConfig()
        this.getMessageConfig().updateConfig()
        regionsConfig = LightConfig(this, "regions.yml")

        registerCommand("lightshowregion", LSRCommand(this))
        registerListener(PlayerListener(this))

        registerPlaceholders()

        Initiators.startPluginMessage(this)
    }

    override fun onDisable() {
        unregisterPlaceholders()
        server.scheduler.cancelTasks(this)
    }

    private fun registerPlaceholders() {
        if (server.pluginManager.isPluginEnabled("PlaceholderAPI") && ! PlaceholderAPI.isRegistered(description.name.toLowerCase())) {
            PAPIExtension(this).register()
        }
    }

    private fun unregisterPlaceholders() {
        try {
            if (server.pluginManager.isPluginEnabled("PlaceholderAPI") && PlaceholderAPI.isRegistered(description.name.toLowerCase())) {
                PAPIExtension(this).unregister()
            }
        } catch (ignored: Exception) { }
    }

    fun getMessageConfig(): LightConfig { return messageConfig!! }
    fun getRegionsConfig(): LightConfig { return regionsConfig!! }
}