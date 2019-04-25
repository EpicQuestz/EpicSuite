package de.stealwonders.epicsuite.chat;

import de.stealwonders.epicsuite.EpicSuite;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.caching.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import javax.annotation.Nonnull;

public class ChatHighlight implements Listener {

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (message.toLowerCase().contains(player.getName().toLowerCase())) {

                final Object group = getGroup(event.getPlayer());
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

    @SuppressWarnings("deprecation")
    private Object getGroup(@Nonnull final Player player) {
        switch (EpicSuite.getPlugin().getPermissionHandler()) {
            case PERMISSIONSEX:
                final PermissionUser permissionUser = PermissionsEx.getUser(player);
                final PermissionGroup[] groups = permissionUser.getGroups();
                PermissionGroup highestGroup = groups[0];
                for (final PermissionGroup permissionGroup : groups) {
                    if (permissionGroup.getRank() < highestGroup.getRank()) {
                        highestGroup = permissionGroup;
                    }
                }
                return highestGroup;
            case LUCKPERMS:
                return LuckPerms.getApi().getGroup(LuckPerms.getApi().getUser(player.getUniqueId()).getPrimaryGroup());
            default:
                return null;

        }
    }

    private String getSuffix(final Object object) {
        switch (EpicSuite.getPlugin().getPermissionHandler()) {
            case PERMISSIONSEX:
                if (object instanceof PermissionGroup) {
                    final PermissionGroup permissionGroup = (PermissionGroup) object;
                    return permissionGroup.getSuffix();
                }
            case LUCKPERMS:
                if (object instanceof Group) {
                    final Group group = (Group) object;
                    final Contexts contexts = LuckPerms.getApi().getContextManager().getStaticContexts();
                    final MetaData metaData = group.getCachedData().getMetaData(contexts);
                    return metaData.getSuffix();
                }
            default:
                return null;
        }
    }
}
