package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class ExtendedPlayerMoveEvent extends AbstractExtendedEvent {

	private final GeneralCuboid      fromCuboid;
	private final Set<GeneralCuboid> fromCuboids;
	private final GeneralCuboid      toCuboid;
	private final Set<GeneralCuboid> toCuboids;
	private       boolean            customCancelled;

	public ExtendedPlayerMoveEvent(final CuboidDB db, final PlayerMoveEvent event) {
		super(event);
		fromCuboids = db.getAllByLoc(event.getFrom());
		fromCuboid = db.getPrior(fromCuboids);
		toCuboids = db.getAllByLoc(event.getTo());
		toCuboid = db.getPrior(toCuboids);
		customCancelled = false;
	}

	public boolean isCustomCancelled() {
		return customCancelled;
	}

	public void setCustomCancelled(boolean customCancelled) {
		this.customCancelled = customCancelled;
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
