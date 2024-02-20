package ru.kainlight.lightshowregion.UTILS

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import ru.kainlight.lightshowregion.Main
import ru.kainlight.lightshowregion.library.actionbar

class ActionbarManager {

    private val actionbarTask: MutableMap<Player, BukkitTask> = mutableMapOf()

    fun show(player: Player?) {
        if(player == null) return
        hide(player)

        val isActionbar = Main.INSTANCE.config.getBoolean("main-settings.actionbar")
        if (isActionbar) runTaskUpdateRegionName(player)
    }

    fun toggle(player: Player?): Boolean {
        if(player == null) return false

        if (hide(player)) {
            return true
        } else {
            show(player)
            return false
        }
    }

    fun hide(player: Player?): Boolean {
        if(player == null) return false

        val isShown = actionbarTask.containsKey(player)
        val task = actionbarTask.remove(player)

        if (isShown && task != null) task.cancel()
        return isShown
    }

    fun runTaskUpdateRegionName(player: Player): Unit {
        val disabledWorlds = Main.INSTANCE.config.getStringList("region-settings.disabled-worlds")
        val global = Main.INSTANCE.getMessageConfig().config.getString("actionbar.global")

        actionbarTask.put(player, Main.INSTANCE.server.scheduler.runTaskTimerAsynchronously(Main.INSTANCE, Runnable {
            if (player == null) return@Runnable
            val playerWorldName = player.world.name
            if (global.isNullOrEmpty() || disabledWorlds.contains(playerWorldName)) return@Runnable

            val regionName = Main.INSTANCE.regionManager.sendRegionName(player)
            player.actionbar(regionName)
        }, 0L, 20L))
    }
}