package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.entity.EntityInteractEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedEntityInteractEvent extends AbstractExtendedEvent {

    @Getter private final GeneralCuboid      cuboid;
    @Getter private final Set<GeneralCuboid> cuboids;

    public ExtendedEntityInteractEvent(final CuboidDB db, final EntityInteractEvent event) {
        super(event);
        cuboids = db.getAllByLoc(event.getBlock().getLocation());
        cuboid = db.getPrior(cuboids);
    }

}
