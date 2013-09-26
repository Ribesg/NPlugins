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
