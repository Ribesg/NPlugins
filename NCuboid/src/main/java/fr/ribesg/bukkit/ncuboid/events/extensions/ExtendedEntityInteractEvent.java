/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedEntityInteractEvent.java  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityInteractEvent
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.entity.EntityInteractEvent;

import java.util.Set;

public class ExtendedEntityInteractEvent extends AbstractExtendedEvent {

	private final GeneralRegion      region;
	private final Set<GeneralRegion> regions;

	public ExtendedEntityInteractEvent(final RegionDb db, final EntityInteractEvent event) {
		super(event);
		regions = db.getAllByLocation(event.getBlock().getLocation());
		region = db.getPrior(regions);
	}

	public GeneralRegion getRegion() {
		return region;
	}

	public Set<GeneralRegion> getRegions() {
		return regions;
	}
}
