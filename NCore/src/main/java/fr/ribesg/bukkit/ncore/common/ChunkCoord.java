/***************************************************************************
 * Project file:    NPlugins - NCore - ChunkCoord.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.common.ChunkCoord               *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Basically stores a String and 2 integers:
 * World name and 2D chunk coordinates.
 */
public class ChunkCoord {

	private static final String SEPARATOR = "##";

	private final int x, z;
	private final String worldName;

	public ChunkCoord(final int x, final int z, final String worldName) {
		this.x = x;
		this.z = z;
		this.worldName = worldName;
	}

	public ChunkCoord(final Chunk bukkitChunk) {
		x = bukkitChunk.getX();
		z = bukkitChunk.getZ();
		worldName = bukkitChunk.getWorld().getName();
	}

	public ChunkCoord(final NLocation loc) {
		x = loc.getBlockX() >> 4;
		z = loc.getBlockZ() >> 4;
		worldName = loc.getWorldName();
	}

	@Override
	public String toString() {
		return worldName + SEPARATOR + x + SEPARATOR + z;
	}

	/**
	 * Yeah it's a deserialization method.
	 */
	public static ChunkCoord fromString(final String string) {
		final String[] split = string.split(SEPARATOR);
		return new ChunkCoord(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[0]);
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public String getWorldName() {
		return worldName;
	}

	public World getBukkitWorld() {
		return Bukkit.getWorld(getWorldName());
	}

	public Chunk getBukkitChunk() {
		final World world = getBukkitWorld();
		if (world != null) {
			return world.getChunkAt(getX(), getZ());
		} else {
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (worldName == null ? 0 : worldName.toLowerCase().hashCode());
		result = prime * result + x;
		result = prime * result + z;
		return result;
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
		final ChunkCoord other = (ChunkCoord) obj;
		if (x != other.x) {
			return false;
		}
		if (z != other.z) {
			return false;
		}
		if (worldName == null) {
			if (other.worldName != null) {
				return false;
			}
		} else if (!worldName.equalsIgnoreCase(other.worldName)) {
			return false;
		}
		return true;
	}
}
