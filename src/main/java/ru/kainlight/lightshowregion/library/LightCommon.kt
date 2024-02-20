package ru.kainlight.lightshowregion.library

import org.bukkit.Bukkit
import org.bukkit.Server
import ru.kainlight.lightshowregion.library.UTILS.OLD.Parser
import java.nio.charset.StandardCharsets
import java.util.*

object LightCommon {
    /// private val VERSION_PATTERN: Pattern = Pattern.compile("MC: (\\d+\\.\\d+)")

    fun Server.generateUniqueId(username: String) = UUID.nameUUIDFromBytes("OfflinePlayer:$username".toByteArray(StandardCharsets.UTF_8))
    fun isVersion(number: String) = Bukkit.getServer().version.contains(number)

    fun logger(message: String): LightCommon {
        val messageComponent = Parser.hexString(message)
        Bukkit.getServer().consoleSender.sendMessage(messageComponent)
        return this;
    }

    fun higher(version: String): Boolean {
        val serverVersion = Bukkit.getServer().version
        val serverVersionParts = serverVersion.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[2].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val targetVersionParts = version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val serverMajor = serverVersionParts[0].toInt()
        val serverMinor = serverVersionParts[1].toInt()

        val targetMajor = targetVersionParts[0].toInt()
        val targetMinor = targetVersionParts[1].toInt()

        return if (serverMajor > targetMajor) {
            true
        } else if (serverMajor == targetMajor) {
            serverMinor >= targetMinor
        } else {
            false
        }
    }

    fun lower(targetVersion: String?): Boolean {
        val serverVersion = Bukkit.getVersion()

        // Извлечение версии Minecraft из строки
        val versionParts = serverVersion.split("MC: ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1].split("\\)".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val serverMinecraftVersion = versionParts[0]

        // Сравнение версий
        return serverMinecraftVersion.compareTo(targetVersion!!) < 0
    }

    /*fun lower(String version): Boolean {
        String serverVersion = Bukkit.getServer().getVersion();
        String[] serverVersionParts = serverVersion.split(" ")[2].split("\\.");
        String[] targetVersionParts = version.split("\\.");

        int serverMajor = Integer.parseInt(serverVersionParts[0]);
        int serverMinor = Integer.parseInt(serverVersionParts[1]);

        int targetMajor = Integer.parseInt(targetVersionParts[0]);
        int targetMinor = Integer.parseInt(targetVersionParts[1]);

        if (serverMajor < targetMajor) {
            return true;
        } else if (serverMajor == targetMajor) {
            return serverMinor <= targetMinor;
        } else {
            return false;
        }
    }*/
}
