package de.stealwonders.epicsuite.resourcepack;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PackTracker implements Listener {

    private static final Map<UUID, String> PACK_TRACKER = new HashMap<>();

    public static void setLatestPack(final UUID uuid, final String hash) {
        PACK_TRACKER.put(uuid, hash);
    }

    public static String getLatestPack(final UUID uuid) {
        return PACK_TRACKER.get(uuid);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        PACK_TRACKER.remove(event.getPlayer().getUniqueId());
    }

}
