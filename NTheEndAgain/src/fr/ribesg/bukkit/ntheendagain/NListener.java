package fr.ribesg.bukkit.ntheendagain;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class NListener implements Listener {

    @SuppressWarnings("unused") private final NTheEndAgain plugin;

    public NListener(final NTheEndAgain instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderDragonDeath(final EntityDeathEvent event) {
        // TODO
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        // TODO
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityCreatePortal(final EntityCreatePortalEvent event) {
        // TODO
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkLoad(final ChunkLoadEvent event) {
        // TODO
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        // TODO
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityRegainHealth(final EntityRegainHealthEvent event) {
        // TODO
    }
}
