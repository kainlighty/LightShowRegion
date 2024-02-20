package ru.kainlight.lightshowregion.HOOKS

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import ru.kainlight.lightshowregion.Main
import java.util.*

class PAPIExtension(private val plugin: Main) : PlaceholderExpansion() {
    override fun getIdentifier(): String = plugin.description.name.lowercase(Locale.getDefault())
    override fun getAuthor(): String = plugin.description.authors[0]
    override fun getVersion(): String = plugin.description.version
    override fun persist(): Boolean = true

    override fun onRequest(offlinePlayer: OfflinePlayer, identifier: String): String? {
        if (identifier.equals("custom", ignoreCase = true)) {
            return plugin.regionManager.sendRegionName(offlinePlayer.player)
        }

        return null
    }
}
