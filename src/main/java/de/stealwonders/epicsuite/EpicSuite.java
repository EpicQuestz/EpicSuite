package de.stealwonders.epicsuite;

import co.aikar.commands.PaperCommandManager;
import de.stealwonders.epicsuite.chat.ChatHighlight;
import de.stealwonders.epicsuite.chat.ChatNotification;
import de.stealwonders.epicsuite.commands.*;
import de.stealwonders.epicsuite.scoreboard.TablistSorter;
import de.stealwonders.epicsuite.storage.SettingsFile;
import de.stealwonders.epicsuite.storage.StorageFile;
import de.stealwonders.epicsuite.tablist.TablistHandler;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class EpicSuite extends JavaPlugin implements Listener {

    private PaperCommandManager commandManager;

    private SettingsFile settingsFile;
    private StorageFile storageFile;

    private ChatNotification chatNotification;
    private TablistSorter tablistSorter;

    private LuckPerms luckPermsApi;

    private DonatorMessageCommands donatorMessageCommands;

    @Override
    public void onEnable() {
        // Plugin startup logic

        commandManager = new PaperCommandManager(this);

        settingsFile = new SettingsFile(this);
        storageFile = new StorageFile(this);

        chatNotification = new ChatNotification(this);

        final Plugin luckPerms = getServer().getPluginManager().getPlugin("LuckPerms");
        if (luckPerms != null) {
            final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                luckPermsApi = provider.getProvider();
                tablistSorter = new TablistSorter(this, luckPermsApi);
            }
        } else {
            Bukkit.getLogger().severe("No permission handler found (LuckPerms)\nDisabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        donatorMessageCommands = new DonatorMessageCommands(this);

        registerListeners();
        registerCommands();

        fetchNotficationSubscribers();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new ChatHighlight(luckPermsApi), this);
        this.getServer().getPluginManager().registerEvents(chatNotification, this);
        this.getServer().getPluginManager().registerEvents(donatorMessageCommands, this);
        this.getServer().getPluginManager().registerEvents(tablistSorter, this);
        this.getServer().getPluginManager().registerEvents(new TablistHandler(this), this);
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private void registerCommands() {
        commandManager.registerCommand(new ChatClearCommand());
        commandManager.registerCommand(donatorMessageCommands);
        commandManager.registerCommand(new EmojiCommands());
        commandManager.registerCommand(new NotificationCommand(this));
        commandManager.registerCommand(new PingCommand());
        commandManager.registerCommand(new ResourcePackCommand(this));
    }

    private void fetchNotficationSubscribers() {
        if (getStorageFile().getSubscribers() != null) {
            for (final String string : getStorageFile().getSubscribers()) {
                chatNotification.addSubscriber(UUID.fromString(string));
            }
        }
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

    public LuckPerms getLuckPermsApi() {
        return luckPermsApi;
    }

}
