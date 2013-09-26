package fr.ribesg.bukkit.ncuboid.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents an extension of a Bukkit event.
 * <p/>
 * This is used to make the Region detection computation only once.
 */
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
	private final Event baseEvent;

	public AbstractExtendedEvent(final Event event) {
		baseEvent = event;
	}

	public Event getBaseEvent() {
		return baseEvent;
	}
}
