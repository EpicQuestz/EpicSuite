package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("ping")
@Description("Displays your or a players current server ping")
public class PingCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onCommand(final Player player, @Optional final OnlinePlayer target) {
        final String message = target != null ? "§d" + target.getPlayer().getName() + "'s ping is " +  target.getPlayer().spigot().getPing() + "ms" : "§dYour ping is " +  player.spigot().getPing() + "ms";
        player.sendMessage(message);
    }
    
}
