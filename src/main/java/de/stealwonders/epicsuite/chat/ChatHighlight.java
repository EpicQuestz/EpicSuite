package de.stealwonders.epicsuite.chat;

import me.lucko.luckperms.api.*;
import me.lucko.luckperms.api.caching.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
        Track track = luckPermsApi.getTrack("default");
        User user = luckPermsApi.getUser(player.getUniqueId());
        for (String groupName : track.getGroups()) {
            Group group = luckPermsApi.getGroup(groupName);
            if (user.inheritsGroup(group)) {
                return group;
            }
        }
        return null;
    }

    private String getSuffix(final Group group) {
        final Contexts contexts = luckPermsApi.getContextManager().getStaticContexts();
        final MetaData metaData = group.getCachedData().getMetaData(contexts);
        return metaData.getSuffix();
    }
}
