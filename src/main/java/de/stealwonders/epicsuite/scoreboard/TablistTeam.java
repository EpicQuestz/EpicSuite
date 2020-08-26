package de.stealwonders.epicsuite.scoreboard;

import net.luckperms.api.model.group.Group;
import org.bukkit.ChatColor;

public class TablistTeam {

    private final Group group;
    private final int priority;
    private final ChatColor color;

    public TablistTeam(final Group group, final int priority, final ChatColor color) {
        this.group = group;
        this.priority = priority;
        this.color = color;
    }

    public Group getGroup() {
        return group;
    }

    public int getPriority() {
        return priority;
    }

    public ChatColor getColor() {
        return color;
    }

}
