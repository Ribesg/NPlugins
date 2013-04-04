package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedEntityExplodeEvent extends AbstractExtendedEvent {

    @Getter private final Map<Block, PlayerCuboid> blockCuboidsMap;

    public ExtendedEntityExplodeEvent(final CuboidDB db, final EntityExplodeEvent event) {
        super(event);
        blockCuboidsMap = new HashMap<Block, PlayerCuboid>();
        for (final Block b : event.blockList()) {
            final PlayerCuboid cuboid = db.getPriorByLoc(b.getLocation());
            if (cuboid != null) {
                blockCuboidsMap.put(b, cuboid);
            }
        }
    }

}
