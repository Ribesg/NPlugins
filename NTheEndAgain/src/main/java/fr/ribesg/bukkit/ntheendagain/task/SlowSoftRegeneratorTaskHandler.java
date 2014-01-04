/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - SlowSoftRegeneratorTaskHandler.java
 * Full Class name: fr.ribesg.bukkit.ntheendagain.task.SlowSoftRegeneratorTaskHandler
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.task;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class SlowSoftRegeneratorTaskHandler {

	private class SlowSoftRegeneratorTask extends BukkitRunnable {

		private final Iterator<EndChunk> iterator;
		private final int                nbChunks;

		public SlowSoftRegeneratorTask(final Iterator<EndChunk> iterator, final int nbChunks) {
			this.iterator = iterator;
			this.nbChunks = nbChunks;
		}

		@Override
		public void run() {
			int regen = 0;
			while (regen < this.nbChunks && iterator.hasNext()) {
				final EndChunk c = this.iterator.next();
				if (c.hasToBeRegen()) {
					final World world = c.getCoords().getBukkitWorld();
					if (world != null) {
						world.loadChunk(c.getX(), c.getZ());
						regen++;
					} else {
						// Something bad happen, stop there
						this.cancel();
					}
				}
			}
			if (!iterator.hasNext()) {
				this.cancel();
			}
		}
	}

	private final EndWorldHandler handler;

	private final int slowSoftRegenChunks;
	private final int slowSoftRegenTimer;

	private SlowSoftRegeneratorTask task;

	public SlowSoftRegeneratorTaskHandler(final EndWorldHandler handler) {
		this.handler = handler;
		if (handler.getSlowSoftRegeneratorTaskHandler() != null) {
			handler.getSlowSoftRegeneratorTaskHandler().cancel();
		}
		handler.setSlowSoftRegeneratorTaskHandler(this);

		this.slowSoftRegenChunks = handler.getConfig().getSlowSoftRegenChunks();
		this.slowSoftRegenTimer = handler.getConfig().getSlowSoftRegenTimer();
	}

	public void run() {
		this.task = new SlowSoftRegeneratorTask(handler.getChunks().getSafeChunksList().iterator(), this.slowSoftRegenChunks);
		this.task.runTaskTimer(this.handler.getPlugin(), this.slowSoftRegenTimer, this.slowSoftRegenTimer);
	}

	public void cancel() {
		if (this.task != null) {
			try {
				this.task.cancel();
			} catch (IllegalStateException ignored) {
				// Ignored
			}
		}
	}
}
