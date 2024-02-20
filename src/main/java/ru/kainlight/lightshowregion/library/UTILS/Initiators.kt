package ru.kainlight.lightshowregion.library.UTILS

import ru.kainlight.lightshowregion.library.LightCommon
import ru.kainlight.lightshowregion.library.LightPlugin

object Initiators {
    fun startPluginMessage(plugin: LightPlugin) {
        LightCommon
            .logger("")
            .logger("&c » &7" + plugin.description.name + " enabled")
            .logger("&c » &7Version: " + plugin.description.version)
        GitHubUpdater(plugin).start()
        LightCommon.logger("")
    }
}
