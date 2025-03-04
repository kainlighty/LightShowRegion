package ru.kainlight.lightshowregion.api

import org.bukkit.scheduler.BukkitTask
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.getAudience
import ru.kainlight.lightlibrary.ifFalse
import ru.kainlight.lightlibrary.multiActionbar
import ru.kainlight.lightshowregion.Main

class IActionbar(private val plugin: Main,
                 private val showedPlayer: ShowedPlayer,
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
        DebugBukkit.info("Player ${showedPlayer.getPlayer().name} actionbar hidden")
        return true
    }

    override fun show() {
        plugin.config.getBoolean("main-settings.actionbar").ifFalse { return }
        this.isActive = true
        run()
    }

    private fun run(): BukkitTask {
        task?.cancel()
        val disabledWorlds = plugin.config.getStringList("region-settings.disabled-worlds")
        val global = plugin.getMessages().getString("bar.global")
        val player = showedPlayer.getPlayer()
        DebugBukkit.info("Player ${player.name} actionbar showed")

        val task = plugin.runTaskTimerAsynchronously(Runnable {
            player.isOnline.ifFalse {
                DebugBukkit.info("Actionbar task cancelled for ${player.name} because player is offline")
                hide()
                return@Runnable
            }
            if (global.isNullOrEmpty() || disabledWorlds.contains(player.world.name)) return@Runnable

            val text = LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionName(player) ?: return@Runnable
            player.getAudience().multiActionbar(text)
        }, 0L, 20L)

        this.task = task
        return task
    }

    override fun toString(): String {
        return "Actionbar(showedPlayer=$showedPlayer, taskId=${task?.taskId}, isActive=$isActive)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IActionbar) return false
        return showedPlayer == other.showedPlayer
    }

    override fun hashCode(): Int {
        val result = showedPlayer.hashCode()
        return result
    }

}