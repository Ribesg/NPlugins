/***************************************************************************
 * Project file:    NPlugins - NGeneral - ProtectionSignListener.java      *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.protectionsign.ProtectionSignListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.protectionsign;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ncore.utils.PlayerIdsUtils;
import fr.ribesg.bukkit.ncore.utils.SignUtils;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Location;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Iterator;

public class ProtectionSignListener implements Listener {

	// ############ //
	// ## Fields ## //
	// ############ //

	private final ProtectionSignFeature feature;

	// ################# //
	// ## Constructor ## //
	// ################# //

	public ProtectionSignListener(final ProtectionSignFeature feature) {
		this.feature = feature;
	}

	// #################### //
	// ## Event Handlers ## //
	// #################### //

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onSignChange(final SignChangeEvent event) {
		final String[] lines = event.getLines();
		if (ProtectionSignFeature.getProtectionStrings().contains(ColorUtils.stripColorCodes(lines[0]).toLowerCase())) {
			// This is a Protection sign
			final Location loc = event.getBlock().getLocation();
			if (!Perms.hasProtectionSign(event.getPlayer())) {
				lines[0] = ProtectionSignFeature.ERROR;
				lines[1] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine1());
				lines[2] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine2());
				lines[3] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine3());
			} else if (feature.protectsSomething(loc)) {
				if (feature.canPlaceSign(event.getPlayer().getName(), loc)) {
					lines[0] = ProtectionSignFeature.PROTECTION;
					lines[1] = ProtectionSignFeature.SECONDARY_PREFIX + PlayerIdsUtils.getId(ColorUtils.stripColorCodes(lines[1]));
					lines[2] = ProtectionSignFeature.SECONDARY_PREFIX + PlayerIdsUtils.getId(ColorUtils.stripColorCodes(lines[2]));
					lines[3] = ProtectionSignFeature.PRIMARY_PREFIX + PlayerIdsUtils.getId(event.getPlayer().getName());
					feature.clearCache(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName());
				} else {
					lines[0] = ProtectionSignFeature.ERROR;
					lines[1] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignAlreadyProtectedMsgLine1());
					lines[2] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignAlreadyProtectedMsgLine2());
					lines[3] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignAlreadyProtectedMsgLine3());
				}
			} else {
				lines[0] = ProtectionSignFeature.ERROR;
				lines[1] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNothingToProtectMsgLine1());
				lines[2] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNothingToProtectMsgLine2());
				lines[3] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNothingToProtectMsgLine3());
			}
			for (int i = 0; i < 4; i++) {
				event.setLine(i, lines[i]);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBreakBlock(final BlockBreakEvent event) {
		final Block b = event.getBlock();
		if (SignUtils.isSign(b)) {
			if (!feature.canBreak(b, event.getPlayer())) {
				feature.getPlugin().sendMessage(event.getPlayer(), MessageId.general_protectionsign_breakDenied);
				event.setCancelled(true);
			} else {
				feature.clearCache(b.getX(), b.getY(), b.getZ(), b.getWorld().getName());
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!feature.canUse(event.getPlayer(), event.getClickedBlock())) {
				feature.getPlugin().sendMessage(event.getPlayer(), MessageId.general_protectionsign_accessDenied);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(final EntityExplodeEvent event) {
		final Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
			if (!feature.canBreak(it.next(), null)) {
				it.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryMoveItemEvent(final InventoryMoveItemEvent event) {
		InventoryType type = event.getSource().getType();
		InventoryHolder holder = event.getSource().getHolder();
		Block b = null;
		switch (type) {
			case HOPPER:
				if (holder instanceof Hopper) {
					b = ((Hopper) holder).getBlock();
				} // else Minecart Hopper
				break;
			case CHEST:
				if (holder instanceof Chest) {
					b = ((Chest) holder).getBlock();
				} else if (holder instanceof DoubleChest) {
					b = ((DoubleChest) holder).getLocation().getBlock();
				} // else Minecart Chest
				break;
			case BEACON:
				b = ((Beacon) holder).getBlock();
				break;
			case BREWING:
				b = ((BrewingStand) holder).getBlock();
				break;
			case FURNACE:
				b = ((Furnace) holder).getBlock();
				break;
			case DISPENSER:
				if (holder instanceof Dispenser) {
					b = ((Dispenser) holder).getBlock();
				} // else Minecart Dispenser
				break;
			case DROPPER:
				b = ((Dropper) holder).getBlock();
				break;
			default:
				return;
		}
		if (b != null) {
			if (feature.getPlugin().isDebugEnabled()) {
				feature.getPlugin().debug("InventoryMoveEvent with source at " + NLocation.toString(b.getLocation()));
			}
			event.setCancelled(feature.isProtected(b) != null);
		}

		if (!event.isCancelled()) {
			type = event.getDestination().getType();
			holder = event.getDestination().getHolder();
			b = null;
			switch (type) {
				case HOPPER:
					if (holder instanceof Hopper) {
						b = ((Hopper) holder).getBlock();
					} // else Minecart Hopper
					break;
				case CHEST:
					if (holder instanceof Chest) {
						b = ((Chest) holder).getBlock();
					} else if (holder instanceof DoubleChest) {
						b = ((DoubleChest) holder).getLocation().getBlock();
					} // else Minecart Chest
					break;
				case BEACON:
					b = ((Beacon) holder).getBlock();
					break;
				case BREWING:
					b = ((BrewingStand) holder).getBlock();
					break;
				case FURNACE:
					b = ((Furnace) holder).getBlock();
					break;
				case DISPENSER:
					if (holder instanceof Dispenser) {
						b = ((Dispenser) holder).getBlock();
					} // else Minecart Dispenser
					break;
				case DROPPER:
					b = ((Dropper) holder).getBlock();
					break;
				default:
					return;
			}
			if (b != null) {
				if (feature.getPlugin().isDebugEnabled()) {
					feature.getPlugin().debug("InventoryMoveEvent with destination at " + NLocation.toString(b.getLocation()));
				}
				event.setCancelled(feature.isProtected(b) != null);
			}
		}
	}
}
