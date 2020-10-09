package de.stealwonders.epicsuite;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import de.stealwonders.epicsuite.autobroadcast.BroadcastTask;
import de.stealwonders.epicsuite.chat.ChatHighlight;
import de.stealwonders.epicsuite.chat.ChatNotification;
import de.stealwonders.epicsuite.commands.ChatClearCommand;
import de.stealwonders.epicsuite.commands.DonatorMessageCommands;
import de.stealwonders.epicsuite.commands.EmojiCommands;
import de.stealwonders.epicsuite.commands.NotificationCommand;
import de.stealwonders.epicsuite.commands.PingCommand;
import de.stealwonders.epicsuite.commands.ReloadCommand;
import de.stealwonders.epicsuite.commands.ResourcePackCommand;
import de.stealwonders.epicsuite.commands.VoteCommand;
import de.stealwonders.epicsuite.resourcepack.ResourcePack;
import de.stealwonders.epicsuite.scoreboard.TablistSorter;
import de.stealwonders.epicsuite.storage.StorageFile;
import de.stealwonders.epicsuite.tablist.TablistHandler;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.stream.Collectors;

public final class EpicSuite extends JavaPlugin implements Listener {

    private PaperCommandManager commandManager;

    private StorageFile storageFile;

    private ChatNotification chatNotification;
    private TablistSorter tablistSorter;

    private LuckPerms luckPermsApi;

    private DonatorMessageCommands donatorMessageCommands;
    private ResourcePackCommand resourcePackCommand;

    private BroadcastTask broadcastTask;

    @Override
    public void onEnable() {
        // Plugin startup logic

        commandManager = new PaperCommandManager(this);

        storageFile = new StorageFile(this);

        chatNotification = new ChatNotification(this);

        final Plugin luckPerms = getServer().getPluginManager().getPlugin("LuckPerms");
        if (luckPerms != null) {
            final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                luckPermsApi = provider.getProvider();
                tablistSorter = new TablistSorter(this, luckPermsApi);
                tablistSorter.addPlayers(); // Adds players that are already on the server (on reload).
            }
        } else {
            Bukkit.getLogger().severe("No permission handler found (LuckPerms)\nDisabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        donatorMessageCommands = new DonatorMessageCommands(this);
        resourcePackCommand = new ResourcePackCommand(this);

        broadcastTask = new BroadcastTask(this);

        registerListeners();
        setupAutoBroadcast();
        registerCommandContexts();
        registerCommandCompletions();
        registerCommands();

        fetchNotficationSubscribers();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        tablistSorter.removePlayers(); // Removes players that are still on the server (on reload).
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(broadcastTask, this);
        this.getServer().getPluginManager().registerEvents(new ChatHighlight(luckPermsApi), this);
        this.getServer().getPluginManager().registerEvents(chatNotification, this);
        this.getServer().getPluginManager().registerEvents(donatorMessageCommands, this);
        this.getServer().getPluginManager().registerEvents(resourcePackCommand, this);
        this.getServer().getPluginManager().registerEvents(tablistSorter, this);
        this.getServer().getPluginManager().registerEvents(new TablistHandler(this), this);
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private void setupAutoBroadcast() {
        final long delay = this.getConfig().getLong("autobroadcast.delay");
        Bukkit.getServer().getScheduler().runTaskTimer(this, broadcastTask, 0L, 20 * 60 * delay);
    }

    private void registerCommandContexts() {
        commandManager.getCommandContexts().registerContext(ResourcePack.class, context -> {
            ResourcePack resourcePack = null;
            final String arg = context.popFirstArg();
            for (final ResourcePack pack : resourcePackCommand.getResourcePacks()) {
                if (pack.getKey().equals(arg)) {
                    resourcePack = pack;
                }
            }
            if (resourcePack != null) {
                return resourcePack;
            } else {
                throw new InvalidCommandArgument("This resource pack does not exist.", false);
            }
        });
    }

    private void registerCommandCompletions() {
        commandManager.getCommandCompletions().registerAsyncCompletion("resourcepack", c ->
            resourcePackCommand.getResourcePacks().stream().map(ResourcePack::getKey).collect(Collectors.toList())
        );
    }

    private void registerCommands() {
        commandManager.registerCommand(new ChatClearCommand());
        commandManager.registerCommand(donatorMessageCommands);
        commandManager.registerCommand(new EmojiCommands());
        commandManager.registerCommand(new NotificationCommand(this));
        commandManager.registerCommand(new PingCommand());
        commandManager.registerCommand(new ReloadCommand(this));
        commandManager.registerCommand(resourcePackCommand);
        commandManager.registerCommand(new VoteCommand(this));
    }

    private void fetchNotficationSubscribers() {
        if (getStorageFile().getSubscribers() != null) {
            for (final String string : getStorageFile().getSubscribers()) {
                chatNotification.addSubscriber(UUID.fromString(string));
            }
        }
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
