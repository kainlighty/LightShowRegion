package ru.kainlight.lightshowregion

import ru.kainlight.lightlibrary.LightConfig
import ru.kainlight.lightlibrary.LightPlugin
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.UTILS.Parser
import ru.kainlight.lightshowregion.api.ILightShowRegionAPI
import ru.kainlight.lightshowregion.api.LightShowRegionAPI
import ru.kainlight.lightshowregion.commands.LSRCommand
import ru.kainlight.lightshowregion.hooks.CustomPlaceholder
import ru.kainlight.lightshowregion.listeners.PlayerListener

class Main : LightPlugin() {

    internal lateinit var regionsConfig: LightConfig
    private var papiExpansion: CustomPlaceholder? = null

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
        this.enable()

        this.reloadConfigs()

        LightShowRegionAPI.setProvider(ILightShowRegionAPI(this))

        registerCommand("lightshowregion", LSRCommand(this))
        registerListener(PlayerListener())

        papiExpansion = CustomPlaceholder(this)
        papiExpansion?.register()
    }

    override fun onDisable() {
        runCatching {
            LightShowRegionAPI.getProvider().getShowedPlayers().forEach { it.hideAll() }
            papiExpansion?.unregister()
            LightShowRegionAPI.removeProvider()

        }
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
}