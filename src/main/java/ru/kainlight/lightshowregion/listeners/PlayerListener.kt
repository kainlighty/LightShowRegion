package ru.kainlight.lightshowregion.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightshowregion.api.LightShowRegionAPI

internal class PlayerListener : Listener {

    @EventHandler fun onPlayerJoin(event: PlayerJoinEvent) { event.player.active() }
    @EventHandler fun onPlayerQuit(event: PlayerQuitEvent) { event.player.disactivate() }
    @EventHandler fun onPlayerKick(event: PlayerKickEvent) { event.player.disactivate() }

    private fun Player.active() {
        val api = LightShowRegionAPI.getProvider()
        api.getShowedPlayer(this)?.let { showedPlayer ->

            val hasUpdated = showedPlayer.updatePlayer()

            if(!hasUpdated) {
                DebugBukkit.warn("Player ${this.name} not updated. There may be problems!")
            }

            showedPlayer.getActionbar().takeIf { it.isActive }?.show()
            showedPlayer.getBossbar().takeIf { it.isActive }?.show()
        } ?: run {
            api.createShowedPlayer(this).let {
                DebugBukkit.info("Player ${this.name} created")
                it.showAll()
            }
        }
    }

    private fun Player.disactivate() {
        LightShowRegionAPI.getProvider().getShowedPlayer(this)?.let { showedPlayer ->
            showedPlayer.getActionbar().takeIf { it.isActive }?.apply {
                hide()
                isActive = true
            }
            showedPlayer.getBossbar().takeIf { it.isActive }?.apply {
                hide()
                isActive = true
            }
        }
    }
}
