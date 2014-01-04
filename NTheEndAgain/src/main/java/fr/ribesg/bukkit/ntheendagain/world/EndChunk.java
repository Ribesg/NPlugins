/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - EndChunk.java                *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.world.EndChunk           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.world;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public EndChunk(final EndChunks container, final int x, final int z, final String world) {
		this.container = container;
		coords = new ChunkCoord(x, z, world);
		hasToBeRegen = false;
		isProtected = false;
		containsCrystal = false;
		crystals = null;
		savedDragons = 0;
	}

	public EndChunk(final EndChunks container, final Chunk bukkitChunk) {
		this.container = container;
		coords = new ChunkCoord(bukkitChunk);
		hasToBeRegen = false;
		isProtected = false;
		containsCrystal = false;
		searchCrystals(bukkitChunk);
		savedDragons = 0;
	}

	public boolean hasToBeRegen() {
		return !isProtected && hasToBeRegen;
	}

	public void setToBeRegen(final boolean value) {
		hasToBeRegen = !isProtected && value;
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
			crystals = new HashSet<>();
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

	public int getSavedDragons() {
		return savedDragons;
	}

	public void incrementSavedDragons() {
		savedDragons++;
		container.incrementTotalSavedDragons();
	}

	public void resetSavedDragons() {
		container.decrementTotalSavedDragons(savedDragons);
		savedDragons = 0;
	}

	public void store(final ConfigurationSection parent) {
		final ConfigurationSection chunkSection = parent.createSection(coords.toString());
		chunkSection.set("hasToBeRegen", hasToBeRegen());
		chunkSection.set("isProtected", isProtected());
		chunkSection.set("containsCrystal", containsCrystal());
		final List<String> locations = new ArrayList<>();
		if (crystals != null) {
			for (final Location loc : crystals) {
				locations.add(NLocation.toString(loc));
			}
			chunkSection.set("crystals", locations);
		}
		chunkSection.set("savedDragons", getSavedDragons());
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
		return coords;
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
