package de.stealwonders.epicsuite.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PingCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			try {
				Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
				int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
				player.sendMessage("§dYour ping is " + ping + "ms");
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException exception) {
				player.sendMessage("§cTheir was an error retrieving your ping. Please contact a server administrator.");
				exception.printStackTrace();
			}

		} else {
			sender.sendMessage("You must be a player to execute this command.");
		}
		return false;
	}
}
