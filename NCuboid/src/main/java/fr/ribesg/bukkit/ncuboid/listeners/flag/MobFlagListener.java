/***************************************************************************
 * Project file:    NPlugins - NCuboid - MobFlagListener.java              *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.MobFlagListener*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPotionSplashEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashSet;
import java.util.Set;

public class MobFlagListener extends AbstractListener {

	private static Set<EntityType> mobs;

	private static Set<EntityType> getMobs() {
		if (mobs == null) {
			mobs = new HashSet<>();
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
			final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getLocation());
			if (region != null && region.getFlag(Flag.MOB)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(final ExtendedEntityDamageEvent ext) {
		if (ext.getBaseEvent() instanceof EntityDamageByEntityEvent) {
			final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ext.getBaseEvent();
			if (getMobs().contains(event.getDamager().getType()) || ext.isDamagerProjectile() && ext.getShooter() != null && getMobs().contains(ext.getShooter().getType())) {
				if (ext.getEntityRegion() != null && ext.getEntityRegion().getFlag(Flag.MOB) || ext.getDamagerRegion() != null && ext.getDamagerRegion().getFlag(Flag.MOB)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPotionSplash(final ExtendedPotionSplashEvent ext) {
		final PotionSplashEvent event = (PotionSplashEvent) ext.getBaseEvent();
		final ProjectileSource shooter = event.getPotion().getShooter();
		if (shooter instanceof LivingEntity && getMobs().contains(((Entity) shooter).getType())) {
			if (ext.hasNegativeEffect()) {
				GeneralRegion region;
				for (final LivingEntity e : event.getAffectedEntities()) {
					if (e.getType() == EntityType.PLAYER) {
						region = getPlugin().getDb().getPriorByLocation(e.getLocation());
						if (region != null && region.getFlag(Flag.MOB)) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
}
