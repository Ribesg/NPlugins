package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;

public class ExtendedEntityDamageEvent extends AbstractExtendedEvent {

	private final Set<GeneralRegion> entityRegions;
	private       Set<GeneralRegion> damagerRegions;
	private final GeneralRegion      entityRegion;
	private       GeneralRegion      damagerRegion;

	private boolean damagerProjectile = false;
	private Entity shooter;

	public ExtendedEntityDamageEvent(final RegionDb db, final EntityDamageEvent event) {
		super(event);
		entityRegions = db.getAllByLocation(event.getEntity().getLocation());
		entityRegion = db.getPrior(entityRegions);
		if (event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if (damager instanceof Projectile) {
				damager = ((Projectile) damager).getShooter();
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
