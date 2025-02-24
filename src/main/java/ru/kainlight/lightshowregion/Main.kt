package ru.kainlight.lightshowregion

import ru.kainlight.lightlibrary.LightConfig
import ru.kainlight.lightlibrary.LightPlugin
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.UTILS.Parser
import ru.kainlight.lightshowregion.API.ILightShowRegionAPI
import ru.kainlight.lightshowregion.API.LightShowRegionAPI
import ru.kainlight.lightshowregion.COMMANDS.LSRCommand
import ru.kainlight.lightshowregion.HOOKS.PAPIExtension
import ru.kainlight.lightshowregion.LISTENERS.PlayerListener

class Main : LightPlugin() {

    internal lateinit var regionsConfig: LightConfig
    private var papiExpansion: PAPIExtension? = null

    override fun onLoad() {
        this.saveDefaultConfig()
        configurationVersion = 1.6
        this.updateConfig()

        LightConfig.saveLanguages(this, "main-settings.lang")
        messageConfig.configurationVersion = 1.6
        this.messageConfig.updateConfig()

        regionsConfig = LightConfig(this, null, "regions.yml")
        regionsConfig.saveDefaultConfig()
    }

    override fun onEnable() {
        instance = this
        setLightPluginInstance()

        LightShowRegionAPI.setProvider(ILightShowRegionAPI(this))

        this.reloadConfigs()

        createBukkitAudience()

        registerCommand("lightshowregion", LSRCommand(this))
        registerListener(PlayerListener())

        papiExpansion = PAPIExtension(this)
        papiExpansion?.register()

        checkUpdates()
        enableMessage()
    }

    override fun onDisable() {
        LightShowRegionAPI.getProvider().getShowedPlayers().forEach {
            it.hideAll()
        }

        try {
            papiExpansion?.unregister()
        } catch (_: Exception) {}

        LightShowRegionAPI.removeProvider()
        unregisterListeners()
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

    companion object {
        private lateinit var instance: Main
    }
}