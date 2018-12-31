package de.stealwonders.epicsuite.scoreboard;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class TablistTeam<T> {

	private T t;
	private int priority;
	private ChatColor color;

	public TablistTeam(final T t, final int priority, final ChatColor color) {
		this.t = t;
		this.priority = priority;
		this.color = color;
	}

	public T getT() {
		return t;
	}

	public int getPriority() {
		return priority;
	}

	public ChatColor getColor() {
		return color;
	}

	public String getName() {
		if (getT() instanceof PermissionGroup) {
			PermissionGroup permissionGroup = (PermissionGroup) getT();
			return permissionGroup.getName();
		} else if (getT() instanceof Group) {
			Group group = (Group) getT();
			return group.getName();
		}
		return null;
	}

	public boolean isInGroup(Player player) {
		if (getT() instanceof PermissionGroup) {
			PermissionGroup permissionGroup = (PermissionGroup) getT();
			PermissionUser permissionUser = PermissionsEx.getUser(player);
			return permissionUser.inGroup(permissionGroup);
		} else if (getT() instanceof Group) {
			Group group = (Group) getT();
			return  LuckPerms.getApi().getUser(player.getUniqueId()).inheritsGroup(group);
		}
		return false;
	}

}
