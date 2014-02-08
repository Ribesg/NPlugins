/***************************************************************************
 * Project file:    NPlugins - NCuboid - FarmFlagListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.FarmFlagListener
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
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityInteractEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEntityEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.HashSet;
import java.util.Set;

public class FarmFlagListener extends AbstractListener {

	private static Set<EntityType> animals;

	private static Set<EntityType> getAnimals() {
		if (animals == null) {
			animals = new HashSet<>();
			animals.add(EntityType.BAT);
			animals.add(EntityType.CHICKEN);
			animals.add(EntityType.COW);
			animals.add(EntityType.HORSE);
			animals.add(EntityType.IRON_GOLEM);
			animals.add(EntityType.MUSHROOM_COW);
			animals.add(EntityType.OCELOT);
			animals.add(EntityType.PIG);
			animals.add(EntityType.SHEEP);
			animals.add(EntityType.SNOWMAN);
			animals.add(EntityType.SQUID);
			animals.add(EntityType.VILLAGER);
			animals.add(EntityType.WOLF);
		}
		return animals;
	}

	public FarmFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(final ExtendedEntityDamageEvent ext) {
		if (ext.getBaseEvent() instanceof EntityDamageByEntityEvent) {
			final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ext.getBaseEvent();
			final Player p;
			if (event.getDamager().getType() == EntityType.PLAYER) {
				p = (Player) event.getDamager();
			} else if (event.getDamager() instanceof Projectile) {
				final LivingEntity shooter = ((Projectile) event.getDamager()).getShooter();
				if (shooter != null && shooter.getType() == EntityType.PLAYER) {
					p = (Player) shooter;
				} else {
					return;
				}
			} else {
				return;
			}
			if (getAnimals().contains(event.getEntityType()) &&
			    ext.getEntityRegion() != null &&
			    ext.getEntityRegion().getFlag(Flag.FARM) &&
			    !ext.getEntityRegion().isUser(p)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteractEvent(final ExtendedPlayerInteractEvent ext) {
		final PlayerInteractEvent event = (PlayerInteractEvent) ext.getBaseEvent();
		if (event.getAction() == Action.PHYSICAL && event.hasBlock() && event.getClickedBlock().getType() == Material.SOIL) {
			if (ext.getClickedRegion() != null && ext.getClickedRegion().getFlag(Flag.FARM)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityInteractEvent(final ExtendedEntityInteractEvent ext) {
		final EntityInteractEvent event = (EntityInteractEvent) ext.getBaseEvent();
		if (event.getBlock().getType() == Material.SOIL) {
			if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.FARM)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteractEntity(final ExtendedPlayerInteractEntityEvent ext) {
		final PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) ext.getBaseEvent();
		if (getAnimals().contains(event.getRightClicked().getType())) {
			if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.FARM) && !ext.getRegion().isUser(event.getPlayer())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerShearEntity(final PlayerShearEntityEvent event) {
		final GeneralRegion cuboid = getPlugin().getDb().getPriorByLocation(event.getEntity().getLocation());
		if (cuboid != null && cuboid.getFlag(Flag.FARM) && !cuboid.isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
