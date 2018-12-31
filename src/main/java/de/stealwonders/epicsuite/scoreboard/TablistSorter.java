package de.stealwonders.epicsuite.scoreboard;

import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.scoreboard.impl.LuckPermsHandler;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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

	public TablistSorter(LuckPermsApi luckPermsApi) {
		scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		teams = new HashMap<>();

		if (EpicSuite.getPlugin().getSettingsFile().getSortableTeams() != null) {
			init(EpicSuite.getPlugin().getSettingsFile().getSortableTeams());
		}

		new LuckPermsHandler(luckPermsApi, this);
	}

	private void init(final ArrayList<TablistTeam> tablistTeams) {
		for (final TablistTeam tablistTeam : tablistTeams) {
			final String name = tablistTeam.getPriority() + "_" + tablistTeam.getName();

			Team team = scoreboard.getTeam(name);
			if (team != null) {
				team.unregister();
			}

			team = scoreboard.registerNewTeam(tablistTeam.getPriority() + "_" + tablistTeam.getName());
			team.setColor(tablistTeam.getColor());
			teams.put(tablistTeam, team);

			EpicSuite.getPlugin().getLogger().info("Registering group " + tablistTeam.getColor() + tablistTeam.getName() + ChatColor.RESET + " ...");
		}
	}

	@SuppressWarnings("deprecation")
	private void addPlayer(final Player player) {
		if (teams != null) {
			TablistTeam team = null;
			for (final TablistTeam tablistTeam : teams.keySet()) {
				if (tablistTeam.isInGroup(player)) {
					if (team != null) {
						if (team.getPriority() > tablistTeam.getPriority()) {
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

	@SuppressWarnings("deprecation")
	private void removePlayer(final Player player) {
		if (teams != null) {
			for (final Team team : teams.values()) {
				if (team.getPlayers().contains(player)) {
					team.removePlayer(player);
				}
			}
		}
	}

	public void updatePlayer(final Player player) {
		removePlayer(player);
		addPlayer(player);
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

	@EventHandler
	public void onRankUpdate(final PermissionEntityEvent event) {
		if (event.getAction() == PermissionEntityEvent.Action.RANK_CHANGED) {
			final Player player = Bukkit.getPlayer(UUID.fromString(event.getEntityIdentifier()));
			if (player != null) {
				updatePlayer(player);
			}
		}
	}
}
