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
        int regenOnRespawn = handler.getConfig().getRegenOnRespawn();
        if (regenOnRespawn == 1 || regenOnRespawn == 2 && handler.getNumberOfAliveEnderDragons() == 0) {
            handler.regen();
        }
        handler.respawnDragons();
    }
}
