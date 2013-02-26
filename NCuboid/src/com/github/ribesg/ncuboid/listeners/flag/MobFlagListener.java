package com.github.ribesg.ncuboid.listeners.flag;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.Flag;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import com.github.ribesg.ncuboid.events.extensions.ExtendedPotionSplashEvent;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

public class MobFlagListener extends AbstractListener {

    private static Set<EntityType> mobs;

    private static Set<EntityType> getMobs() {
        if (mobs == null) {
            mobs = new HashSet<EntityType>();
            mobs.add(EntityType.BLAZE);
            mobs.add(EntityType.CAVE_SPIDER);
            mobs.add(EntityType.CREEPER);
            mobs.add(EntityType.ENDER_DRAGON);
            mobs.add(EntityType.ENDERMAN);
            mobs.add(EntityType.GHAST);
            mobs.add(EntityType.GIANT);
            mobs.add(EntityType.MAGMA_CUBE);
            mobs.add(EntityType.PIG_ZOMBIE);
            mobs.add(EntityType.SILVERFISH);
            mobs.add(EntityType.SKELETON);
            mobs.add(EntityType.SLIME);
            mobs.add(EntityType.SPIDER);
            mobs.add(EntityType.WITCH);
            mobs.add(EntityType.WITHER);
            mobs.add(EntityType.ZOMBIE);
        }
        return mobs;
    }

    public MobFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (getMobs().contains(event.getEntityType())) {
            final PlayerCuboid cuboid = CuboidDB.getInstance().getPriorByLoc(event.getLocation());
            if (cuboid != null && cuboid.getFlag(Flag.MOB)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamageByEntity(final ExtendedEntityDamageEvent ext) {
        if (ext.getBaseEvent() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ext.getBaseEvent();
            if (getMobs().contains(event.getDamager()) || ext.isDamagerProjectile() && getMobs().contains(((Projectile) event.getDamager()).getShooter().getType())) {
                if (ext.getEntityCuboid() != null && ext.getEntityCuboid().getFlag(Flag.MOB) || ext.getDamagerCuboid() != null && ext.getDamagerCuboid().getFlag(Flag.MOB)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPotionSplash(final ExtendedPotionSplashEvent ext) {
        final PotionSplashEvent event = (PotionSplashEvent) ext.getBaseEvent();
        if (getMobs().contains(event.getPotion().getShooter().getType())) {
            if (ext.hasNegativeEffect()) {
                PlayerCuboid c;
                for (final LivingEntity e : ext.getEntityCuboidsMap().keySet()) {
                    if (e.getType() == EntityType.PLAYER) {
                        c = ext.getEntityCuboidsMap().get(e);
                        if (c != null && c.getFlag(Flag.PVP)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
