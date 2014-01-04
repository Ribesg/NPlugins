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
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

public class ExtendedPlayerTeleportEvent extends AbstractExtendedEvent {

	private final GeneralRegion      fromRegion;
	private final Set<GeneralRegion> fromRegions;
	private final GeneralRegion      toRegion;
	private final Set<GeneralRegion> toRegions;

	public ExtendedPlayerTeleportEvent(final RegionDb db, final PlayerTeleportEvent event) {
		super(event);
		fromRegions = db.getAllByLocation(event.getFrom());
		fromRegion = db.getPrior(fromRegions);
		toRegions = db.getAllByLocation(event.getTo());
		toRegion = db.getPrior(toRegions);
	}

	public GeneralRegion getFromRegion() {
		return fromRegion;
	}

	public Set<GeneralRegion> getFromRegions() {
		return fromRegions;
	}

	public GeneralRegion getToRegion() {
		return toRegion;
	}

	public Set<GeneralRegion> getToRegions() {
		return toRegions;
	}
}
