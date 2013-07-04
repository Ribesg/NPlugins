package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class ExtendedPlayerInteractEvent extends AbstractExtendedEvent {

    private final GeneralCuboid      cuboid;
    private final Set<GeneralCuboid> cuboids;

    // Called only if event.hasBlock()
    public ExtendedPlayerInteractEvent(final CuboidDB db, final PlayerInteractEvent event) {
        super(event);
        if (event.hasBlock()) {
            cuboids = db.getAllByLoc(event.getClickedBlock().getLocation());
            cuboid = db.getPrior(cuboids);
        } else {
            cuboids = null;
            cuboid = null;
        }
    }

    public GeneralCuboid getCuboid() {
        return cuboid;
    }

    public Set<GeneralCuboid> getCuboids() {
        return cuboids;
    }
}
