/***************************************************************************
 * Project file:    NPlugins - NCore - XPDistributionEvent.java            *
 * Full Class name: fr.ribesg.bukkit.ncore.event.theendagain.XPDistributionEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.event.theendagain;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;

/**
 * This event is called by the NTheEndAgain node just before giving xp
 * to players after an EnderDragon's death.
 * The cancellation of this event will have the exact same effect than
 * emptying the xpMap: nobody will receive anything.
 */
public class XPDistributionEvent extends Event implements Cancellable {

	// Handlers
	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	private final Map<String, Integer> xpMap;
	private final int                  totalXPAmount;
	private       boolean              cancelled;

	public XPDistributionEvent(final Map<String, Integer> xpMap, final int totalXPAmount) {
		this.xpMap = xpMap;
		this.totalXPAmount = totalXPAmount;
		this.cancelled = false;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	/**
	 * A map linking Player names to the XP they will receive.
	 * The amount of XP can be modified, and you are free to add or remove people from this map.
	 */
	public Map<String, Integer> getXpMap() {
		return xpMap;
	}

	/** The total amount of XP given for this Dragon's death, for reference. */
	public int getTotalXPAmount() {
		return totalXPAmount;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}
}
