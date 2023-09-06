package ru.kainlight.lightshowregion.COMMANDS;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.kainlight.lightshowregion.Main;
import ru.kainlight.lightshowregion.UTILS.Parser;

import java.util.*;

public final class LightShowRegion implements CommandExecutor {

    private final Main plugin;

    public LightShowRegion(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginCommand("lightshowregion").setTabCompleter(new TabCompletion());
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (!sender.hasPermission("lightshowregion.help")) return true;

            plugin.getMessenger().sendMessage(sender, " &f&m   &c&l LIGHTSHOWREGION HELP &f&m   ");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr toggle");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr add <region> <name>");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr remove <region>");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr blacklist add <region>");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr blacklist remove <region>");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr notify");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr global");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr reload <config|bar>");
            plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr reconfig (only console)");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle" -> {
                if(!(sender instanceof Player player)) return true;
                if (!player.hasPermission("lightshowregion.toggle")) return true;

                if(plugin.getActionbarManager().toggle(player)) {
                    String toggleMessage = plugin.getMessageConfig().getConfig().getString("region.toggle.disabled");
                    plugin.getMessenger().sendMessage(player, toggleMessage);
                } else {
                    String toggleMessage = plugin.getMessageConfig().getConfig().getString("region.toggle.enabled");
                    plugin.getMessenger().sendMessage(player, toggleMessage);
                }

                return true;
            }
            case "reload" -> {
                if (!sender.hasPermission("lightshowregion.reload.config") || !sender.hasPermission("lightshowregion.reload.bar")) return true;

                if (args.length < 2) {
                    plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr reload <config|bar>");
                    return true;
                }
                if (args[1].equalsIgnoreCase("config")) {
                    if (!sender.hasPermission("lightshowregion.reload.config")) return true;

                    this.reloadConfigs();

                    String reloadConfigs = plugin.getMessageConfig().getConfig().getString("region.reload.config");
                    plugin.getMessenger().sendMessage(sender, reloadConfigs);
                    return true;
                }
                if (args[1].equalsIgnoreCase("bar")) {
                    if (!sender.hasPermission("lightshowregion.reload.bar")) return true;

                    plugin.getServer().getOnlinePlayers().forEach(player -> plugin.getActionbarManager().show(player));
                    String reloadBar = plugin.getMessageConfig().getConfig().getString("region.reload.bar");
                    plugin.getMessenger().sendMessage(sender, reloadBar);
                    return true;
                }
            }
            case "reconfig" -> {
                if (sender instanceof Player) return true;

                plugin.updateConfig();
                plugin.getMessageConfig().updateConfig();
                plugin.getLogger().warning("All configurations has been updated");
                return true;
            }
            case "global" -> {
                if (!sender.hasPermission("lightshowregion.global")) return true;

                boolean isHideGlobalRegion = plugin.getConfig().getBoolean("region-settings.hide-global-region");
                plugin.getConfig().set("region-settings.hide-global-region", !isHideGlobalRegion);
                plugin.saveConfig();

                String global = plugin.getMessageConfig().getConfig().getString("region.global");
                plugin.getMessenger().sendMessage(sender, global);
            }
            case "add" -> {
                if (!sender.hasPermission("lightshowregion.add")) return true;

                if (args.length < 3) {
                    plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr add <region> <name>");
                    return true;
                }

                String region = args[1];
                boolean isRegionExists = plugin.getRegionsConfig().getConfig().contains("custom." + region);
                if (isRegionExists) {
                    String regionExists = plugin.getMessageConfig().getConfig().getString("region.exists").replace("<region>", region);
                    plugin.getMessenger().sendMessage(sender, regionExists);
                    return true;
                }

                String path = String.join(".", "custom", region);
                StringBuilder st = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    st.append(args[i]).append(" ");
                }
                String rgcustomname = st.toString();
                String rgcustomnamehex = Parser.get().hexString(rgcustomname.substring(1));

                plugin.getRegionsConfig().getConfig().set(path, rgcustomname.substring(0, rgcustomname.length() - 1));
                plugin.getRegionsConfig().saveConfig();
                String regionAdded = plugin.getMessageConfig().getConfig().getString("region.added")
                        .replace("<region>", region)
                        .replace("<name>", rgcustomnamehex);
                plugin.getMessenger().sendMessage(sender, regionAdded);
            }
            case "remove" -> {
                if (!sender.hasPermission("lightshowregion.remove")) return true;

                if (args.length < 2) {
                    plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr remove <region>");
                    return true;
                }

                String region = args[1];
                boolean isRegionExists = plugin.getRegionsConfig().getConfig().contains("custom." + region);
                if (!isRegionExists) {
                    String regionNotFound = plugin.getMessageConfig().getConfig().getString("region.notFound").replace("<region>", region);
                    plugin.getMessenger().sendMessage(sender, regionNotFound);
                    return true;
                }

                String path = String.join(".", "custom", region);

                plugin.getRegionsConfig().getConfig().set(path, null);
                plugin.getRegionsConfig().saveConfig();
                String regionRemoved = plugin.getMessageConfig().getConfig().getString("region.removed").replace("<region>", region);
                plugin.getMessenger().sendMessage(sender, regionRemoved);
            }
            case "blacklist" -> {
                List<String> blacklisted = plugin.getConfig().getStringList("region-settings.blacklist");
                if (args[1].equalsIgnoreCase("add")) {
                    if (!sender.hasPermission("lightshowregion.blacklist.add")) return true;

                    if (args.length < 3) {
                        plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr blacklist add <region>");
                        return true;
                    }

                    String region = args[2];
                    if (blacklisted.contains(region)) {
                        String message = plugin.getMessageConfig().getConfig().getString("region.exists").replace("<region>", region);
                        plugin.getMessenger().sendMessage(sender, message);
                        return true;
                    }

                    String message = blacklistManage(blacklisted, region, "added");
                    plugin.getMessenger().sendMessage(sender, message);
                    return true;
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("lightshowregion.blacklist.remove")) return true;

                    if (args.length < 3) {
                        plugin.getMessenger().sendMessage(sender, "&c&l » &f/lsr blacklist remove <region>");
                        return true;
                    }

                    String region = args[2];
                    if (!blacklisted.contains(region)) {
                        String blacklistNotFound = plugin.getMessageConfig().getConfig().getString("region.notFound").replace("<region>", region);
                        plugin.getMessenger().sendMessage(sender, blacklistNotFound);
                        return true;
                    }

                    String message = blacklistManage(blacklisted, region, "removed");
                    plugin.getMessenger().sendMessage(sender, message);
                    return true;
                }
            }
            default -> { return true; }
        }
        return true;
    }

    private String blacklistManage(List<String> blacklisted, String region, String action) {
        blacklisted.add(region);
        plugin.getConfig().set("region-settings.blacklist", blacklisted);
        plugin.saveConfig();
        plugin.reloadConfig();
        String blacklist = plugin.getMessageConfig().getConfig().getString("region.blacklist." + action).replace("<region>", region);
        return blacklist;
    }

    private void reloadConfigs() {
        plugin.saveDefaultConfig();
        plugin.getMessageConfig().saveDefaultConfig();
        plugin.getRegionsConfig().saveDefaultConfig();

        plugin.reloadConfig();
        plugin.getMessageConfig().reloadConfig();
        plugin.getRegionsConfig().reloadConfig();
    }




    final class TabCompletion implements TabCompleter {

        private final Map<String, List<String>> completions = new HashMap<>();

        public TabCompletion() {
            completions.put("forUser", Collections.singletonList("toggle"));
            completions.put("all", Arrays.asList("toggle", "add", "remove", "blacklist", "notify", "global", "reload"));
            completions.put("blacklist", Arrays.asList("add", "remove"));
            completions.put("reload", Arrays.asList("config", "plugin", "bar"));
        }

        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {

            if (args.length == 1) {
                if(sender.hasPermission("lightshowregion.help")) {
                    return completions.get("all");
                } else {
                    return completions.get("forUser");
                }
            }

            switch (args[0].toLowerCase()) {
                case "blacklist" -> {
                    if (args.length == 2 && sender.hasPermission("lightshowregion.blacklist.add") || sender.hasPermission("lightshowregion.blacklist.remove")) {
                        return completions.get("blacklist");
                    }
                }
                case "reload" -> {
                    if (args.length == 2 && sender.hasPermission("lightshowregion.reload.bar") || sender.hasPermission("lightshowregion.reload.config")) {
                        return completions.get("reload");
                    }
                }
            }
            return null;
        }

    }

}
