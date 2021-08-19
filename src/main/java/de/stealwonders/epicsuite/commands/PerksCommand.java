package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Values;
import de.stealwonders.epicsuite.EpicSuite;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

@CommandAlias("perks|perk")
@Description("Sends you a list of perks for a specific rank")
public class PerksCommand extends BaseCommand {

    private static final String CONFIGURATION_ROOT = "perks";

    private final EpicSuite plugin;

    public PerksCommand(final EpicSuite plugin) {
        this.plugin = plugin;
        final ConfigurationSection perksSection = plugin.getConfig().getConfigurationSection(CONFIGURATION_ROOT);
        if (perksSection == null) return;
        plugin.getCommandManager().getCommandCompletions().registerAsyncCompletion("perkable-ranks", context ->
            perksSection.getKeys(false)
        );
    }

    @Default
    public void onCommand(final CommandSender commandSender) {
        final ConfigurationSection perksSection = plugin.getConfig().getConfigurationSection(CONFIGURATION_ROOT);
        if (perksSection == null) return;
        commandSender.sendMessage("");
        commandSender.sendMessage("§e§lList of all ranks with perks:");
        perksSection.getKeys(false).forEach(rank -> {
            Component component = Component.text().hoverEvent(HoverEvent.showText(Component.text("Show " + rank.toLowerCase() + " perks"))).clickEvent(ClickEvent.runCommand("/perks " + rank))
                .append(Component.text(" ✹ ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(rank).color(NamedTextColor.AQUA))
                .append(Component.space())
                .append(Component.text("(").color(NamedTextColor.AQUA))
                .append(Component.text(perksSection.getString(rank + ".pricing"))).color(NamedTextColor.YELLOW)
                .append(Component.text(")").color(NamedTextColor.AQUA))
                .build();
            commandSender.sendMessage(component);
        });
        commandSender.sendMessage("");
    }

    @Default
    @CommandCompletion("@perkable-ranks")
    public void onPerk(final CommandSender commandSender, @Values("@perkable-ranks") String rank) {
        rank = capitalize(rank.toLowerCase());
        final ConfigurationSection perksSection = plugin.getConfig().getConfigurationSection(CONFIGURATION_ROOT);
        if (perksSection == null) return;
        commandSender.sendMessage("");
        commandSender.sendMessage("§e§lPerks for " + rank + ":");
        perksSection.getStringList(rank + ".perks").forEach(perk -> commandSender.sendMessage("§8 ✹ §b" + perk));
        commandSender.sendMessage("");
    }

    private String capitalize(String string) {
        if (string == null || string.isEmpty()) return string;
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

}
