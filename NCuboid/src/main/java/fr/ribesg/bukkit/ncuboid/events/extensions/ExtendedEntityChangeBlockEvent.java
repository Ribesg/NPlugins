/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedEntityChangeBlockEvent.java
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityChangeBlockEvent
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class ExtendedEntityChangeBlockEvent extends AbstractExtendedEvent {

	private final GeneralRegion blockRegion;

	public ExtendedEntityChangeBlockEvent(final RegionDb db, final EntityChangeBlockEvent event) {
		super(event);
		blockRegion = db.getPriorByLocation(event.getBlock().getLocation());
	}

	public GeneralRegion getBlockRegion() {
		return blockRegion;
	}
}
