package de.stealwonders.epicsuite.chat;

import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.PermissionHandler;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.MetaData;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatHighlight implements Listener {

	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {

		String message = event.getMessage();

		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (message.toLowerCase().contains(player.getName().toLowerCase())) {

				final Object group = getGroup(event.getPlayer());
				final String suffix = getSuffix(group);
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
	private Object getGroup(@Nonnull final Player player) {
		if (EpicSuite.getPlugin().getPermissionHandler() == PermissionHandler.PERMISSIONSEX) {
			final PermissionUser permissionUser = PermissionsEx.getUser(player);

			final PermissionGroup[] groups = permissionUser.getGroups();
			PermissionGroup highestGroup = groups[0];

			for (final PermissionGroup permissionGroup : groups) {
				if (permissionGroup.getRank() < highestGroup.getRank()) {
					highestGroup = permissionGroup;
				}
			}
			return highestGroup;
		} else if (EpicSuite.getPlugin().getPermissionHandler() == PermissionHandler.LUCKPERMS) {
			return LuckPerms.getApi().getGroup(LuckPerms.getApi().getUser(player.getUniqueId()).getPrimaryGroup());
		}
		return null;
	}

	private String getSuffix(Object object) {
		if (EpicSuite.getPlugin().getPermissionHandler() == PermissionHandler.PERMISSIONSEX) {
			if (object instanceof PermissionGroup) {
				PermissionGroup permissionGroup = (PermissionGroup) object;
				return permissionGroup.getSuffix();
			}
		} else if (EpicSuite.getPlugin().getPermissionHandler() == PermissionHandler.LUCKPERMS) {
			if (object instanceof Group) {
				Group group = (Group) object;
				Contexts contexts = LuckPerms.getApi().getContextManager().getStaticContexts();
				MetaData metaData = group.getCachedData().getMetaData(contexts);
				return metaData.getSuffix();
			}
		}
		return "";
	}
}
