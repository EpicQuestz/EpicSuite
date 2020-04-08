package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import org.bukkit.entity.Player;

@CommandAlias("ping")
@Description("Displays your current server ping")
public class PingCommand extends BaseCommand {

    @Default
    public void onCommand(final Player player, @Optional final Player target) {
        final String message = target != null ? "§dYour ping is " +  target.spigot().getPing() + "ms" : "§dYour ping is " +  player.spigot().getPing() + "ms";
        player.sendMessage(message);
    }

}
