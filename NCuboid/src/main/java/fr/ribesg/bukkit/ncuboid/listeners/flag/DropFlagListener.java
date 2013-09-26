package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerDropItemEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropFlagListener extends AbstractListener {

	public DropFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDropItem(final ExtendedPlayerDropItemEvent ext) {
		final PlayerDropItemEvent event = (PlayerDropItemEvent) ext.getBaseEvent();
		if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.DROP) && !ext.getRegion().isAllowedPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
