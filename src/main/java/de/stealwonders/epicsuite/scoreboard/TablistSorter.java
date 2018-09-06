package de.stealwonders.epicsuite.scoreboard;

import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.HashMap;

public class TablistSorter implements Listener {

	private Scoreboard scoreboard;
	private HashMap<TablistTeam, Team> teams;

	public TablistSorter() {
		scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		teams = new HashMap<>();

		if (EpicSuite.getPlugin().getSettingsFile().getSortableTeams() != null) {
			init(EpicSuite.getPlugin().getSettingsFile().getSortableTeams());
		}
	}

	private void init(final ArrayList<TablistTeam> tablistTeams) {
		for (final TablistTeam tablistTeam : tablistTeams) {
			final String name = tablistTeam.getPriority() + "_" + tablistTeam.getPermissionGroup().getName();

			Team team = scoreboard.getTeam(name);
			if (team != null) {
				team.unregister();
			}

			team = scoreboard.registerNewTeam(tablistTeam.getPriority() + "_" + tablistTeam.getPermissionGroup().getName());
			team.setColor(tablistTeam.getColor());
			teams.put(tablistTeam, team);
		}
	}

	private void addPlayer(final Player player) {
		if (teams != null) {
			final PermissionUser permissionUser = PermissionsEx.getUser(player);
			TablistTeam team = null;
			for (final TablistTeam tablistTeam : teams.keySet()) {
				if (permissionUser.inGroup(tablistTeam.getPermissionGroup())) {
					if (team != null) {
						if (team.getPriority() < tablistTeam.getPriority()) {
							team = tablistTeam;
						}
					} else {
						team = tablistTeam;
					}
				}
			}
			if (team != null) {
				teams.get(team).addPlayer(player);
			}
		}
	}

	private void removePlayer(final Player player) {
		if (teams != null) {
			for (final Team team : teams.values()) {
				if (team.getPlayers().contains(player)) {
					team.removePlayer(player);
				}
			}
		}
	}

	public HashMap<TablistTeam, Team> getTeams() {
		return teams;
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		addPlayer(event.getPlayer());
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		removePlayer(event.getPlayer());
	}
}
