package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedEntityDamageEvent extends AbstractExtendedEvent {

    @Getter private final Set<GeneralCuboid> entityCuboids;
    @Getter private Set<GeneralCuboid>       damagerCuboids;
    @Getter private final GeneralCuboid      entityCuboid;
    @Getter private GeneralCuboid            damagerCuboid;

    @Getter private boolean                  damagerProjectile = false;
    @Getter private Entity                   shooter;

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

}
