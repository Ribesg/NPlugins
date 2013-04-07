package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedEntityExplodeEvent extends AbstractExtendedEvent {

    @Getter private final GeneralCuboid             entityCuboid;
    @Getter private final Map<Block, GeneralCuboid> blockCuboidsMap;

    public ExtendedEntityExplodeEvent(final CuboidDB db, final EntityExplodeEvent event) {
        super(event);
        blockCuboidsMap = new HashMap<Block, GeneralCuboid>();
        for (final Block b : event.blockList()) {
            final GeneralCuboid cuboid = db.getPriorByLoc(b.getLocation());
            if (cuboid != null) {
                blockCuboidsMap.put(b, cuboid);
            }
        }
        entityCuboid = blockCuboidsMap.get(event.getEntity().getLocation().getBlock());
    }

}
