package fr.ribesg.bukkit.ncuboid.events.extensions;

import lombok.Getter;

import org.bukkit.event.player.PlayerDropItemEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerDropItemEvent extends AbstractExtendedEvent {

    @Getter GeneralCuboid cuboid;

    public ExtendedPlayerDropItemEvent(final CuboidDB db, final PlayerDropItemEvent event) {
        super(event);
        cuboid = db.getPriorByLoc(event.getPlayer().getLocation());
    }

}
