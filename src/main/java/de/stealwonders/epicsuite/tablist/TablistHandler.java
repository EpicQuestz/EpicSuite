package de.stealwonders.epicsuite.tablist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public record TablistHandler(FileConfiguration config) implements Listener {

    private static final String HEADER_PATH = "tablist.header";
    private static final String FOOTER_PATH = "tablist.footer";

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final String headerString = config.getString(HEADER_PATH);
        final String footerString = config.getString(FOOTER_PATH);

        final Component header = headerString != null ? LegacyComponentSerializer.legacyAmpersand().deserialize(headerString.replaceAll("\\{player}", player.getName())) : null;
        final Component footer = footerString != null ? LegacyComponentSerializer.legacyAmpersand().deserialize(footerString.replaceAll("\\{player}", player.getName())) : null;

        if (header != null) player.sendPlayerListHeader(header);
        if (footer != null) player.sendPlayerListFooter(footer);
    }

}
