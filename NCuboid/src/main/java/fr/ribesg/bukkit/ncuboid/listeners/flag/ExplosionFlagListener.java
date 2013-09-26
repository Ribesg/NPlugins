package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.FlagAtt;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityExplodeEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionFlagListener extends AbstractListener {

	public ExplosionFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(final ExtendedEntityExplodeEvent ext) {
		final EntityExplodeEvent event = (EntityExplodeEvent) ext.getBaseEvent();
		for (final Block b : ext.getBlockRegionsMap().keySet()) {
			if (ext.getBlockRegionsMap().get(b).getFlag(Flag.EXPLOSION_BLOCK)) {
				event.blockList().remove(b);
			}
		}
		if (ext.getEntityRegion() != null) {
			final Integer blockDropRatio = ext.getEntityRegion().getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP);
			if (blockDropRatio != null) {
				event.setYield(blockDropRatio / 100f);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(final ExtendedEntityDamageEvent ext) {
		final EntityDamageEvent event = (EntityDamageEvent) ext.getBaseEvent();
		if (event.getEntityType() == EntityType.DROPPED_ITEM && event.getCause() == DamageCause.ENTITY_EXPLOSION) {
			if (ext.getEntityRegion() != null && ext.getEntityRegion().getFlag(Flag.EXPLOSION_ITEM) ||
			    ext.getDamagerRegion() != null && ext.getDamagerRegion().getFlag(Flag.EXPLOSION_ITEM)) {
				event.setCancelled(true);
			}
		}
	}
}
