package fr.ribesg.bukkit.ncuboid.listeners.flag;

import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerDropItemEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEntityEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

public class CreativeFlagListener extends AbstractListener {

    public CreativeFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMoveBlock(final ExtendedPlayerMoveEvent ext) {
        @SuppressWarnings("unused")
        final PlayerMoveEvent event = (PlayerMoveEvent) ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            // TODO Handle player GameMode
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
        final PlayerInteractEvent event = (PlayerInteractEvent) ext.getBaseEvent();
        if (event.hasBlock()) {
            if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.CREATIVE)) {
                switch (event.getClickedBlock().getType()) {
                    case CHEST:
                    case DISPENSER:
                    case FURNACE:
                    case BURNING_FURNACE:
                    case BREWING_STAND:
                    case BEACON:
                        event.setCancelled(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractEntity(final ExtendedPlayerInteractEntityEvent ext) {
        final PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) ext.getBaseEvent();
        if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.CHEST)) {
            switch (event.getRightClicked().getType()) {
                case ITEM_FRAME:
                    event.setCancelled(true);
                    break;
                case MINECART:
                    event.setCancelled(event.getRightClicked() instanceof StorageMinecart);
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDropItem(final ExtendedPlayerDropItemEvent ext) {
        final PlayerDropItemEvent event = (PlayerDropItemEvent) ext.getBaseEvent();
        if (ext.getPlayerCuboid() != null && ext.getPlayerCuboid().getFlag(Flag.CREATIVE)) {
            event.setCancelled(true);
        }
    }
}
