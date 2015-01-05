/***************************************************************************
 * Project file:    NPlugins - NCuboid - RegionDb.java                     *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.RegionDb                *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.PlayerRegion.RegionState;
import fr.ribesg.bukkit.ncuboid.config.GroupConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegionDb implements Iterable<GeneralRegion> {

    private final NCuboid plugin;

    private final Map<String, PlayerRegion>          byName;    // RegionName ; Region
    private final Map<UUID, Set<PlayerRegion>>       byOwner;   // OwnerId ; Regions of this owner
    private final Map<UUID, PlayerRegion>            tmpRegions; // OwnerId ; Temporary Region (Owner's Selection)
    private final Map<ChunkCoord, Set<PlayerRegion>> byChunks;  // Chunk ; Regions in this chunk
    private final Map<String, WorldRegion>           byWorld;   // WorldName ; Region

    public RegionDb(final NCuboid instance) {
        this.byName = new HashMap<>();
        this.byOwner = new HashMap<>();
        this.tmpRegions = new HashMap<>();
        this.byChunks = new HashMap<>();
        this.byWorld = new HashMap<>();
        this.plugin = instance;
    }

    public NCuboid getPlugin() {
        return this.plugin;
    }

    // #################### //
    // ## ADDING REGIONS ## //
    // #################### //

    public void add(final PlayerRegion region) {
        this.addByName(region);
        this.addByOwner(region);
        this.addByChunks(region);
        if (this.plugin.getDynmapBridge().isInitialized()) {
            this.plugin.getDynmapBridge().handle(region);
        }
    }

    public void addByName(final PlayerRegion region) {
        this.byName.put(region.getRegionName(), region);
    }

    public void addByOwner(final PlayerRegion region) {
        final UUID id = region.getOwnerId();
        if (this.byOwner.containsKey(id)) {
            this.byOwner.get(id).add(region);
        } else {
            final Set<PlayerRegion> newSet = new HashSet<>();
            newSet.add(region);
            this.byOwner.put(id, newSet);
        }
    }

    public void addSelection(final PlayerRegion region) {
        this.tmpRegions.put(region.getOwnerId(), region);
    }

    public void addByChunks(final PlayerRegion region) {
        for (final ChunkCoord k : region.getChunks()) {
            Set<PlayerRegion> set = this.byChunks.get(k);
            if (set == null) {
                set = new HashSet<>(1);
                this.byChunks.put(k, set);
            }
            set.add(region);
        }
    }

    public void addByWorld(final WorldRegion region) {
        this.byWorld.put("world_" + region.getWorldName(), region);
    }

    // ###################### //
    // ## REMOVING REGIONS ## //
    // ###################### //

    public void remove(final PlayerRegion region) {
        this.removeByName(region);
        this.removeByOwner(region);
        this.removeByChunks(region);
        if (this.plugin.getDynmapBridge().isInitialized()) {
            this.plugin.getDynmapBridge().hide(region);
        }
    }

    public void removeByName(final PlayerRegion region) {
        if (this.byName.containsKey(region.getRegionName())) {
            this.byName.remove(region.getRegionName());
        }
    }

    public void removeByOwner(final PlayerRegion region) {
        final UUID id = region.getOwnerId();
        if (this.byOwner.containsKey(id)) {
            final Set<PlayerRegion> set = this.byOwner.get(id);
            if (set.contains(region)) {
                set.remove(region);
            }
            if (set.isEmpty()) {
                this.byOwner.remove(id);
            }
        }
    }

    public PlayerRegion removeSelection(final UUID id) {
        return this.tmpRegions.remove(id);
    }

    public void removeByChunks(final PlayerRegion region) {
        for (final ChunkCoord k : region.getChunks()) {
            if (this.byChunks.containsKey(k)) {
                final Set<PlayerRegion> set = this.byChunks.get(k);
                if (set.contains(region)) {
                    set.remove(region);
                }
                if (set.isEmpty()) {
                    this.byChunks.remove(k);
                }
            }
        }
    }

    public void removeByWorld(final String worldName) {
        this.byWorld.remove("world_" + worldName);
    }

    // ##################### //
    // ## GETTING REGIONS ## //
    // ##################### //

    public GeneralRegion getPriorByLocation(final Location loc) {
        return this.getPrior(this.getAllByLocation(loc));
    }

    public GeneralRegion getPrior(final SortedSet<GeneralRegion> regions) {
        if (regions == null) {
            return null;
        } else {
            return regions.first();
        }
    }

    public SortedSet<GeneralRegion> getAllByLocation(final Location loc) {
        return this.getAllByLocation(new NLocation(loc));
    }

    public SortedSet<GeneralRegion> getAllByLocation(final NLocation loc) {
        final ChunkCoord chunkKey = new ChunkCoord(loc);
        final SortedSet<GeneralRegion> regions = new TreeSet<>();
        if (this.byWorld.containsKey("world_" + loc.getWorldName())) {
            regions.add(this.byWorld.get("world_" + loc.getWorldName()));
        }
        if (!this.byChunks.containsKey(chunkKey)) {
            return regions.isEmpty() ? null : regions;
        } else {
            regions.addAll(this.byChunks.get(chunkKey));
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
        final GeneralRegion r = this.byName.get(regionName);
        if (r == null && regionName.startsWith("world_")) {
            return this.getByWorld(regionName.substring(6));
        } else {
            return r;
        }
    }

    public Set<PlayerRegion> getByOwner(final UUID id) {
        return this.byOwner.get(id);
    }

    public PlayerRegion getSelection(final UUID id) {
        return this.tmpRegions.get(id);
    }

    public WorldRegion getByWorld(final String worldName) {
        return this.byWorld.get("world_" + worldName);
    }

    public int size() {
        return this.byName.size() + this.byWorld.size();
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
            return this.result;
        }

        /**
         * Only available if OVERLAP result
         */
        public GeneralRegion getRegion() {
            return this.region;
        }

        public int getMaxValue() {
            return this.maxValue;
        }

        public long getValue() {
            return this.value;
        }
    }

    public CreationResult canCreate(final Player player) {
        final UUID id = player.getUniqueId();
        final GroupConfig config = this.plugin.getPluginConfig().getGroupConfig(player);
        final PlayerRegion r = this.getSelection(id);

        if (r == null || r.getState() != RegionState.TMPSTATE2) {
            return new CreationResult(CreationResultEnum.DENIED_NO_SELECTION);
        }

        // Amount of regions
        if (config.getMaxRegionNb() != -1) {
            final int nbRegion = this.getByOwner(id) == null ? 0 : this.getByOwner(id).size();
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
        final WorldRegion worldRegion = this.getByWorld(r.getWorldName());
        if (worldRegion != null && !worldRegion.isUser(player)) {
            return new CreationResult(CreationResultEnum.DENIED_OVERLAP, worldRegion);
        }
        final Set<PlayerRegion> potentiallyOverlappingRegions = new HashSet<>();
        for (final ChunkCoord c : r.getChunks()) {
            if (this.byChunks.containsKey(c)) {
                for (final PlayerRegion pr : this.byChunks.get(c)) {
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
        return this.byName.values().iterator();
    }

    public Iterator<WorldRegion> worldRegionsIterator() {
        return this.byWorld.values().iterator();
    }

    @Override
    public Iterator<GeneralRegion> iterator() {
        return new Iterator<GeneralRegion>() {

            final Iterator<PlayerRegion> playerRegionsIterator = RegionDb.this.playerRegionsIterator();
            final Iterator<WorldRegion> worldRegionsIterator = RegionDb.this.worldRegionsIterator();

            @Override
            public boolean hasNext() {
                return this.playerRegionsIterator.hasNext() || this.worldRegionsIterator.hasNext();
            }

            @Override
            public GeneralRegion next() {
                if (this.playerRegionsIterator.hasNext()) {
                    return this.playerRegionsIterator.next();
                } else if (this.worldRegionsIterator.hasNext()) {
                    return this.worldRegionsIterator.next();
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
