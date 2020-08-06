package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@CommandPermission("epicsuite.donatormessage")
public class DonatorMessageCommands extends BaseCommand implements Listener {

    private final EpicSuite plugin;

    public DonatorMessageCommands(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("setjoinmessage")
    @Description("Set your custom join message for when you join the server")
    public void onSetJoinMessage(final Player player, final String message) {
        if (message.contains(player.getName())) {
            plugin.getStorageFile().setPlayerJoinMessage(player, message);
            player.sendMessage("§dSet join message to: " + message);
        } else {
            player.sendMessage("§dJoin or leave messages must contain your name!");
        }
    }

    @CommandAlias("deljoinmessage")
    @Description("Delete your custom join message")
    public void onDelJoinMessage(final Player player) {
        plugin.getStorageFile().deletePlayerJoinMessage(player);
        player.sendMessage("§dRemoved join message.");
    }

    @CommandAlias("setleavemessage")
    @Description("Set your custom leave message for when you join the server")
    public void onSetLeaveMessage(final Player player, final String message) {
        if (message.contains(player.getName())) {
            plugin.getStorageFile().setPlayerLeaveMessage(player, message);
            player.sendMessage("§dSet leave message to: " + message);
        } else {
            player.sendMessage("§dJoin or leave messages must contain your name!");
        }
    }

    @CommandAlias("delleavemessage")
    @Description("Delete your custom leave message")
    public void onDelLeaveMessage(final Player player) {
        plugin.getStorageFile().deletePlayerLeaveMessage(player);
        player.sendMessage("§dRemoved leave message.");
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            final String message = plugin.getStorageFile().getPlayerJoinMessage(event.getPlayer());
            if (message != null) {
                Bukkit.broadcastMessage(ChatColor.GRAY + message);
            } else {
                Bukkit.broadcastMessage(ChatColor.GRAY + event.getPlayer().getName() + " has joined the server.");
            }
        });
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            final String message = plugin.getStorageFile().getPlayerLeaveMessage(event.getPlayer());
            if (message != null) {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + message);
            } else {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + event.getPlayer().getName() + " has left the server.");
            }
        });
    }

}
