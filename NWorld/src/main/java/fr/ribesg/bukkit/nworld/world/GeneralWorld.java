/***************************************************************************
 * Project file:    NPlugins - NWorld - GeneralWorld.java                  *
 * Full Class name: fr.ribesg.bukkit.nworld.world.GeneralWorld             *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.world;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.WorldUtils;
import fr.ribesg.bukkit.nworld.NWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

/** @author Ribesg */
public abstract class GeneralWorld implements Comparable<GeneralWorld> {

	public enum WorldType {
		STOCK,
		STOCK_NETHER,
		STOCK_END,
		ADDITIONAL,
		ADDITIONAL_SUB_NETHER,
		ADDITIONAL_SUB_END,
		UNKNOWN;

		public static boolean isStock(final GeneralWorld world) {
			switch (world.getType()) {
				case STOCK:
				case STOCK_NETHER:
				case STOCK_END:
					return true;
				default:
					return false;
			}
		}

		public static boolean isAdditional(final GeneralWorld world) {
			switch (world.getType()) {
				case ADDITIONAL:
				case ADDITIONAL_SUB_NETHER:
				case ADDITIONAL_SUB_END:
					return true;
				default:
					return false;
			}
		}
	}

	protected final NWorld plugin;

	protected String    worldName;
	protected NLocation spawnLocation;
	protected String    requiredPermission;
	protected boolean   enabled;
	protected boolean   hidden;
	protected WorldType type;

	public GeneralWorld(final NWorld instance,
	                    final String worldName,
	                    final NLocation spawnLocation,
	                    final String requiredPermission,
	                    final boolean enabled,
	                    final boolean hidden) {
		this.plugin = instance;
		this.worldName = worldName;
		this.spawnLocation = spawnLocation;
		this.requiredPermission = requiredPermission;
		this.enabled = enabled;
		this.hidden = hidden;
		this.type = WorldType.UNKNOWN;
		if (!plugin.getWorlds().containsKey(worldName)) {
			plugin.getWorlds().put(worldName, this);
		}
	}

	/**
	 * Constructor for sub-classes, to be allowed to have the time to compute
	 * the name of AdditionalSubWorlds based on parentWorld's name
	 * <p/>
	 * Don't forget to initialize fields after the call to super()
	 */
	protected GeneralWorld(final NWorld instance) {
		this.plugin = instance;
	}

	public World create() {
		return create(org.bukkit.WorldType.NORMAL);
	}

	public World create(org.bukkit.WorldType type) {
		if (type == null) {
			type = org.bukkit.WorldType.NORMAL;
		}
		try {
			if (WorldUtils.isLoaded(getWorldName()) != null || WorldUtils.exists(getWorldName()) != null) {
				throw new IllegalStateException("World already exists");
			}
		} catch (IOException e) {
			return null;
		}
		final WorldCreator creator = new WorldCreator(getWorldName());
		creator.seed(getSeed());
		creator.type(type);
		switch (getType()) {
			case ADDITIONAL:
			case STOCK:
				creator.environment(World.Environment.NORMAL);
				break;
			case ADDITIONAL_SUB_NETHER:
			case STOCK_NETHER:
				creator.environment(World.Environment.NETHER);
				break;
			case ADDITIONAL_SUB_END:
			case STOCK_END:
				creator.environment(World.Environment.THE_END);
				break;
			default:
				throw new IllegalStateException("Incorrect world type: " + getType());
		}
		final World result = creator.createWorld();
		setSpawnLocation(result.getSpawnLocation());
		return result;
	}

	public boolean isLoaded() {
		return WorldUtils.isLoaded(getWorldName()) != null;
	}

	public World load() {
		try {
			if (isLoaded()) {
				throw new IllegalStateException("World already loaded");
			} else if (WorldUtils.exists(getWorldName()) == null) {
				throw new IllegalStateException("World does not exists");
			}
		} catch (IOException e) {
			return null;
		}
		final WorldCreator creator = new WorldCreator(getWorldName());
		final World result = creator.createWorld();
		if (getSpawnLocation() == null) {
			setSpawnLocation(result.getSpawnLocation());
		}
		this.setEnabled(true);
		return result;
	}

	public void unload() {
		if (!isLoaded()) {
			throw new IllegalStateException("World not loaded");
		}

		// Teleport players to another world
		final Location spawn = plugin.getWorlds().get(Bukkit.getWorlds().get(0).getName()).getSpawnLocation().toBukkitLocation();
		for (final Player p : Bukkit.getWorld(getWorldName()).getPlayers()) {
			plugin.sendMessage(p, MessageId.world_teleportedBecauseOfWorldUnload);
			p.teleport(spawn);
		}

		// Unload the world
		Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.unloadWorld(getWorldName(), true);
			}
		}, 1L);

		this.setEnabled(false);
	}

	public boolean exists() {
		try {
			return WorldUtils.exists(getWorldName()) != null;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/** @return Null if the world doesn't exist / isn't loaded */
	public World getBukkitWorld() {
		return Bukkit.getWorld(getWorldName());
	}

	public abstract long getSeed();

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(final String worldName) {
		this.worldName = worldName;
	}

	public NLocation getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(final NLocation spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public void setSpawnLocation(final Location spawnLocation) {
		setSpawnLocation(new NLocation(spawnLocation));
	}

	public String getRequiredPermission() {
		return requiredPermission;
	}

	public void setRequiredPermission(final String requiredPermission) {
		this.requiredPermission = requiredPermission;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	public WorldType getType() {
		return type;
	}

	public void setType(final WorldType type) {
		this.type = type;
	}

	@Override
	public int compareTo(final GeneralWorld o) {
		return worldName.compareTo(o.worldName);
	}

	public boolean isMalformed() {
		return false;
	}
}
