/***************************************************************************
 * Project file:    NPlugins - NCuboid - WarpgateFlagListener.java         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.WarpgateFlagListener
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
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class WarpgateFlagListener extends AbstractListener {

	public WarpgateFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
		final PlayerGridMoveEvent event = (PlayerGridMoveEvent) ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			if (ext.getToRegion() != null && ext.getToRegion().getFlag(Flag.WARPGATE)) {
				event.getPlayer().teleport(ext.getToRegion().getLocAttribute(Attribute.EXTERNAL_POINT));
				ext.setCustomCancelled(true);
				event.setCancelled(true);
			}
		}
	}
}
