package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.events.ReloadConfigurationEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("epicsuite")
@CommandPermission("epicsuite.command")
public class ReloadCommand extends BaseCommand {

    private final EpicSuite plugin;

    public ReloadCommand(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reloadconfig|reload")
    @Description("Reload and applies configuration changes")
    @CommandPermission("epicsuite.reload")
    public void onReloadConfig(final CommandSender commandSender) {
        plugin.getSettingsFile().reload();
        Bukkit.getPluginManager().callEvent(new ReloadConfigurationEvent(commandSender));
        commandSender.sendMessage("§dReloaded configuration.");
    }

}
