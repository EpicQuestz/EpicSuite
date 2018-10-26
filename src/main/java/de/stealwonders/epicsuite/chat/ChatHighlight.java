package de.stealwonders.epicsuite.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;

public class ChatHighlight implements Listener {

	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {

		String message = event.getMessage();

		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (message.toLowerCase().contains(player.getName().toLowerCase())) {

				final String suffix = getGroup(player).getSuffix();
				final String colorString = suffix.substring(suffix.length() - 1);
				final ChatColor color = ChatColor.getByChar(colorString);

				final String string = "§6§o" + player.getName() + color;
				final String regex = "(?i)" + player.getName() + "(?-i)";
				message = message.replaceAll(regex, string);
				event.setMessage(message);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private PermissionGroup getGroup(final Player player) {
		final PermissionUser permissionUser = PermissionsEx.getUser(player);

		final PermissionGroup[] groups = permissionUser.getGroups();
		PermissionGroup highestGroup = groups[0];

		for (final PermissionGroup permissionGroup : groups) {
			if (permissionGroup.getRank() < highestGroup.getRank()) {
				highestGroup = permissionGroup;
			}
		}
		return highestGroup;
	}
}
