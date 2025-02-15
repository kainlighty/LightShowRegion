package ru.kainlight.lightshowregion.HOOKS

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import ru.kainlight.lightlibrary.equalsIgnoreCase
import ru.kainlight.lightshowregion.Main
import ru.kainlight.lightshowregion.UTILS.RegionManager
import java.util.*

class PAPIExtension(private val description: PluginDescriptionFile) : PlaceholderExpansion() {

    override fun getIdentifier(): String = description.name.lowercase()
    override fun getAuthor(): String = description.authors[0]
    override fun getVersion(): String = description.version
    override fun persist(): Boolean = true

    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        return if (identifier.equalsIgnoreCase("custom")) {
            RegionManager.getRegionName(player)
        } else null
    }
}
