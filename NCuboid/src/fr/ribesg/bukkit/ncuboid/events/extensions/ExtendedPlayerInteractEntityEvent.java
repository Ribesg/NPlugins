package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Set;

public class ExtendedPlayerInteractEntityEvent extends AbstractExtendedEvent {

	private final GeneralCuboid      cuboid;
	private final Set<GeneralCuboid> cuboids;

	public ExtendedPlayerInteractEntityEvent(final CuboidDB db, final PlayerInteractEntityEvent event) {
		super(event);
		cuboids = db.getAllByLoc(event.getRightClicked().getLocation());
		cuboid = db.getPrior(cuboids);
	}

	public GeneralCuboid getCuboid() {
		return cuboid;
	}

	public Set<GeneralCuboid> getCuboids() {
		return cuboids;
	}
}
