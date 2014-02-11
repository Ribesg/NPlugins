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
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.task.SlowSoftRegeneratorTaskHandler;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import org.bukkit.Bukkit;
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

	public RegenHandler(final EndWorldHandler worldHandler) {
		this.worldHandler = worldHandler;
	}

	public void regen() {
		regen(worldHandler.getConfig().getRegenMethod());
	}

	public void regen(final int type) {
		kickPlayers();
		Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

			@Override
			public void run() {
				switch (type) {
					case 0:
						hardRegen(false);
						break;
					case 1:
						softRegen(false);
						break;
					case 2:
						crystalRegen();
						break;
					default:
						break;
				}
			}
		}, EndWorldHandler.KICK_TO_REGEN_DELAY);
	}

	public void hardRegenOnStop() {
		hardRegen(true);
	}

	/*package*/ void regenThenRespawn() {
		regen();
		Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

			@Override
			public void run() {
				worldHandler.getRespawnHandler().respawnNoRegen();
			}
		}, EndWorldHandler.REGEN_TO_RESPAWN_DELAY);
	}

	private void hardRegen(final boolean pluginDisabled) {
		final NTheEndAgain plugin = worldHandler.getPlugin();
		final World endWorld = worldHandler.getEndWorld();
		final EndChunks chunks = worldHandler.getChunks();

		plugin.getLogger().info("Regenerating End world \"" + endWorld.getName() + "\"...");
		kickPlayers();
		softRegen(pluginDisabled);

		final long totalChunks = chunks.size();
		long i = 0, regen = 0;
		long lastTime = System.currentTimeMillis();
		for (final EndChunk c : chunks) {
			if (System.currentTimeMillis() - lastTime > 500) {
				plugin.getLogger().info(regen + " chunks regenerated (" + (i * 100 / totalChunks) + "% done)");
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
		plugin.getLogger().info("Done.");
	}

	private void softRegen(final boolean pluginDisabled) {
		worldHandler.getChunks().softRegen();

		/*
		 * Instantly regen the spawn chunk to prevent NPE when an Entity
		 * tries to teleport here and we regen the chunk on Chunk Load.
		 */
		worldHandler.getEndWorld().getChunkAt(END_SPAWN_CHUNK_X, END_SPAWN_CHUNK_Z).load(true);

		if (!pluginDisabled) {
			// Launch Slow Soft Regen task
			worldHandler.setSlowSoftRegeneratorTaskHandler(new SlowSoftRegeneratorTaskHandler(worldHandler));
			worldHandler.getSlowSoftRegeneratorTaskHandler().run();
		}
	}

	private void crystalRegen() {
		worldHandler.getChunks().crystalRegen();
	}

	private void kickPlayers() {
		final Config config = worldHandler.getConfig();
		final NTheEndAgain plugin = worldHandler.getPlugin();
		final World endWorld = worldHandler.getEndWorld();

		switch (config.getRegenAction()) {
			case 0:
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
				for (final Player p : endWorld.getPlayers()) {
					// TODO Future: Use spawn point defined by NWorld, when NWorld will do it
					//              and if NWorld is enabled of course
					p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
					plugin.sendMessage(p, MessageId.theEndAgain_worldRegenerating);
				}
			default:
				// Not possible.
				break;
		}
	}
}
