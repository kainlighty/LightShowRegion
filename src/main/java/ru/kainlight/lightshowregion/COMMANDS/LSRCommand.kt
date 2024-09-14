package ru.kainlight.lightshowregion.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import ru.kainlight.lightlibrary.UTILS.Parser
import ru.kainlight.lightlibrary.equalsIgnoreCase
import ru.kainlight.lightlibrary.message
import ru.kainlight.lightlibrary.multiMessage
import ru.kainlight.lightshowregion.Main
import ru.kainlight.lightshowregion.getAudience

@Suppress("WARNINGS")
class LSRCommand(private var plugin: Main) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 0) {
            if (! sender.hasPermission("lightshowregion.help")) return true
            val helpMessage = plugin.messageConfig.getConfig().getStringList("help.commands")

            helpMessage.forEach { sender.getAudience().multiMessage(it) }
            return true
        }

        val helpSection = plugin.messageConfig.getConfig().getConfigurationSection("help")

        return when (args[0].lowercase()) {
            "add" -> {
                if (! sender.hasPermission("lightshowregion.add")) return true;

                if (args.size < 3) {
                    helpSection?.getString("add")?.let {
                        sender.getAudience().multiMessage(it)
                    }
                    return true
                }

                val region: String = args[1]
                val isRegionExists: Boolean = plugin.getRegionsConfig().getConfig().contains("custom." + region)
                if (isRegionExists) {
                    plugin.messageConfig.getConfig().getString("region.exists")?.let {
                        sender.getAudience().multiMessage(it.replace("%region%", region))
                    }
                    return true;
                }

                val path = listOf("custom", region).joinToString(".")
                val st = StringBuilder()

                for (i in 2 until args.size) {
                    st.append(args[i]).append(" ")
                }

                val rgcustomname: String = st.toString()
                val rgcustomnamehex: String = Parser.hexString(rgcustomname.substring(1));

                plugin.getRegionsConfig().getConfig().set(path, rgcustomname.substring(0, rgcustomname.length - 1))
                plugin.getRegionsConfig().saveConfig()

                plugin.messageConfig.getConfig().getString("region.added")?.let {
                    sender.getAudience().multiMessage(it.replace("%region%", region).replace("%name%", rgcustomnamehex))
                }

                return true
            }

            "remove" -> {
                if (! sender.hasPermission("lightshowregion.remove")) return true
                if (args.size < 2) {
                    helpSection?.getString("remove")?.let {
                        sender.getAudience().multiMessage(it)
                    }
                    return true
                }

                val region = args[1]
                val isRegionExists = plugin.getRegionsConfig().getConfig().contains("custom.$region")
                if (! isRegionExists) {
                    plugin.messageConfig.getConfig().getString("region.notFound")?.let {
                        sender.getAudience().multiMessage(it.replace("%region%", region))
                    }
                    return true
                }

                val path = java.lang.String.join(".", "custom", region)
                plugin.getRegionsConfig().getConfig()[path] = null
                plugin.getRegionsConfig().saveConfig()

                plugin.messageConfig.getConfig().getString("region.removed")?.let {
                    sender.getAudience().multiMessage(it.replace("%region%", region))
                }
                return true
            }

            "blacklist" -> {
                val blacklisted = plugin.config.getStringList("region-settings.blacklist")

                if (args[1].equalsIgnoreCase("add")) {
                    if (! sender.hasPermission("lightshowregion.blacklist.add")) return true

                    if (args.size < 3) {
                        helpSection?.getString("blacklist.add")?.let {
                            sender.getAudience().multiMessage(it)
                        }
                        return true
                    }

                    val region = args[2]
                    if (blacklisted.contains(region)) {
                        plugin.messageConfig.getConfig().getString("region.exists")?.let {
                            sender.getAudience().multiMessage(it.replace("%region%", region))
                        }
                        return true
                    }

                    blacklisted.add(region)

                    plugin.config["region-settings.blacklist"] = blacklisted
                    plugin.saveConfig()
                    plugin.reloadConfig()

                    plugin.messageConfig.getConfig().getString("region.blacklist.added")?.let {
                        sender.getAudience().multiMessage(it.replace("%region%", region))
                    }

                    return true
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (! sender.hasPermission("lightshowregion.blacklist.remove")) return true

                    if (args.size < 3) {
                        helpSection?.getString("blacklist.remove")?.let {
                            sender.getAudience().multiMessage(it)
                        }
                        return true
                    }

                    val region = args[2]
                    if (!blacklisted.contains(region)) {
                        plugin.messageConfig.getConfig().getString("region.notFound")?.let {
                            sender.getAudience().multiMessage(it.replace("%region%", region))
                        }
                        return true
                    }

                    blacklisted.remove(region)

                    plugin.config["region-settings.blacklist"] = blacklisted
                    plugin.saveConfig()
                    plugin.reloadConfig()

                    plugin.messageConfig.getConfig().getString("region.blacklist.removed")?.let {
                        sender.getAudience().multiMessage(it.replace("%region%", region))
                    }
                    return true
                }
                return true
            }

            "global" -> {
                if (! sender.hasPermission("lightshowregion.global")) return true
                val isHideGlobalRegion = plugin.config.getBoolean("region-settings.hide-global-region")
                plugin.config["region-settings.hide-global-region"] = ! isHideGlobalRegion
                plugin.saveConfig()

                plugin.messageConfig.getConfig().getString("region.global")?.let {
                    sender.getAudience().multiMessage(it)
                }
                return true
            }

            "reload" -> {
                if (! sender.hasPermission("lightshowregion.reload.config") || ! sender.hasPermission("lightshowregion.reload.bar")) return true

                if (args.size < 2) {
                    helpSection?.getString("reload")?.let {
                        sender.getAudience().multiMessage(it)
                    }
                    return true
                }
                if (args[1].equalsIgnoreCase("config")) {
                    if (! sender.hasPermission("lightshowregion.reload.config")) return true

                    this.reloadConfigs()
                    plugin.messageConfig.getConfig().getString("region.reload.config")?.let {
                        sender.getAudience().multiMessage(it)
                    }
                    return true
                }
                if (args[1].equalsIgnoreCase("bar")) {
                    if (! sender.hasPermission("lightshowregion.reload.bar")) return true

                    plugin.server.onlinePlayers.forEach {
                        plugin.actionbarManager.show(it)
                    }
                    plugin.messageConfig.getConfig().getString("region.reload.bar")?.let {
                        sender.getAudience().multiMessage(it)
                    }
                    return true
                }

                return true
            }

            else -> return false
        }
    }

    private fun blacklistManage(blacklisted: MutableList<String>, region: String, action: String): String? {
        blacklisted.add(region)

        plugin.config["region-settings.blacklist"] = blacklisted
        plugin.saveConfig()
        plugin.reloadConfig()

        return plugin.messageConfig.getConfig().getString("region.blacklist.$action")?.replace("%region%", region)
    }

    private fun reloadConfigs() {
        plugin.saveDefaultConfig()
        plugin.messageConfig.saveDefaultConfig()
        plugin.getRegionsConfig().saveDefaultConfig()

        plugin.reloadConfig()
        plugin.messageConfig.reloadConfig()
        plugin.getRegionsConfig().reloadConfig()

        plugin.reloadParseMode()
    }


    private val completions: Map<String, List<String>> = mapOf(
        "forUser" to listOf("toggle"),
        "all" to listOf("add", "remove", "blacklist", "global", "reload"),
        "blacklist" to listOf("add", "remove"),
        "reload" to listOf("config", "bar")
    )

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (args.size == 1) {
            return if (sender.hasPermission("lightshowregion.help")) completions["all"]
            else completions["forUser"]
        }

        val arg = args[0].lowercase()

        if (arg == "blacklist") {
            if (args.size == 2 && sender.hasPermission("lightshowregion.blacklist.add") || sender.hasPermission("lightshowregion.blacklist.remove")) {
                return completions["blacklist"]
            }
        }

        if (arg == "reload") {
            if (args.size == 2 && sender.hasPermission("lightshowregion.reload.bar") || sender.hasPermission("lightshowregion.reload.config")) {
                return completions["reload"]
            }
        }

        return null
    }

}