package ru.kainlight.lightshowregion.API

import org.bukkit.entity.Player
import ru.kainlight.lightshowregion.Main

@Suppress("UNUSED")
internal class ILightShowRegionAPI(private val plugin: Main) : LightShowRegionAPI {

    private val showedPlayers: MutableMap<Player, ShowedPlayer> = mutableMapOf()
    private val regionHandler: RegionHandler = IRegionHandler(plugin)

    override fun createShowedPlayer(player: Player): ShowedPlayer {
        val showedPlayer = IShowedPlayer(plugin, player)
        showedPlayers.put(player, showedPlayer)
        return showedPlayer
    }

    override fun getOrCreateShowedPlayer(player: Player): ShowedPlayer {
        return (getShowedPlayer(player) ?: createShowedPlayer(player))
    }

    override fun deleteShowedPlayer(player: Player): Boolean {
        return showedPlayers.remove(player) != null
    }

    override fun deleteShowedPlayer(showedPlayer: ShowedPlayer): Boolean {
        return showedPlayers.values.remove(showedPlayer)
    }

    override fun getShowedPlayer(player: Player): ShowedPlayer? {
        return showedPlayers.get(player)
    }

    override fun getShowedPlayers(): List<ShowedPlayer> {
        return showedPlayers.values.toList()
    }

    override fun isShowedPlayer(player: Player?): Boolean {
        return showedPlayers.keys.contains(player)
    }

    override fun reloadActionbar(player: Player) {
        this.getOrCreateShowedPlayer(player).actionbar.show()
    }

    override fun reloadActionbars() {
        plugin.server.onlinePlayers.forEach {
            this.reloadActionbar(it)
        }
    }

    override fun unloadActionbars() {
        showedPlayers.values.forEach {
            it.actionbar.hide()
        }
    }

    override fun reloadBossbar(player: Player) {
        this.getOrCreateShowedPlayer(player).bossbar.show()
    }

    override fun reloadBossbars() {
        plugin.server.onlinePlayers.forEach {
            this.reloadBossbar(it)
        }
    }

    override fun unloadBossbars() {
        showedPlayers.values.forEach {
            it.bossbar.hide()
        }
    }

    override fun addDisabledWorld(world: String): Boolean {
        val disabledWorlds = plugin.config.getStringList("region-settings.disabled-worlds")
        if(disabledWorlds.contains(world)) return false
        disabledWorlds.add(world)
        plugin.config.set("region-settings.disabled-worlds", disabledWorlds)
        plugin.saveConfig()
        return true
    }
    override fun getDisabledWorlds(): List<String> = plugin.config.getStringList("region-settings.disabled-worlds")
    override fun removeDisabledWorld(world: String?): Boolean {
        if(world == null) return false
        val disabledWorlds = plugin.config.getStringList("region-settings.disabled-worlds")
        if(!disabledWorlds.contains(world)) return false
        disabledWorlds.remove(world)
        plugin.config.set("region-settings.disabled-worlds", disabledWorlds)
        plugin.saveConfig()
        return true
    }

    override fun getRegionHandler(): RegionHandler {
        return regionHandler
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ILightShowRegionAPI) return false

        return showedPlayers == other.showedPlayers &&
                regionHandler == other.regionHandler
    }

    override fun hashCode(): Int {
        var result = showedPlayers.hashCode()
        result = 31 * result + regionHandler.hashCode()
        return result
    }

    override fun toString(): String {
        return "LightShowRegionAPI(showedPlayers=$showedPlayers, regionHandler=$regionHandler, disabledWorlds=${getDisabledWorlds()})"
    }


}