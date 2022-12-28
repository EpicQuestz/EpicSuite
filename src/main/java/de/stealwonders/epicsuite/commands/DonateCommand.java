package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.command.CommandSender;

@CommandAlias("donate|donation")
@Description("Sends you the url to the donation page")
public class DonateCommand extends BaseCommand {

    private static final String DONATION_URL_PATH = "donationurl";

    private final EpicSuite plugin;

    public DonateCommand(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(final CommandSender commandSender) {
        commandSender.sendMessage("");
        commandSender.sendMessage("§e§lSupport us on:");
        commandSender.sendMessage("§8 ✹ §b" + plugin.getConfig().getString(DONATION_URL_PATH));
        commandSender.sendMessage("");
    }

}
