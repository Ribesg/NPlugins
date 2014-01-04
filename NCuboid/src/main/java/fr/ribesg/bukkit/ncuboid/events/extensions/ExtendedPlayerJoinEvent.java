/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPlayerJoinEvent.java      *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerJoinEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public class ExtendedPlayerJoinEvent extends AbstractExtendedEvent {

	private final GeneralRegion      region;
	private final Set<GeneralRegion> regions;

	public ExtendedPlayerJoinEvent(final RegionDb db, final PlayerJoinEvent event) {
		super(event);
		regions = db.getAllByLocation(event.getPlayer().getLocation());
		region = db.getPrior(regions);
	}

	public GeneralRegion getRegion() {
		return region;
	}

	public Set<GeneralRegion> getRegions() {
		return regions;
	}
}
