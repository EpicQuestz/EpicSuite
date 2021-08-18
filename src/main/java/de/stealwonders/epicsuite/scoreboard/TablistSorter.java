package de.stealwonders.epicsuite.scoreboard;

import de.stealwonders.epicsuite.EpicSuite;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.log.LogPublishEvent;
import net.luckperms.api.event.user.track.UserTrackEvent;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class TablistSorter implements Listener {

    private static final String SORTABLE_GROUPS_PATH_ROOT = "groups";

    private final EpicSuite plugin;
    private final Scoreboard scoreboard;
    private final HashMap<TablistTeam, Team> teams = new HashMap<>();

    public TablistSorter(final EpicSuite plugin, final LuckPerms luckPermsApi) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        final EventBus eventBus = luckPermsApi.getEventBus();
        eventBus.subscribe(LogPublishEvent.class, event -> event.setCancelled(true));
        eventBus.subscribe(UserTrackEvent.class, this::onRankUpdate);

        getSortableTeams();
        init(getSortableTeams());
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

    public ArrayList<TablistTeam> getSortableTeams() {
        if (plugin.getConfig().isSet(SORTABLE_GROUPS_PATH_ROOT)) {

            final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection(SORTABLE_GROUPS_PATH_ROOT);

            final ArrayList<TablistTeam> sortableTeams = new ArrayList<>();
            if (configurationSection == null) return sortableTeams;

            for (final String group : configurationSection.getKeys(false)) {

                final String SORTABLE_GROUPS_PATH_PRIORITY = group + ".priority";
                final String SORTABLE_GROUPS_PATH_COLOR =  group + ".color";

                final int priority = configurationSection.isSet(SORTABLE_GROUPS_PATH_PRIORITY) ? configurationSection.getInt(SORTABLE_GROUPS_PATH_PRIORITY) : 0;
                final ChatColor color = configurationSection.isSet(SORTABLE_GROUPS_PATH_COLOR) ? ChatColor.valueOf(configurationSection.getString(SORTABLE_GROUPS_PATH_COLOR)) : ChatColor.WHITE;

                final Group luckGroup = plugin.getLuckPermsApi().getGroupManager().getGroup(group);
                if (luckGroup != null) {
                    final TablistTeam team = new TablistTeam(luckGroup, priority, color);
                    sortableTeams.add(team);
                }
            }
            return sortableTeams;
        }
        return new ArrayList<>();
    }

    private void onRankUpdate(final UserTrackEvent event) {
        final Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
        if (player != null) {
            updatePlayer(player);
        }
    }

    private void addPlayer(final Player player) {
        TablistTeam team = null;
        for (final TablistTeam tablistTeam : teams.keySet()) {

            if (player.hasPermission("group." + tablistTeam.getGroup().getName())) {
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

    private void removePlayer(final Player player) {
        for (final Team team : teams.values()) {
            if (team.getEntries().contains(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }

    public void updatePlayer(final Player player) {
        removePlayer(player);
        addPlayer(player);
    }

    public void addPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void removePlayers() {
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);
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
