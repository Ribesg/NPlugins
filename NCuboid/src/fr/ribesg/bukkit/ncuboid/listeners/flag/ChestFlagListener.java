package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityExplodeEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEntityEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestFlagListener extends AbstractListener {

	public ChestFlagListener(final NCuboid instance) {
		super(instance);
	}

	// We don't care if hasBlock()==false, so ignoreCancelled is true
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
		final PlayerInteractEvent event = (PlayerInteractEvent) ext.getBaseEvent();
		if (event.hasBlock()) {
			if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.CHEST) && !ext.getCuboid().isAllowedPlayer(event.getPlayer())) {
				switch (event.getClickedBlock().getType()) {
					case CHEST:
					case TRAPPED_CHEST:
					case DISPENSER:
					case DROPPER:
					case HOPPER:
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
		if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.CHEST) && !ext.getCuboid().isAllowedPlayer(event.getPlayer())) {
			switch (event.getRightClicked().getType()) {
				case ITEM_FRAME:
				case MINECART_CHEST:
				case MINECART_HOPPER:
				case MINECART_FURNACE:
					event.setCancelled(true);
					break;
				default:
					break;
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(final ExtendedEntityExplodeEvent ext) {
		final EntityExplodeEvent event = (EntityExplodeEvent) ext.getBaseEvent();
		for (final Block b : ext.getBlockCuboidsMap().keySet()) {
			if (ext.getBlockCuboidsMap().get(b).getFlag(Flag.CHEST)) {
				switch (b.getType()) {
					case CHEST:
					case TRAPPED_CHEST:
					case DISPENSER:
					case FURNACE:
					case DROPPER:
					case HOPPER:
					case BURNING_FURNACE:
					case BREWING_STAND:
					case BEACON:
						event.blockList().remove(b);
						break;
					default:
						break;
				}
			}
		}
	}
}
