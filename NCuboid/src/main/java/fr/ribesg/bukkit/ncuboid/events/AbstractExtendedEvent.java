/***************************************************************************
 * Project file:    NPlugins - NCuboid - AbstractExtendedEvent.java        *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
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
