package ru.kainlight.lightshowregion.api

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Location
import org.bukkit.entity.Player
import ru.kainlight.lightlibrary.API.WorldGuardAPI
import ru.kainlight.lightshowregion.Main
import java.util.*

internal class IRegionHandler(private val plugin: Main) : RegionHandler {

    override fun addCustomRegion(regionId: String, regionName: String): Boolean {
        val customRegions = plugin.regionsConfig.getConfig().getConfigurationSection("custom")!!
        if(customRegions.contains(regionId)) return false
        customRegions.set(regionId, regionName)
        plugin.regionsConfig.saveConfig()
        return true
    }

    override fun getCustomRegionName(player: Player?): String? {
        if (player == null) return null
        val actionBarSection = plugin.getMessages().getConfigurationSection("bar")!!
        val sortBy = plugin.config.getString("region-settings.sorted-by") ?: "RANDOM"

        val region = this.getRegionId(player.location, sortBy)
        if (region == null) {
            val isGlobalRegion = this.isGlobalRegion()
            if(isGlobalRegion) {
                val globalRegionMessage = actionBarSection.getString("global") ?: return null
                if(globalRegionMessage.isEmpty() || globalRegionMessage.isBlank()) return null
                return globalRegionMessage
            }
            return null
        }

        val blacklist = this.getBlacklist()
        if (blacklist.contains(region)) {
            val actionbarMessage = actionBarSection.getString("blacklisted") ?: return null
            if(actionbarMessage.isEmpty() || actionbarMessage.isBlank()) return null
            return actionbarMessage.replace("%region%", region)
        }

        val regionCustomName = getCustomRegionNameFromConfig(region)
        return if (regionCustomName != null) {
            val regionBarMessage = actionBarSection.getString("region") ?: return null

            if (regionCustomName.startsWith("!")) regionCustomName.substring(1)
            else regionBarMessage.replace("%region%", regionCustomName)
        } else {
            val compareRegionPlayers = compareRegionPlayers(player, region)
            if(compareRegionPlayers.isEmpty() || compareRegionPlayers.isBlank()) return null
            compareRegionPlayers
        }
    }

    override fun removeCustomRegion(regionId: String?): Boolean {
        if (regionId == null) return false
        val customRegions = plugin.regionsConfig.getConfig().getConfigurationSection("custom")!!
        if (!customRegions.contains(regionId)) return false
        customRegions.set(regionId, null) // TODO: Ранее .remove, которого уже нет
        plugin.regionsConfig.saveConfig()
        return true
    }

    override fun getCustomRegionIds(): Set<String> = plugin.regionsConfig.getConfig().getConfigurationSection("custom")?.getKeys(false) ?: emptySet()
    override fun isCustomRegion(regionId: String?): Boolean = getCustomRegionIds().contains(regionId)

    override fun addBlacklist(regionId: String): Boolean {
        val blacklist = plugin.config.getStringList("region-settings.blacklist")
        if(blacklist.contains(regionId)) return false
        blacklist.add(regionId)
        plugin.config.set("region-settings.blacklist", blacklist)
        plugin.saveConfig()
        return true
    }
    override fun getBlacklist(): List<String> = plugin.config.getStringList("region-settings.blacklist")
    override fun removeBlacklist(regionId: String?): Boolean {
        if(regionId == null) return false
        val blacklist = plugin.config.getStringList("region-settings.blacklist")
        if(!blacklist.contains(regionId)) return false
        blacklist.remove(regionId)
        plugin.config.set("region-settings.blacklist", blacklist)
        plugin.saveConfig()
        return true
    }

    override fun isGlobalRegion(): Boolean {
        return plugin.config.getBoolean("region-settings.global-region")
    }

    override fun setGlobalRegion(value: Boolean) {
        plugin.config.set("region-settings.global-region", value)
        plugin.saveConfig()
    }

    private fun getRegionId(location: Location, sortBy: String?): String? {
        val regions: Set<ProtectedRegion> = WorldGuardAPI.getRegions(location)
        if(regions.isEmpty()) return null
        if(sortBy == null) return null

        return when(sortBy.lowercase()) {
            "highest" -> regions.maxByOrNull { it.priority }?.id
            "lowest" -> regions.minByOrNull { it.priority }?.id
            "random" -> regions.map { it.id }.random()
            else -> null
        }
    }

    private fun compareRegionPlayers(player: Player, regionId: String): String {
        val actionBarSection = plugin.getMessages().getConfigurationSection("bar")!!
        val location = player.location

        val regionOwners: Set<UUID> = WorldGuardAPI.getRegionOwners(location, regionId)?.uniqueIds ?: emptySet()
        val regionMembers: Set<UUID> = WorldGuardAPI.getRegionMembers(location, regionId)?.uniqueIds ?: emptySet()

        val regionOwner: String = regionOwners.joinToString { plugin.server.getOfflinePlayer(it).name ?: "" }
        val playerUniqueId: UUID = player.uniqueId

        return if (regionOwners.contains(playerUniqueId) || regionMembers.contains(playerUniqueId)) {
            val your: String = actionBarSection.getString("your")!!
            your.replace("%region%", regionId).replace("%owners%", regionOwner)
        } else {
            val notYour: String = actionBarSection.getString("not-your")!!
            notYour.replace("%region%", regionId).replace("%owners%", regionOwner)
        }
    }

    private fun getCustomRegionNameFromConfig(regionName: String): String? {
        val customRegionName = plugin.regionsConfig.getConfig().getString("custom.$regionName") ?: return null
        if(customRegionName.isEmpty() || customRegionName.isBlank()) return null
        return customRegionName
    }

    override fun toString(): String {
        return "RegionHandler(customRegion=${getCustomRegionIds()}, blacklist=${getBlacklist()}, isGlobalRegion=${isGlobalRegion()})"
    }
}