/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPlayerDropItemEvent.java  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerDropItemEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ExtendedPlayerDropItemEvent extends AbstractExtendedEvent {

	private final GeneralRegion region;

	public ExtendedPlayerDropItemEvent(final RegionDb db, final PlayerDropItemEvent event) {
		super(db.getPlugin(), event);
		region = db.getPriorByLocation(event.getPlayer().getLocation());
	}

	public GeneralRegion getRegion() {
		return region;
	}
}
