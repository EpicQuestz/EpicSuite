package de.stealwonders.epicsuite;

import de.stealwonders.epicsuite.chat.ChatHighlight;
import de.stealwonders.epicsuite.chat.ChatNotification;
import de.stealwonders.epicsuite.commands.ChatClearCommand;
import de.stealwonders.epicsuite.commands.NotificationCommand;
import de.stealwonders.epicsuite.commands.PingCommand;
import de.stealwonders.epicsuite.scoreboard.TablistSorter;
import de.stealwonders.epicsuite.storage.SettingsFile;
import de.stealwonders.epicsuite.storage.StorageFile;
import de.stealwonders.epicsuite.tablist.TablistHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class EpicSuite extends JavaPlugin {

	private static EpicSuite plugin;

	SettingsFile settingsFile;
	StorageFile storageFile;

	ChatNotification chatNotification;
	TablistSorter tablistSorter;

	@Override
	public void onEnable() {
		// Plugin startup logic

		plugin = this;

		settingsFile = new SettingsFile(this);
		storageFile = new StorageFile(this);

		chatNotification = new ChatNotification();
		tablistSorter = new TablistSorter();


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
		this.getServer().getPluginManager().registerEvents(tablistSorter, this);
		this.getServer().getPluginManager().registerEvents(new TablistHandler(), this);
	}

	private void registerCommands() {
		this.getCommand("clearchat").setExecutor(new ChatClearCommand());
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

	public SettingsFile getSettingsFile() {
		return settingsFile;
	}

	public StorageFile getStorageFile() {
		return storageFile;
	}

	public ChatNotification getChatNotifier() {
		return chatNotification;
	}
}
