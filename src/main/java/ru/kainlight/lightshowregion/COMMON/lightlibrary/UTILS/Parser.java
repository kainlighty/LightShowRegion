package ru.kainlight.lightshowregion.COMMON.lightlibrary.UTILS;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class Parser {

    private static final Parser parser = new Parser();
    public static Parser get() {
        return parser;
    }

    private static final Pattern hexPatten = Pattern.compile("#[0-9A-Fa-f]{6}");

    private static final LegacyComponentSerializer legacyAmpersand = LegacyComponentSerializer.legacyAmpersand();
    private static final LegacyComponentSerializer legacySection = LegacyComponentSerializer.legacySection();

    public TextComponent hex(String input) {
        Matcher matcher = hexPatten.matcher(input);

        while (matcher.find()) {
            String hexColor = matcher.group();
            if (!hexColor.startsWith("&#")) {
                input = input.replace(hexColor, "&" + hexColor);
            }
        }

        return legacyAmpersand.deserialize(input);
    }

    public String hexString(String input) {
        String serialize = LegacyComponentSerializer.legacySection().serialize(hex(input));
        return serialize;
    }

    public List<String> hex(List<String> messages) {
        if(messages.isEmpty()) return List.of("");
        List<String> copyMessages = new ArrayList<>();
        messages.forEach(message -> {
            String s = hexString(message);
            copyMessages.add(s);
        });

        return copyMessages;
    }

    public String replacedString(@NotNull Component text, String replaceOn, String replaceable) {
        Component component = text.replaceText(TextReplacementConfig.builder()
                .matchLiteral(replaceOn)
                .replacement(replaceable)
                .build());
        return legacySection.serialize(component);
    }

    public Component replacedComponent(@NotNull Component text, String replaceOn, String replaceable) {
        return text.replaceText(TextReplacementConfig.builder()
                .matchLiteral(replaceOn)
                .replacement(replaceable)
                .build());
    }
}