package de.stealwonders.epicsuite.commands;

import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.chat.ChatNotification;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotificationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player =  (Player) sender;
            final ChatNotification chatNotifier = EpicSuite.getPlugin().getChatNotifier();

            if (chatNotifier.isSubscriber(player.getUniqueId())) {
                chatNotifier.removeSubscriber(player.getUniqueId());
                player.sendMessage("§dDisabled chat notifications");
            } else {
                chatNotifier.addSubscriber(player.getUniqueId());
                player.sendMessage("§dEnabled chat notifications");
            }

        } else {
            sender.sendMessage("You must be a player to execute this command.");
        }
        return false;
    }
}
