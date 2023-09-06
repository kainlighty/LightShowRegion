package ru.kainlight.lightshowregion.UTILS;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import ru.kainlight.lightshowregion.Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Parser {

    private static final Parser parser = new Parser();
    public static Parser get() { return parser; }

    private final Pattern pattern = Pattern.compile("#?&?([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");

    public Component hex(@NotNull String message) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String colorCode = matcher.group(1);
            try {
                TextColor color = TextColor.fromHexString(colorCode);
                if (color != null) {
                    String replacement = TextColor.color(color).toString();
                    matcher.appendReplacement(buffer, replacement);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        matcher.appendTail(buffer);
        TextComponent result = LegacyComponentSerializer.legacy('&').deserialize(buffer.toString());
        return result;
    }

    public String hexString(@NotNull String message) {
        return LegacyComponentSerializer.legacySection().serialize(hex(message));
    }

    public String replacedString(@NotNull Component text, String replaceOn, String replaceable) {
        Component component = text.replaceText(TextReplacementConfig.builder()
                .matchLiteral(replaceOn)
                .replacement(replaceable)
                .build());
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public Component replacedComponent(@NotNull Component text, String replaceOn, String replaceable) {
        return text.replaceText(TextReplacementConfig.builder()
                .matchLiteral(replaceOn)
                .replacement(replaceable)
                .build());
    }

    public Parser logger(@NotNull String message) {
        Main.getInstance().getServer().getConsoleSender().sendMessage(hex(message));
        return this;
    }

}