package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.FlagAtt;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class ClosedFlagListener extends AbstractListener {

	public ClosedFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMoveBlock(final ExtendedPlayerMoveEvent ext) {
		final PlayerMoveEvent event = (PlayerMoveEvent) ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			final GeneralRegion r = ext.getFromRegion();
			if (r != null && r.getFlag(Flag.CLOSED) && !r.equals(ext.getToRegion())) {
				final Location loc = r.getLocFlagAtt(FlagAtt.INTERNAL_POINT);
				if (loc == null) {
					event.setTo(new Location(event.getFrom().getWorld(),
					                         event.getFrom().getBlockX() + 0.5,
					                         event.getFrom().getBlockY() + 0.1,
					                         event.getFrom().getBlockZ() + 0.5,
					                         event.getTo().getYaw(),
					                         event.getTo().getPitch()));
				} else {
					event.setTo(new Location(loc.getWorld(),
					                         loc.getBlockX() + 0.5,
					                         loc.getBlockY() + 0.1,
					                         loc.getBlockZ() + 0.5,
					                         event.getTo().getYaw(),
					                         event.getTo().getPitch()));
				}
				ext.setCustomCancelled(true);
			}
		}
	}
}
