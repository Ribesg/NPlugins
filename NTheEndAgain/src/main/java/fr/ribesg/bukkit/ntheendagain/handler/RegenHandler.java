/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - RegenHandler.java            *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.handler.RegenHandler     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.handler;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.task.SlowSoftRegeneratorTaskHandler;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Ribesg
 */
public class RegenHandler {

	/**
	 * The End spawn Location is always [100;50;0]
	 */
	private static final int END_SPAWN_CHUNK_X = 100 >> 4;
	/**
	 * The End spawn Location is always [100;50;0]
	 */
	private static final int END_SPAWN_CHUNK_Z = 0;

	private final EndWorldHandler worldHandler;
	private final NTheEndAgain    plugin;

	public RegenHandler(final EndWorldHandler worldHandler) {
		this.worldHandler = worldHandler;
		this.plugin = worldHandler.getPlugin();
	}

	public void regen() {
		plugin.entering(getClass(), "regen");

		regen(worldHandler.getConfig().getRegenMethod());

		plugin.exiting(getClass(), "regen");
	}

	public void regen(final int type) {
		plugin.entering(getClass(), "regen(int)");

		plugin.debug("Kicking players out of the world/server...");
		kickPlayers();

		plugin.debug("Schedule regen task in " + EndWorldHandler.KICK_TO_REGEN_DELAY + " ticks");
		Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

			@Override
			public void run() {
				plugin.entering(getClass(), "run", "task from regen(int)");

				switch (type) {
					case 0:
						plugin.debug("Hard regen...");
						hardRegen(false);
						break;
					case 1:
						plugin.debug("Soft regen...");
						softRegen(false);
						break;
					case 2:
						plugin.debug("Crystal regen...");
						crystalRegen();
						break;
					default:
						break;
				}

				plugin.exiting(getClass(), "run", "task from regen(int)");
			}
		}, EndWorldHandler.KICK_TO_REGEN_DELAY);

		plugin.exiting(getClass(), "regen(int)");
	}

	public void hardRegenOnStop() {
		plugin.entering(getClass(), "hardRegenOnStop");

		hardRegen(true);

		plugin.exiting(getClass(), "hardRegenOnStop");
	}

	/*package*/ void regenThenRespawn() {
		plugin.entering(getClass(), "regenThenRespawn");

		regen();

		plugin.debug("Scheduling respawn task in " + EndWorldHandler.REGEN_TO_RESPAWN_DELAY + "ticks");
		Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

			@Override
			public void run() {
				plugin.entering(getClass(), "run", "task from regenThenRespawn");

				worldHandler.getRespawnHandler().respawnNoRegen();

				plugin.exiting(getClass(), "run", "task from regenThenRespawn");
			}
		}, EndWorldHandler.REGEN_TO_RESPAWN_DELAY);

		plugin.exiting(getClass(), "regenThenRespawn");
	}

	private void hardRegen(final boolean pluginDisabled) {
		plugin.entering(getClass(), "hardRegen");

		final NTheEndAgain plugin = worldHandler.getPlugin();
		final World endWorld = worldHandler.getEndWorld();
		final EndChunks chunks = worldHandler.getChunks();

		final String prefix = "[REGEN " + endWorld.getName() + "] ";
		plugin.info(prefix + "Regenerating End world...");

		plugin.debug("Kicking players out of the world/server...");
		kickPlayers();

		plugin.debug("Calling softRegen to set all chunks to toBeRegen...");
		softRegen(pluginDisabled);

		final long totalChunks = chunks.size();
		long i = 0, regen = 0;
		long lastTime = System.currentTimeMillis();

		plugin.debug("Starting regeneration...");
		for (final EndChunk c : chunks) {
			if (System.currentTimeMillis() - lastTime > 500) {
				plugin.info(prefix + regen + " chunks regenerated (" + (i * 100 / totalChunks) + "% done)");
				lastTime = System.currentTimeMillis();
			}
			if (c.hasToBeRegen()) {
				c.cleanCrystalLocations();
				c.resetSavedDragons();
				for (final Entity e : endWorld.getChunkAt(c.getX(), c.getZ()).getEntities()) {
					if (e.getType() == EntityType.ENDER_DRAGON) {
						worldHandler.getDragons().remove(e.getUniqueId());
						worldHandler.getLoadedDragons().remove(e.getUniqueId());
					}
					e.remove();
				}
				endWorld.regenerateChunk(c.getX(), c.getZ());
				c.setToBeRegen(false);
				regen++;
			}
			i++;
		}
		plugin.info(prefix + "Done.");

		plugin.exiting(getClass(), "hardRegen");
	}

	private void softRegen(final boolean pluginDisabled) {
		plugin.entering(getClass(), "softRegen");

		plugin.debug("Calling softRegen on chunks...");
		worldHandler.getChunks().softRegen();

		/*
		 * Instantly regen the spawn chunk to prevent NPE when an Entity
		 * tries to teleport here and we regen the chunk on Chunk Load.
		 */
		plugin.debug("Regenerating spawn chunks...");
		worldHandler.getEndWorld().getChunkAt(END_SPAWN_CHUNK_X, END_SPAWN_CHUNK_Z).load(true);

		if (!pluginDisabled) {
			// Launch Slow Soft Regen task
			plugin.debug("Not disabling plugin, launch slow regen task...");
			worldHandler.setSlowSoftRegeneratorTaskHandler(new SlowSoftRegeneratorTaskHandler(worldHandler));
			worldHandler.getSlowSoftRegeneratorTaskHandler().run();
		}

		plugin.exiting(getClass(), "softRegen");
	}

	private void crystalRegen() {
		plugin.entering(getClass(), "crystalRegen");

		worldHandler.getChunks().crystalRegen();

		plugin.exiting(getClass(), "crystalRegen");
	}

	private void kickPlayers() {
		plugin.entering(getClass(), "kickPlayers");

		final Config config = worldHandler.getConfig();
		final NTheEndAgain plugin = worldHandler.getPlugin();
		final World endWorld = worldHandler.getEndWorld();

		switch (config.getRegenAction()) {
			case 0:
				plugin.debug("Kicking players...");
				final String[] lines = plugin.getMessages().get(MessageId.theEndAgain_worldRegenerating);
				final StringBuilder messageBuilder = new StringBuilder(lines[0]);
				for (int i = 1; i < lines.length; i++) {
					messageBuilder.append('\n');
					messageBuilder.append(lines[i]);
				}
				final String message = messageBuilder.toString();
				for (final Player p : endWorld.getPlayers()) {
					p.kickPlayer(message);
				}
			case 1:
				plugin.debug("Teleporting players...");
				final World world = Bukkit.getWorlds().get(0);
				final WorldNode worldNode = plugin.getCore().getWorldNode();
				final Location spawnLoc;
				if (worldNode == null) {
					spawnLoc = world.getSpawnLocation();
				} else {
					spawnLoc = worldNode.getWorldSpawnLocation(world.getName());
				}
				for (final Player p : endWorld.getPlayers()) {
					p.teleport(spawnLoc);
					plugin.sendMessage(p, MessageId.theEndAgain_worldRegenerating);
				}
			default:
				// Not possible.
				break;
		}

		plugin.exiting(getClass(), "kickPlayers");
	}
}
