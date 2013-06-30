package fr.ribesg.bukkit.ntheendagain.tasks;

import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;
import org.bukkit.entity.EnderDragon;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class UnexpectedDragonDeathHandlerTask extends BukkitRunnable {

    private final EndWorldHandler handler;

    public UnexpectedDragonDeathHandlerTask(final EndWorldHandler handler) {
        this.handler = handler;
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
                handler.decrementDragonCount();
            }
            found = false;
        }
    }
}
