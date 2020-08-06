package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.chat.ChatNotification;
import org.bukkit.entity.Player;

@CommandAlias("chatnotification|notification|chatping")
@Description("Toggles the chat notifier on / off")
public class NotificationCommand extends BaseCommand {

    private final EpicSuite plugin;

    public NotificationCommand(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(final Player player) {
        final ChatNotification chatNotifier = plugin.getChatNotifier();
        if (chatNotifier.isSubscriber(player.getUniqueId())) {
            chatNotifier.removeSubscriber(player.getUniqueId());
            player.sendMessage("§dDisabled chat notifications");
        } else {
            chatNotifier.addSubscriber(player.getUniqueId());
            player.sendMessage("§dEnabled chat notifications");
        }
    }

}
