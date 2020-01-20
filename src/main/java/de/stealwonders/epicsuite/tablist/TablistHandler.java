package de.stealwonders.epicsuite.tablist;

import de.stealwonders.epicsuite.EpicSuite;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TablistHandler implements Listener {

    private String headerString;
    private String footerString;

    public TablistHandler(final EpicSuite plugin) {
        headerString = plugin.getSettingsFile().getConfiguration().getString("tablist.header");
        footerString = plugin.getSettingsFile().getConfiguration().getString("tablist.footer");
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final TextComponent header = new TextComponent(headerString.replaceAll("\\{player}", event.getPlayer().getName()));
        final TextComponent footer = new TextComponent(footerString.replaceAll("\\{player}", event.getPlayer().getName()));
        player.setPlayerListHeaderFooter(header, footer);
    }
}
