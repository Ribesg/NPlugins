package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerTeleportEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportFlagListener extends AbstractListener {

	public TeleportFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(final ExtendedPlayerTeleportEvent ext) {
		final PlayerTeleportEvent event = (PlayerTeleportEvent) ext.getBaseEvent();
		if (ext.getFromCuboid() != null && ext.getFromCuboid().getFlag(Flag.TELEPORT)) {
			event.setCancelled(true);
		} else if (ext.getToCuboid() != null && ext.getToCuboid().getFlag(Flag.TELEPORT)) {
			event.setCancelled(true);
		}
	}
}
