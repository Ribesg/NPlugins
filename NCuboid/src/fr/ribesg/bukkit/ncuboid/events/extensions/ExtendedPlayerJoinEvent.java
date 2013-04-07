package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerJoinEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerJoinEvent extends AbstractExtendedEvent {

    @Getter private final GeneralCuboid      cuboid;
    @Getter private final Set<GeneralCuboid> cuboids;

    public ExtendedPlayerJoinEvent(final CuboidDB db, final PlayerJoinEvent event) {
        super(event);
        cuboids = db.getAllByLoc(event.getPlayer().getLocation());
        cuboid = db.getPrior(cuboids);
    }

}
