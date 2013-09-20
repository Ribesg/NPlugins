package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDb;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

public class ExtendedPlayerTeleportEvent extends AbstractExtendedEvent {

	private final GeneralCuboid      fromCuboid;
	private final Set<GeneralCuboid> fromCuboids;
	private final GeneralCuboid      toCuboid;
	private final Set<GeneralCuboid> toCuboids;

	public ExtendedPlayerTeleportEvent(final CuboidDb db, final PlayerTeleportEvent event) {
		super(event);
		fromCuboids = db.getAllByLocation(event.getFrom());
		fromCuboid = db.getPrior(fromCuboids);
		toCuboids = db.getAllByLocation(event.getTo());
		toCuboid = db.getPrior(toCuboids);
	}

	public GeneralCuboid getFromCuboid() {
		return fromCuboid;
	}

	public Set<GeneralCuboid> getFromCuboids() {
		return fromCuboids;
	}

	public GeneralCuboid getToCuboid() {
		return toCuboid;
	}

	public Set<GeneralCuboid> getToCuboids() {
		return toCuboids;
	}
}
