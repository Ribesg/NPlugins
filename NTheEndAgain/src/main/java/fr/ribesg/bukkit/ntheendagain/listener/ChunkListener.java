/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - ChunkListener.java           *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.listener.ChunkListener   *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.listener;

import fr.ribesg.bukkit.ncore.event.theendagain.ChunkRegenEvent;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World.Environment;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles Chunk Load and Unload events
 *
 * @author Ribesg
 */
public class ChunkListener implements Listener {

    private final NTheEndAgain plugin;

    public ChunkListener(final NTheEndAgain instance) {
        this.plugin = instance;
    }

    /**
     * Handles Chunk regen at load, with still-alive EnderDragons consideration,
     * and EnderDragon spawn / load on Chunk Load.
     *
     * @param event a Chunk Load Event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEndChunkLoad(final ChunkLoadEvent event) {
        if (event.getWorld().getEnvironment() == Environment.THE_END) {
            final String worldName = event.getWorld().getName();
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
            if (handler != null) {
                final EndChunks chunks = handler.getChunks();
                final Chunk chunk = event.getChunk();
                EndChunk endChunk = chunks.getChunk(worldName, chunk.getX(), chunk.getZ());

                /*
                 * Chunk has to be regen
                 *   - Forget every dragons in it
                 *   - Regenerate the chunk
                 *   - Schedule a refresh
                 */
                if (endChunk != null && endChunk.hasToBeRegen()) {
                    final ChunkRegenEvent regenEvent = new ChunkRegenEvent(chunk);
                    Bukkit.getPluginManager().callEvent(regenEvent);
                    if (!regenEvent.isCancelled()) {
                        for (final Entity e : chunk.getEntities()) {
                            if (e.getType() == EntityType.ENDER_DRAGON) {
                                final EnderDragon ed = (EnderDragon)e;
                                if (handler.getDragons().containsKey(ed.getUniqueId())) {
                                    handler.getDragons().remove(ed.getUniqueId());
                                    handler.getLoadedDragons().remove(ed.getUniqueId());
                                }
                            }
                            e.remove();
                        }
                        endChunk.cleanCrystalLocations();
                        final int x = endChunk.getX(), z = endChunk.getZ();
                        event.getWorld().regenerateChunk(x, z);
                        Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

                            @Override
                            public void run() {
                                event.getWorld().refreshChunk(x, z);
                            }
                        }, 100L);
                    }
                    endChunk.setToBeRegen(false);
                }

                /*
                 * Chunk does not need to be regen
                 *   - Check if we knew this chunk, if not, now we do
                 *   - Check for new EnderDragons
                 *   - Re-add known Dragons to Loaded set
                 */
                else {
                    if (endChunk == null) {
                        endChunk = chunks.addChunk(chunk);
                    }
                    for (final Entity e : chunk.getEntities()) {
                        if (e.getType() == EntityType.ENDER_DRAGON) {
                            final EnderDragon ed = (EnderDragon)e;
                            if (!handler.getDragons().containsKey(ed.getUniqueId())) {
                                ed.setMaxHealth(handler.getConfig().getEdHealth());
                                ed.setHealth(ed.getMaxHealth());
                                handler.getDragons().put(ed.getUniqueId(), new HashMap<String, Double>());
                            }
                            handler.getLoadedDragons().add(ed.getUniqueId());
                        } else if (e.getType() == EntityType.ENDER_CRYSTAL) {
                            endChunk.addCrystalLocation(e);
                        }
                    }
                }
                endChunk.resetSavedDragons();
            }
        }
    }

    /**
     * Remove the unloaded EnderDragons from the loaded set
     *
     * @param event a Chunk Unload Event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEndChunkUnload(final ChunkUnloadEvent event) {
        if (event.getWorld().getEnvironment() == Environment.THE_END) {
            final String worldName = event.getWorld().getName();
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
            if (handler != null) {
                EndChunk chunk = handler.getChunks().getChunk(event.getChunk());
                if (chunk == null) {
                    chunk = handler.getChunks().addChunk(event.getChunk());
                }
                for (final Entity e : event.getChunk().getEntities()) {
                    if (e.getType() == EntityType.ENDER_DRAGON) {
                        final EnderDragon ed = (EnderDragon)e;
                        handler.getLoadedDragons().remove(ed.getUniqueId());
                        chunk.incrementSavedDragons();
                    }
                }
            }
        }
    }
}
