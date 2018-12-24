package de.stealwonders.epicsuite.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PingCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

		if (sender instanceof Player) {

			final Player player = (Player) sender;
			player.sendMessage("§dYour ping is " + player.spigot().getPing() + "ms");

		} else {
			sender.sendMessage("You must be a player to execute this command.");
		}
		return false;
	}
}
