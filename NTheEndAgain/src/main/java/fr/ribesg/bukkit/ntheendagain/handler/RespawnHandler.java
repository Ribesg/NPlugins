/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - RespawnHandler.java          *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.handler.RespawnHandler   *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.handler;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/** @author Ribesg */
public class RespawnHandler {

	private static final Random rand = new Random();

	private final EndWorldHandler worldHandler;

	public RespawnHandler(EndWorldHandler worldHandler) {
		this.worldHandler = worldHandler;
	}

	public void respawn() {
		if (worldHandler.getConfig().getRegenType() == 1) {
			worldHandler.getRegenHandler().regenThenRespawn();
		} else {
			respawnDragons();
		}
	}

	public void respawnLater() {
		Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

			@Override
			public void run() {
				respawn();
			}
		}, worldHandler.getConfig().getRandomRespawnTimer() * 20L);
	}

	/*package*/ void respawnNoRegen() {
		respawnDragons();
	}

	private void respawnDragons() {
		final int nbAlive = worldHandler.getNumberOfAliveEnderDragons();
		final int respawnNumber = worldHandler.getConfig().getRespawnNumber();
		int respawning = 0;
		for (int i = nbAlive; i < respawnNumber; i++) {
			respawnDragon();
			respawning++;
		}
		if (respawning > 1) {
			worldHandler.getPlugin()
			            .broadcastMessage(MessageId.theEndAgain_respawnedX,
			                              Integer.toString(respawning),
			                              worldHandler.getEndWorld().getName());
		} else if (respawning == 1) {
			worldHandler.getPlugin().broadcastMessage(MessageId.theEndAgain_respawned1, worldHandler.getEndWorld().getName());
		}
	}

	private void respawnDragon() {
		final World world = worldHandler.getEndWorld();
		final EndChunks chunks = worldHandler.getChunks();
		// Create a random location near the center
		final int x = rand.nextInt(81) - 40; // [-40;40]
		final int y = 100 + rand.nextInt(21); // [100;120]
		final int z = rand.nextInt(81) - 40; // [-40;40]
		final Location loc = new Location(world, x, y, z);

		boolean regenerated = false;

		for (int a = (x >> 4) - 1; a < (x >> 4) + 1; a++) {
			for (int b = (z >> 4) - 1; b < (z >> 4) + 1; b++) {
				final Chunk chunk = world.getChunkAt(a, b);
				final EndChunk endChunk = chunks.getChunk(world.getName(), chunk.getX(), chunk.getZ());
				if (endChunk != null && endChunk.hasToBeRegen()) {
					world.regenerateChunk(chunk.getX(), chunk.getZ());
					endChunk.setToBeRegen(false);
					regenerated = true;
				}
			}
		}
		if (regenerated) {
			Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

				@Override
				public void run() {
					world.spawnEntity(loc, EntityType.ENDER_DRAGON);
				}
			}, EndWorldHandler.REGEN_TO_RESPAWN_DELAY);
		} else {
			world.spawnEntity(loc, EntityType.ENDER_DRAGON);
		}
	}

}
