package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.command.CommandSender;

@CommandAlias("apply|promote|promotion")
@Description("Gives one the URL to the builder promotion application")
public class ApplyCommand extends BaseCommand {

    private static final String APPLY_URL_PATH = "applyurl";

    private final EpicSuite plugin;

    public ApplyCommand(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(final CommandSender commandSender) {
        commandSender.sendMessage("");
        commandSender.sendMessage("§e§lLearn how to apply for Builder:");
        commandSender.sendMessage("§8 ✹ §b" + plugin.getConfig().getString(APPLY_URL_PATH));
        commandSender.sendMessage("");
    }

}
