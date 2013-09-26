package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class CuboidDb implements Iterable<GeneralCuboid> {

	private final NCuboid plugin;

	private final Map<String, PlayerCuboid>          byName;    // CuboidName ; Cuboid
	private final Map<String, Set<PlayerCuboid>>     byOwner;   // OwnerName ; Cuboids of this owner
	private final Map<String, PlayerCuboid>          tmpCuboids; // OwnerName ; Temporary Cuboid (Owner's Selection)
	private final Map<ChunkCoord, Set<PlayerCuboid>> byChunks;  // Chunk ; Cuboids in this chunk
	private final Map<String, WorldCuboid>           byWorld;   // WorldName ; Cuboid

	public CuboidDb(final NCuboid instance) {
		byName = new HashMap<>();
		byOwner = new HashMap<>();
		tmpCuboids = new HashMap<>();
		byChunks = new HashMap<>();
		byWorld = new HashMap<>();
		plugin = instance;
	}

	public void add(final PlayerCuboid cuboid) {
		addByName(cuboid);
		addByOwner(cuboid);
		addByChunks(cuboid);
		this.plugin.getDynmapBridge().handle(cuboid);
	}

	public void addByName(final PlayerCuboid cuboid) {
		byName.put(cuboid.getCuboidName(), cuboid);
	}

	public void addByOwner(final PlayerCuboid cuboid) {
		if (byOwner.containsKey(cuboid.getOwnerName())) {
			byOwner.get(cuboid.getOwnerName()).add(cuboid);
		} else {
			final Set<PlayerCuboid> newSet = new HashSet<>();
			newSet.add(cuboid);
			byOwner.put(cuboid.getOwnerName(), newSet);
		}
	}

	public void addSelection(final PlayerCuboid cuboid) {
		tmpCuboids.put(cuboid.getOwnerName(), cuboid);
	}

	public void addByChunks(final PlayerCuboid cuboid) {
		for (final ChunkCoord k : cuboid.getChunks()) {
			if (byChunks.containsKey(k)) {
				byChunks.get(k).add(cuboid);
			} else {
				final Set<PlayerCuboid> newSet = new HashSet<>();
				newSet.add(cuboid);
				byChunks.put(k, newSet);
			}
		}
	}

	public void addByWorld(final WorldCuboid c) {
		byWorld.put(c.getWorldName(), c);
	}

	public void remove(final PlayerCuboid cuboid) {
		removeByName(cuboid);
		removeByOwner(cuboid);
		removeByChunks(cuboid);
		this.plugin.getDynmapBridge().hide(cuboid);
	}

	public void removeByName(final PlayerCuboid cuboid) {
		if (byName.containsKey(cuboid.getCuboidName())) {
			byName.remove(cuboid.getCuboidName());
		}
	}

	public void removeByOwner(final PlayerCuboid cuboid) {
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

	public PlayerCuboid removeSelection(final String ownerName) {
		if (tmpCuboids.containsKey(ownerName)) {
			return tmpCuboids.remove(ownerName);
		}
		return null;
	}

	public void removeByChunks(final PlayerCuboid cuboid) {
		for (final ChunkCoord k : cuboid.getChunks()) {
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

	public void removeByWorld(final String worldName) {
		if (byWorld.containsKey(worldName)) {
			byWorld.remove(worldName);
		}
	}

	public GeneralCuboid getPriorByLocation(final Location loc) {
		return getPrior(getAllByLocation(loc));
	}

	public GeneralCuboid getPrior(final Set<GeneralCuboid> cuboids) {
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
					final Iterator<GeneralCuboid> it = cuboids.iterator();
					final GeneralCuboid c1 = it.next();
					final GeneralCuboid c2 = it.next();
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
										return c1;
									} else { // This can't return 0 as names are unique, so it's < or >
										return c2;
									}
							}
					}
				default: // Let's compare them all in O(n) time
					final int maxPriority = 0; // "current" max priority in cuboids Set
					final SortedMap<Long, GeneralCuboid> sizeMap = new TreeMap<>(); // TotalSize ; Cuboid
					for (final GeneralCuboid c : cuboids) {
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

	public Set<GeneralCuboid> getAllByLocation(final Location loc) {
		return getAllByLocation(new NLocation(loc));
	}

	public Set<GeneralCuboid> getAllByLocation(final NLocation loc) {
		final ChunkCoord chunkKey = new ChunkCoord(loc);
		final Set<GeneralCuboid> cuboids = new HashSet<>();
		if (byWorld.containsKey(loc.getWorldName())) {
			cuboids.add(byWorld.get(loc.getWorldName()));
		}
		if (!byChunks.containsKey(chunkKey)) {
			return cuboids.isEmpty() ? null : cuboids;
		} else {
			cuboids.addAll(byChunks.get(chunkKey));
			final Iterator<GeneralCuboid> it = cuboids.iterator();
			while (it.hasNext()) {
				if (!it.next().contains(loc)) {
					it.remove();
				}
			}
			return cuboids.isEmpty() ? null : cuboids;
		}
	}

	public PlayerCuboid getByName(final String cuboidName) {
		return byName.get(cuboidName);
	}

	public Set<PlayerCuboid> getByOwner(final String ownerName) {
		return byOwner.get(ownerName);
	}

	public PlayerCuboid getSelection(final String ownerName) {
		return tmpCuboids.get(ownerName);
	}

	public WorldCuboid getByWorld(final String worldName) {
		return byWorld.get(worldName);
	}

	public Iterator<PlayerCuboid> playerCuboidIterator() {
		return byName.values().iterator();
	}

	public Iterator<WorldCuboid> worldCuboidIterator() {
		return byWorld.values().iterator();
	}

	@Override
	public Iterator<GeneralCuboid> iterator() {
		return new Iterator<GeneralCuboid>() {

			final Iterator<PlayerCuboid> playerCuboidIterator = CuboidDb.this.playerCuboidIterator();
			final Iterator<WorldCuboid> worldCuboidIterator = CuboidDb.this.worldCuboidIterator();

			@Override
			public boolean hasNext() {
				return playerCuboidIterator.hasNext() || worldCuboidIterator.hasNext();
			}

			@Override
			public GeneralCuboid next() {
				if (playerCuboidIterator.hasNext()) {
					return playerCuboidIterator.next();
				} else if (worldCuboidIterator.hasNext()) {
					return worldCuboidIterator.next();
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
