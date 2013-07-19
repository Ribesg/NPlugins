package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;

/** @author Ribesg */
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
}
