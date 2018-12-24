package de.stealwonders.epicsuite.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShrugCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {

			final Player player = (Player) sender;
			player.chat("¯\\_(ツ)_/¯");

		} else {
			sender.sendMessage("You must be a player to execute this command.");
		}
		return false;
	}
}
