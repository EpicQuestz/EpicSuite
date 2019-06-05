package de.stealwonders.epicsuite.scoreboard;

import de.stealwonders.epicsuite.EpicSuite;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.log.LogPublishEvent;
import me.lucko.luckperms.api.event.user.track.UserTrackEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TablistSorter implements Listener {

    private EpicSuite plugin;
    private LuckPermsApi luckPermsApi;
    private Scoreboard scoreboard;
    private HashMap<TablistTeam, Team> teams;

    public TablistSorter(final EpicSuite plugin, final LuckPermsApi luckPermsApi) {
        this.plugin = plugin;
        this.luckPermsApi = luckPermsApi;
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        teams = new HashMap<>();

        final EventBus eventBus = luckPermsApi.getEventBus();
        eventBus.subscribe(LogPublishEvent.class, e -> e.getCancellationState().set(true));
        eventBus.subscribe(UserTrackEvent.class, this::onRankUpdate);

        if (plugin.getSettingsFile().getSortableTeams() != null) {
            init(plugin.getSettingsFile().getSortableTeams());
        }
    }

    private void init(final ArrayList<TablistTeam> tablistTeams) {
        for (final TablistTeam tablistTeam : tablistTeams) {
            final String name = tablistTeam.getPriority() + "_" + tablistTeam.getGroup().getName();

            Team team = scoreboard.getTeam(name);
            if (team != null) {
                team.unregister();
            }

            team = scoreboard.registerNewTeam(tablistTeam.getPriority() + "_" + tablistTeam.getGroup().getName());
            team.setColor(tablistTeam.getColor());
            teams.put(tablistTeam, team);

            plugin.getLogger().info("Registering group " + tablistTeam.getColor() + tablistTeam.getGroup().getName() + ChatColor.RESET + " ...");
        }
    }

    private void addPlayer(final Player player) {
        if (teams != null) {
            TablistTeam team = null;
            for (final TablistTeam tablistTeam : teams.keySet()) {

                if (Objects.requireNonNull(luckPermsApi.getUser(player.getUniqueId())).inheritsGroup(tablistTeam.getGroup())) {
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
                teams.get(team).addEntry(player.getName());
            }
        }
    }

    private void removePlayer(final Player player) {
        if (teams != null) {
            for (final Team team : teams.values()) {
                if (team.getEntries().contains(player.getName())) {
                    team.removeEntry(player.getName());
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

    private void onRankUpdate(final UserTrackEvent event) {
        final Player player = Bukkit.getPlayer(event.getUser().getUuid());
        if (player != null) {
            updatePlayer(player);
        }
    }

}
