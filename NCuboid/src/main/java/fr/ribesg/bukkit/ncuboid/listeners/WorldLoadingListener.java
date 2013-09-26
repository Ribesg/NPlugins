package fr.ribesg.bukkit.ncuboid.listeners;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.WorldRegion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadingListener extends AbstractListener {

	public WorldLoadingListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldLoad(final WorldLoadEvent event) {
		if (getPlugin().getDb().getByWorld(event.getWorld().getName()) == null) {
			getPlugin().getDb().addByWorld(new WorldRegion(event.getWorld().getName()));
		}
	}

}
