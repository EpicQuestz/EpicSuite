package de.stealwonders.epicsuite.chat;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class ChatHighlight implements Listener {

    private LuckPermsApi luckPermsApi;

    public ChatHighlight(final LuckPermsApi luckPermsApi) {
        this.luckPermsApi = luckPermsApi;
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (message.toLowerCase().contains(player.getName().toLowerCase())) {

                final Group group = getGroup(event.getPlayer());
                final String suffix = getSuffix(group);
                final String colorString = suffix.substring(suffix.length() - 1);
                final ChatColor color = ChatColor.getByChar(colorString);

                final String string = "§6§o" + player.getName() + color;
                final String regex = "(?i)" + player.getName() + "(?-i)";
                message = message.replaceAll(regex, string);
                event.setMessage(message);

            }
        }
    }

    private Group getGroup(final Player player) {
        return luckPermsApi.getGroup(Objects.requireNonNull(luckPermsApi.getUser(player.getUniqueId())).getPrimaryGroup());
    }

    private String getSuffix(final Group group) {
        final Contexts contexts = luckPermsApi.getContextManager().getStaticContexts();
        final MetaData metaData = group.getCachedData().getMetaData(contexts);
        return metaData.getSuffix();
    }
}
