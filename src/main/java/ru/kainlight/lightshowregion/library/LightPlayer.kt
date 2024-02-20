package ru.kainlight.lightshowregion.library

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.kainlight.lightshowregion.Main
import ru.kainlight.lightshowregion.library.UTILS.OLD.Parser

val audiences = BukkitAudiences.create(Main.INSTANCE)

/// * PLAYER
fun Player.message(message: String?) {
    if(message.isNullOrEmpty()) return

    val component = Parser.hex(message)
    audiences.player(this).sendMessage(component)
}
fun CommandSender.message(message: String?) {
    if(message.isNullOrEmpty()) return

    val component = Parser.hex(message)
    audiences.sender(this).sendMessage(component)
}
fun Player.actionbar(message: String?) {
    if(message.isNullOrEmpty()) return

    val component = Parser.hex(message)
    audiences.player(this).sendActionBar(component)
}