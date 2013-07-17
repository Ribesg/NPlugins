package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class PassFlagListener extends AbstractListener {

    public PassFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMoveBlock(final ExtendedPlayerMoveEvent ext) {
        final PlayerMoveEvent event = (PlayerMoveEvent) ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            if (ext.getFromCuboid() != null && ext.getFromCuboid().getFlag(Flag.PASS) && !ext.getFromCuboid().equals(ext.getToCuboid())) {
                event.setTo(new Location(event.getFrom().getWorld(),
                                         event.getFrom().getBlockX() + 0.5,
                                         event.getFrom().getBlockY() + 0.25,
                                         event.getFrom().getBlockZ() + 0.5,
                                         event.getTo().getYaw(),
                                         event.getTo().getPitch()));
                ext.setCustomCancelled(true);
            }
        }
    }
}
