package de.stealwonders.epicsuite.scoreboard;

import org.bukkit.ChatColor;
import ru.tehkode.permissions.PermissionGroup;

public class TablistTeam {

	private PermissionGroup permissionGroup;
	private int priority;
	private ChatColor color;

	public TablistTeam(final PermissionGroup permissionGroup, final int priority, final ChatColor color) {
		this.permissionGroup = permissionGroup;
		this.priority = priority;
		this.color = color;
	}

	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public int getPriority() {
		return priority;
	}

	public ChatColor getColor() {
		return color;
	}

}
