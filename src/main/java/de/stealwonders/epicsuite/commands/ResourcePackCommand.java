package de.stealwonders.epicsuite.commands;

import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResourcePackCommand implements CommandExecutor {

    private String url;
    private String hash;

    public ResourcePackCommand(final EpicSuite plugin) {
        url = plugin.getSettingsFile().getConfiguration().getString("resourcepack.url");
        hash = plugin.getSettingsFile().getConfiguration().getString("resourcepack.hash");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            player.setResourcePack(url, hash);
            player.sendMessage("§dPrompting resource pack download... If you don't see one make sure to enable server resource packs in your server list.");
        } else {
            sender.sendMessage("You must be a player to execute this command.");
        }
        return false;
    }
}
