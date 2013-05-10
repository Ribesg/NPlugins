package fr.ribesg.bukkit.nenchantingegg.altar;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;

public class Altar {

    private final static int              MAX_RADIUS = 5;

    @Getter private final Location        centerLocation;
    @Getter private final Set<ChunkCoord> chunks;

    @Getter @Setter private AltarState    state;

    public Altar(final Location loc) {
        centerLocation = loc;

        chunks = new HashSet<ChunkCoord>();

        final int x = loc.getBlockX();
        final int z = loc.getBlockZ();

        chunks.add(new ChunkCoord((x + MAX_RADIUS) / 16, (z + MAX_RADIUS) / 16, loc.getWorld().getName()));
        chunks.add(new ChunkCoord((x + MAX_RADIUS) / 16, (z - MAX_RADIUS) / 16, loc.getWorld().getName()));
        chunks.add(new ChunkCoord((x - MAX_RADIUS) / 16, (z + MAX_RADIUS) / 16, loc.getWorld().getName()));
        chunks.add(new ChunkCoord((x - MAX_RADIUS) / 16, (z - MAX_RADIUS) / 16, loc.getWorld().getName()));

        for (final ChunkCoord chunk : chunks) {
            NEnchantingEgg.getInstance().getAltarMap().put(chunk, this);
        }

        setState(AltarState.INACTIVE);
    }
}
