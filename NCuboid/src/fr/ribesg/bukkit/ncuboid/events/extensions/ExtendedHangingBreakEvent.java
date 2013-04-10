package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.hanging.HangingBreakEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedHangingBreakEvent extends AbstractExtendedEvent {

    @Getter private final GeneralCuboid      cuboid;
    @Getter private final Set<GeneralCuboid> cuboids;

    public ExtendedHangingBreakEvent(final CuboidDB db, final HangingBreakEvent event) {
        super(event);
        cuboids = db.getAllByLoc(event.getEntity().getLocation());
        cuboid = db.getPrior(cuboids);
    }

}
