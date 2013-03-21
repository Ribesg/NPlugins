package fr.ribesg.bukkit.ncuboid.beans;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunkKey {
    
    @Getter @Setter private int   x, z;
    @Getter @Setter private World world;
    
    public ChunkKey(final World world, final int x, final int z) {
        setX(x);
        setZ(z);
        setWorld(world);
    }
    
    public ChunkKey(final Location loc) {
        final Chunk bukkitChunk = loc.getChunk();
        setX(bukkitChunk.getX());
        setZ(bukkitChunk.getZ());
        setWorld(bukkitChunk.getWorld());
    }
    
    public ChunkKey(final Chunk bukkitChunk) {
        setX(bukkitChunk.getX());
        setZ(bukkitChunk.getZ());
        setWorld(bukkitChunk.getWorld());
    }
    
    public Chunk getBukkitChunk() {
        return getWorld().getChunkAt(getX(), getZ());
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (world == null ? 0 : world.getName().hashCode());
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
        final ChunkKey other = (ChunkKey) obj;
        if (world == null) {
            if (other.world != null) {
                return false;
            }
        } else if (other.world == null) {
            return false;
        } else if (!world.getName().equals(other.world.getName())) {
            return false;
        }
        if (x != other.x) {
            return false;
        }
        if (z != other.z) {
            return false;
        }
        return true;
    }
}
