package ru.kainlight.lightshowregion.COMMANDS

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import ru.kainlight.lightshowregion.Main
import ru.kainlight.lightshowregion.library.message


class LSRCommand(private var plugin: Main) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (args.size == 0) {
            if (! sender.hasPermission("lightshowregion.help")) return true
            sender.sendMessage("")
            sender.message(" &f&m   &c&l LIGHTSHOWREGION HELP &f&m   ")
            sender.message("&c&l » &f/lsr add <region> <name>")
            sender.message("&c&l » &f/lsr remove <region>")
            sender.message("&c&l » &f/lsr blacklist add <region>")
            sender.message("&c&l » &f/lsr blacklist remove <region>")
            sender.message("&c&l » &f/lsr global")
            sender.message("&c&l » &f/lsr reload <config|bar>")
            sender.sendMessage("")
            return true
        }

        return when (args[0].lowercase()) {
            "reload" -> {
                if (! sender.hasPermission("lightshowregion.reload.config") || ! sender.hasPermission("lightshowregion.reload.bar")) return true

                if (args.size < 2) {
                    sender.message("&c&l » &f/lsr reload <config|bar>")
                    return true
                }
                if (args[1].equals("config", ignoreCase = true)) {
                    if (! sender.hasPermission("lightshowregion.reload.config")) return true

                    this.reloadConfigs()
                    plugin.getMessageConfig().config.getString("region.reload.config")?.let {
                        sender.message(it)
                    }
                    return true
                }
                if (args[1].equals("bar", ignoreCase = true)) {
                    if (! sender.hasPermission("lightshowregion.reload.bar")) return true

                    plugin.server.onlinePlayers.forEach { player: Player? -> plugin.actionbarManager.show(player) }
                    plugin.getMessageConfig().config.getString("region.reload.bar")?.let {
                        sender.message(it)
                    }
                    return true
                }

                return true
            }

            "remove" -> {
                if (! sender.hasPermission("lightshowregion.remove")) return true
                if (args.size < 2) {
                    sender.message("&c&l » &f/lsr remove <region>")
                    return true
                }

                val region = args[1]
                val isRegionExists = plugin.getRegionsConfig().config.contains("custom.$region")
                if (! isRegionExists) {
                    plugin.getMessageConfig().config.getString("region.notFound")?.let {
                        sender.message(it.replace("<region>", region))
                    }
                    return true
                }

                val path = java.lang.String.join(".", "custom", region)
                plugin.getRegionsConfig().config[path] = null
                plugin.getRegionsConfig().saveConfig()

                plugin.getMessageConfig().config.getString("region.removed")?.let {
                    sender.message(it.replace("<region>", region))
                }
                return true
            }

            "blacklist" -> {
                val blacklisted = plugin.config.getStringList("region-settings.blacklist")

                if (args[1].equals("add", ignoreCase = true)) {
                    if (! sender.hasPermission("lightshowregion.blacklist.add")) return true

                    if (args.size < 3) {
                        sender.message("&c&l » &f/lsr blacklist add <region>")
                        return true
                    }

                    val region = args[2]
                    if (blacklisted.contains(region)) {
                        plugin.getMessageConfig().config.getString("region.exists")?.let {
                            sender.message(it.replace("<region>", region))
                        }
                        return true
                    }

                    blacklistManage(blacklisted, region, "added")?.let {
                        sender.message(it)
                    }

                    return true
                }
                if (args[1].equals("remove", ignoreCase = true)) {
                    if (! sender.hasPermission("lightshowregion.blacklist.remove")) return true

                    if (args.size < 3) {
                        sender.message("&c&l » &f/lsr blacklist remove <region>")
                        return true
                    }

                    val region = args[2]
                    if (! blacklisted.contains(region)) {
                        plugin.getMessageConfig().config.getString("region.notFound")?.let {
                            sender.message(it.replace("<region>", region))
                        }
                        return true
                    }

                    blacklistManage(blacklisted, region, "removed")?.let {
                        sender.message(it)
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
        val blacklist =
            plugin.getMessageConfig().config.getString("region.blacklist.$action")?.replace("<region>", region)
        return blacklist
    }

    private fun reloadConfigs() {
        plugin.saveDefaultConfig()
        plugin.getMessageConfig().saveDefaultConfig()
        plugin.getRegionsConfig().saveDefaultConfig()

        plugin.reloadConfig()
        plugin.getMessageConfig().reloadConfig()
        plugin.getRegionsConfig().reloadConfig()
    }


    private val completions: Map<String, List<String>> = mapOf(
        "forUser" to listOf("toggle"),
        "all" to listOf("add", "remove", "blacklist", "global", "reload"),
        "blacklist" to listOf("add", "remove"),
        "reload" to listOf("config", "bar")
    )

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {
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

        if(arg == "reload") {
            if (args.size == 2 && sender.hasPermission("lightshowregion.reload.bar") || sender.hasPermission("lightshowregion.reload.config")) {
                return completions["reload"]
            }
        }

        return null
    }

}