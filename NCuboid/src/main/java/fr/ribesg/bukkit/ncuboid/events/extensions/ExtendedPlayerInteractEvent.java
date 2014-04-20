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
import java.util.SortedSet;

public class ExtendedPlayerInteractEvent extends AbstractExtendedEvent {

	private final GeneralRegion            clickedRegion;
	private final SortedSet<GeneralRegion> clickedRegions;
	private final GeneralRegion            relativeClickedRegion;
	private final SortedSet<GeneralRegion> relativeClickedRegions;

	// Called only if event.hasBlock()
	public ExtendedPlayerInteractEvent(final RegionDb db, final PlayerInteractEvent event) {
		super(event);
		if (event.hasBlock()) {
			clickedRegions = db.getAllByLocation(event.getClickedBlock().getLocation());
			clickedRegion = db.getPrior(clickedRegions);
			relativeClickedRegions = db.getAllByLocation(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation());
			relativeClickedRegion = db.getPrior(relativeClickedRegions);
		} else {
			clickedRegions = null;
			clickedRegion = null;
			relativeClickedRegion = null;
			relativeClickedRegions = null;
		}
	}

	public GeneralRegion getClickedRegion() {
		return clickedRegion;
	}

	public Set<GeneralRegion> getClickedRegions() {
		return clickedRegions;
	}

	public GeneralRegion getRelativeClickedRegion() {
		return relativeClickedRegion;
	}

	public Set<GeneralRegion> getRelativeClickedRegions() {
		return relativeClickedRegions;
	}
}
