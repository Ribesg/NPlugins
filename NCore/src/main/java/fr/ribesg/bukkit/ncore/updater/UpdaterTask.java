/***************************************************************************
 * Project file:    NPlugins - NCore - UpdaterTask.java                    *
 * Full Class name: fr.ribesg.bukkit.ncore.updater.UpdaterTask             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.updater;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdaterTask extends BukkitRunnable{

	private final Updater updater;

	public UpdaterTask(final Updater updater) {
		this.updater = updater;
	}

	@Override
	public void run() {
		this.updater.checkForUpdates();
	}
}
