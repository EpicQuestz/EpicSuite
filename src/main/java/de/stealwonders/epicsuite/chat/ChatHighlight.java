package de.stealwonders.epicsuite.chat;

import me.lucko.luckperms.api.*;
import me.lucko.luckperms.api.caching.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatHighlight implements Listener {

    private LuckPermsApi luckPermsApi;

    public ChatHighlight(final LuckPermsApi luckPermsApi) {
        this.luckPermsApi = luckPermsApi;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        final String findColorPattern = "(?<=§)\\w";

        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (message.toLowerCase().contains(player.getName().toLowerCase())) {

                final String[] parts = message.split(player.getName());
                ChatColor lastColor = ChatColor.WHITE;

                // If a player only says someones name without and text
                if (parts.length == 0) {
                    message = message.replaceAll(player.getName(), "§6§o" + player.getName());
                }
                
                for (final String part : parts) {

                    // Searches if the text part has a color code defined somewhere
                    // If one is found it is set as the lastColor
                    // If multiple are found the last one will be assigned
                    final Pattern pattern = Pattern.compile(findColorPattern);
                    final Matcher matcher = pattern.matcher(part);
                    if (matcher.find()) {
                        final String colorString = matcher.group(matcher.groupCount());
                        lastColor = ChatColor.getByChar(colorString);
                    }

                    // Defines the name replacement Text
                    final String coloredName = "§6§o" + player.getName() + lastColor;
                    // Defines which username (if there are multiple in the message) should be replaced this time
                    final String replacementRegex = "(?<=" + part + ")" + player.getName() + "(?-i)";
                    // Defines the regex for getting text around a username to the nearest whitespace to identify if it's link
                    final String linkRegex = "[^ ]+(?<=" + part + ")" + player.getName() + "[^ ]+";

                    String potentialLinkString = null;

                    final Pattern linkPattern = Pattern.compile(linkRegex);
                    final Matcher linkMatcher = linkPattern.matcher(message);

                    if (linkMatcher.find()) {
                        potentialLinkString = linkMatcher.group(linkMatcher.groupCount());
                    }

                    // Only replace the username if no link pattern was found
                    if (potentialLinkString == null) {
                        message = message.replaceAll(replacementRegex, coloredName);
                    } else {
                        if (!isLink(potentialLinkString)) {
                            message = message.replaceAll(replacementRegex, coloredName);
                        }
                    }
                }

                event.setMessage(message);
            }
        }
    }

    private boolean isLink(final String string) {
        final String regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    private Group getGroup(final Player player) {
        final Track track = luckPermsApi.getTrack("default");
        if (track != null) {
            System.out.println(track);
            final User user = luckPermsApi.getUser(player.getUniqueId());
            System.out.println(user);
            for (final String groupName : track.getGroups()) {
                final Group group = luckPermsApi.getGroup(groupName);
                if (user.inheritsGroup(group)) {
                    return group;
                }
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
