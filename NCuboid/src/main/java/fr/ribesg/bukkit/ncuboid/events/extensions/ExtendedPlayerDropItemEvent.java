package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ExtendedPlayerDropItemEvent extends AbstractExtendedEvent {

	private GeneralRegion region;

	public ExtendedPlayerDropItemEvent(final RegionDb db, final PlayerDropItemEvent event) {
		super(event);
		region = db.getPriorByLocation(event.getPlayer().getLocation());
	}

	public GeneralRegion getRegion() {
		return region;
	}
}
