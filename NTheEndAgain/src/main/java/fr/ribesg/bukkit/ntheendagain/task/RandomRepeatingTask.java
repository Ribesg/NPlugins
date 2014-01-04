/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - RandomRepeatingTask.java     *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.task.RandomRepeatingTask *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.task;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/** @author Ribesg */
public abstract class RandomRepeatingTask extends BukkitRunnable {

	protected final EndWorldHandler worldHandler;

	protected RandomRepeatingTask(final EndWorldHandler worldHandler) {
		this.worldHandler = worldHandler;
	}

	/**
	 * Schedule this task
	 *
	 * @param plugin the plugin to attach the task
	 */
	public BukkitTask schedule(final JavaPlugin plugin) {
		return Bukkit.getScheduler().runTaskLater(plugin, this, getInitialDelay());
	}

	@Override
	public void run() {
		exec();
		final long delay = getDelay();
		Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), this, delay * 20);
		worldHandler.getConfig().setNextRespawnTaskTime(System.nanoTime() + delay * 1_000_000_000);
	}

	/**
	 * Execute the task.
	 * This is equivalent to the run() task of a standard repeating task
	 */
	public abstract void exec();

	/**
	 * Get the initial delay for this task
	 *
	 * @return the initial delay, in seconds
	 */
	protected abstract long getInitialDelay();

	/**
	 * Build an initial delay according to the nextTaskTime
	 *
	 * @param nextTaskTime the next Task Time
	 *
	 * @return the initial delay, in seconds
	 */
	protected long buildInitialDelay(final long nextTaskTime) {
		long initialDelay = nextTaskTime - System.nanoTime();
		if (initialDelay < 0) {
			initialDelay = 0;
		}
		return initialDelay / 1_000_000_000;
	}

	/** @return a Random value between the minimum and maximum delay set in config */
	protected abstract long getDelay();
}
