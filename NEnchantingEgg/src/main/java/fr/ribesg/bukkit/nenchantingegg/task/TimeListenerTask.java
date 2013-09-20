package fr.ribesg.bukkit.nenchantingegg.task;

import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * This task listen to time changes and notices Altars
 * Should be run Sync with a low delay
 */
public class TimeListenerTask extends BukkitRunnable {

	private final NEnchantingEgg    plugin;
	private final Map<String, Long> previousTimeMap;

	public TimeListenerTask(final NEnchantingEgg instance) {
		plugin = instance;
		previousTimeMap = new HashMap<>();
		for (final World w : Bukkit.getWorlds()) {
			previousTimeMap.put(w.getName(), w.getTime());
		}
	}

	@Override
	public void run() {
		for (final World w : Bukkit.getWorlds()) {
			final long actualTime = w.getTime();
			final Long previousTime = previousTimeMap.get(w.getName());
			if (previousTime != null) {
				if (MinecraftTime.isDayTime(previousTime) && MinecraftTime.isNightTime(actualTime)) {
					plugin.getAltars().timeChange(w.getName(), MinecraftTime.DAY, MinecraftTime.NIGHT);
				} else if (MinecraftTime.isNightTime(previousTime) && MinecraftTime.isDayTime(actualTime)) {
					plugin.getAltars().timeChange(w.getName(), MinecraftTime.NIGHT, MinecraftTime.DAY);
				}
			}
			previousTimeMap.put(w.getName(), actualTime);
		}
	}
}
