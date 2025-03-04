package ru.kainlight.lightshowregion.api

import org.bukkit.entity.Player
import ru.kainlight.lightshowregion.Main

internal data class IShowedPlayer(private val plugin: Main, private var p: Player) : ShowedPlayer {

    val actionbar = IActionbar(plugin, this)
    val bossbar = IBossbar(plugin, this)

    override fun updatePlayer(): Boolean {
        val player = plugin.server.getPlayer(p.uniqueId) ?: return false
        p = player
        return true
    }

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

    override fun getPlayer(): Player {
        return p
    }

    override fun getActionbar(): Actionbar {
        return this.actionbar
    }

    override fun getBossbar(): Bossbar {
        return this.bossbar
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IShowedPlayer) return false
        return p.uniqueId == other.p.uniqueId &&
                actionbar == other.actionbar &&
                bossbar == other.bossbar
    }

    override fun hashCode(): Int {
        var result = p.uniqueId.hashCode()
        result = 31 * result + actionbar.hashCode()
        result = 31 * result + bossbar.hashCode()
        return result
    }

    override fun toString(): String {
        return "IShowedPlayer(player=$p, actionbar=$actionbar, bossbar=$bossbar)"
    }

}