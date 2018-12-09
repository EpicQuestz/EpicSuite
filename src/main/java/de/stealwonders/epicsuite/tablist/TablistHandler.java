package de.stealwonders.epicsuite.tablist;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TablistHandler implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		final TextComponent header = new TextComponent("\n §2Epic§4Questz\n§7Welcome to the server " + player.getName() + ".\n");
		final TextComponent footer = new TextComponent("\n   §7Visit our website at www.epicquestz.com   \n ");

		player.setPlayerListHeaderFooter(header, footer);
	}
}
