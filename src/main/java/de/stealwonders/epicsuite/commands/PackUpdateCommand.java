package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.resourcepack.PackTracker;
import de.stealwonders.epicsuite.resourcepack.PackValidator;
import de.stealwonders.epicsuite.resourcepack.ResourcePack;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

@CommandAlias("epicsuite")
@CommandPermission("epicsuite.command")
public class PackUpdateCommand extends BaseCommand {

    private static final String PACK_UPDATE_PATH = "packupdate";
    private static final String RESOURCE_PACK_PATH = "resourcepacks";

    private final EpicSuite plugin;

    public PackUpdateCommand(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @Subcommand("packupdate|updatepack")
    @CommandPermission("epicsuite.packupdate")
    @Description("Update and applies resource pack changes")
    @CommandCompletion("@resourcepack")
    public void onReloadConfig(final CommandSender commandSender, final ResourcePack resourcePack, final String hash) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage("§cYou must be a console to execute this command.");
            return;
        }

        // Sanity check: does the configuration exist and is the update feature enabled?
        final ConfigurationSection packUpdateSection = plugin.getConfig().getConfigurationSection(PACK_UPDATE_PATH);
        if (packUpdateSection == null) return;

        if (!packUpdateSection.getBoolean("enabled")) {
            commandSender.sendMessage("§cPack updates are disabled. Please enable them in the config or manually update your resource pack configuration.");
            return;
        }

        final String baseUrl = packUpdateSection.getString("baseurl");
        if (baseUrl == null) {
            commandSender.sendMessage("§cNo base url is set. Please set one in the config or manually update your resource pack configuration.");
            return;
        }

        // Check if pack is valid
        try {
            if (!PackValidator.isValid(baseUrl, hash)) {
                commandSender.sendMessage("§cThe hash you provided is invalid. Please provide a hash corresponding to the pack.");
                return;
            }
        } catch (Exception exception) {
            commandSender.sendMessage("§cFailed to verify the resource pack.");
            plugin.getLogger().warning(exception.getMessage());
            return;
        }

        // Remember the old hash of the pack to update
        String oldUrl = resourcePack.getUrl();
        String oldHash = resourcePack.getHash();

        // Update resource pack information
        resourcePack.setUrl(baseUrl + "/" + hash + ".zip");
        resourcePack.setHash(hash);

        boolean updated = false;

        // Update resource pack configuration
        final Configuration configuration = plugin.getConfig();
        if (configuration.isConfigurationSection(RESOURCE_PACK_PATH)) {
            final ConfigurationSection configurationSection = configuration.getConfigurationSection(RESOURCE_PACK_PATH);
            if (configurationSection != null) {
                for (final String key : configurationSection.getKeys(false)) {
                    if (key.equalsIgnoreCase(resourcePack.getKey())) {
                        configuration.set(RESOURCE_PACK_PATH + "." + key + ".url", resourcePack.getUrl());
                        configuration.set(RESOURCE_PACK_PATH + "." + key + ".hash", resourcePack.getHash());
                        plugin.saveConfig();
                        commandSender.sendMessage("§aSuccessfully updated resource pack configuration.");
                        updated = true;
                        break;
                    }
                }
            }
        }

        // Abort if resource pack configuration could not be updated and revert changes
        if (!updated) {
            commandSender.sendMessage("§cFailed to update resource pack configuration.");
            resourcePack.setUrl(oldUrl);
            resourcePack.setHash(oldHash);
            return;
        }

        // Update resource pack for all players online
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerResourcePackStatusEvent.Status status = player.getResourcePackStatus();
            if (status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                final String oldPlayerHash = PackTracker.getLatestPack(player.getUniqueId());
                if (oldPlayerHash != null && oldPlayerHash.equals(oldHash)) {
                    player.setResourcePack(resourcePack.getUrl(), resourcePack.getHash());
                    PackTracker.setLatestPack(player.getUniqueId(), hash);
                }
            }
        }
    }

}
