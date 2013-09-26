package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

import java.util.Set;

public class ExtendedHangingBreakEvent extends AbstractExtendedEvent {

	private final GeneralRegion      region;
	private final Set<GeneralRegion> regions;

	public ExtendedHangingBreakEvent(final RegionDb db, final HangingBreakEvent event) {
		super(event);
		regions = db.getAllByLocation(event.getEntity().getLocation());
		region = db.getPrior(regions);
	}

	public GeneralRegion getRegion() {
		return region;
	}

	public Set<GeneralRegion> getRegions() {
		return regions;
	}
}
