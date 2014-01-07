/***************************************************************************
 * Project file:    NPlugins - NCore - PlayerGridMoveEvent.java            *
 * Full Class name: fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.event;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This event is thrown when a Player move in a different block.
 *
 * @author Ribesg
 */
public class PlayerGridMoveEvent extends PlayerMoveEvent {

	// Handlers
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public PlayerGridMoveEvent(final Player player, final Location from, final Location to) {
		super(player, from, to);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
