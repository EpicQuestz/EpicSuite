package de.stealwonders.epicsuite.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import org.bukkit.entity.Player;

@CommandPermission("epicsuite.chat.emojis")
public class EmojiCommands extends BaseCommand {

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
