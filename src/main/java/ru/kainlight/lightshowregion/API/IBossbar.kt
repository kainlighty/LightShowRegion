package ru.kainlight.lightshowregion.API

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import ru.kainlight.lightlibrary.UTILS.DebugBukkit
import ru.kainlight.lightlibrary.UTILS.Parser
import ru.kainlight.lightlibrary.getAudience
import ru.kainlight.lightshowregion.Main

class IBossbar(private val plugin: Main,
               private val player: Player,
               override var isActive: Boolean = false
) : Bossbar {

    private var bar: BossBar? = null
    private var task: BukkitTask? = null

    override fun toggle(): Boolean {
        return if (hide()) true else {
            show()
            false
        }
    }

    override fun show() {
        if (!plugin.config.getBoolean("main-settings.bossbar")) return
        hide()

        createBar()
        this.isActive = true
        this.run()
        player.getAudience().showBossBar(bar!!)
    }

    override fun hide(): Boolean {
        val currentBar = bar ?: return false
        player.getAudience().hideBossBar(currentBar)
        val task = task ?: return false
        task.cancel()

        this.task = null
        this.bar = null
        this.isActive = false
        return true
    }

    private fun createBar(): BossBar {
        val color = BossBar.Color.valueOf(plugin.config.getString("bossbar-settings.color")?.uppercase() ?: "RED")
        val style = BossBar.Overlay.valueOf(plugin.config.getString("bossbar-settings.style")?.uppercase() ?: "PROGRESS")
        return BossBar.bossBar(Component.empty(), 1.0f, color, style).also { bar = it }
    }

    private fun run(): BukkitTask {
        val disabledWorlds = plugin.config.getStringList("region-settings.disabled-worlds")
        val global = plugin.getMessages().getString("bar.global")

        return plugin.runTaskTimer(Runnable {
            if (bar == null || !player.isOnline) {
                hide()
                DebugBukkit.info("Bossbar task cancelled for ${player.name} because player is offline")
                return@Runnable
            }
            if (global.isNullOrEmpty() || disabledWorlds.contains(player.world.name)) return@Runnable

            LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionName(player).let { title ->
                setTitle(title)
            }
        }, 0, 20).also { task = it }
    }

    private fun setTitle(text: String?) {
        val title = if(text == null) Component.empty() else  Parser.mini(text)
        bar?.name(title)
    }

    override fun toString(): String {
        return "Bossbar(bar=$bar, taskId=${task?.taskId}, isActive=$isActive"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IBossbar) return false
        return player.uniqueId == other.player.uniqueId
    }

    override fun hashCode(): Int {
        var result = player.uniqueId.hashCode()
        return result
    }
}