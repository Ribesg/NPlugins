package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public class ExtendedPlayerJoinEvent extends AbstractExtendedEvent {

    private final GeneralCuboid      cuboid;
    private final Set<GeneralCuboid> cuboids;

    public ExtendedPlayerJoinEvent(final CuboidDB db, final PlayerJoinEvent event) {
        super(event);
        cuboids = db.getAllByLoc(event.getPlayer().getLocation());
        cuboid = db.getPrior(cuboids);
    }

    public GeneralCuboid getCuboid() {
        return cuboid;
    }

    public Set<GeneralCuboid> getCuboids() {
        return cuboids;
    }
}
