package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EndermanGriefFlagListener extends AbstractListener {

	public EndermanGriefFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.ENDERMAN) {
			final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlock().getLocation());
			if (region != null && region.getFlag(Flag.ENDERMANGRIEF)) {
				((Enderman) event.getEntity()).setCarriedMaterial(Material.JACK_O_LANTERN.getNewData((byte) 0));
				event.setCancelled(true);
			}
		}
	}
}
