package fr.ribesg.bukkit.ngeneral.feature.protectionsign;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ncore.utils.UsernameUtils;
import fr.ribesg.bukkit.ngeneral.Perms;
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

	public ProtectionSignListener(ProtectionSignFeature feature) {
		this.feature = feature;
	}

	// #################### //
	// ## Event Handlers ## //
	// #################### //

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		String[] lines = event.getLines();
		if (ProtectionSignFeature.getProtectionStrings().contains(ColorUtils.stripColorCodes(lines[0]).toLowerCase())) {
			// This is a Protection sign
			if (!Perms.hasProtectionSign(event.getPlayer())) {
				lines[0] = ProtectionSignFeature.ERROR;
				lines[1] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine1());
				lines[2] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine2());
				lines[3] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine3());
			} else if (feature.protectsSomething(event.getBlock().getLocation())) {
				if (feature.canPlaceSign(event.getPlayer().getName(), event.getBlock().getLocation())) {
					lines[0] = ProtectionSignFeature.PROTECTION;
					lines[1] = ProtectionSignFeature.SECONDARY_PREFIX + UsernameUtils.getId(ColorUtils.stripColorCodes(lines[1]));
					lines[2] = ProtectionSignFeature.SECONDARY_PREFIX + UsernameUtils.getId(ColorUtils.stripColorCodes(lines[2]));
					lines[3] = ProtectionSignFeature.PRIMARY_PREFIX + UsernameUtils.getId(event.getPlayer().getName());
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
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		if (!feature.canBreak(event.getBlock(), event.getPlayer())) {
			feature.getPlugin().sendMessage(event.getPlayer(), MessageId.general_protectionsign_breakDenied);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!feature.canUse(event.getPlayer(), event.getClickedBlock())) {
				feature.getPlugin().sendMessage(event.getPlayer(), MessageId.general_protectionsign_accessDenied);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event) {
		final Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
			if (!feature.canBreak(it.next(), null)) {
				it.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
		final InventoryType type = event.getSource().getType();
		final InventoryHolder holder = event.getSource().getHolder();
		Block b;
		switch (type) {
			case HOPPER:
				b = ((Hopper) holder).getBlock();
				break;
			case CHEST:
				if (holder instanceof Chest) {
					b = ((Chest) holder).getBlock();
				} else {
					b = ((DoubleChest) holder).getLocation().getBlock();
				}
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
				b = ((Dispenser) holder).getBlock();
				break;
			case DROPPER:
				b = ((Dropper) holder).getBlock();
				break;
			default:
				return;
		}
		event.setCancelled(feature.isProtected(b) != null);
	}
}
