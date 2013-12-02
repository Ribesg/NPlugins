/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedHangingBreakEvent.java    *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedHangingBreakEvent
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

import java.util.Set;

public class ExtendedHangingBreakEvent extends AbstractExtendedEvent {

	private final GeneralRegion      region;
	private final Set<GeneralRegion> regions;

	public ExtendedHangingBreakEvent(final RegionDb db, final HangingBreakEvent event) {
		super(event);
		regions = db.getAllByLocation(event.getEntity().getLocation());
		region = db.getPrior(regions);
	}

	public GeneralRegion getRegion() {
		return region;
	}

	public Set<GeneralRegion> getRegions() {
		return regions;
	}
}
