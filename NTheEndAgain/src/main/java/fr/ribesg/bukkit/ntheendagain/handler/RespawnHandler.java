/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - RespawnHandler.java          *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.handler.RespawnHandler   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.handler;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Ribesg
 */
public class RespawnHandler {

    private static final Random RANDOM = new Random();

    private final EndWorldHandler worldHandler;
    private final NTheEndAgain    plugin;

    public RespawnHandler(final EndWorldHandler worldHandler) {
        this.worldHandler = worldHandler;
        this.plugin = worldHandler.getPlugin();
    }

    public boolean respawn() {
        this.plugin.entering(this.getClass(), "respawn");
        final boolean result;

        if (this.worldHandler.getConfig().getRegenType() == 1) {
            this.plugin.debug("Regen before respan");
            this.worldHandler.getRegenHandler().regenThenRespawn();
            result = true;
        } else {
            this.plugin.debug("Respawn now!");
            result = this.respawnDragons();
        }

        this.plugin.exiting(this.getClass(), "respawn", Boolean.toString(result));
        return result;
    }

    public void respawnLater() {
        this.plugin.entering(this.getClass(), "respawnLater");

        Bukkit.getScheduler().runTaskLater(this.worldHandler.getPlugin(), new BukkitRunnable() {

            @Override
            public void run() {
                fr.ribesg.bukkit.ntheendagain.handler.RespawnHandler.this.respawn();
            }
        }, this.worldHandler.getConfig().getRandomRespawnTimer() * 20L);

        this.plugin.entering(this.getClass(), "respawnLater");
    }

    /*package*/ void respawnNoRegen() {
        this.plugin.entering(this.getClass(), "respawnNoRegen");

        this.respawnDragons();

        this.plugin.exiting(this.getClass(), "respawnNoRegen");
    }

    private boolean respawnDragons() {
        this.plugin.entering(this.getClass(), "respawnDragons");

        final int nbAlive = this.worldHandler.getNumberOfAliveEnderDragons();
        final int respawnNumber = this.worldHandler.getConfig().getRespawnNumber();
        int respawning = 0;
        boolean result = true;
        for (int i = nbAlive; i < respawnNumber; i++) {
            result &= this.respawnDragon();
            respawning++;
        }
        this.plugin.debug("Called respawnDragon " + respawning + " times");
        if (respawning > 1) {
            this.worldHandler.getPlugin().broadcastMessage(MessageId.theEndAgain_respawnedX, Integer.toString(respawning), this.worldHandler.getEndWorld().getName());
        } else if (respawning == 1) {
            this.worldHandler.getPlugin().broadcastMessage(MessageId.theEndAgain_respawned1, this.worldHandler.getEndWorld().getName());
        }

        this.plugin.exiting(this.getClass(), "respawnDragons", Boolean.toString(result));
        return result;
    }

    private boolean respawnDragon() {
        this.plugin.entering(this.getClass(), "respawnDragon");
        boolean result = false;

        final World world = this.worldHandler.getEndWorld();
        final EndChunks chunks = this.worldHandler.getChunks();
        // Create a random location near the center
        final int x = RANDOM.nextInt(81) - 40; // [-40;40]
        final int y = 100 + RANDOM.nextInt(21); // [100;120]
        final int z = RANDOM.nextInt(81) - 40; // [-40;40]
        final Location loc = new Location(world, x, y, z);

        this.plugin.debug("Will spawn at " + NLocation.toString(loc));

        boolean regenerated = false;

        for (int a = (x >> 4) - 1; a < (x >> 4) + 1; a++) {
            for (int b = (z >> 4) - 1; b < (z >> 4) + 1; b++) {
                final Chunk chunk = world.getChunkAt(a, b);
                final EndChunk endChunk = chunks.getChunk(world.getName(), chunk.getX(), chunk.getZ());
                if (endChunk != null && endChunk.hasToBeRegen()) {
                    this.plugin.debug("Chunk at coords " + a + ';' + b + " needs to be regen first");
                    world.regenerateChunk(chunk.getX(), chunk.getZ());
                    endChunk.setToBeRegen(false);
                    regenerated = true;
                }
            }
        }
        if (regenerated) {
            this.plugin.debug("At least one chunk has been regen, respawn later");
            Bukkit.getScheduler().runTaskLater(this.worldHandler.getPlugin(), new BukkitRunnable() {

                @Override
                public void run() {
                    if (world.spawnEntity(loc, EntityType.ENDER_DRAGON) == null) {
                        fr.ribesg.bukkit.ntheendagain.handler.RespawnHandler.this.retryRespawn(loc);
                    }
                    fr.ribesg.bukkit.ntheendagain.handler.RespawnHandler.this.worldHandler.getConfig().setNextRespawnTaskTime(System.nanoTime() + fr.ribesg.bukkit.ntheendagain.handler.RespawnHandler.this.worldHandler.getConfig().getRandomRespawnTimer() * 1_000_000_000);
                }
            }, EndWorldHandler.REGEN_TO_RESPAWN_DELAY);
        } else {
            this.plugin.debug("No chunk has been regen, respawn now");
            if (world.spawnEntity(loc, EntityType.ENDER_DRAGON) == null) {
                this.retryRespawn(loc);
            }
            result = true;
        }

        this.plugin.exiting(this.getClass(), "respawnDragon");
        return result;
    }

    private void retryRespawn(final Location loc) {
        new BukkitRunnable() {

            @Override
            public void run() {
                fr.ribesg.bukkit.ntheendagain.handler.RespawnHandler.this.plugin.info("Failed to spawn an EnderDragon at Location " + NLocation.toString(loc) + ", trying again in 1 second");
                if (loc.getWorld().spawnEntity(loc, EntityType.ENDER_DRAGON) != null) {
                    this.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }
}
