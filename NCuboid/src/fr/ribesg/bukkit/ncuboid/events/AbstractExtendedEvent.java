package fr.ribesg.bukkit.ncuboid.events;

import lombok.Getter;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class AbstractExtendedEvent extends Event {
    // Handlers
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    // Actual Event Extension
    @Getter private final Event baseEvent;

    public AbstractExtendedEvent(final Event event) {
        baseEvent = event;
    }
}
