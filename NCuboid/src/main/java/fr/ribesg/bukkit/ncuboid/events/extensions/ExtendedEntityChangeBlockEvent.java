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
