package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("emote")
@CommandPermission("epicsuite.chat.emojis")
public class EmojiCommands extends BaseCommand {

    @Default
    @Subcommand("list")
    public void onCommand(final CommandSender commandSender) {
        commandSender.sendMessage("");
        commandSender.sendMessage(" §e/shrug      §b¯\\_(ツ)_/¯");
        commandSender.sendMessage(" §e/tableflip  §b(╯°□°）╯︵ ┻━┻");
        commandSender.sendMessage(" §e/unflip     §b┬─┬ ノ( ゜-゜ノ)");
        commandSender.sendMessage(" §e/riot       §bヽ༼ຈل͜ຈ༽ﾉ RIOT ヽ༼ຈل͜ຈ༽ﾉ\"");
        commandSender.sendMessage(" §e/lenny      §b( ͡° ͜ʖ ͡°)");
        commandSender.sendMessage(" §e/sad        §b(ಥ﹏ಥ)");
        commandSender.sendMessage(" §e/kawaii     §b(づ｡◕‿‿◕｡)づ");
        commandSender.sendMessage("");
    }

    @CommandAlias("shrug")
    public void onShrug(final Player player) {
        player.chat("¯\\_(ツ)_/¯");
    }

    @CommandAlias("tableflip")
    public void onTableflip(final Player player) {
        player.chat("(╯°□°）╯︵ ┻━┻");
    }

    @CommandAlias("unflip")
    public void onUnflip(final Player player) {
        player.chat("┬─┬ ノ( ゜-゜ノ)");
    }

    @CommandAlias("riot")
    public void onRiot(final Player player) {
        player.chat("ヽ༼ຈل͜ຈ༽ﾉ RIOT ヽ༼ຈل͜ຈ༽ﾉ");
    }

    @CommandAlias("lenny")
    public void onLenny(final Player player) {
        player.chat("( ͡° ͜ʖ ͡°)");
    }

    @CommandAlias("sad")
    public void onSad(final Player player) {
        player.chat("(ಥ﹏ಥ)");
    }

    @CommandAlias("kawaii")
    public void onKawaii(final Player player) {
        player.chat("(づ｡◕‿‿◕｡)づ");
    }

}
