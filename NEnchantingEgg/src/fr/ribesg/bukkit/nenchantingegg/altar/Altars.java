package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Altars {

	private final NEnchantingEgg plugin;

	private final Map<String, Set<Altar>> perWorld;
	private final Map<ChunkCoord, Altar>  perChunk;

	public Altars(NEnchantingEgg plugin) {
		this.plugin = plugin;

		perWorld = new HashMap<>();
		perChunk = new HashMap<>();
	}

	public void onEnable() {
		for (Altar altar : getAltars()) {
			altar.setState(AltarState.INACTIVE);
		}
	}

	public void onDisable() {
		for (Altar altar : getAltars()) {
			altar.hardResetToInactive();
		}
	}

	public Set<Altar> getAltars() {
		return new HashSet<>(perChunk.values());
	}

	public boolean canAdd(final Altar altar, final double minDistance) {
		final NLocation l = altar.getCenterLocation();
		if (perWorld.containsKey(l.getWorldName())) {
			final double minDistanceSquared = minDistance * minDistance;
			final Set<Altar> set = perWorld.get(l.getWorldName());
			for (final Altar other : set) {
				if (l.distanceSquared(other.getCenterLocation()) < minDistanceSquared) {
					return false;
				}
			}
		}
		return true;
	}

	public void add(final Altar altar) {
		final World w = altar.getCenterLocation().getWorld();
		Set<Altar> set;
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
	}

	public void remove(final Altar altar) {
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
	}

	public Altar get(final ChunkCoord coord) {
		return perChunk.get(coord);
	}

	public void timeChange(final String worldName, final MinecraftTime fromMinecraftTime, final MinecraftTime toMinecraftTime) {
		if (perWorld.containsKey(worldName)) {
			if (fromMinecraftTime == MinecraftTime.DAY && toMinecraftTime == MinecraftTime.NIGHT) {
				for (final Altar a : perWorld.get(worldName)) {
					if (a.getState() == AltarState.INACTIVE) {
						plugin.getInactiveToActiveTransition().doTransition(a);
					}
				}
			} else if (fromMinecraftTime == MinecraftTime.NIGHT && toMinecraftTime == MinecraftTime.DAY) {
				for (final Altar a : perWorld.get(worldName)) {
					if (a.getState() == AltarState.ACTIVE) {
						a.hardResetToInactive();
						Location loc = a.getCenterLocation().toBukkitLocation();
						if (loc != null) {
							loc.getWorld().createExplosion(loc, 0f, false);
						}
					} else if (a.getState() == AltarState.LOCKED) {
						a.setState(AltarState.INACTIVE);
					}
				}
			}
		}
	}

	public void chunkLoad(final ChunkCoord c) {
		if (perChunk.containsKey(c)) {
			final World world = Bukkit.getWorld(c.getWorldName());
			final Altar altar = perChunk.get(c);
			if (altar.getState() == AltarState.INACTIVE && MinecraftTime.isNightTime(world.getTime())) {
				plugin.getInactiveToActiveTransition().doTransition(altar);
			} else if (altar.getState() != AltarState.INACTIVE && MinecraftTime.isNightTime(world.getTime())) {
				altar.hardResetToInactive();
			}
		}
	}
}
