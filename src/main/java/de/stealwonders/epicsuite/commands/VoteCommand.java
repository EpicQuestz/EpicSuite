package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.command.CommandSender;

@CommandAlias("vote")
@Description("Sends you the PMC vote links")
public class VoteCommand extends BaseCommand {

    private final EpicSuite plugin;

    public VoteCommand(final EpicSuite plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(final CommandSender commandSender) {
        commandSender.sendMessage("");
        commandSender.sendMessage("§e§lVote for EpicQuestz at:");
        plugin.getConfig().getStringList("votelinks").forEach(voteLink -> {
            commandSender.sendMessage("§8 ✹ §b" + voteLink);
        });
        commandSender.sendMessage("");
    }

}
