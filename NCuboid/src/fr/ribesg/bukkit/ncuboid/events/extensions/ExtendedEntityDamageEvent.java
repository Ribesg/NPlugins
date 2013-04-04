package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedEntityDamageEvent extends AbstractExtendedEvent {

    @Getter private final Set<PlayerCuboid> entityCuboids;
    @Getter private Set<PlayerCuboid>       damagerCuboids;
    @Getter private final PlayerCuboid      entityCuboid;
    @Getter private PlayerCuboid            damagerCuboid;

    private boolean                         isDamagerProjectile = false;

    public ExtendedEntityDamageEvent(final CuboidDB db, final EntityDamageEvent event) {
        super(event);
        entityCuboids = db.getAllByLoc(event.getEntity().getLocation());
        entityCuboid = db.getPrior(entityCuboids);
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Projectile) {
                damager = ((Projectile) damager).getShooter();
                isDamagerProjectile = true;
            }
            damagerCuboids = db.getAllByLoc(damager.getLocation());
            damagerCuboid = db.getPrior(damagerCuboids);
        }
    }

    public boolean isDamagerProjectile() {
        return isDamagerProjectile;
    }

}
