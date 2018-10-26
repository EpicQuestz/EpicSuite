package de.stealwonders.epicsuite.storage;

import com.google.common.collect.ImmutableList;
import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StorageFile {

	private File file;
	private FileConfiguration configuration;

	private ArrayList<UUID> subscribers = new ArrayList<>();

	private static final String SUBSCRIBER_PATH = "notifications.subscribers";

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
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final InvalidConfigurationException exception) {
			exception.printStackTrace();
		}
	}

	public void save() {
		try {
			configuration.save(file);
		} catch (final IOException e) {
			e.printStackTrace();
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
		subscribers.removeAll(Collections.singleton(uuid.toString()));
		getConfiguration().set(SUBSCRIBER_PATH, subscribers);
		save();
	}

	public ImmutableList<String> getSubscribers() {
		if (getConfiguration().isSet(SUBSCRIBER_PATH)) {
			final List<String> subscribers = getConfiguration().getStringList(SUBSCRIBER_PATH);
			return ImmutableList.copyOf(subscribers);
		}
		return null;
	}
}
