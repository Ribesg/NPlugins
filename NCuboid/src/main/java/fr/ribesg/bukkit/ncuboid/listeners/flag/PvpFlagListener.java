/***************************************************************************
 * Project file:    NPlugins - NCuboid - PvpFlagListener.java              *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.PvpFlagListener*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerTeleportEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPotionSplashEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PvpFlagListener extends AbstractListener {

	public PvpFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(final ExtendedEntityDamageEvent ext) {
		if (ext.getBaseEvent() instanceof EntityDamageByEntityEvent) {
			final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ext.getBaseEvent();
			if (event.getEntityType() == EntityType.PLAYER && (event.getDamager().getType() == EntityType.PLAYER || ext.isDamagerProjectile() && ext.getShooter() != null && ext.getShooter() instanceof Player)) {
				if (ext.getEntityRegion() != null && ext.getEntityRegion().getFlag(Flag.PVP) || ext.getDamagerRegion() != null && ext.getDamagerRegion().getFlag(Flag.PVP)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPotionSplash(final ExtendedPotionSplashEvent ext) {
		final PotionSplashEvent event = (PotionSplashEvent) ext.getBaseEvent();
		if (event.getPotion().getShooter() instanceof Player) {
			if (ext.hasNegativeEffect()) {
				GeneralRegion region;
				for (final LivingEntity e : ext.getEntityRegionsMap().keySet()) {
					if (e.getType() == EntityType.PLAYER) {
						region = ext.getEntityRegionsMap().get(e);
						if (region != null && region.getFlag(Flag.PVP)) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
		final PlayerGridMoveEvent event = (PlayerGridMoveEvent) ext.getBaseEvent();
		if (!ext.isCustomCancelled() && !event.isCancelled()) {
			if (!ext.getToRegion().getFlag(Flag.PVP_HIDE) && ext.getToRegion().getFlag(Flag.PVP) && !ext.getFromRegions().contains(ext.getToRegion())) {
				// Entering PVP Area
				getPlugin().sendMessage(event.getPlayer(), MessageId.cuboid_enteringPvpArea, ext.getToRegion().getRegionName());
			} else if (!ext.getFromRegion().getFlag(Flag.PVP_HIDE) && ext.getFromRegion().getFlag(Flag.PVP) && !ext.getToRegions().contains(ext.getFromRegion())) {
				// Exiting PVP Area
				getPlugin().sendMessage(event.getPlayer(), MessageId.cuboid_exitingPvpArea, ext.getFromRegion().getRegionName());
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(final ExtendedPlayerTeleportEvent ext) {
		final PlayerTeleportEvent event = (PlayerTeleportEvent) ext.getBaseEvent();
		if (!event.isCancelled()) {
			if (!ext.getToRegion().getFlag(Flag.PVP_HIDE) && ext.getToRegion().getFlag(Flag.PVP) && !ext.getFromRegions().contains(ext.getToRegion())) {
				// Entering PVP Area
				getPlugin().sendMessage(event.getPlayer(), MessageId.cuboid_enteringPvpArea, ext.getToRegion().getRegionName());
			} else if (!ext.getFromRegion().getFlag(Flag.PVP_HIDE) && ext.getFromRegion().getFlag(Flag.PVP) && !ext.getToRegions().contains(ext.getFromRegion())) {
				// Exiting PVP Area
				getPlugin().sendMessage(event.getPlayer(), MessageId.cuboid_exitingPvpArea, ext.getFromRegion().getRegionName());
			}
		}
	}
}
