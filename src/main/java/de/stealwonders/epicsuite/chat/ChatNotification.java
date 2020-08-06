package de.stealwonders.epicsuite.chat;

import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class ChatNotification implements Listener {

    private final EpicSuite plugin;

    public ChatNotification(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final AsyncPlayerChatEvent event) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (event.getPlayer() != player) {
                if (event.getMessage().toLowerCase().contains(player.getName().toLowerCase())) {
                    if (isSubscriber(player.getUniqueId())) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            addSubscriber(event.getPlayer().getUniqueId());
        }
    }

    public void addSubscriber(final UUID uuid) {
        plugin.getStorageFile().addSubscriber(uuid);
    }

    public void removeSubscriber(final UUID uuid) {
        plugin.getStorageFile().removeSubscriber(uuid);
    }

    public boolean isSubscriber(final UUID uuid) {
        return plugin.getStorageFile().isSubscriber(uuid);
    }

}
