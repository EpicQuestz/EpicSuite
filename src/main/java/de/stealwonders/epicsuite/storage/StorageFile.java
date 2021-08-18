package de.stealwonders.epicsuite.storage;

import com.google.common.collect.ImmutableList;
import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StorageFile {

    private final File file;
    private final FileConfiguration configuration;

    private final HashSet<UUID> subscribers = new HashSet<>();

    private static final String SUBSCRIBER_PATH = "notifications.subscribers";
    private static final String JOINMESSAGE_PATH = "donator.joinmessage";
    private static final String LEAVEMESSAGE_PATH = "donator.leavemessage";

    public StorageFile(final EpicSuite plugin) {
        file = new File(plugin.getDataFolder(), "storage.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        configuration = new YamlConfiguration();

        try {
            configuration.load(file);
        } catch (final IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public boolean isSubscriber(final UUID uuid) {
        return subscribers.contains(uuid);
    }

    public void addSubscriber(final UUID uuid) {
        subscribers.add(uuid);
        final List<String> stringList = subscribers.stream().map(UUID::toString).collect(Collectors.toList());
        getConfiguration().set(SUBSCRIBER_PATH, stringList);
        save();
    }

    public void removeSubscriber(final UUID uuid) {
        subscribers.remove(uuid);
        final List<String> stringList = subscribers.stream().map(UUID::toString).collect(Collectors.toList());
        getConfiguration().set(SUBSCRIBER_PATH, stringList);
        save();
    }

    public ImmutableList<String> getSubscribers() {
        if (getConfiguration().isSet(SUBSCRIBER_PATH)) {
            final List<String> subscribers = getConfiguration().getStringList(SUBSCRIBER_PATH);
            return ImmutableList.copyOf(subscribers);
        }
        return null;
    }

    public String getPlayerJoinMessage(final Player player) {
        return getConfiguration().getString(JOINMESSAGE_PATH + "." + player.getUniqueId());
    }

    public String getPlayerLeaveMessage(final Player player) {
        return getConfiguration().getString(LEAVEMESSAGE_PATH + "." + player.getUniqueId());
    }

    public void setPlayerJoinMessage(final Player player, final String string) {
        getConfiguration().set(JOINMESSAGE_PATH + "." + player.getUniqueId(), string);
        save();
    }

    public void setPlayerLeaveMessage(final Player player, final String string) {
        getConfiguration().set(LEAVEMESSAGE_PATH + "." + player.getUniqueId(), string);
        save();
    }

    public void deletePlayerJoinMessage(final Player player) {
        getConfiguration().set(JOINMESSAGE_PATH + "." + player.getUniqueId(), null);
        save();
    }

    public void deletePlayerLeaveMessage(final Player player) {
        getConfiguration().set(LEAVEMESSAGE_PATH + "." + player.getUniqueId(), null);
        save();
    }

}
