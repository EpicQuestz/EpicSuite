package de.stealwonders.epicsuite.vote;

import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VoteListener implements Listener {

    @EventHandler
    public void onVote(final VotifierEvent event) {
        final Player player = Bukkit.getPlayer(event.getVote().getUsername());
        if (player != null) {
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }
    }

}
