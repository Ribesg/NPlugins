/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - EndChunk.java                *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.world.EndChunk           *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.world;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EndChunk {

    private final EndChunks container;

    private final ChunkCoord    coords;
    private       boolean       hasToBeRegen;
    private       boolean       isProtected;
    private       boolean       containsCrystal;
    private       Set<Location> crystals;
    private       int           savedDragons;

    public EndChunk(final EndChunks container, final ChunkCoord coords) {
        this(container, coords.getX(), coords.getZ(), coords.getWorldName());
    }

    public EndChunk(final EndChunks container, final Chunk bukkitChunk) {
        this(container, bukkitChunk.getX(), bukkitChunk.getZ(), bukkitChunk.getWorld().getName());
    }

    public EndChunk(final EndChunks container, final int x, final int z, final String world) {
        this.container = container;
        this.coords = new ChunkCoord(x, z, world);
        this.hasToBeRegen = false;
        this.isProtected = false;
        this.containsCrystal = false;
        this.crystals = null;
        this.savedDragons = 0;
    }

    public boolean hasToBeRegen() {
        return !this.isProtected && this.hasToBeRegen;
    }

    public void setToBeRegen(final boolean value) {
        this.hasToBeRegen = !this.isProtected && value;
    }

    public boolean isProtected() {
        return this.isProtected;
    }

    public void setProtected(final boolean value) {
        this.isProtected = value;
    }

    public boolean containsCrystal() {
        return this.containsCrystal;
    }

    public void setContainsCrystal(final boolean value) {
        this.containsCrystal = value;
    }

    public int getX() {
        return this.coords.getX();
    }

    public int getZ() {
        return this.coords.getZ();
    }

    public String getWorldName() {
        return this.coords.getWorldName();
    }

    public void addCrystalLocation(final Entity e) {
        if (this.crystals == null) {
            this.crystals = new HashSet<>();
        }
        this.crystals.add(e.getLocation());
        this.containsCrystal = true;
    }

    public void cleanCrystalLocations() {
        this.crystals = null;
        this.containsCrystal = false;
    }

    public void searchCrystals() {
        final World w = Bukkit.getWorld(this.getWorldName());
        if (w != null) {
            final Chunk c = w.getChunkAt(this.getX(), this.getZ());
            this.searchCrystals(c);
        }
    }

    public void searchCrystals(final Chunk bukkitChunk) {
        this.cleanCrystalLocations();
        for (final Entity e : bukkitChunk.getEntities()) {
            if (e.getType() == EntityType.ENDER_CRYSTAL) {
                this.addCrystalLocation(e);
            }
        }
    }

    public Set<Location> getCrystalLocations() {
        return this.crystals;
    }

    public int getSavedDragons() {
        return this.savedDragons;
    }

    public void incrementSavedDragons() {
        this.savedDragons++;
        this.container.incrementTotalSavedDragons();
    }

    public void resetSavedDragons() {
        this.container.decrementTotalSavedDragons(this.savedDragons);
        this.savedDragons = 0;
    }

    public void store(final ConfigurationSection parent) {
        final ConfigurationSection chunkSection = parent.createSection(this.coords.toString());
        chunkSection.set("hasToBeRegen", this.hasToBeRegen());
        chunkSection.set("isProtected", this.isProtected);
        chunkSection.set("containsCrystal", this.containsCrystal());
        final List<String> locations = new ArrayList<>();
        if (this.crystals != null) {
            for (final Location loc : this.crystals) {
                locations.add(NLocation.toString(loc));
            }
            chunkSection.set("crystals", locations);
        }
        chunkSection.set("savedDragons", this.savedDragons);
    }

    public static EndChunk rebuild(final EndChunks container, final ConfigurationSection chunkSection) {
        final String chunkCoordString = chunkSection.getName();
        final ChunkCoord coords = ChunkCoord.fromString(chunkCoordString);
        final boolean hasToBeRegen = chunkSection.getBoolean("hasToBeRegen", false);
        final boolean isProtected = chunkSection.getBoolean("isProtected", false);
        final boolean containsCrystal = chunkSection.getBoolean("containsCrystal", false);
        final EndChunk chunk = new EndChunk(container, coords.getX(), coords.getZ(), coords.getWorldName());
        chunk.setProtected(isProtected);
        chunk.setToBeRegen(hasToBeRegen);
        chunk.setContainsCrystal(containsCrystal);

        if (containsCrystal) {
            final List<String> locations = chunkSection.getStringList("crystals");
            final Set<Location> crystals = new HashSet<>();
            for (final String loc : locations) {
                crystals.add(NLocation.toLocation(loc));
            }
            chunk.crystals = crystals;
        }

        chunk.savedDragons = chunkSection.getInt("savedDragons", 0);
        return chunk;
    }

    public ChunkCoord getCoords() {
        return this.coords;
    }

    @Override
    public int hashCode() {
        return this.coords.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final EndChunk other = (EndChunk)obj;
        if (this.coords == null) {
            if (other.coords != null) {
                return false;
            }
        } else if (!this.coords.equals(other.coords)) {
            return false;
        }
        return true;
    }
}
