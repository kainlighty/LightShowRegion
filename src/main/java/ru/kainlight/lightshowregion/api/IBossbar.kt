package ru.kainlight.lightshowregion.api

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import ru.kainlight.lightlibrary.API.LightTask
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.UTILS.Parser
import ru.kainlight.lightshowregion.Main

class IBossbar(private val plugin: Main,
               private val showedPlayer: ShowedPlayer,
               override var isActive: Boolean = false
) : Bossbar {

    private var bar: BossBar? = null
    private var task: LightTask? = null

    override fun toggle(): Boolean {
        return if (hide()) true else {
            show()
            false
        }
    }

    override fun hide(): Boolean {
        val currentBar = bar ?: return false
        showedPlayer.getPlayer().hideBossBar(currentBar)
        val task = task ?: return false
        task.cancel()

        this.task = null
        this.bar = null
        this.isActive = false
        DebugBukkit.info("Player ${showedPlayer.getPlayer().name} actionbar hidden")
        return true
    }

    override fun show() {
        if (!plugin.config.getBoolean("main-settings.bossbar")) return
        hide()

        createBar()
        this.isActive = true
        this.run()
        showedPlayer.getPlayer().showBossBar(bar!!)
    }

    private fun createBar(): BossBar {
        val color = BossBar.Color.valueOf(plugin.config.getString("bossbar-settings.color")?.uppercase() ?: "RED")
        val style = BossBar.Overlay.valueOf(plugin.config.getString("bossbar-settings.style")?.uppercase() ?: "PROGRESS")
        return BossBar.bossBar(Component.empty(), 1.0f, color, style).also {
            DebugBukkit.info("Bossbar created for player ${showedPlayer.getPlayer().name}")
            bar = it
        }
    }

    private fun run() {
        val disabledWorlds = plugin.config.getStringList("region-settings.disabled-worlds")
        val global = plugin.getMessages().getString("bar.global")
        val player = showedPlayer.getPlayer() ?: return

        DebugBukkit.info("Player ${player.name} bossbar showed")

        val updateLogic = { cancelCallback: () -> Unit ->
            if (bar == null || !player.isOnline) {
                hide()
                DebugBukkit.info("Bossbar task cancelled for ${player.name}")
                cancelCallback()
            } else if (!global.isNullOrEmpty() && !disabledWorlds.contains(player.world.name)) {
                val title = LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionName(player)
                setTitle(title)
            }
        }

        if (plugin.isFolia) {
            val foliaTask = player.scheduler.runAtFixedRate(plugin, {
                updateLogic { it.cancel() }
            }, null, 1L, 20L)

            // Сохраняем в наш класс. У Folia нет taskId, используем hash
            this.task = LightTask("Folia-${foliaTask.hashCode()}") { foliaTask?.cancel() }
        } else {
            val bukkitTask = plugin.runTaskTimer(Runnable {
                updateLogic { this.task?.cancel() }
            }, 0L, 20L)

            this.task = LightTask(bukkitTask.taskId.toString()) { bukkitTask.cancel() }
        }
    }

    private fun setTitle(text: String?) {
        val title = if(text == null) Component.empty() else Parser.mini(text)
        bar?.name(title) ?: DebugBukkit.error("Title is not set because bossbar is null")
    }

    override fun toString(): String {
        return "Bossbar(bar=$bar, taskId=${task?.taskId}, isActive=$isActive"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IBossbar) return false
        return showedPlayer == other.showedPlayer
    }

    override fun hashCode(): Int {
        val result = showedPlayer.hashCode()
        return result
    }
}