package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Set;

public class ExtendedPlayerInteractEntityEvent extends AbstractExtendedEvent {

	private final GeneralRegion      region;
	private final Set<GeneralRegion> regions;

	public ExtendedPlayerInteractEntityEvent(final RegionDb db, final PlayerInteractEntityEvent event) {
		super(event);
		regions = db.getAllByLocation(event.getRightClicked().getLocation());
		region = db.getPrior(regions);
	}

	public GeneralRegion getRegion() {
		return region;
	}

	public Set<GeneralRegion> getRegions() {
		return regions;
	}
}
