/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPlayerGridMoveEvent.java  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

import java.util.Set;
import java.util.SortedSet;

public class ExtendedPlayerGridMoveEvent extends AbstractExtendedEvent {

	private final GeneralRegion            fromRegion;
	private final SortedSet<GeneralRegion> fromRegions;
	private final GeneralRegion            toRegion;
	private final SortedSet<GeneralRegion> toRegions;
	private       boolean                  customCancelled;

	public ExtendedPlayerGridMoveEvent(final RegionDb db, final PlayerGridMoveEvent event) {
		super(db.getPlugin(), event);
		fromRegions = db.getAllByLocation(event.getFrom());
		fromRegion = db.getPrior(fromRegions);
		toRegions = db.getAllByLocation(event.getTo());
		toRegion = db.getPrior(toRegions);
		customCancelled = false;
	}

	public boolean isCustomCancelled() {
		return customCancelled;
	}

	public void setCustomCancelled(final boolean customCancelled) {
		this.customCancelled = customCancelled;
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
