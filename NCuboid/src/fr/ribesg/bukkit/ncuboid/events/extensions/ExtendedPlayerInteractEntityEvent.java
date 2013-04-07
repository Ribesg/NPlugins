package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerInteractEntityEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerInteractEntityEvent extends AbstractExtendedEvent {

    @Getter private final GeneralCuboid      cuboid;
    @Getter private final Set<GeneralCuboid> cuboids;

    public ExtendedPlayerInteractEntityEvent(final CuboidDB db, final PlayerInteractEntityEvent event) {
        super(event);
        cuboids = db.getAllByLoc(event.getRightClicked().getLocation());
        cuboid = db.getPrior(cuboids);
    }

}
