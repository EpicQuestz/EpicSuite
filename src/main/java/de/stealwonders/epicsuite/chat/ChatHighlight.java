package de.stealwonders.epicsuite.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHighlight implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		String message = event.getMessage();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (message.toLowerCase().contains(player.getName().toLowerCase())) {
				String string = "§6§o" + player.getName() + ChatColor.getLastColors(event.getMessage());
				String regex = "(?i)" + player.getName() + "(?-i)";
				message = message.replaceAll(regex, string);
				event.setMessage(message);
			}
		}
	}
}
