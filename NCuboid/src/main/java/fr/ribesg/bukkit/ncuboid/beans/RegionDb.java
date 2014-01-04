/***************************************************************************
 * Project file:    NPlugins - NCuboid - RegionDb.java                     *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.RegionDb                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.config.GroupConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class RegionDb implements Iterable<GeneralRegion> {

	private final NCuboid plugin;

	private final Map<String, PlayerRegion>          byName;    // RegionName ; Region
	private final Map<String, Set<PlayerRegion>>     byOwner;   // OwnerName ; Regions of this owner
	private final Map<String, PlayerRegion>          tmpRegions; // OwnerName ; Temporary Region (Owner's Selection)
	private final Map<ChunkCoord, Set<PlayerRegion>> byChunks;  // Chunk ; Regions in this chunk
	private final Map<String, WorldRegion>           byWorld;   // WorldName ; Region

	public RegionDb(final NCuboid instance) {
		byName = new HashMap<>();
		byOwner = new HashMap<>();
		tmpRegions = new HashMap<>();
		byChunks = new HashMap<>();
		byWorld = new HashMap<>();
		plugin = instance;
	}

	// #################### //
	// ## ADDING REGIONS ## //
	// #################### //

	public void add(final PlayerRegion region) {
		addByName(region);
		addByOwner(region);
		addByChunks(region);
		if (this.plugin.getDynmapBridge().isInitialized()) {
			this.plugin.getDynmapBridge().handle(region);
		}
	}

	public void addByName(final PlayerRegion region) {
		byName.put(region.getRegionName(), region);
	}

	public void addByOwner(final PlayerRegion region) {
		if (byOwner.containsKey(region.getOwnerName())) {
			byOwner.get(region.getOwnerName()).add(region);
		} else {
			final Set<PlayerRegion> newSet = new HashSet<>();
			newSet.add(region);
			byOwner.put(region.getOwnerName(), newSet);
		}
	}

	public void addSelection(final PlayerRegion region) {
		tmpRegions.put(region.getOwnerName(), region);
	}

	public void addByChunks(final PlayerRegion region) {
		for (final ChunkCoord k : region.getChunks()) {
			Set<PlayerRegion> set = byChunks.get(k);
			if (set == null) {
				set = new HashSet<>(1);
				byChunks.put(k, set);
			}
			set.add(region);
		}
	}

	public void addByWorld(final WorldRegion region) {
		byWorld.put("world_" + region.getWorldName(), region);
	}

	// ###################### //
	// ## REMOVING REGIONS ## //
	// ###################### //

	public void remove(final PlayerRegion region) {
		removeByName(region);
		removeByOwner(region);
		removeByChunks(region);
		if (this.plugin.getDynmapBridge().isInitialized()) {
			this.plugin.getDynmapBridge().hide(region);
		}
	}

	public void removeByName(final PlayerRegion region) {
		if (byName.containsKey(region.getRegionName())) {
			byName.remove(region.getRegionName());
		}
	}

	public void removeByOwner(final PlayerRegion region) {
		if (byOwner.containsKey(region.getOwnerName())) {
			final Set<PlayerRegion> set = byOwner.get(region.getOwnerName());
			if (set.contains(region)) {
				set.remove(region);
			}
			if (set.isEmpty()) {
				byOwner.remove(region.getOwnerName());
			}
		}
	}

	public PlayerRegion removeSelection(final String ownerName) {
		if (tmpRegions.containsKey(ownerName)) {
			return tmpRegions.remove(ownerName);
		}
		return null;
	}

	public void removeByChunks(final PlayerRegion region) {
		for (final ChunkCoord k : region.getChunks()) {
			if (byChunks.containsKey(k)) {
				final Set<PlayerRegion> set = byChunks.get(k);
				if (set.contains(region)) {
					set.remove(region);
				}
				if (set.isEmpty()) {
					byChunks.remove(k);
				}
			}
		}
	}

	public void removeByWorld(final String worldName) {
		byWorld.remove("world_" + worldName);
	}

	// ##################### //
	// ## GETTING REGIONS ## //
	// ##################### //

	public GeneralRegion getPriorByLocation(final Location loc) {
		return getPrior(getAllByLocation(loc));
	}

	public GeneralRegion getPrior(final Set<GeneralRegion> regions) {
		if (regions == null) {
			return null;
		} else {
			// Switch on number of regions at this location
			switch (regions.size()) {
				case 0:
					return null;
				case 1: // We're done
					return regions.iterator().next();
				case 2: // It's not too complicated
					final Iterator<GeneralRegion> it = regions.iterator();
					final GeneralRegion region1 = it.next();
					final GeneralRegion region2 = it.next();
					final int priorityCompare = Integer.compare(region1.getPriority(), region2.getPriority());
					// Check priority
					if (priorityCompare < 0) {
						return region2;
					} else if (priorityCompare > 0) {
						return region1;
					} else {
						final int sizeCompare = Long.compare(region1.getTotalSize(), region2.getTotalSize());
						if (sizeCompare < 0) {
							return region1;
						} else if (sizeCompare > 0) {
							return region2;
						} else {
							// Wtf ! Well, we should always return the same one.
							// So let's return in alphabetic order
							if (region1.getRegionName().compareTo(region2.getRegionName()) < 0) {
								return region1;
							} else { // This can't return 0 as names are unique, so it's < or >
								return region2;
							}
						}
					}
				default: // Let's compare them all in O(n) time
					int maxPriority = Integer.MIN_VALUE; // "current" max priority in regions Set
					final SortedMap<Long, GeneralRegion> sizeMap = new TreeMap<>(); // TotalSize ; Region
					for (final GeneralRegion region : regions) {
						if (region.getPriority() > maxPriority) {
							// Higher priority spotted, all previous regions are less interesting
							maxPriority = region.getPriority();
							sizeMap.clear();
							sizeMap.put(region.getTotalSize(), region);
						} else if (region.getPriority() == maxPriority) {
							sizeMap.put(region.getTotalSize(), region);
						}
					}
					// TreeMap is already sorted by size, take the first one
					return sizeMap.get(sizeMap.firstKey());
			}
		}
	}

	public Set<GeneralRegion> getAllByLocation(final Location loc) {
		return getAllByLocation(new NLocation(loc));
	}

	public Set<GeneralRegion> getAllByLocation(final NLocation loc) {
		final ChunkCoord chunkKey = new ChunkCoord(loc);
		final Set<GeneralRegion> regions = new HashSet<>();
		if (byWorld.containsKey("world_" + loc.getWorldName())) {
			regions.add(byWorld.get("world_" + loc.getWorldName()));
		}
		if (!byChunks.containsKey(chunkKey)) {
			return regions.isEmpty() ? null : regions;
		} else {
			regions.addAll(byChunks.get(chunkKey));
			final Iterator<GeneralRegion> it = regions.iterator();
			while (it.hasNext()) {
				if (!it.next().contains(loc)) {
					it.remove();
				}
			}
			return regions.isEmpty() ? null : regions;
		}
	}

	public GeneralRegion getByName(final String regionName) {
		final GeneralRegion r = byName.get(regionName);
		if (r == null && regionName.startsWith("world_")) {
			return getByWorld(regionName.substring(6));
		} else {
			return r;
		}
	}

	public Set<PlayerRegion> getByOwner(final String ownerName) {
		return byOwner.get(ownerName);
	}

	public PlayerRegion getSelection(final String ownerName) {
		return tmpRegions.get(ownerName);
	}

	public WorldRegion getByWorld(final String worldName) {
		return byWorld.get("world_" + worldName);
	}

	public int size() {
		return byName.size() + byWorld.size();
	}

	// ##################################### //
	// ## CHECKING REGION CREATION RIGHTS ## //
	// ##################################### //

	public enum CreationResultEnum {
		OK,
		DENIED_TOO_MUCH,
		DENIED_TOO_LONG,
		DENIED_TOO_BIG,
		DENIED_OVERLAP,
		DENIED_NO_SELECTION
	}

	public class CreationResult {

		private final CreationResultEnum result;
		private final GeneralRegion      region;
		private final int                maxValue;
		private final long               value;

		public CreationResult(final CreationResultEnum result) {
			this.result = result;
			this.region = null;
			this.maxValue = 0;
			this.value = 0;
		}

		public CreationResult(final CreationResultEnum result, final int maxValue, final long value) {
			this.result = result;
			this.region = null;
			this.maxValue = maxValue;
			this.value = value;
		}

		public CreationResult(final CreationResultEnum result, final GeneralRegion region) {
			this.result = result;
			this.region = region;
			this.maxValue = 0;
			this.value = 0;
		}

		public CreationResultEnum getResult() {
			return result;
		}

		/** Only available if OVERLAP result */
		public GeneralRegion getRegion() {
			return region;
		}

		public int getMaxValue() {
			return maxValue;
		}

		public long getValue() {
			return value;
		}
	}

	public CreationResult canCreate(final Player player) {
		final String playerName = player.getName();
		final GroupConfig config = plugin.getPluginConfig().getGroupConfig(player);
		final PlayerRegion r = getSelection(playerName);

		if (r == null || r.getState() != PlayerRegion.RegionState.TMPSTATE2) {
			return new CreationResult(CreationResultEnum.DENIED_NO_SELECTION);
		}

		// Amount of regions
		if (config.getMaxRegionNb() != -1) {
			final int nbRegion = getByOwner(playerName) == null ? 0 : getByOwner(playerName).size();
			if (nbRegion >= config.getMaxRegionNb()) {
				return new CreationResult(CreationResultEnum.DENIED_TOO_MUCH, config.getMaxRegionNb(), nbRegion);
			}
		}

		// Length of each dimension
		if (config.getMaxRegion1DSize() != -1) {
			if (r.getMaxLength() >= config.getMaxRegion1DSize()) {
				return new CreationResult(CreationResultEnum.DENIED_TOO_LONG, config.getMaxRegion1DSize(), r.getMaxLength());
			}
		}

		// Total size
		if (config.getMaxRegion3DSize() != -1) {
			if (r.getTotalSize() >= config.getMaxRegion3DSize()) {
				return new CreationResult(CreationResultEnum.DENIED_TOO_BIG, config.getMaxRegion3DSize(), r.getTotalSize());
			}
		}

		// Overlaping with other cuboids
		final WorldRegion worldRegion = getByWorld(r.getWorldName());
		if (worldRegion != null && !worldRegion.isUser(player)) {
			return new CreationResult(CreationResultEnum.DENIED_OVERLAP, worldRegion);
		}
		final Set<PlayerRegion> potentiallyOverlappingRegions = new HashSet<>();
		for (final ChunkCoord c : r.getChunks()) {
			if (byChunks.containsKey(c)) {
				for (final PlayerRegion pr : byChunks.get(c)) {
					potentiallyOverlappingRegions.add(pr);
				}
			}
		}
		for (final PlayerRegion pr : potentiallyOverlappingRegions) {
			if (r.overlaps(pr) && !pr.isAdmin(player)) {
				return new CreationResult(CreationResultEnum.DENIED_OVERLAP, pr);
			}
		}

		return new CreationResult(CreationResultEnum.OK);
	}

	// ############################ //
	// ## ITERATING OVER REGIONS ## //
	// ############################ //

	public Iterator<PlayerRegion> playerRegionsIterator() {
		return byName.values().iterator();
	}

	public Iterator<WorldRegion> worldRegionsIterator() {
		return byWorld.values().iterator();
	}

	@Override
	public Iterator<GeneralRegion> iterator() {
		return new Iterator<GeneralRegion>() {

			final Iterator<PlayerRegion> playerRegionsIterator = RegionDb.this.playerRegionsIterator();
			final Iterator<WorldRegion> worldRegionsIterator = RegionDb.this.worldRegionsIterator();

			@Override
			public boolean hasNext() {
				return playerRegionsIterator.hasNext() || worldRegionsIterator.hasNext();
			}

			@Override
			public GeneralRegion next() {
				if (playerRegionsIterator.hasNext()) {
					return playerRegionsIterator.next();
				} else if (worldRegionsIterator.hasNext()) {
					return worldRegionsIterator.next();
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
