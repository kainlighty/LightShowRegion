package ru.kainlight.lightshowregion.api

import org.bukkit.entity.Player

interface ShowedPlayer {

    fun updatePlayer(): Boolean
    fun getPlayer(): Player

    fun getActionbar(): Actionbar
    fun getBossbar(): Bossbar

    fun toggleAll()
    fun showAll()
    fun hideAll()

    /// ....
}