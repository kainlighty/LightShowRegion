package ru.kainlight.lightshowregion.api

import ru.kainlight.lightlibrary.API.LightTask
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.multiActionbar
import ru.kainlight.lightshowregion.Main

class IActionbar(private val plugin: Main,
                 private val showedPlayer: ShowedPlayer,
                 override var isActive: Boolean = false
) : Actionbar {

    private var task: LightTask? = null

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
        if (!plugin.config.getBoolean("main-settings.actionbar")) return
        this.isActive = true
        run()
    }

    private fun run() {
        task?.cancel()
        val disabledWorlds = plugin.config.getStringList("region-settings.disabled-worlds")
        val global = plugin.getMessages().getString("bar.global")
        val player = showedPlayer.getPlayer() ?: return

        DebugBukkit.info("Player ${player.name} actionbar showed")

        val updateLogic = { cancelCallback: () -> Unit ->
            if(!player.isOnline) {
                DebugBukkit.info("Actionbar task cancelled for ${player.name} because player is offline")
                hide()
                cancelCallback()
            } else if (!global.isNullOrEmpty() && !disabledWorlds.contains(player.world.name)) {
                val text = LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionName(player)
                if (text != null) player.multiActionbar(text)
            }
        }

        if (plugin.isFolia) {
            // В Folia всё, что работает с игроком, должно крутиться в его EntityScheduler
            val foliaTask = player.scheduler.runAtFixedRate(plugin, {
                updateLogic { it.cancel() }
            }, null, 1L, 20L)

            this.task = LightTask("Folia-${foliaTask.hashCode()}") { foliaTask?.cancel() }
        } else {
            val bukkitTask = plugin.runTaskTimerAsynchronously(Runnable {
                updateLogic { this.task?.cancel() }
            }, 0L, 20L)

            this.task = LightTask(bukkitTask.taskId.toString()) { bukkitTask.cancel() }
        }
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