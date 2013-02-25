package com.github.ribesg.ncuboid.events.extensions;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.events.AbstractExtendedEvent;

public class ExtendedEntityExplodeEvent extends AbstractExtendedEvent {

    @Getter private final Map<Block, PlayerCuboid> blockCuboidsMap;

    public ExtendedEntityExplodeEvent(final EntityExplodeEvent event) {
        super(event);
        blockCuboidsMap = new HashMap<Block, PlayerCuboid>();
        for (final Block b : event.blockList()) {
            final PlayerCuboid cuboid = CuboidDB.getInstance().getPriorByLoc(b.getLocation());
            if (cuboid != null) {
                blockCuboidsMap.put(b, cuboid);
            }
        }
    }

}
