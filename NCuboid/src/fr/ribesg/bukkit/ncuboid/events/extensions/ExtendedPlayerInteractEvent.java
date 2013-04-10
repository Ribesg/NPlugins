package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerInteractEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerInteractEvent extends AbstractExtendedEvent {

    @Getter private final GeneralCuboid      cuboid;
    @Getter private final Set<GeneralCuboid> cuboids;

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

}
