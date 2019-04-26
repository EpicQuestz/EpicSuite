package de.stealwonders.epicsuite.scoreboard.impl;

import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.scoreboard.TablistSorter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import java.util.UUID;

public class PermissionsExHandler implements Listener {

    private TablistSorter tablistSorter;

    public PermissionsExHandler(final EpicSuite plugin, final TablistSorter tablistSorter) {
        this.tablistSorter = tablistSorter;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRankUpdate(final PermissionEntityEvent event) {
        if (event.getAction() == PermissionEntityEvent.Action.RANK_CHANGED) {
            final Player player = Bukkit.getPlayer(UUID.fromString(event.getEntityIdentifier()));
            if (player != null) {
                tablistSorter.updatePlayer(player);
            }
        }
    }
}
