package de.stealwonders.epicsuite.storage;

import de.stealwonders.epicsuite.EpicSuite;
import de.stealwonders.epicsuite.scoreboard.TablistTeam;
import net.luckperms.api.model.group.Group;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsFile {

    private EpicSuite plugin;

    private File file;
    private FileConfiguration configuration;

    private static final String SORTABLE_GROUPS_PATH_ROOT = "groups";

    public SettingsFile(final EpicSuite plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "settings.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("settings.yml", false);
        }

        configuration = new YamlConfiguration();

        try {
            configuration.load(file);
        } catch (final IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            configuration.load(file);
        } catch (final InvalidConfigurationException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public void delete() {
        file.delete();
        configuration = null;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public ArrayList<TablistTeam> getSortableTeams() {
        if (getConfiguration().isSet(SORTABLE_GROUPS_PATH_ROOT)) {

            final ConfigurationSection configurationSection = getConfiguration().getConfigurationSection(SORTABLE_GROUPS_PATH_ROOT);

            final ArrayList<TablistTeam> sortableTeams = new ArrayList<>();

            for (final String group : configurationSection.getKeys(false)) {

                final String SORTABLE_GROUPS_PATH_PRIORITY = group + ".priority";
                final String SORTABLE_GROUPS_PATH_COLOR =  group + ".color";

                final int priority = configurationSection.isSet(SORTABLE_GROUPS_PATH_PRIORITY) ? configurationSection.getInt(SORTABLE_GROUPS_PATH_PRIORITY) : 0;
                final ChatColor color = configurationSection.isSet(SORTABLE_GROUPS_PATH_COLOR) ? ChatColor.valueOf(configurationSection.getString(SORTABLE_GROUPS_PATH_COLOR)) : ChatColor.WHITE;

                final Group luckGroup = plugin.getLuckPermsApi().getGroupManager().getGroup(group);
                if (luckGroup != null) {
                    final TablistTeam team = new TablistTeam(luckGroup, priority, color);
                    sortableTeams.add(team);
                }
            }
            return sortableTeams;
        }
        return null;
    }

}
