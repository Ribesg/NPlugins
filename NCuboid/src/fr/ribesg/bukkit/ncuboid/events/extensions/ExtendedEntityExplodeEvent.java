package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;
import java.util.Map;

public class ExtendedEntityExplodeEvent extends AbstractExtendedEvent {

	private final GeneralCuboid             entityCuboid;
	private final Map<Block, GeneralCuboid> blockCuboidsMap;

	public ExtendedEntityExplodeEvent(final CuboidDB db, final EntityExplodeEvent event) {
		super(event);
		blockCuboidsMap = new HashMap<>();
		for (final Block b : event.blockList()) {
			final GeneralCuboid cuboid = db.getPriorByLoc(b.getLocation());
			if (cuboid != null) {
				blockCuboidsMap.put(b, cuboid);
			}
		}
		entityCuboid = blockCuboidsMap.get(event.getLocation().getBlock());
	}

	public Map<Block, GeneralCuboid> getBlockCuboidsMap() {
		return blockCuboidsMap;
	}

	public GeneralCuboid getEntityCuboid() {
		return entityCuboid;
	}
}
