package de.stealwonders.epicsuite.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EmojiCommands implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (sender instanceof Player) {

            final Player player = (Player) sender;

            if (player.hasPermission("epicsuite.chat.emojis")) {

                switch (label.toLowerCase()) {

                    case "shrug":
                        player.chat("¯\\_(ツ)_/¯");
                        break;
                    case "tableflip":
                        player.chat("(╯°□°）╯︵ ┻━┻");
                        break;
                    case "unflip":
                        player.chat("┬─┬ ノ( ゜-゜ノ)");
                        break;
                    case "riot":
                        player.chat("ヽ༼ຈل͜ຈ༽ﾉ RIOT ヽ༼ຈل͜ຈ༽ﾉ");
                        break;
                }
            } else {
                player.sendMessage("§dYou need to §5§ldonate §dto be able to use this feature. Use §5§l/buy §dfor more information.");
            }

        } else {
            sender.sendMessage("You must be a player to execute this command.");
        }
        return false;
    }
}
