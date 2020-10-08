package de.stealwonders.epicsuite.autobroadcast;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class BroadcastTask implements Runnable {

    private static final String PREFIX = color("&8[&4Epic&2Questz&8] &e");
    private final List<String> messages;
    private int index;

    public BroadcastTask(final List<String> messages) {
        this.messages = messages;
        this.index = 0;
    }

    @Override
    public void run() {
        if (messages.size() >= 1) {
            if (Bukkit.getOnlinePlayers().size() >= 1) {
                Bukkit.broadcastMessage(PREFIX + color(messages.get(index)));
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
}
