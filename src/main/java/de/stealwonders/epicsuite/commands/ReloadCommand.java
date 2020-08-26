package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.events.ReloadConfigurationEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("epicsuite reload")
@Description("Reload and applies configuration changes")
@CommandPermission("epicsuite.reload")
public class ReloadCommand extends BaseCommand {

    @Default
    public void onCommand(final CommandSender commandSender) {
        Bukkit.getPluginManager().callEvent(new ReloadConfigurationEvent(commandSender));
        commandSender.sendMessage("§dReloaded configuration.");
    }

}
