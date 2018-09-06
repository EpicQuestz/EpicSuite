package de.stealwonders.epicsuite.storage;

import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StorageFile {

	private File file;
	private FileConfiguration configuration;

	private static final String SUBSCRIBER_PATH = "notifications.subscribers";

	public StorageFile(EpicSuite plugin) {
		file = new File(plugin.getDataFolder(), "storage.yml");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}

		configuration = new YamlConfiguration();

		try {
			configuration.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException exception) {
			exception.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public void save() {
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		file.delete();
		configuration = null;
	}

	public FileConfiguration getConfiguration() {
		return configuration;
	}

	public void addSubscriber(UUID uuid) {
		List<String> subscribers = getSubscribers();
		if (subscribers != null) {
			if (!subscribers.contains(uuid)) {
				subscribers.add(uuid.toString());
				getConfiguration().set(SUBSCRIBER_PATH, subscribers);
				Bukkit.getScheduler().runTaskAsynchronously(EpicSuite.getPlugin(), () -> save());
			}
		}
	}

	public void removeSubscriber(UUID uuid) {
		List<String> subscribers = getSubscribers();
		if (subscribers != null) {
			subscribers.removeAll(Collections.singleton(uuid.toString()));
			getConfiguration().set(SUBSCRIBER_PATH, subscribers);
			Bukkit.getScheduler().runTaskAsynchronously(EpicSuite.getPlugin(), () -> save());
		}
	}

	public List<String> getSubscribers() {
		if (getConfiguration().isSet(SUBSCRIBER_PATH)) {
			List<String> subscribers = getConfiguration().getStringList(SUBSCRIBER_PATH);
			return subscribers;
		}
		return null;
	}
}
