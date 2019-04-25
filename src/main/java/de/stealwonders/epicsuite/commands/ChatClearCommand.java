package de.stealwonders.epicsuite.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatClearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender.hasPermission("epicsuite.clearchat")) {
            for (int i = 0; i < 100; i++) {
                Bukkit.broadcastMessage("");
            }
            Bukkit.broadcastMessage("§dThe chat was cleared by §l§n" + sender.getName());
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");
        }
        return false;
    }
}
