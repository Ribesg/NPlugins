package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ExtendedPlayerDropItemEvent extends AbstractExtendedEvent {

	private GeneralCuboid cuboid;

	public ExtendedPlayerDropItemEvent(final CuboidDB db, final PlayerDropItemEvent event) {
		super(event);
		cuboid = db.getPriorByLoc(event.getPlayer().getLocation());
	}

	public GeneralCuboid getCuboid() {
		return cuboid;
	}
}
