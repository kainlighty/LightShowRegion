package ru.kainlight.lightshowregion.API

import org.bukkit.entity.Player
import ru.kainlight.lightshowregion.Main

internal data class IShowedPlayer(private val plugin: Main, override val player: Player) : ShowedPlayer {

    override val actionbar = IActionbar(plugin, player)
    override val bossbar = IBossbar(plugin, player)

    override fun toggleAll() {
        actionbar.toggle()
        bossbar.toggle()
    }

    override fun showAll() {
        actionbar.show()
        bossbar.show()
    }

    override fun hideAll() {
        actionbar.hide()
        bossbar.hide()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IShowedPlayer) return false
        return player.uniqueId == other.player.uniqueId &&
                actionbar == other.actionbar &&
                bossbar == other.bossbar
    }

    override fun hashCode(): Int {
        var result = player.uniqueId.hashCode()
        result = 31 * result + actionbar.hashCode()
        result = 31 * result + bossbar.hashCode()
        return result
    }

    override fun toString(): String {
        return "IShowedPlayer(player=$player, actionbar=$actionbar, bossbar=$bossbar)"
    }

}