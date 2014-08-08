/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPlayerTeleportEvent.java  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerTeleportEvent
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

import org.bukkit.event.player.PlayerTeleportEvent;

public class ExtendedPlayerTeleportEvent extends AbstractExtendedEvent {

	private final GeneralRegion            fromRegion;
	private final SortedSet<GeneralRegion> fromRegions;
	private final GeneralRegion            toRegion;
	private final SortedSet<GeneralRegion> toRegions;

	public ExtendedPlayerTeleportEvent(final RegionDb db, final PlayerTeleportEvent event) {
		super(db.getPlugin(), event);
		this.fromRegions = db.getAllByLocation(event.getFrom());
		this.fromRegion = db.getPrior(this.fromRegions);
		this.toRegions = db.getAllByLocation(event.getTo());
		this.toRegion = db.getPrior(this.toRegions);
	}

	public GeneralRegion getFromRegion() {
		return this.fromRegion;
	}

	public Set<GeneralRegion> getFromRegions() {
		return this.fromRegions;
	}

	public GeneralRegion getToRegion() {
		return this.toRegion;
	}

	public Set<GeneralRegion> getToRegions() {
		return this.toRegions;
	}
}
