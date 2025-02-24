package ru.kainlight.lightshowregion.API

import org.bukkit.entity.Player

interface ShowedPlayer {

    val player: Player
    val actionbar: Actionbar
    val bossbar: Bossbar

    fun toggleAll()
    fun showAll()
    fun hideAll()

    /// ....
}