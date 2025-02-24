package ru.kainlight.lightshowregion.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import ru.kainlight.lightlibrary.*
import ru.kainlight.lightlibrary.API.WorldGuardAPI
import ru.kainlight.lightshowregion.API.LightShowRegionAPI
import ru.kainlight.lightshowregion.Main

internal class LSRCommand(private var plugin: Main) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val senderAudience = sender.getAudience()

        if (args.isEmpty()) return sender.sendHelp()

        val helpSection = plugin.messageConfig.getConfig().getConfigurationSection("help")
        return when (args[0].lowercase()) {
            "toggle" -> {
                if(!sender.hasLSRPermission("toggle")) return true

                sender.getPlayer()?.let { player ->
                    val showedPlayer = LightShowRegionAPI.getProvider().getOrCreateShowedPlayer(player)
                    if (args.size == 1) {
                        showedPlayer.toggleAll()
                        return true
                    }

                    when(args[1].lowercase()) {
                        "actionbar" -> showedPlayer.actionbar.toggle()
                        "bossbar" -> showedPlayer.bossbar.toggle()
                        else -> return true
                    }
                }
                return true
            }
            "add" -> {
                if (! sender.hasLSRPermission("add")) return true;

                if (args.size < 3) {
                    helpSection?.getString("add")?.let {
                        senderAudience.multiMessage(it)
                    }
                    return true
                }

                val region: String = args[1]
                if (LightShowRegionAPI.getProvider().getRegionHandler().isCustomRegion(region)) {
                    plugin.messageConfig.getConfig().getString("region.exists")?.let {
                        senderAudience.multiMessage(it.replace("%region%", region))
                    }
                    return true
                }

                val st = StringBuilder()

                for (i in 2 until args.size) {
                    st.append(args[i]).append(" ")
                }

                val regionCustomName: String = st.toString()
                val regionNameToConfig = regionCustomName.substring(0, regionCustomName.length - 1)

                if(LightShowRegionAPI.getProvider().getRegionHandler().addCustomRegion(region, regionNameToConfig)) {
                    plugin.messageConfig.getConfig().getString("region.added")?.let {
                        senderAudience.multiMessage(it.replace("%region%", region).replace("%name%", regionNameToConfig))
                    }
                }

                return true
            }
            "remove" -> {
                if (! sender.hasLSRPermission("remove")) return true
                if (args.size < 2) {
                    helpSection?.getString("remove")?.let {
                        senderAudience.multiMessage(it)
                    }
                    return true
                }

                val region = args[1]
                if (!LightShowRegionAPI.getProvider().getRegionHandler().isCustomRegion(region)) {
                    plugin.messageConfig.getConfig().getString("region.notFound")?.let {
                        senderAudience.multiMessage(it.replace("%region%", region))
                    }
                    return true
                }

                LightShowRegionAPI.getProvider().getRegionHandler().removeCustomRegion(region)
                plugin.messageConfig.getConfig().getString("region.removed")?.let {
                    senderAudience.multiMessage(it.replace("%region%", region))
                }
                return true
            }

            "blacklist" -> {
                when(args[1].lowercase()) {
                    "add" -> {
                        if (! sender.hasLSRPermission("blacklist.add")) return true

                        if (args.size < 3) {
                            helpSection?.getString("blacklist.add")?.let {
                                senderAudience.multiMessage(it)
                            }
                            return true
                        }

                        val region = args[2]
                        if(LightShowRegionAPI.getProvider().getRegionHandler().addBlacklist(region)) {
                            plugin.messageConfig.getConfig().getString("region.blacklist.added")?.replace("%region%", region)?.let {
                                senderAudience.multiMessage(it)
                            }
                        } else {
                            plugin.messageConfig.getConfig().getString("region.exists")?.let {
                                senderAudience.multiMessage(it.replace("%region%", region))
                            }
                        }

                        return true
                    }
                    "remove" -> {
                        if (! sender.hasLSRPermission("blacklist.remove")) return true

                        if (args.size < 3) {
                            helpSection?.getString("blacklist.remove")?.let {
                                senderAudience.multiMessage(it)
                            }
                            return true
                        }

                        val region = args[2]
                        if(LightShowRegionAPI.getProvider().getRegionHandler().removeBlacklist(region)) {
                            plugin.messageConfig.getConfig().getString("region.blacklist.removed")?.replace("%region%", region)?.let {
                                senderAudience.multiMessage(it)
                            }
                        } else {
                            plugin.messageConfig.getConfig().getString("region.notFound")?.let {
                                senderAudience.multiMessage(it.replace("%region%", region))
                            }
                        }
                        return true
                    }
                }
                return true
            }

            "global" -> {
                if (! sender.hasLSRPermission("global")) return true
                val isGlobalRegion = LightShowRegionAPI.getProvider().getRegionHandler().isGlobalRegion()
                val newValueGlobalRegion = !isGlobalRegion

                LightShowRegionAPI.getProvider().getRegionHandler().setGlobalRegion(newValueGlobalRegion)
                plugin.messageConfig.getConfig().getString("region.global")?.let {
                    senderAudience.multiMessage(it)
                }
                return true
            }

            "reload" -> {
                if (! sender.hasLSRPermission("reload.config") || ! sender.hasLSRPermission("reload.actionbar") || !sender.hasLSRPermission("reload.bossbar")) return true

                if (args.size < 2) {
                    helpSection?.getString("reload")?.let {
                        senderAudience.multiMessage(it)
                    }
                    return true
                }

                when(args[1].lowercase()) {
                    "config" -> {
                        if (! sender.hasLSRPermission("reload.config")) return true

                        plugin.reloadConfigs()
                        plugin.messageConfig.getConfig().getString("region.reload.config")?.let {
                            senderAudience.multiMessage(it)
                        }
                        return true
                    }
                    "actionbar" -> {
                        if (! sender.hasLSRPermission("reload.actionbar")) return true

                        LightShowRegionAPI.getProvider().reloadActionbars()
                        plugin.messageConfig.getConfig().getString("region.reload.actionbar")?.let {
                            senderAudience.multiMessage(it)
                        }
                        return true
                    }
                    "bossbar" -> {
                        if (! sender.hasLSRPermission("reload.bossbar")) return true

                        LightShowRegionAPI.getProvider().reloadActionbars()
                        plugin.messageConfig.getConfig().getString("region.reload.bossbar")?.let {
                            senderAudience.multiMessage(it)
                        }
                        return true
                    }
                    "bars" -> {
                        if (! sender.hasLSRPermission("reload.bars")) return true

                        plugin.server.onlinePlayers.forEach {
                            LightShowRegionAPI.getProvider().getOrCreateShowedPlayer(it).showAll()
                        }
                        plugin.messageConfig.getConfig().getString("region.reload.actionbar").sendMessage(senderAudience)
                        plugin.messageConfig.getConfig().getString("region.reload.bossbar").sendMessage(senderAudience)
                        return true
                    }
                }
                return true
            }

            else -> return sender.sendHelp()
        }
    }

    private fun blacklistManage(blacklisted: MutableList<String>, region: String, action: String): String? {
        plugin.config.set("region-settings.blacklist", blacklisted)
        plugin.saveConfig()
        plugin.reloadConfig()

        return plugin.messageConfig.getConfig().getString("region.blacklist.$action")?.replace("%region%", region)
    }

    private fun CommandSender.sendHelp(): Boolean {
        if (! this.hasPermission("lightshowregion.help")) return true
        plugin.messageConfig.getConfig().getStringList("help.commands").forEach {
                this.getAudience().multiMessage(it)
            }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String>? {
        if(!command.name.equalsIgnoreCase("lightshowregion") || !command.aliases.contains("lsr")) return null
        val completions = mutableListOf<String>()

        when (args.size) {
            1 -> {
                if(sender.hasLSRPermission("toggle", false)) completions.add("toggle")
                if(!sender.hasLSRPermission("help", false)) return null
                if(sender.hasLSRPermission("add", false)) completions.add("add")
                if(sender.hasLSRPermission("remove", false)) completions.add("remove")
                if(sender.hasLSRPermission("blacklist.add", false) || sender.hasLSRPermission("blacklist.remove", false)) completions.add("blacklist")
                if(sender.hasLSRPermission("global", false)) completions.add("global")
                if(sender.hasLSRPermission("reload.config", false) || sender.hasLSRPermission("reload.bar", false)) completions.add("reload")
            }

            2 -> {
                when (args[0].lowercase()) {
                    "toggle" -> {
                        if(!sender.hasLSRPermission("toggle", false)) return null
                        completions.addAll(listOf("actionbar", "bossbar"))
                    }

                    "add" -> {
                        if(!sender.hasLSRPermission("add", false)) return null

                        sender.getPlayer()?.let { player ->
                            val existingRegions = LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionIds()
                            val regions = WorldGuardAPI.getRegionNames(player.location).filterNot { it in existingRegions }
                            if(regions.isNotEmpty()) completions.addAll(regions) else completions.add("<name>")
                        }
                    }

                    "remove" -> {
                        if(!sender.hasLSRPermission("remove", false)) return null
                        val existingRegions = LightShowRegionAPI.getProvider().getRegionHandler().getCustomRegionIds()
                        if (existingRegions.isNotEmpty()) completions.addAll(existingRegions) else completions.add("<name>")
                    }

                    "blacklist" -> {
                        if(sender.hasLSRPermission("blacklist.add", false)) completions.add("add")
                        if(sender.hasLSRPermission("blacklist.remove", false)) completions.add("remove")
                    }

                    "reload" -> {
                        if(sender.hasLSRPermission("reload.config", false)) completions.add("config")
                        if(sender.hasLSRPermission("reload.bar", false)) completions.addAll(listOf("actionbar", "bossbar", "bars"))
                    }
                }
            }

            3 -> {
                if(args[1].equalsIgnoreCase("add")) {
                    if(!sender.hasLSRPermission("blacklist.add", false)) return null
                    sender.getPlayer()?.let { player ->
                        val blacklistRegions = LightShowRegionAPI.getProvider().getRegionHandler().getBlacklist()
                        val regions = WorldGuardAPI.getRegionNames(player.location).filterNot { it in blacklistRegions }
                        if(regions.isNotEmpty()) completions.addAll(regions) else completions.add("<name>")
                    }
                }
                if(args[1].equalsIgnoreCase("remove")) {
                    if(!sender.hasLSRPermission("blacklist.remove", false)) return null
                    val blacklistRegions = LightShowRegionAPI.getProvider().getRegionHandler().getBlacklist()

                    if(blacklistRegions.isNotEmpty()) completions.addAll(blacklistRegions)
                    else completions.add("<name>")
                }
            }
        }

        return completions.distinct().filter { it.startsWithIgnoreCase(args.last()) }
    }

    private fun CommandSender.hasLSRPermission(permission: String, message: Boolean = true): Boolean {
        val perm = "lightshowregion.$permission"
        if(this.hasPermission(perm)) return true
        else {
            if(message) {
                plugin.messageConfig.getConfig().getString("no-permissions")?.replace("%permission%", perm)?.let {
                    this.getAudience().multiMessage(it)
                }
            }
        }
        return false
    }

}