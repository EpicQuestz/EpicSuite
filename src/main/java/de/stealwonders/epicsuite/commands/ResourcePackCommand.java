package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.events.ReloadConfigurationEvent;
import de.stealwonders.epicsuite.resourcepack.ResourcePack;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("resourcepack|rp|texturepack")
@Description("Opens the resource pack download popup")
public class ResourcePackCommand extends BaseCommand implements Listener {

    private static final String CONFIGURATION_ROOT = "resourcepacks";

    private final EpicSuite plugin;
    private final List<ResourcePack> resourcePacks = new ArrayList<>();

    public ResourcePackCommand(final EpicSuite plugin) {
        this.plugin = plugin;
        loadResourcePacks();
    }

    @Default
    @CommandCompletion("@resourcepack")
    public void onCommand(final Player player, final ResourcePack resourcePack) {
        player.setResourcePack(resourcePack.getUrl(), resourcePack.getHash());
        player.sendMessage("§dPrompting resource pack download... If you don't see one make sure to enable server resource packs in your server list.");
    }

    public List<ResourcePack> getResourcePacks() {
        return resourcePacks;
    }

    @EventHandler
    public void onReload(final ReloadConfigurationEvent event) {
        resourcePacks.clear();
        loadResourcePacks();
    }

    private void loadResourcePacks() {
        final Configuration configuration = plugin.getSettingsFile().getConfiguration();
        if (configuration.isConfigurationSection(CONFIGURATION_ROOT)) {
            final ConfigurationSection configurationSection = configuration.getConfigurationSection(CONFIGURATION_ROOT);
            if (configurationSection != null) {
                for (final String key : configurationSection.getKeys(false)) {
                    final String url = configuration.getString(CONFIGURATION_ROOT + "." + key + ".url");
                    final String hash = configuration.getString(CONFIGURATION_ROOT + "." + key + ".hash");
                    if (url != null && hash != null) {
                        resourcePacks.add(new ResourcePack(key, url, hash));
                    }
                }
            }
        }
    }
}
