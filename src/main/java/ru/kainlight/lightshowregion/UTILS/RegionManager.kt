package ru.kainlight.lightshowregion.UTILS

import me.clip.placeholderapi.PlaceholderAPI
import org.apache.commons.lang3.StringUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import ru.kainlight.lightshowregion.Main

class RegionManager {

    fun sendRegionName(player: Player?): String {
        if (player == null) return ""
        val names: MutableList<String?> = ArrayList()
        val msgs = Main.INSTANCE.getMessageConfig().getConfig()

        val hideGlobalRegion: Boolean = Main.INSTANCE.getConfig().getBoolean("region-settings.hide-global-region")
        val globalBarMessage: String = msgs.getString("actionbar.global")!!

        val region: String = getRegion(player)
        if (region != "") {
            val regionCustomName: String? = getCustomRegionName(region)
            if (!regionCustomName.isNullOrEmpty()) {
                val blacklist: List<String> = Main.INSTANCE.getConfig().getStringList("region-settings.blacklist")
                val blacklistBarMessage: String = msgs.getString("actionbar.blacklisted")!!
                val regionBarMessage: String = msgs.getString("actionbar.region")!!

                if (blacklist.contains(region)) {
                    return blacklistBarMessage
                }

                names.add(regionCustomName)
                return if (regionCustomName.startsWith("!")) regionCustomName.substring(1)
                else regionBarMessage.replace("<region>", regionCustomName)
            } else {
                return compareRegionPlayers(player, region)
            }
        }

        return if (names.isEmpty() && ! hideGlobalRegion) globalBarMessage
        else StringUtils.join(names, ", ")
    }

    private fun compareRegionPlayers(player: Player, regionName: String): String {
        val msgs = Main.INSTANCE.getMessageConfig().getConfig()

        val notYour: String = msgs.getString("actionbar.not-your")!!
        val your: String = msgs.getString("actionbar.your")!!

        val regionOwner: String = getRegionOwner(player)
        val regionMembers: String = getRegionMembers(player)

        return if (regionOwner.contains(player.name) || regionMembers.contains(player.name)) {
            your.replace("<name>", regionName).replace("<owners>", regionOwner)
        } else {
            notYour.replace("<name>", regionName).replace("<owners>", regionOwner)
        }
    }

    private fun getCustomRegionName(region: String): String? {
        val customRegions: ConfigurationSection = Main.INSTANCE.getRegionsConfig().getConfig().getConfigurationSection("custom.") ?: return null
        return if (customRegions.contains(region)) customRegions.getString(region) else null
    }

    private fun getRegion(player: Player) = PlaceholderAPI.setPlaceholders(player, "%worldguard_region_name%")
    private fun getRegionOwner(player: Player) = PlaceholderAPI.setPlaceholders(player, "%worldguard_region_owner%")
    private fun getRegionMembers(player: Player) = PlaceholderAPI.setPlaceholders(player, "%worldguard_region_members%")

}