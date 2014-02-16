/***************************************************************************
 * Project file:    NPlugins - NCuboid - BuildFlagListener.java            *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.BuildFlagListener
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
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedHangingBreakEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEntityEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class BuildFlagListener extends AbstractListener {

	public BuildFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
		final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation());
		if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBucketFill(final PlayerBucketFillEvent event) {
		final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlockClicked().getLocation());
		if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	// We don't care if hasBlock()==false, so ignoreCancelled is true
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
		final PlayerInteractEvent event = (PlayerInteractEvent) ext.getBaseEvent();
		final Player player = event.getPlayer();
		if (event.hasBlock()) {
			if (event.hasItem() && event.getItem().getType() == Material.STICK) {
				// Handled in PlayerStickListener
				return;
			}
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.hasItem()) {
					// Fire or vehicle
					if (event.getItem().getType() == Material.FLINT_AND_STEEL ||
					    event.getItem().getType() == Material.FIREBALL ||
					    event.getItem().getType() == Material.MINECART ||
					    event.getItem().getType() == Material.BOAT) {
						if (ext.getRelativeClickedRegion() != null &&
						    ext.getRelativeClickedRegion().getFlag(Flag.BUILD) &&
						    !ext.getRelativeClickedRegion().isUser(player)) {
							event.setCancelled(true);
							return;
						}
					}
					// Disc
					else if (event.getClickedBlock().getType() == Material.JUKEBOX && event.getItem().getType().isRecord()) {
						if (ext.getClickedRegion() != null &&
						    ext.getClickedRegion().getFlag(Flag.BUILD) &&
						    !ext.getClickedRegion().isUser(event.getPlayer())) {
							event.setCancelled(true);
							return;
						}
					}
				}
				// Repeater, Comparator
				if (event.getClickedBlock().getType() == Material.DIODE_BLOCK_OFF ||
				    event.getClickedBlock().getType() == Material.DIODE_BLOCK_ON ||
				    event.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_OFF ||
				    event.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_ON) {
					if (ext.getClickedRegion() != null &&
					    ext.getClickedRegion().getFlag(Flag.BUILD) &&
					    !ext.getClickedRegion().isUser(event.getPlayer())) {
						event.setCancelled(true);
					}
				}
			} else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				// Fire block
				if (event.getClickedBlock().getRelative(event.getBlockFace()).getType() == Material.FIRE) {
					if (ext.getRelativeClickedRegion() != null &&
					    ext.getRelativeClickedRegion().getFlag(Flag.BUILD) &&
					    !ext.getRelativeClickedRegion().isUser(player)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) {
		final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlock().getLocation());
		if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event) {
		final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlock().getLocation());
		if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockDamage(final BlockDamageEvent event) {
		final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlock().getLocation());
		if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onHangingBreakByEntity(final ExtendedHangingBreakEvent ext) {
		if (ext.getBaseEvent() instanceof HangingBreakByEntityEvent) {
			final HangingBreakByEntityEvent event = (HangingBreakByEntityEvent) ext.getBaseEvent();
			if (event.getRemover().getType() == EntityType.PLAYER) {
				final Player player = (Player) event.getRemover();
				if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.BUILD) && !ext.getRegion().isUser(player)) {
					event.setCancelled(true);
				}
			} else {
				if (ext.getRegion() != null && (ext.getRegion().getFlag(Flag.BUILD) || ext.getRegion().getFlag(Flag.MOB))) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteractEntity(final ExtendedPlayerInteractEntityEvent ext) {
		final PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) ext.getBaseEvent();
		if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.BUILD) && !ext.getRegion().isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(final ExtendedEntityDamageEvent ext) {
		if (ext.getBaseEvent() instanceof EntityDamageByEntityEvent) {
			final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ext.getBaseEvent();
			if (event.getEntityType() == EntityType.ITEM_FRAME) {
				if (event.getDamager().getType() == EntityType.PLAYER) {
					final Player player = (Player) event.getDamager();
					if (ext.getEntityRegion() != null && ext.getEntityRegion().getFlag(Flag.BUILD) && !ext.getEntityRegion().isUser(player)) {
						event.setCancelled(true);
					}
				} else {
					if (ext.getEntityRegion() != null && (ext.getEntityRegion().getFlag(Flag.BUILD) || ext.getEntityRegion().getFlag(Flag.MOB))) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onHangingPlace(final HangingPlaceEvent event) {
		final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getEntity().getLocation());
		if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onVehicleDestroy(final VehicleDestroyEvent event) {
		if (event.getAttacker() != null && event.getAttacker().getType() == EntityType.PLAYER) {
			final Player player = (Player) event.getAttacker();
			final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getVehicle().getLocation());
			if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(player)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStructureGrow(final StructureGrowEvent event) {
		if (event.isFromBonemeal()) {
			final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getLocation());
			if (region != null && region.getFlag(Flag.BUILD) && !region.isUser(event.getPlayer())) {
				event.setCancelled(true);
			}
		}
	}
}
