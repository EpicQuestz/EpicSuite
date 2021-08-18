package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.storage.StorageFile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

@CommandPermission("epicsuite.donatormessage")
public class DonatorMessageCommands extends BaseCommand implements Listener {

    private final StorageFile storage;

    public DonatorMessageCommands(final StorageFile storage) {
        this.storage = storage;
    }

    @CommandAlias("setjoinmessage")
    @Description("Set your custom join message for when you join the server")
    public void onSetJoinMessage(final Player player, final String message) {
        if (message.contains(player.getName())) {
            storage.setPlayerJoinMessage(player, message);
            player.sendMessage("§dSet join message to: " + message);
        } else {
            player.sendMessage("§dJoin or leave messages must contain your name!");
        }
    }

    @CommandAlias("deljoinmessage")
    @Description("Delete your custom join message")
    public void onDelJoinMessage(final Player player) {
        storage.deletePlayerJoinMessage(player);
        player.sendMessage("§dRemoved join message.");
    }

    @CommandAlias("setleavemessage")
    @Description("Set your custom leave message for when you join the server")
    public void onSetLeaveMessage(final Player player, final String message) {
        if (message.contains(player.getName())) {
            storage.setPlayerLeaveMessage(player, message);
            player.sendMessage("§dSet leave message to: " + message);
        } else {
            player.sendMessage("§dJoin or leave messages must contain your name!");
        }
    }

    @CommandAlias("delleavemessage")
    @Description("Delete your custom leave message")
    public void onDelLeaveMessage(final Player player) {
        storage.deletePlayerLeaveMessage(player);
        player.sendMessage("§dRemoved leave message.");
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        event.joinMessage(null);
        final Player player = event.getPlayer();
        final String message = storage.getPlayerJoinMessage(player);
        Bukkit.broadcast(Component.text(Objects.requireNonNullElseGet(message, () -> player.getName() + " has joined the server.")).color(NamedTextColor.GRAY));
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        event.quitMessage(null);
        final Player player = event.getPlayer();
        final String message = storage.getPlayerLeaveMessage(player);
        Bukkit.broadcast(Component.text(Objects.requireNonNullElseGet(message, () -> player.getName() + " has left the server.")).color(NamedTextColor.DARK_GRAY));
    }

}
