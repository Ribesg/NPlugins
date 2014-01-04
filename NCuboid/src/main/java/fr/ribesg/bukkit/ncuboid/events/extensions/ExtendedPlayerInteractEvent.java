/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPlayerInteractEvent.java  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class ExtendedPlayerInteractEvent extends AbstractExtendedEvent {

	private final GeneralRegion      region;
	private final Set<GeneralRegion> regions;

	// Called only if event.hasBlock()
	public ExtendedPlayerInteractEvent(final RegionDb db, final PlayerInteractEvent event) {
		super(event);
		if (event.hasBlock()) {
			regions = db.getAllByLocation(event.getClickedBlock().getLocation());
			region = db.getPrior(regions);
		} else {
			regions = null;
			region = null;
		}
	}

	public GeneralRegion getRegion() {
		return region;
	}

	public Set<GeneralRegion> getRegions() {
		return regions;
	}
}
