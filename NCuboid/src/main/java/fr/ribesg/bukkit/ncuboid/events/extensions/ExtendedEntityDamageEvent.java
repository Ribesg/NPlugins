/***************************************************************************
 * Project file:    NPlugins - NCuboid - ExtendedEntityDamageEvent.java    *
 * Full Class name: fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.events.extensions;

import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

import java.util.Set;
import java.util.SortedSet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ExtendedEntityDamageEvent extends AbstractExtendedEvent {

    private final SortedSet<GeneralRegion> entityRegions;
    private       SortedSet<GeneralRegion> damagerRegions;
    private final GeneralRegion            entityRegion;
    private       GeneralRegion            damagerRegion;

    private boolean damagerProjectile;
    private Entity  shooter;

    public ExtendedEntityDamageEvent(final RegionDb db, final EntityDamageEvent event) {
        super(db.getPlugin(), event);
        this.entityRegions = db.getAllByLocation(event.getEntity().getLocation());
        this.entityRegion = db.getPrior(this.entityRegions);
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
            if (damager instanceof Projectile) {
                final Projectile projectile = (Projectile)damager;
                if (projectile.getShooter() instanceof Entity) {
                    damager = (Entity)projectile.getShooter();
                }
                this.damagerProjectile = true;
                this.shooter = damager;
            }
            if (damager != null) {
                this.damagerRegions = db.getAllByLocation(damager.getLocation());
                this.damagerRegion = db.getPrior(this.damagerRegions);
            }
        }
    }

    public GeneralRegion getDamagerRegion() {
        return this.damagerRegion;
    }

    public Set<GeneralRegion> getDamagerRegions() {
        return this.damagerRegions;
    }

    public boolean isDamagerProjectile() {
        return this.damagerProjectile;
    }

    public GeneralRegion getEntityRegion() {
        return this.entityRegion;
    }

    public Set<GeneralRegion> getEntityRegions() {
        return this.entityRegions;
    }

    public Entity getShooter() {
        return this.shooter;
    }
}
