/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Altars.java                *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.Altars           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Altars {

	private final NEnchantingEgg plugin;

	private final Map<String, Set<Altar>> perWorld;
	private final Map<ChunkCoord, Altar>  perChunk;

	public Altars(final NEnchantingEgg plugin) {
		this.plugin = plugin;

		perWorld = new HashMap<>();
		perChunk = new HashMap<>();
	}

	public void onEnable() {
		plugin.entering(getClass(), "onEnable");

		for (final Altar altar : getAltars()) {
			altar.setState(AltarState.INACTIVE);
		}

		plugin.exiting(getClass(), "onEnable");
	}

	public void onDisable() {
		plugin.entering(getClass(), "onDisable");

		for (final Altar altar : getAltars()) {
			altar.hardResetToInactive(false);
		}

		plugin.exiting(getClass(), "onDisable");
	}

	public Set<Altar> getAltars() {
		return new HashSet<>(perChunk.values());
	}

	public boolean canAdd(final Altar altar, final double minDistance) {
		plugin.entering(getClass(), "canAdd");

		boolean result = true;
		final NLocation l = altar.getCenterLocation();
		plugin.debug("Trying to add Altar at location " + l.toString());

		if (perWorld.containsKey(l.getWorldName())) {
			final double minDistanceSquared = minDistance * minDistance;
			if (plugin.isDebugEnabled()) {
				plugin.debug("There are already altars in this world");
				plugin.debug("Required minimal distance: " + minDistance + " (squared: " + minDistanceSquared + ")");
			}
			final Set<Altar> set = perWorld.get(l.getWorldName());
			for (final Altar other : set) {
				final double distanceSquared = l.distance2DSquared(other.getCenterLocation());
				if (plugin.isDebugEnabled()) {
					plugin.debug("Distance (squared) with " + other.getCenterLocation() + ": " + distanceSquared);
				}
				if (distanceSquared < minDistanceSquared) {
					plugin.debug("Too close, can't add");
					result = false;
					break;
				}
			}
		}

		plugin.exiting(getClass(), "canAdd");
		return result;
	}

	public void add(final Altar altar) {
		plugin.entering(getClass(), "add");

		final World w = altar.getCenterLocation().getWorld();
		final Set<Altar> set;
		if (perWorld.containsKey(w.getName())) {
			set = perWorld.get(w.getName());
		} else {
			set = new HashSet<>();
		}
		set.add(altar);
		perWorld.put(w.getName(), set);
		for (final ChunkCoord c : altar.getChunks()) {
			perChunk.put(c, altar);
		}

		plugin.exiting(getClass(), "add");
	}

	public void remove(final Altar altar) {
		plugin.entering(getClass(), "remove");

		final World w = altar.getCenterLocation().getWorld();
		if (perWorld.containsKey(w.getName())) {
			final Set<Altar> set = perWorld.get(w.getName());
			set.remove(altar);
			if (set.size() == 0) {
				perWorld.remove(w.getName());
			}
		}
		for (final ChunkCoord c : altar.getChunks()) {
			perChunk.remove(c);
		}

		plugin.exiting(getClass(), "remove");
	}

	public Altar get(final ChunkCoord coord) {
		return perChunk.get(coord);
	}

	public void time(final String worldName, final MinecraftTime currentTime) {
		plugin.entering(getClass(), "time");

		if (perWorld.containsKey(worldName)) {
			if (currentTime == MinecraftTime.NIGHT) {
				for (final Altar a : perWorld.get(worldName)) {
					if (a.getState() == AltarState.INACTIVE) {
						plugin.debug("Activating altar at location " + a.getCenterLocation());
						plugin.getInactiveToActiveTransition().doTransition(a);
					}
				}
			} else {
				for (final Altar a : perWorld.get(worldName)) {
					if (a.getState() == AltarState.ACTIVE) {
						plugin.debug("Hard-reseting altar at location " + a.getCenterLocation());
						a.hardResetToInactive(false);
					} else if (a.getState() == AltarState.LOCKED) {
						plugin.debug("Unlocking altar at location " + a.getCenterLocation());
						a.setState(AltarState.INACTIVE);
					}
				}
			}
		}

		plugin.exiting(getClass(), "time");
	}

	public void chunkLoad(final ChunkCoord c) {
		plugin.entering(getClass(), "chunkLoad");

		if (perChunk.containsKey(c)) {
			plugin.debug("Chunk contains an Altar");
			final World world = Bukkit.getWorld(c.getWorldName());
			final Altar altar = perChunk.get(c);
			if (altar.getState() == AltarState.INACTIVE && MinecraftTime.isNightTime(world.getTime())) {
				plugin.debug("Activating altar at location " + altar.getCenterLocation());
				plugin.getInactiveToActiveTransition().doTransition(altar);
			} else if (altar.getState() != AltarState.INACTIVE && MinecraftTime.isNightTime(world.getTime())) {
				plugin.debug("Hard-reseting altar at location " + altar.getCenterLocation());
				altar.hardResetToInactive(false);
			}
		}

		plugin.exiting(getClass(), "chunkLoad");
	}
}
