package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class UnexpectedDragonDeathHandlerTask extends BukkitRunnable {

    private final EndWorldHandler handler;

    public UnexpectedDragonDeathHandlerTask(final EndWorldHandler handler) {
        this.handler = handler;
    }

    /**
     * Schedule this task
     *
     * @param plugin the plugin to attach the task
     */
    public BukkitTask schedule(JavaPlugin plugin) {
        return Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 20L);
    }

    @Override
    public void run() {
        boolean found = false;

        final Iterator<UUID> it = handler.getLoadedDragons().iterator();
        final Collection<EnderDragon> dragons = handler.getEndWorld().getEntitiesByClass(EnderDragon.class);
        while (it.hasNext()) {
            final UUID id = it.next();
            for (final EnderDragon ed : dragons) {
                if (id == ed.getUniqueId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                // This EnderDragon was deleted some other way than after his death, forget about him
                handler.getDragons().remove(id);
                it.remove();
            }
            found = false;
        }
    }
}
