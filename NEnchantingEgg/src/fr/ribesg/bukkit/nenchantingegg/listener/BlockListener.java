package fr.ribesg.bukkit.nenchantingegg.listener;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BlockListener implements Listener {

    private final NEnchantingEgg plugin;

    public BlockListener(final NEnchantingEgg instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityExplode(final EntityExplodeEvent event) {
        final Set<Chunk> chunks = new HashSet<Chunk>();
        Chunk c;
        for (final Block b : event.blockList()) {
            c = b.getLocation().getChunk();
            if (!chunks.contains(c)) {
                chunks.add(c);
            }
        }

        final Set<Altar> altars = new HashSet<Altar>();
        ChunkCoord coord;
        Altar altar;
        for (final Chunk chunk : chunks) {
            coord = new ChunkCoord(chunk);
            altar = plugin.getAltars().get(coord);
            if (altar != null) {
                altars.add(altar);
            }
        }

        final Iterator<Block> it = event.blockList().iterator();
        Block b;
        while (it.hasNext()) {
            b = it.next();
            for (final Altar a : altars) {
                if (a.isAltarXYZ(b.getX() - a.getCenterLocation().getBlockX(), b.getY() - a.getCenterLocation().getBlockY(), b.getZ() - a.getCenterLocation().getBlockZ()
                                )) {
                    it.remove();
                }
            }
        }
    }
}
