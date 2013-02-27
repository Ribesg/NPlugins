package fr.ribesg.bukkit.ntheendagain.world;

import lombok.Getter;

import org.bukkit.Chunk;

public class ChunkCoord {

    @Getter private final int    x, z;
    @Getter private final String worldName;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (worldName == null ? 0 : worldName.hashCode());
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
        } else if (!worldName.equals(other.worldName)) {
            return false;
        }
        return true;
    }
}
