/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - UnexpectedDragonDeathHandlerTask.java
 * Full Class name: fr.ribesg.bukkit.ntheendagain.task.UnexpectedDragonDeathHandlerTask
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.task;

import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class UnexpectedDragonDeathHandlerTask extends BukkitRunnable {

    private final EndWorldHandler handler;
    private final String[]        message;

    public UnexpectedDragonDeathHandlerTask(final EndWorldHandler handler) {
        super();
        this.handler = handler;
        final FrameBuilder frame = new FrameBuilder();
        frame.addLine("An EnderDragon has been lost!", FrameBuilder.Option.CENTER);
        frame.addLine("Maybe it was removed by another plugin/command?");
        frame.addLine("If you had respawnType set to \"after death\", you will");
        frame.addLine("need to manually respawn the Dragon(s) with /end respawn!");
        frame.addLine("Remember: EnderDragons should DIE to be handled correctly.");
        this.message = frame.build();
    }

    /**
     * Schedule this task
     *
     * @param plugin the plugin to attach the task
     */
    public BukkitTask schedule(final JavaPlugin plugin) {
        return Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 20L);
    }

    @Override
    public void run() {
        boolean found = false;

        final Iterator<UUID> it = this.handler.getLoadedDragons().iterator();
        final Collection<EnderDragon> dragons = this.handler.getEndWorld().getEntitiesByClass(EnderDragon.class);
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
                this.handler.getDragons().remove(id);
                it.remove();
                for (final String line : this.message) {
                    this.handler.getPlugin().getLogger().warning(line);
                }
            }
            found = false;
        }
    }
}
