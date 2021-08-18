package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("clearchat")
@CommandPermission("epicsuite.clearchat")
@Description("Clears the server chat")
public class ChatClearCommand extends BaseCommand {

    @Default
    public void onCommand(final CommandSender commandSender) {
        for (int i = 0; i < 100; i++) {
            Bukkit.broadcast(Component.empty());
        }
        Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize("§dThe chat was cleared by §l§n" + commandSender.getName()));
        Bukkit.broadcast(Component.empty());
        Bukkit.broadcast(Component.empty());
    }
}
