package fr.ribesg.bukkit.ntheendagain.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.ncore.utils.Utils;

public class EndChunk {

    @Getter private final ChunkCoord coords;
    private boolean                  hasToBeRegen;
    private boolean                  isProtected;
    private boolean                  containsCrystal;
    private Set<Location>            crystals;

    public EndChunk(final int x, final int z, final String world) {
        coords = new ChunkCoord(x, z, world);
        hasToBeRegen = false;
        isProtected = false;
        containsCrystal = false;
        crystals = null;
    }

    public EndChunk(final Chunk bukkitChunk) {
        coords = new ChunkCoord(bukkitChunk);
        hasToBeRegen = false;
        isProtected = false;
        containsCrystal = false;
        searchCrystals(bukkitChunk);
    }

    public boolean hasToBeRegen() {
        return isProtected ? false : hasToBeRegen;
    }

    public void setToBeRegen(final boolean value) {
        hasToBeRegen = isProtected ? false : value;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(final boolean value) {
        isProtected = value;
    }

    public boolean containsCrystal() {
        return containsCrystal;
    }

    public void setContainsCrystal(final boolean value) {
        containsCrystal = value;
    }

    public int getX() {
        return coords.getX();
    }

    public int getZ() {
        return coords.getZ();
    }

    public String getWorldName() {
        return coords.getWorldName();
    }

    public void addCrystalLocation(final Entity e) {
        if (crystals == null) {
            crystals = new HashSet<Location>();
        }
        crystals.add(e.getLocation());
        containsCrystal = true;
    }

    public void cleanCrystalLocations() {
        crystals = null;
        containsCrystal = false;
    }

    public void searchCrystals() {
        final World w = Bukkit.getWorld(getWorldName());
        if (w != null) {
            final Chunk c = w.getChunkAt(getX(), getZ());
            searchCrystals(c);
        }
    }

    public void searchCrystals(final Chunk bukkitChunk) {
        cleanCrystalLocations();
        for (final Entity e : bukkitChunk.getEntities()) {
            if (e.getType() == EntityType.ENDER_CRYSTAL) {
                addCrystalLocation(e);
            }
        }
    }

    public Set<Location> getCrystalLocations() {
        return crystals;
    }

    public void store(final ConfigurationSection parent) {
        final ConfigurationSection chunkSection = parent.createSection(coords.toString());
        chunkSection.set("hasToBeRegen", hasToBeRegen());
        chunkSection.set("isProtected", isProtected());
        chunkSection.set("containsCrystal", containsCrystal());
        final List<String> locations = new ArrayList<String>();
        if (crystals != null) {
            for (final Location loc : crystals) {
                locations.add(Utils.toString(loc));
            }
            chunkSection.set("crystals", locations);
        }
    }

    public static EndChunk rebuild(final ConfigurationSection chunkSection) {
        final String chunkCoordString = chunkSection.getName();
        final ChunkCoord coords = ChunkCoord.fromString(chunkCoordString);
        final boolean hasToBeRegen = chunkSection.getBoolean("hasToBeRegen");
        final boolean isProtected = chunkSection.getBoolean("isProtected");
        final boolean containsCrystal = chunkSection.getBoolean("containsCrystal");
        final EndChunk chunk = new EndChunk(coords.getX(), coords.getZ(), coords.getWorldName());
        chunk.setProtected(isProtected);
        chunk.setToBeRegen(hasToBeRegen);
        chunk.setContainsCrystal(containsCrystal);

        if (containsCrystal) {
            final List<String> locations = chunkSection.getStringList("crystals");
            final Set<Location> crystals = new HashSet<Location>();
            for (final String loc : locations) {
                crystals.add(Utils.toLocation(loc));
            }
            chunk.crystals = crystals;
        }
        return chunk;
    }

    @Override
    public int hashCode() {
        return coords.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EndChunk other = (EndChunk) obj;
        if (coords == null) {
            if (other.coords != null) {
                return false;
            }
        } else if (!coords.equals(other.coords)) {
            return false;
        }
        return true;
    }
}
