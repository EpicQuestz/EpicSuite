package de.stealwonders.epicsuite.commands;

import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DonatorMessageCommands implements CommandExecutor, Listener {

    private EpicSuite plugin;

    public DonatorMessageCommands(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("epicsuite.donatormessage")) {

            String message = null;
            if (args.length >= 1) {
                final StringBuilder stringBuilder = new StringBuilder();
                for (final String arg : args) {
                    stringBuilder.append(arg).append(" ");
                }
                message = stringBuilder.toString();
            }

                switch (label.toLowerCase()) {

                    case "setjoinmessage":
                        if (message != null) {
                            if (message.contains(player.getName())) {
                                plugin.getStorageFile().setPlayerJoinMessage(player, message);
                                player.sendMessage("§dSet join message to: " + message);
                            } else {
                                player.sendMessage("§dJoin or leave messages must contain your name!");
                            }
                        } else {
                            player.sendMessage("§dPlease specify a join message.");
                        }
                        break;

                    case "setleavemessage":
                        if (message != null) {
                            if (message.contains(player.getName())) {
                                plugin.getStorageFile().setPlayerLeaveMessage(player, message);
                                player.sendMessage("§dSet leave message to: " + message);
                            } else {
                                player.sendMessage("§dJoin or leave messages must contain your name!");
                            }
                        } else {
                            player.sendMessage("§dPlease specify a leave message.");
                        }
                        break;

                    case "deljoinmessage":
                        plugin.getStorageFile().deletePlayerJoinMessage(player);
                        player.sendMessage("§dRemoved join message.");
                        break;

                    case "delleavemessage":
                        plugin.getStorageFile().deletePlayerLeaveMessage(player);
                        player.sendMessage("§dRemoved leave message.");
                        break;
                }

        } else {
            player.sendMessage("§dYou need to §5§ldonate &dto be able to use this feature. Use §5§l/buy §dfor more information.");
        }
        return false;
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
