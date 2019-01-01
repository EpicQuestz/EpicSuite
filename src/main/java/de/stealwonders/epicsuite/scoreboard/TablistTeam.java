package de.stealwonders.epicsuite.scoreboard;

import de.stealwonders.epicsuite.EpicSuite;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Objects;

public class TablistTeam<T> {

    private T t;
    private int priority;
    private ChatColor color;

    public TablistTeam(final T t, final int priority, final ChatColor color) {
        this.t = t;
        this.priority = priority;
        this.color = color;
    }

    public T getT() {
        return t;
    }

    public int getPriority() {
        return priority;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        switch (EpicSuite.getPlugin().getPermissionHandler()) {
            case PERMISSIONSEX:
                if (getT() instanceof PermissionGroup) {
                    final PermissionGroup permissionGroup = (PermissionGroup) getT();
                    return permissionGroup.getName();
                }
            case LUCKPERMS:
                if (getT() instanceof Group) {
                    final Group group = (Group) getT();
                    return group.getName();
                }
            default:
                return null;
        }
    }

    public boolean isInGroup(final Player player) {
        switch (EpicSuite.getPlugin().getPermissionHandler()) {
            case PERMISSIONSEX:
                if (getT() instanceof PermissionGroup) {
                    final PermissionGroup permissionGroup = (PermissionGroup) getT();
                    final PermissionUser permissionUser = PermissionsEx.getUser(player);
                    return permissionUser.inGroup(permissionGroup);
                }
            case LUCKPERMS:
                if (getT() instanceof Group) {
                    final Group group = (Group) getT();
                    return Objects.requireNonNull(LuckPerms.getApi().getUser(player.getUniqueId())).inheritsGroup(group);
                }
            default:
                return false;
        }
    }
}
