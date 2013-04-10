package fr.ribesg.bukkit.ntheendagain.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

/**
 * @author Ribesg
 */
public class RespawnTask extends BukkitRunnable {

    private EndWorldHandler handler;

    public RespawnTask(EndWorldHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.getConfig().setLastTaskExecTime(System.currentTimeMillis());
        if (handler.getConfig().getRegenOnRespawn() == 1) {
            handler.regen();
        }
        handler.respawnDragons();
    }
}
