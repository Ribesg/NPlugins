package fr.ribesg.bukkit.ntheendagain.tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

/**
 * @author Ribesg
 */
public class RespawnTask extends BukkitRunnable {

    private final EndWorldHandler handler;

    public RespawnTask(final EndWorldHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        final int regenType = handler.getConfig().getRegenType();
        if (regenType == 1) {
            handler.regen();
        }
        handler.respawnDragons();

        final Config config = handler.getConfig();
        final Random rand = new Random();
        final int randomRespawnTimer = rand.nextInt(config.getRespawnTimerMax() - config.getRespawnTimerMin()) + config.getRespawnTimerMin();
        Bukkit.getScheduler().runTaskLater(handler.getPlugin(), this, randomRespawnTimer * 20);
        handler.getConfig().setNextRespawnTaskTime(System.currentTimeMillis() + randomRespawnTimer);
    }
}
