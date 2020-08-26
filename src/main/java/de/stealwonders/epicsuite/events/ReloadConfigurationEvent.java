package de.stealwonders.epicsuite.events;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReloadConfigurationEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final CommandSender commandSender;

    public ReloadConfigurationEvent(final CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
