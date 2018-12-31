package de.stealwonders.epicsuite.scoreboard.impl;

import de.stealwonders.epicsuite.scoreboard.TablistSorter;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.log.LogPublishEvent;
import me.lucko.luckperms.api.event.user.track.UserTrackEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LuckPermsHandler {

    TablistSorter tablistSorter;

    public LuckPermsHandler(LuckPermsApi luckPermsApi, TablistSorter tablistSorter) {
        this.tablistSorter = tablistSorter;
        EventBus eventBus = luckPermsApi.getEventBus();
        eventBus.subscribe(LogPublishEvent.class, e -> e.getCancellationState().set(true));
        eventBus.subscribe(UserTrackEvent.class, this::onRankUpdate);
    }

    private void onRankUpdate(UserTrackEvent event) {
        final Player player = Bukkit.getPlayer(event.getUser().getUuid());
        if (player != null) {
            tablistSorter.updatePlayer(player);
        }
    }
}
