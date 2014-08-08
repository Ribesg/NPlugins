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

import java.util.Set;
import java.util.SortedSet;

import org.bukkit.event.player.PlayerInteractEvent;

public class ExtendedPlayerInteractEvent extends AbstractExtendedEvent {

	private final GeneralRegion            clickedRegion;
	private final SortedSet<GeneralRegion> clickedRegions;
	private final GeneralRegion            relativeClickedRegion;
	private final SortedSet<GeneralRegion> relativeClickedRegions;

	// Called only if event.hasBlock()
	public ExtendedPlayerInteractEvent(final RegionDb db, final PlayerInteractEvent event) {
		super(db.getPlugin(), event);
		if (event.hasBlock()) {
			this.clickedRegions = db.getAllByLocation(event.getClickedBlock().getLocation());
			this.clickedRegion = db.getPrior(this.clickedRegions);
			this.relativeClickedRegions = db.getAllByLocation(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation());
			this.relativeClickedRegion = db.getPrior(this.relativeClickedRegions);
		} else {
			this.clickedRegions = null;
			this.clickedRegion = null;
			this.relativeClickedRegion = null;
			this.relativeClickedRegions = null;
		}
	}

	public GeneralRegion getClickedRegion() {
		return this.clickedRegion;
	}

	public Set<GeneralRegion> getClickedRegions() {
		return this.clickedRegions;
	}

	public GeneralRegion getRelativeClickedRegion() {
		return this.relativeClickedRegion;
	}

	public Set<GeneralRegion> getRelativeClickedRegions() {
		return this.relativeClickedRegions;
	}
}
