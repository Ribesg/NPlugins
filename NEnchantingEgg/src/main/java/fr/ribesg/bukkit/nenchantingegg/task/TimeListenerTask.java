/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - TimeListenerTask.java      *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.task.TimeListenerTask  *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.task;

import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This task notifies Altars about time
 * Should be run Sync with a not too low delay
 */
public class TimeListenerTask extends BukkitRunnable {

	private final NEnchantingEgg plugin;

	public TimeListenerTask(final NEnchantingEgg instance) {
		plugin = instance;
	}

	@Override
	public void run() {
		for (final World w : Bukkit.getWorlds()) {
			final long actualTime = w.getTime();
			final MinecraftTime time = MinecraftTime.get(actualTime);
			plugin.getAltars().time(w.getName(), time);
		}
	}
}
