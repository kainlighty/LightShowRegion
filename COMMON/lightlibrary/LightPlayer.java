package ru.kainlight.lightshowregion.COMMON.lightlibrary;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kainlight.lightshowregion.COMMON.lightlibrary.UTILS.Parser;
import ru.kainlight.lightshowregion.Main;

import java.time.Duration;
import java.util.List;

@SuppressWarnings("unused")
public final class LightPlayer {

    @Getter
    private static final BukkitAudiences audience = BukkitAudiences.create(Main.getInstance());

    private final Audience sender;

    private LightPlayer(CommandSender sender) {
        this.sender = audience.sender(sender);
    }

    private LightPlayer(Player player) {
        this.sender = audience.player(player);
    }

    public static LightPlayer of(CommandSender sender) {
        return new LightPlayer(sender);
    }

    public static LightPlayer of(Player player) {
        return new LightPlayer(player);
    }

    public void sendClickableHoverMessage(String message, String hover, String command) {
        if (message == null) return;

        Component component = Parser.get().hex(message);
        Component component2 = Parser.get().hex(hover);
        Component hoverComponent = component
                .clickEvent(ClickEvent.runCommand(command))
                .hoverEvent(HoverEvent.showText(component2));

        sender.sendMessage(hoverComponent);
    }

    public void sendClickableMessage(String message, String command) {
        if (message == null) return;
        Component component = Parser.get().hex(message);
        component = component.clickEvent(ClickEvent.runCommand(command));

        sender.sendMessage(component);
    }

    public void sendMessage(Component component) {
        if (component == null) return;

        sender.sendMessage(component);
    }

    public void sendMessage2(String message) {
        if (message == null) return;

        Component component = Parser.get().hex(message);
        sender.sendMessage(component);
    }

    public void sendMessage(String message) {
        if (message == null) return;
        Component component = Parser.get().hex(message);

        sender.sendMessage(component);
    }

    public void sendMessage(List<String> message) {
        if (message == null || message.isEmpty()) return;
        message.forEach(this::sendMessage);
    }

    public void sendActionbar(String message) {
        if (message == null) return;
        Component component = Parser.get().hex(message);
        sender.sendActionBar(component);
    }

    public void sendHoverMessage(String message, String hover) {
        if (message == null) return;

        Component component = Parser.get().hex(message);
        Component hoverComponent = component.hoverEvent(HoverEvent.showText(component));

        sender.sendMessage(hoverComponent);
    }

    @SuppressWarnings("all")
    public void sendTitle(String title, String subtitle, long fadeIn, long stay, long fadeOut) {
        Component titleComponent = Parser.get().hex(title);
        Component subtitleComponent = Parser.get().hex(subtitle);

        Title.Times times = Title.Times.of(Duration.ofSeconds(fadeIn), Duration.ofSeconds(stay), Duration.ofSeconds(fadeOut));
        Title titleToSend = Title.title(titleComponent, subtitleComponent, times);

        sender.showTitle(titleToSend);
    }

    public void sendTitle(String title, String subtitle) {
        Component titleComponent = Parser.get().hex(title);
        Component subtitleComponent = Parser.get().hex(subtitle);

        Title titleToSend = Title.title(titleComponent, subtitleComponent);

        sender.showTitle(titleToSend);
    }

    public void clearTitle() {
        sender.clearTitle();
    }

    public void showBossBar(BossBar bossBar) {
        sender.showBossBar(bossBar);
    }

    public void hideBossBar(BossBar bossBar) {
        sender.hideBossBar(bossBar);
    }

    public static void sendMessage(String message, Player... players) {
        if (message == null) return;

        Component component = Parser.get().hex(message);
        for (Player player : players) {
            audience.player(player).sendMessage(component);
        }
    }

    public static void sendMessageForAll(String message) {
        if (message == null) return;
        Component component = Parser.get().hex(message);

        Bukkit.getServer().getOnlinePlayers().forEach(online -> audience.player(online).sendMessage(component));
    }

    public static void sendMessageForAll(List<String> messages) {
        if (messages == null || messages.isEmpty()) return;

        messages.forEach(LightPlayer::sendMessageForAll);
    }

    public static void sendMessageForAll(String... message) {
        if (message == null) return;

        for (String msg : message) {
            sendMessageForAll(msg);
        }

    }

}