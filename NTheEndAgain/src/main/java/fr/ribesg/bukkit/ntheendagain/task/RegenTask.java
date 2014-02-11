/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - RegenTask.java               *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.task.RegenTask           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;

/**
 * @author Ribesg
 */
public class RegenTask extends RandomRepeatingTask {

	public RegenTask(final EndWorldHandler handler) {
		super(handler);
	}

	@Override
	public void exec() {
		worldHandler.getRegenHandler().regen();
	}

	@Override
	protected long getInitialDelay() {
		long nextRegenTaskTime = worldHandler.getConfig().getNextRegenTaskTime();
		if (worldHandler.getConfig().getRegenType() == 2) {
			nextRegenTaskTime = 0;
		}
		return buildInitialDelay(nextRegenTaskTime);
	}

	@Override
	protected long getDelay() {
		return worldHandler.getConfig().getRegenTimer();
	}
}
