/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - RespawnTask.java             *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.task.RespawnTask         *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;

/**
 * @author Ribesg
 */
public class RespawnTask extends RandomRepeatingTask {

    public RespawnTask(final EndWorldHandler handler) {
        super(handler);
    }

    @Override
    public boolean exec() {
        this.worldHandler.getPlugin().entering(this.getClass(), "exec");

        final boolean res = this.worldHandler.getRespawnHandler().respawn();

        this.worldHandler.getPlugin().exiting(this.getClass(), "exec", Boolean.toString(res));
        return res;
    }

    @Override
    protected long getInitialDelay() {
        long nextRespawnTaskTime = this.worldHandler.getConfig().getNextRespawnTaskTime();
        if (this.worldHandler.getConfig().getRespawnType() == 4) {
            nextRespawnTaskTime = 0;
        }
        return this.buildInitialDelay(nextRespawnTaskTime);
    }

    @Override
    protected long getDelay() {
        return this.worldHandler.getConfig().getRandomRespawnTimer();
    }

    @Override
    protected void setNextConfigTime(final long date) {
        this.worldHandler.getConfig().setNextRespawnTaskTime(date);
    }
}
