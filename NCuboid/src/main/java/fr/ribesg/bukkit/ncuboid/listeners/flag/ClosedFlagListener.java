/***************************************************************************
 * Project file:    NPlugins - NCuboid - ClosedFlagListener.java           *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.ClosedFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ClosedFlagListener extends AbstractListener {

	public ClosedFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
		final PlayerGridMoveEvent event = (PlayerGridMoveEvent)ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			final GeneralRegion from = ext.getFromRegion();
			if (from != null && from.getFlag(Flag.CLOSED) && !ext.getToRegions().contains(from)) {
				Location loc = from.getLocationAttribute(Attribute.INTERNAL_POINT);
				if (loc == null) {
					loc = event.getFrom();
				}
				event.setTo(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.1, loc.getBlockZ() + 0.5, event.getTo().getYaw(), event.getTo().getPitch()));
				ext.setCustomCancelled(true);
			}
		}
	}
}
