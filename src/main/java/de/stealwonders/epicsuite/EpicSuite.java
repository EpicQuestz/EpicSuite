package de.stealwonders.epicsuite;

import de.stealwonders.epicsuite.chat.ChatHighlight;
import de.stealwonders.epicsuite.chat.ChatNotification;
import de.stealwonders.epicsuite.commands.NotificationCommand;
import de.stealwonders.epicsuite.commands.PingCommand;
import de.stealwonders.epicsuite.storage.StorageFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class EpicSuite extends JavaPlugin {

	private static EpicSuite plugin;

	StorageFile storageFile;

	ChatNotification chatNotification;

	@Override
	public void onEnable() {
		// Plugin startup logic

		plugin = this;

		storageFile = new StorageFile(this);

		chatNotification = new ChatNotification();

		registerListeners();
		registerCommands();

		fetchNotficationSubscribers();

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	private void registerListeners() {
		this.getServer().getPluginManager().registerEvents(new ChatHighlight(), this);
		this.getServer().getPluginManager().registerEvents(chatNotification, this);
	}

	private void registerCommands() {
		this.getCommand("chatnotification").setExecutor(new NotificationCommand());
		this.getCommand("ping").setExecutor(new PingCommand());
	}

	private void fetchNotficationSubscribers() {
		if (getStorageFile().getSubscribers() != null) {
			for (final String string : getStorageFile().getSubscribers()) {
				chatNotification.addSubscriber(UUID.fromString(string));
			}
		}
	}

	public static EpicSuite getPlugin() {
		return plugin;
	}

	public StorageFile getStorageFile() {
		return storageFile;
	}

	public ChatNotification getChatNotifier() {
		return chatNotification;
	}
}
