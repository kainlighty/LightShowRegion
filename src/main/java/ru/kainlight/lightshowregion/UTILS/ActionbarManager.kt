package ru.kainlight.lightshowregion.UTILS

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import ru.kainlight.lightlibrary.getAudience
import ru.kainlight.lightlibrary.multiActionbar
import ru.kainlight.lightshowregion.Main

object ActionbarManager {

    val actionbarTask = mutableMapOf<Player, BukkitTask>()

    fun toggle(player: Player): Boolean {
        return if (hide(player)) true else {
            show(player)
            false
        }
    }

    fun show(player: Player) {
        hide(player)

        val isActionbar = Main.getInstance().config.getBoolean("main-settings.actionbar")
        if (isActionbar) runTaskUpdateRegionName(player)
    }

    fun hide(player: Player): Boolean {
        val task = actionbarTask.get(player)

        return if(task == null) false
        else {
            task.cancel()
            true
        }
    }

    fun runTaskUpdateRegionName(player: Player) {
        val disabledWorlds = Main.getInstance().config.getStringList("region-settings.disabled-worlds")
        val global = Main.getInstance().messageConfig.getConfig().getString("actionbar.global")

        actionbarTask.put(player, Main.getInstance().runTaskTimerAsynchronously(Runnable {
            if (!player.isOnline) return@Runnable
            if (global.isNullOrEmpty() || disabledWorlds.contains(player.world.name)) return@Runnable

            val text = RegionManager.getRegionName(player)
            player.getAudience().multiActionbar(text)
        }, 0L, 20L))
    }

}