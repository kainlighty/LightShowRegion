package ru.kainlight.lightshowregion.HOOKS

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import ru.kainlight.lightlibrary.equalsIgnoreCase
import ru.kainlight.lightshowregion.API.LightShowRegionAPI
import ru.kainlight.lightshowregion.Main

class PAPIExtension(private val plugin: Main) : PlaceholderExpansion() {

    override fun getIdentifier(): String = plugin.description.name.lowercase()
    override fun getAuthor(): String = plugin.description.authors[0]
    override fun getVersion(): String = plugin.description.version
    override fun persist(): Boolean = true

    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        return if (identifier.equalsIgnoreCase("custom")) {
            LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionName(player) ?: ""
        } else null
    }
}
