package com.github.ribesg.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.events.AbstractExtendedEvent;

public class ExtendedEntityDamageEvent extends AbstractExtendedEvent {

    @Getter private final Set<PlayerCuboid> entityCuboids;
    @Getter private Set<PlayerCuboid>       damagerCuboids;
    @Getter private final PlayerCuboid      entityCuboid;
    @Getter private PlayerCuboid            damagerCuboid;

    private boolean                         isDamagerProjectile = false;

    public ExtendedEntityDamageEvent(final EntityDamageEvent event) {
        super(event);
        entityCuboids = CuboidDB.getInstance().getAllByLoc(event.getEntity().getLocation());
        entityCuboid = CuboidDB.getInstance().getPrior(entityCuboids);
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Projectile) {
                damager = ((Projectile) damager).getShooter();
                isDamagerProjectile = true;
            }
            damagerCuboids = CuboidDB.getInstance().getAllByLoc(damager.getLocation());
            damagerCuboid = CuboidDB.getInstance().getPrior(damagerCuboids);
        }
    }

    public boolean isDamagerProjectile() {
        return isDamagerProjectile;
    }

}
