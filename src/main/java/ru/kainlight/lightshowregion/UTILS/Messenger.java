package ru.kainlight.lightshowregion.UTILS;

import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import ru.kainlight.lightshowregion.Main;

import java.util.List;

public final class Messenger {
    private final Main plugin;

    public Messenger(Main plugin) {
        this.plugin = plugin;
    }

    public void sendTitle(Player player, Component title, Component subTitle, int fadeIn, int stay, int fadeOut) {
        Title.Times times = Title.Times.times(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeOut));
        Title resultTitle = Title.title(title, subTitle, times);
        player.showTitle(resultTitle);
    }

    public void sendTitle(Player player, Component title, Component subTitle) {
        Title resultTitle = Title.title(title, subTitle);
        player.showTitle(resultTitle);
    }

    public void clickableText(Player player, String message, String hover, String command) {
        Component messageComponent = Parser.get().hex(message)
                .clickEvent(ClickEvent.runCommand(command))
                .hoverEvent(HoverEvent.showText(Component.text(hover)));
        player.sendMessage(messageComponent);
    }

    public void sendMessage(CommandSender sender, String message) {
        Component messageComponent = Parser.get().hex(message);
        sender.sendMessage(messageComponent);
    }

    public void sendMessage(CommandSender sender, List<String> message) {
        for (String msg : message) {
            Component messageComponent = Parser.get().hex(msg);
            sender.sendMessage(messageComponent);
        }
    }

    public void sendActionbar(Player player, String message) {
        Component messageComponent = Parser.get().hex(message);
        player.sendActionBar(messageComponent);
    }

    public void sendHoverMessage(Player player, String message, String hover) {
        Component mainComponent = Parser.get().hex(message);
        Component hoverComponent = Parser.get().hex(hover);

        mainComponent = mainComponent.hoverEvent(HoverEvent.showText(hoverComponent));
        player.sendMessage(mainComponent);
    }

    public void sendMessageForAll(String message) {
        for (Player onlinePlayers : plugin.getServer().getOnlinePlayers()) {
            sendMessage(onlinePlayers, message);
        }
    }

    public void sendMessageForAll(List<String> message) {
        for (Player onlinePlayers : plugin.getServer().getOnlinePlayers()) {
            for (String msg : message) {
                sendMessage(onlinePlayers, msg);
            }
        }
    }
}
