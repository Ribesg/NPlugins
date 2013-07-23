package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
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
    private final String[]        message;

    public UnexpectedDragonDeathHandlerTask(final EndWorldHandler handler) {
        this.handler = handler;
        FrameBuilder frame = new FrameBuilder();
        frame.addLine("An EnderDragon has been lost!", FrameBuilder.Option.CENTER);
        frame.addLine("Maybe it was removed by another plugin/command?");
        frame.addLine("If you had respawnType set to \"after death\", you will");
        frame.addLine("need to manually respawn the Dragon(s) with /end respawn!");
        frame.addLine("Remember: EnderDragons should DIE to be handled correctly.");
        message = frame.build();
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
                for (String line : message) {
                    handler.getPlugin().getLogger().warning(line);
                }
            }
            found = false;
        }
    }
}
