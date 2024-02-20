package ru.kainlight.lightshowregion.library.UTILS.OLD

import lombok.Getter
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.regex.Pattern

@Getter
object Parser {
    private val hexPatten: Pattern = Pattern.compile("#[0-9A-Fa-f]{6}")

    private val legacyAmpersand = LegacyComponentSerializer.legacyAmpersand()
    private val legacySection = LegacyComponentSerializer.legacySection()

    fun hex(input: String): TextComponent {
        var text = input
        val matcher = hexPatten.matcher(text)

        while (matcher.find()) {
            val hexColor = matcher.group()
            if (!hexColor.startsWith("&#")) {
                text = text.replace(hexColor, "&$hexColor")
            }
        }

        return legacyAmpersand.deserialize(text)
    }

    fun hexString(input: String): String {
        val serialize = LegacyComponentSerializer.legacySection().serialize(hex(input))
        return serialize
    }

    fun hex(messages: List<String>): List<String> {
        if (messages.isEmpty()) return ArrayList()
        val copyMessages: MutableList<String> = ArrayList()
        messages.map { hexString(it) }.forEach {
            copyMessages.add(it)
        }

        return copyMessages
    }
}
