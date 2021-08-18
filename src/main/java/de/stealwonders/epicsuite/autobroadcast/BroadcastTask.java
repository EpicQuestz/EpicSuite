package de.stealwonders.epicsuite.autobroadcast;

import de.stealwonders.epicsuite.events.ConfigurationReloadEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.regex.Pattern;

public class BroadcastTask implements Runnable, Listener {

    private static final Pattern DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?");
    private static final Pattern URL_SCHEME_PATTERN = Pattern.compile("^[a-z][a-z0-9+\\-.]*:");

    public static final TextReplacementConfig URL_REPLACEMENT_CONFIG = TextReplacementConfig.builder()
        .match(DEFAULT_URL_PATTERN)
        .replacement(url -> {
            String clickUrl = url.content();
            if (!URL_SCHEME_PATTERN.matcher(clickUrl).find()) {
                clickUrl = "http://" + clickUrl;
            }
            return url.clickEvent(ClickEvent.openUrl(clickUrl));
        }).build();

    private static final Component PREFIX = LegacyComponentSerializer.legacyAmpersand().deserialize("&8[&4Epic&2Questz&8]");
    private static final String MESSAGES_PATH = "autobroadcast.messages";

    private final FileConfiguration config;
    private final List<String> messages;
    private int index;

    public BroadcastTask(final FileConfiguration config) {
        this.config = config;
        this.messages = config.getStringList(MESSAGES_PATH);
        this.index = 0;
    }

    @Override
    public void run() {
        if (messages.size() >= 1 && Bukkit.getOnlinePlayers().size() >= 1) {
            Bukkit.broadcast(Component.empty());
            Bukkit.broadcast(PREFIX.append(Component.space()).append(LegacyComponentSerializer.legacyAmpersand().deserialize((messages.get(index))).replaceText(URL_REPLACEMENT_CONFIG).color(NamedTextColor.GREEN)));
            Bukkit.broadcast(Component.empty());
            if (index >= messages.size() - 1) {
                index = 0;
            } else {
                index++;
            }
        }
    }

    @EventHandler
    public void onConfigReload(final ConfigurationReloadEvent event) {
        messages.clear();
        messages.addAll(config.getStringList(MESSAGES_PATH));
        index = 0;
    }
}
