package de.stealwonders.epicsuite.autobroadcast;

import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.events.ConfigurationReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class BroadcastTask implements Runnable, Listener {

    private static final String PREFIX = color("&8[&4Epic&2Questz&8] &a");

    private final EpicSuite plugin;
    private List<String> messages;
    private int index;

    public BroadcastTask(final EpicSuite plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig().getStringList("autobroadcast.messages");
        this.index = 0;
    }

    @Override
    public void run() {
        if (messages.size() >= 1) {
            if (Bukkit.getOnlinePlayers().size() >= 1) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(PREFIX + color(messages.get(index)));
                Bukkit.broadcastMessage("");
                if (index >= messages.size() - 1) {
                    index = 0;
                } else {
                    index++;
                }
            }
        }
    }

    public void addMessage(final String message) {
        messages.add(message);
    }

    public void removeMessage(final String message) {
        messages.remove(message);
    }

    public static String color(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @EventHandler
    public void onConfigReload(final ConfigurationReloadEvent event) {
        messages = plugin.getConfig().getStringList("autobroadcast.messages");
        index = 0;
    }
}
