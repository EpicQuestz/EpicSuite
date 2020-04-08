package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.EpicSuite;
import org.bukkit.entity.Player;

@CommandAlias("resourcepack|rp|texturepack")
@Description("Opens the resource pack download popup")
public class ResourcePackCommand extends BaseCommand {

    private String url;
    private String hash;

    public ResourcePackCommand(final EpicSuite plugin) {
        url = plugin.getSettingsFile().getConfiguration().getString("resourcepack.url");
        hash = plugin.getSettingsFile().getConfiguration().getString("resourcepack.hash");
    }

    @Default
    public void onCommand(final Player player) {
        player.setResourcePack(url, hash);
        player.sendMessage("§dPrompting resource pack download... If you don't see one make sure to enable server resource packs in your server list.");
    }

}
