package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

import java.util.Set;

public class ExtendedHangingBreakEvent extends AbstractExtendedEvent {

	private final GeneralCuboid      cuboid;
	private final Set<GeneralCuboid> cuboids;

	public ExtendedHangingBreakEvent(final CuboidDB db, final HangingBreakEvent event) {
		super(event);
		cuboids = db.getAllByLoc(event.getEntity().getLocation());
		cuboid = db.getPrior(cuboids);
	}

	public GeneralCuboid getCuboid() {
		return cuboid;
	}

	public Set<GeneralCuboid> getCuboids() {
		return cuboids;
	}
}
