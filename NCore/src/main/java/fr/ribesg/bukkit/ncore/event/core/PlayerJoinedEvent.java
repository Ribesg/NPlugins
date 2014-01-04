/***************************************************************************
 * Project file:    NPlugins - NCore - PlayerJoinedEvent.java              *
 * Full Class name: fr.ribesg.bukkit.ncore.event.core.PlayerJoinedEvent    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.event.core;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * This event will be thrown once the Player has successfully joined the
 * server i.e. AFTER PlayerJoinEvent.
 * It should be used to send Welcome (and other) messages to the Player.
 */
public class PlayerJoinedEvent extends PlayerEvent {

	// Handlers
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public PlayerJoinedEvent(final Player who) {
		super(who);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
