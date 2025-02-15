package ru.kainlight.lightshowregion.LISTENERS

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.kainlight.lightshowregion.UTILS.ActionbarManager

class PlayerListener() : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (player.hasPermission("lightshowregion.see")) ActionbarManager.toggle(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        ActionbarManager.hide(event.player)
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        ActionbarManager.hide(event.player)
    }
}
