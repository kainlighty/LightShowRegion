package ru.kainlight.lightshowregion.LISTENERS

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.kainlight.lightshowregion.API.LightShowRegionAPI

internal class PlayerListener() : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        val api = LightShowRegionAPI.getProvider()
        val showedPlayer = api.getShowedPlayer(player)
        if (showedPlayer == null) {
            api.createShowedPlayer(player).showAll()
        } else {
            if (showedPlayer.actionbar.isActive) showedPlayer.actionbar.show()
            if (showedPlayer.bossbar.isActive) showedPlayer.bossbar.show()
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        disactivate(event.player)
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        disactivate(event.player)
    }

    private fun disactivate(player: Player) {
        LightShowRegionAPI.getProvider().getShowedPlayer(player)?.let {
            if (it.actionbar.isActive) {
                it.actionbar.hide()
                it.actionbar.isActive = true
            }
            if (it.bossbar.isActive) {
                it.bossbar.hide()
                it.bossbar.isActive = true
            }
        }
    }
}
