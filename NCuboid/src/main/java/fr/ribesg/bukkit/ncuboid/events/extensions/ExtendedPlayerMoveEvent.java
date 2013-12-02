/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedPlayerMoveEvent.java      *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerMoveEvent
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class ExtendedPlayerMoveEvent extends AbstractExtendedEvent {

	private final GeneralRegion      fromRegion;
	private final Set<GeneralRegion> fromRegions;
	private final GeneralRegion      toRegion;
	private final Set<GeneralRegion> toRegions;
	private       boolean            customCancelled;

	public ExtendedPlayerMoveEvent(final RegionDb db, final PlayerMoveEvent event) {
		super(event);
		fromRegions = db.getAllByLocation(event.getFrom());
		fromRegion = db.getPrior(fromRegions);
		toRegions = db.getAllByLocation(event.getTo());
		toRegion = db.getPrior(toRegions);
		customCancelled = false;
	}

	public boolean isCustomCancelled() {
		return customCancelled;
	}

	public void setCustomCancelled(boolean customCancelled) {
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
