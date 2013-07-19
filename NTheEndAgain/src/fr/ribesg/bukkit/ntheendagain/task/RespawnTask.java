package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;

/** @author Ribesg */
public class RespawnTask extends RandomRepeatingTask {

    public RespawnTask(final EndWorldHandler handler) {
        super(handler);
    }

    @Override
    public void exec() {
        worldHandler.getRespawnHandler().respawn();
    }

    @Override
    protected long getInitialDelay() {
        long nextRespawnTaskTime = worldHandler.getConfig().getNextRespawnTaskTime();
        if (worldHandler.getConfig().getRespawnType() == 4) {
            nextRespawnTaskTime = 0;
        }
        return buildInitialDelay(nextRespawnTaskTime);
    }
}
