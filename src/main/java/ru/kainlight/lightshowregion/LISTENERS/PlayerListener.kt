package ru.kainlight.lightshowregion.LISTENERS

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.kainlight.lightshowregion.Main

class PlayerListener(private val plugin: Main) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (player.hasPermission("lightshowregion.see")) plugin.actionbarManager.toggle(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.actionbarManager.hide(event.player)
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        plugin.actionbarManager.hide(event.player)
    }
}
