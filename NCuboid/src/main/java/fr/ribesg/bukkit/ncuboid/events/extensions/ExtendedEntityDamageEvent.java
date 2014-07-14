/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedEntityDamageEvent.java    *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;
import java.util.SortedSet;

public class ExtendedEntityDamageEvent extends AbstractExtendedEvent {

	private final SortedSet<GeneralRegion> entityRegions;
	private       SortedSet<GeneralRegion> damagerRegions;
	private final GeneralRegion            entityRegion;
	private       GeneralRegion            damagerRegion;

	private boolean damagerProjectile = false;
	private Entity shooter;

	public ExtendedEntityDamageEvent(final RegionDb db, final EntityDamageEvent event) {
		super(db.getPlugin(), event);
		entityRegions = db.getAllByLocation(event.getEntity().getLocation());
		entityRegion = db.getPrior(entityRegions);
		if (event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if (damager instanceof Projectile) {
				final Projectile projectile = (Projectile) damager;
				if (projectile.getShooter() instanceof Entity) {
					damager = (Entity) projectile.getShooter();
				}
				damagerProjectile = true;
				shooter = damager;
			}
			if (damager != null) {
				damagerRegions = db.getAllByLocation(damager.getLocation());
				damagerRegion = db.getPrior(damagerRegions);
			}
		}
	}

	public GeneralRegion getDamagerRegion() {
		return damagerRegion;
	}

	public Set<GeneralRegion> getDamagerRegions() {
		return damagerRegions;
	}

	public boolean isDamagerProjectile() {
		return damagerProjectile;
	}

	public GeneralRegion getEntityRegion() {
		return entityRegion;
	}

	public Set<GeneralRegion> getEntityRegions() {
		return entityRegions;
	}

	public Entity getShooter() {
		return shooter;
	}
}
