package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/** @author Ribesg */
public class RegenTask extends BukkitRunnable {

    private final EndWorldHandler handler;

    public RegenTask(final EndWorldHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.regen();

        final Config config = handler.getConfig();
        Bukkit.getScheduler().runTaskLater(handler.getPlugin(), this, config.getRegenTimer() * 20);
        handler.getConfig().setNextRegenTaskTime(System.currentTimeMillis() + config.getRegenTimer());
    }
}
