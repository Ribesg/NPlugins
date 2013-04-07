package fr.ribesg.bukkit.ncuboid.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.WorldCuboid;

public class WorldLoadingListener extends AbstractListener {

    public WorldLoadingListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(final WorldLoadEvent event) {
        if (getPlugin().getDb().getByWorld(event.getWorld()) == null) {
            // Obviously true here, we should change this when we change World to String (worldName)
            getPlugin().getDb().addByWorld(new WorldCuboid(event.getWorld()));
        }
    }

}
