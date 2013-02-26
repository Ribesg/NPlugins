package com.github.ribesg.ncuboid.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.World;

import com.github.ribesg.ncuboid.NCuboid;

public class CuboidDB {

	private static CuboidDB	instance;

	public static CuboidDB getInstance() {
		return instance;
	}

	@SuppressWarnings("unused") private final NCuboid	plugin;

	private final Map<String, PlayerCuboid>				byName;	// CuboidName ; Cuboid
	private final Map<String, Set<PlayerCuboid>>		byOwner;	// OwnerName ; Cuboids of this owner
	private final Map<String, PlayerCuboid>				tmpCuboids; // OwnerName ; Temporary Cuboid (Owner's Selection)
	private final Map<ChunkKey, Set<PlayerCuboid>>		byChunks;	// Chunk ; Cuboids in this chunk
	private final Map<String, WorldCuboid>				byWorld;	// WorldName ; Cuboid

	public CuboidDB(final NCuboid instance) {
		byName = new HashMap<String, PlayerCuboid>();
		byOwner = new HashMap<String, Set<PlayerCuboid>>();
		tmpCuboids = new HashMap<String, PlayerCuboid>();
		byChunks = new HashMap<ChunkKey, Set<PlayerCuboid>>();
		byWorld = new HashMap<String, WorldCuboid>();
		plugin = instance;
		CuboidDB.instance = this;
	}

	public void add(final PlayerCuboid cuboid) {
		addByName(cuboid);
		addByOwner(cuboid);
		addByChunks(cuboid);
	}

	public void addByName(final PlayerCuboid cuboid) {
		byName.put(cuboid.getCuboidName(), cuboid);
	}

	public void addByOwner(final PlayerCuboid cuboid) {
		if (byOwner.containsKey(cuboid.getOwnerName())) {
			byOwner.get(cuboid.getOwnerName()).add(cuboid);
		} else {
			final Set<PlayerCuboid> newSet = new HashSet<PlayerCuboid>();
			newSet.add(cuboid);
			byOwner.put(cuboid.getOwnerName(), newSet);
		}
	}

	public void addTmp(final PlayerCuboid cuboid) {
		tmpCuboids.put(cuboid.getOwnerName(), cuboid);
	}

	public void addByChunks(final PlayerCuboid cuboid) {
		for (final ChunkKey k : cuboid.getChunks()) {
			if (byChunks.containsKey(k)) {
				byChunks.get(k).add(cuboid);
			} else {
				final Set<PlayerCuboid> newSet = new HashSet<PlayerCuboid>();
				newSet.add(cuboid);
				byChunks.put(k, newSet);
			}
		}
	}

	public void addByWorld(final WorldCuboid c) {
		byWorld.put(c.getWorld().getName(), c);
	}

	public void del(final PlayerCuboid cuboid) {
		delByName(cuboid);
		delByOwner(cuboid);
		delByChunks(cuboid);
	}

	public void delByName(final PlayerCuboid cuboid) {
		if (byName.containsKey(cuboid.getCuboidName())) {
			byName.remove(cuboid.getCuboidName());
		}
	}

	public void delByOwner(final PlayerCuboid cuboid) {
		if (byOwner.containsKey(cuboid.getOwnerName())) {
			final Set<PlayerCuboid> set = byOwner.get(cuboid.getOwnerName());
			if (set.contains(cuboid)) {
				set.remove(cuboid);
			}
			if (set.isEmpty()) {
				byOwner.remove(cuboid.getOwnerName());
			}
		}
	}

	public PlayerCuboid delTmp(final String ownerName) {
		if (tmpCuboids.containsKey(ownerName)) {
			return tmpCuboids.remove(ownerName);
		}
		return null;
	}

	public void delByChunks(final PlayerCuboid cuboid) {
		for (final ChunkKey k : cuboid.getChunks()) {
			if (byChunks.containsKey(k)) {
				final Set<PlayerCuboid> set = byChunks.get(k);
				if (set.contains(cuboid)) {
					set.remove(cuboid);
				}
				if (set.isEmpty()) {
					byChunks.remove(k);
				}
			}
		}
	}

	public void delByWorld(final String worldName) {
		if (byWorld.containsKey(worldName)) {
			byWorld.remove(worldName);
		}
	}

	public PlayerCuboid getPriorByLoc(final Location loc) {
		return getPrior(getAllByLoc(loc));
	}

	public PlayerCuboid getPrior(final Set<PlayerCuboid> cuboids) {
		if (cuboids == null) {
			return null;
		} else {
			// Switch on number of cuboids at this location
			switch (cuboids.size()) {
				case 0:
					return null;
				case 1: // We're done
					return cuboids.iterator().next();
				case 2: // It's not too complicated
					final Iterator<PlayerCuboid> it = cuboids.iterator();
					final PlayerCuboid c1 = it.next();
					final PlayerCuboid c2 = it.next();
					switch (Integer.compare(c1.getPriority(), c2.getPriority())) {
					// Check priority
						case -1:
							return c2;
						case 1:
							return c1;
						case 0:
							switch (Long.compare(c1.getTotalSize(), c2.getTotalSize())) {
							// Check size
								case -1:
									return c2;
								case 1:
									return c1;
								case 0:
									// Wtf ! Well, we should always return the same one.
									// So let's return in alphabetic order
									if (c1.getCuboidName().compareTo(c2.getCuboidName()) < 0) {
										// This can't return 0, so it's < or >
										return c1;
									}
							}
					}
				default: // Let's compare them all in O(n) time
					final int maxPriority = 0; // "current" max priority in cuboids Set
					final TreeMap<Long, PlayerCuboid> sizeMap = new TreeMap<Long, PlayerCuboid>(); // TotalSize ; Cuboid
					for (final PlayerCuboid c : cuboids) {
						if (c.getPriority() > maxPriority) {
							// Higher priority spotted, all previous cuboids are less interesting
							sizeMap.clear();
							sizeMap.put(c.getTotalSize(), c);
						} else if (c.getPriority() == maxPriority) {
							sizeMap.put(c.getTotalSize(), c);
						}
					}
					// TreeMap is already sortedby size, take the first one
					return sizeMap.get(sizeMap.firstKey());
			}
		}
	}

	public Set<PlayerCuboid> getAllByLoc(final Location loc) {
		final ChunkKey k = new ChunkKey(loc);
		if (!byChunks.containsKey(k)) {
			return null;
		} else {
			final Set<PlayerCuboid> chunks = new HashSet<PlayerCuboid>();
			chunks.addAll(byChunks.get(k));
			final Iterator<PlayerCuboid> it = chunks.iterator();
			while (it.hasNext()) {
				if (!it.next().contains(loc)) {
					it.remove();
				}
			}
			return chunks.isEmpty() ? null : chunks;
		}
	}

	public PlayerCuboid getByName(final String cuboidName) {
		return byName.get(cuboidName);
	}

	public Set<PlayerCuboid> getByOwner(final String ownerName) {
		return byOwner.get(ownerName);
	}

	public PlayerCuboid getTmp(final String ownerName) {
		return tmpCuboids.get(ownerName);
	}

	public WorldCuboid getByWorld(final World world) {
		return byWorld.get(world.getName());
	}
}
