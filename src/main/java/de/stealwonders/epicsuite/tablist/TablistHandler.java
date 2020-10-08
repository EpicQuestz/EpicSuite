package de.stealwonders.epicsuite.tablist;

import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.events.ConfigurationReloadEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TablistHandler implements Listener {

    private final EpicSuite plugin;
    private String headerString;
    private String footerString;

    public TablistHandler(final EpicSuite plugin) {
        this.plugin = plugin;
        loadTablist();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final TextComponent header = new TextComponent(headerString.replaceAll("\\{player}", event.getPlayer().getName()));
        final TextComponent footer = new TextComponent(footerString.replaceAll("\\{player}", event.getPlayer().getName()));
        player.setPlayerListHeaderFooter(header, footer);
    }

    @EventHandler
    public void onReload(final ConfigurationReloadEvent event) {
        loadTablist();
    }

    private void loadTablist() {
        headerString = plugin.getConfig().getString("tablist.header");
        footerString = plugin.getConfig().getString("tablist.footer");
    }
}
