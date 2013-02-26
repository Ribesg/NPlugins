package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerJoinEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerJoinEvent extends AbstractExtendedEvent {

    @Getter private final PlayerCuboid      cuboid;
    @Getter private final Set<PlayerCuboid> cuboids;

    public ExtendedPlayerJoinEvent(final PlayerJoinEvent event) {
        super(event);
        cuboids = CuboidDB.getInstance().getAllByLoc(event.getPlayer().getLocation());
        cuboid = CuboidDB.getInstance().getPrior(cuboids);
    }

}
