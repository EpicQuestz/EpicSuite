package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

@CommandAlias("vote")
@Description("Sends you the urls for the voting sites")
public class VoteCommand extends BaseCommand {

    private static final String VOTE_LINKS_PATH = "votelinks";

    private final FileConfiguration config;

    public VoteCommand(final FileConfiguration config) {
        this.config = config;
    }

    @Default
    public void onCommand(final CommandSender commandSender) {
        commandSender.sendMessage("");
        commandSender.sendMessage("§e§lVote for EpicQuestz at:");
        config.getStringList(VOTE_LINKS_PATH).forEach(voteLink -> commandSender.sendMessage("§8 ✹ §b" + voteLink));
        commandSender.sendMessage("");
    }

}
