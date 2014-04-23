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
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

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
		plugin.entering(getClass(), "respawn");
		final boolean result;

		if (worldHandler.getConfig().getRegenType() == 1) {
			plugin.debug("Regen before respan");
			worldHandler.getRegenHandler().regenThenRespawn();
			result = true;
		} else {
			plugin.debug("Respawn now!");
			result = respawnDragons();
		}

		plugin.exiting(getClass(), "respawn", Boolean.toString(result));
		return result;
	}

	public void respawnLater() {
		plugin.entering(getClass(), "respawnLater");

		Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

			@Override
			public void run() {
				respawn();
			}
		}, worldHandler.getConfig().getRandomRespawnTimer() * 20L);

		plugin.entering(getClass(), "respawnLater");
	}

	/*package*/ void respawnNoRegen() {
		plugin.entering(getClass(), "respawnNoRegen");

		respawnDragons();

		plugin.exiting(getClass(), "respawnNoRegen");
	}

	private boolean respawnDragons() {
		plugin.entering(getClass(), "respawnDragons");

		final int nbAlive = worldHandler.getNumberOfAliveEnderDragons();
		final int respawnNumber = worldHandler.getConfig().getRespawnNumber();
		int respawning = 0;
		boolean result = true;
		for (int i = nbAlive; i < respawnNumber; i++) {
			result &= respawnDragon();
			respawning++;
		}
		plugin.debug("Called respawnDragon " + respawning + " times");
		if (respawning > 1) {
			worldHandler.getPlugin().broadcastMessage(MessageId.theEndAgain_respawnedX, Integer.toString(respawning), worldHandler.getEndWorld().getName());
		} else if (respawning == 1) {
			worldHandler.getPlugin().broadcastMessage(MessageId.theEndAgain_respawned1, worldHandler.getEndWorld().getName());
		}

		plugin.exiting(getClass(), "respawnDragons", Boolean.toString(result));
		return result;
	}

	private boolean respawnDragon() {
		plugin.entering(getClass(), "respawnDragon");
		boolean result = false;

		final World world = worldHandler.getEndWorld();
		final EndChunks chunks = worldHandler.getChunks();
		// Create a random location near the center
		final int x = RANDOM.nextInt(81) - 40; // [-40;40]
		final int y = 100 + RANDOM.nextInt(21); // [100;120]
		final int z = RANDOM.nextInt(81) - 40; // [-40;40]
		final Location loc = new Location(world, x, y, z);

		plugin.debug("Will spawn at " + NLocation.toString(loc));

		boolean regenerated = false;

		for (int a = (x >> 4) - 1; a < (x >> 4) + 1; a++) {
			for (int b = (z >> 4) - 1; b < (z >> 4) + 1; b++) {
				final Chunk chunk = world.getChunkAt(a, b);
				final EndChunk endChunk = chunks.getChunk(world.getName(), chunk.getX(), chunk.getZ());
				if (endChunk != null && endChunk.hasToBeRegen()) {
					plugin.debug("Chunk at coords " + a + ";" + b + " needs to be regen first");
					world.regenerateChunk(chunk.getX(), chunk.getZ());
					endChunk.setToBeRegen(false);
					regenerated = true;
				}
			}
		}
		if (regenerated) {
			plugin.debug("At least one chunk has been regen, respawn later");
			Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

				@Override
				public void run() {
					if (world.spawnEntity(loc, EntityType.ENDER_DRAGON) == null) {
						retryRespawn(loc);
					}
					worldHandler.getConfig().setNextRespawnTaskTime(System.nanoTime() + worldHandler.getConfig().getRandomRespawnTimer() * 1_000_000_000);
				}
			}, EndWorldHandler.REGEN_TO_RESPAWN_DELAY);
		} else {
			plugin.debug("No chunk has been regen, respawn now");
			if (world.spawnEntity(loc, EntityType.ENDER_DRAGON) == null) {
				retryRespawn(loc);
			}
			result = true;
		}

		plugin.exiting(getClass(), "respawnDragon");
		return result;
	}

	private void retryRespawn(final Location loc) {
		new BukkitRunnable() {

			@Override
			public void run() {
				plugin.info("Failed to spawn an EnderDragon at Location " + NLocation.toString(loc) + ", trying again in 1 second");
				if (loc.getWorld().spawnEntity(loc, EntityType.ENDER_DRAGON) != null) {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}

}
