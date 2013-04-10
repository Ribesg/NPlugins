package fr.ribesg.bukkit.ntheendagain.world;

import lombok.Getter;

import org.bukkit.Chunk;

public class EndChunk {

    private final static String      SEPARATOR = ";";

    @Getter private final ChunkCoord coords;
    private boolean                  hasToBeRegen;
    private boolean                  isProtected;

    public EndChunk(final int x, final int z, final String world) {
        coords = new ChunkCoord(x, z, world);
        hasToBeRegen = false;
        isProtected = false;
    }

    public EndChunk(final Chunk bukkitChunk) {
        coords = new ChunkCoord(bukkitChunk);
        hasToBeRegen = false;
        isProtected = false;
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

    public int getX() {
        return coords.getX();
    }

    public int getZ() {
        return coords.getZ();
    }

    public String getWorldName() {
        return coords.getWorldName();
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append(getWorldName());
        s.append(SEPARATOR);
        s.append(getX());
        s.append(SEPARATOR);
        s.append(getZ());
        s.append(SEPARATOR);
        s.append(isProtected() ? "yes" : "no");
        s.append(SEPARATOR);
        s.append(hasToBeRegen() ? "yes" : "no");
        return s.toString();
    }

    public static EndChunk fromString(final String stringRepresentation) {
        final String[] split = stringRepresentation.split(SEPARATOR);
        if (split.length != 5) {
            return null;
        } else {
            final String world = split[0];
            final int x = Integer.parseInt(split[1]);
            final int z = Integer.parseInt(split[2]);
            boolean isProtected;
            final boolean hasToBeRegen;
            switch (split[3]) {
                case "yes":
                    isProtected = true;
                    break;
                case "no":
                    isProtected = false;
                    break;
                default:
                    return null;
            }
            switch (split[4]) {
                case "yes":
                    hasToBeRegen = true;
                    break;
                case "no":
                    hasToBeRegen = false;
                    break;
                default:
                    return null;
            }
            final EndChunk res = new EndChunk(x, z, world);
            res.setProtected(isProtected);
            res.setToBeRegen(hasToBeRegen);
            return res;
        }
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
