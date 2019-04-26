package de.stealwonders.epicsuite;

import de.stealwonders.epicsuite.chat.ChatHighlight;
import de.stealwonders.epicsuite.chat.ChatNotification;
import de.stealwonders.epicsuite.commands.*;
import de.stealwonders.epicsuite.scoreboard.TablistSorter;
import de.stealwonders.epicsuite.storage.SettingsFile;
import de.stealwonders.epicsuite.storage.StorageFile;
import de.stealwonders.epicsuite.tablist.TablistHandler;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class EpicSuite extends JavaPlugin {

    private static EpicSuite plugin;

    private SettingsFile settingsFile;
    private StorageFile storageFile;

    private ChatNotification chatNotification;
    private TablistSorter tablistSorter;

    private LuckPermsApi luckPermsApi;

    private DonatorMessageCommands donatorMessageCommands;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        settingsFile = new SettingsFile(this);
        storageFile = new StorageFile(this);

        chatNotification = new ChatNotification(this);

        donatorMessageCommands = new DonatorMessageCommands(this);

        final Plugin luckPerms = getServer().getPluginManager().getPlugin("LuckPerms");

        if (luckPerms != null) {
            final RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
            if (provider != null) {
                luckPermsApi = provider.getProvider();
                tablistSorter = new TablistSorter(this, luckPermsApi);
            }
        } else {
            Bukkit.getLogger().severe("No permission handler found (LuckPerms)\nDisabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
        }

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
        this.getServer().getPluginManager().registerEvents(new TablistHandler(), this);
    }

    private void registerCommands() {
        this.getCommand("clearchat").setExecutor(new ChatClearCommand());
        this.getCommand("setjoinmessage").setExecutor(donatorMessageCommands);
        this.getCommand("chatnotification").setExecutor(new NotificationCommand(this));
        this.getCommand("ping").setExecutor(new PingCommand());
        this.getCommand("shrug").setExecutor(new EmojiCommands());
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

    public LuckPermsApi getLuckPermsApi() {
        return luckPermsApi;
    }

}
