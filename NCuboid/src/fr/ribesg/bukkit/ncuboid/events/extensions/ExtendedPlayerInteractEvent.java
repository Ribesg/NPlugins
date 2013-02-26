package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerInteractEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerInteractEvent extends AbstractExtendedEvent {

    @Getter private final PlayerCuboid      cuboid;
    @Getter private final Set<PlayerCuboid> cuboids;

    // Called only if event.hasBlock()
    public ExtendedPlayerInteractEvent(final PlayerInteractEvent event) {
        super(event);
        if (event.hasBlock()) {
            cuboids = CuboidDB.getInstance().getAllByLoc(event.getClickedBlock().getLocation());
            cuboid = CuboidDB.getInstance().getPrior(cuboids);
        } else {
            cuboids = null;
            cuboid = null;
        }
    }

}
