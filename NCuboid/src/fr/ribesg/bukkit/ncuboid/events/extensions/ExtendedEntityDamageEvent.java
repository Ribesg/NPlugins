package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;

public class ExtendedEntityDamageEvent extends AbstractExtendedEvent {

	private final Set<GeneralCuboid> entityCuboids;
	private       Set<GeneralCuboid> damagerCuboids;
	private final GeneralCuboid      entityCuboid;
	private       GeneralCuboid      damagerCuboid;

	private boolean damagerProjectile = false;
	private Entity shooter;

	public ExtendedEntityDamageEvent(final CuboidDB db, final EntityDamageEvent event) {
		super(event);
		entityCuboids = db.getAllByLoc(event.getEntity().getLocation());
		entityCuboid = db.getPrior(entityCuboids);
		if (event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if (damager instanceof Projectile) {
				damager = ((Projectile) damager).getShooter();
				damagerProjectile = true;
				shooter = damager;
			}
			damagerCuboids = db.getAllByLoc(damager.getLocation());
			damagerCuboid = db.getPrior(damagerCuboids);
		}
	}

	public GeneralCuboid getDamagerCuboid() {
		return damagerCuboid;
	}

	public Set<GeneralCuboid> getDamagerCuboids() {
		return damagerCuboids;
	}

	public boolean isDamagerProjectile() {
		return damagerProjectile;
	}

	public GeneralCuboid getEntityCuboid() {
		return entityCuboid;
	}

	public Set<GeneralCuboid> getEntityCuboids() {
		return entityCuboids;
	}

	public Entity getShooter() {
		return shooter;
	}
}
