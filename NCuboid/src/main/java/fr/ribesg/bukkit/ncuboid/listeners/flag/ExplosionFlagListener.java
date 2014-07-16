/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExplosionFlagListener.java        *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.ExplosionFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityChangeBlockEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityExplodeEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
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
			final Integer blockDropRatio = ext.getEntityRegion().getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP);
			if (blockDropRatio != null) {
				event.setYield(blockDropRatio / 100f);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(final ExtendedEntityDamageEvent ext) {
		final EntityDamageEvent event = (EntityDamageEvent) ext.getBaseEvent();
		if (event.getCause() == DamageCause.ENTITY_EXPLOSION) {
			if (event.getEntityType() == EntityType.DROPPED_ITEM) {
				if (ext.getEntityRegion() != null && ext.getEntityRegion().getFlag(Flag.EXPLOSION_ITEM) || ext.getDamagerRegion() != null && ext.getDamagerRegion().getFlag(Flag.EXPLOSION_ITEM)) {
					event.setCancelled(true);
				}
			} else if (event.getEntityType() == EntityType.PLAYER) {
				if (ext.getEntityRegion() != null && ext.getEntityRegion().getFlag(Flag.EXPLOSION_PLAYER) || ext.getDamagerRegion() != null && ext.getDamagerRegion().getFlag(Flag.EXPLOSION_PLAYER)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityChangeBlock(final ExtendedEntityChangeBlockEvent ext) {
		final EntityChangeBlockEvent event = (EntityChangeBlockEvent) ext.getBaseEvent();
		if (event.getEntityType() == EntityType.ENDER_DRAGON || event.getEntityType() == EntityType.WITHER) {
			if (ext.getBlockRegion() != null && ext.getBlockRegion().getFlag(Flag.EXPLOSION_BLOCK)) {
				event.setCancelled(true);
			}
		}
	}
}
