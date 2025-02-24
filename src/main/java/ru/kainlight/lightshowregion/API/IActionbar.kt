package ru.kainlight.lightshowregion.API

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.getAudience
import ru.kainlight.lightlibrary.multiActionbar
import ru.kainlight.lightshowregion.Main

class IActionbar(private val plugin: Main,
                 private val player: Player,
                 override var isActive: Boolean = false
) : Actionbar {

    private var task: BukkitTask? = null

    override fun toggle(): Boolean {
        if(isActive) hide() else show()
        return isActive
    }

    override fun hide(): Boolean {
        val task = task ?: return false
        task.cancel()
        this.task = null
        this.isActive = false
        return true
    }

    override fun show() {
        if(!plugin.config.getBoolean("main-settings.actionbar")) return
        this.isActive = true
        run()
    }

    private fun run(): BukkitTask {
        val disabledWorlds = plugin.config.getStringList("region-settings.disabled-worlds")
        val global = plugin.getMessages().getString("bar.global")
        task?.cancel()

        val task = plugin.runTaskTimerAsynchronously(Runnable {
            if (!player.isOnline) {
                hide()
                DebugBukkit.info("Actionbar task cancelled for ${player.name} because player is offline")
                return@Runnable
            }
            if (global.isNullOrEmpty() || disabledWorlds.contains(player.world.name)) return@Runnable

            val text = LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionName(player)
            player.getAudience().multiActionbar(text)
        }, 0L, 20L)

        this.task = task
        return task
    }

    override fun toString(): String {
        return "Actionbar(player=$player, taskId=${task?.taskId}, isActive=$isActive)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IActionbar) return false
        return player.uniqueId == other.player.uniqueId
    }

    override fun hashCode(): Int {
        var result = player.uniqueId.hashCode()
        return result
    }

}