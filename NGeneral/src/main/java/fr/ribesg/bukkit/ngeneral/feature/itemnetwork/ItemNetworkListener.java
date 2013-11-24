package fr.ribesg.bukkit.ngeneral.feature.itemnetwork;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ncore.utils.SignUtils;
import fr.ribesg.bukkit.ncore.utils.UsernameUtils;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.InventoryContent;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetwork;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ReceiverSign;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ItemNetworkListener implements Listener {

	/** First line of a INet Emitter sign */
	private static final String ITEMNETWORK_EMITTER  = "[" + ChatColor.GREEN + "INet-Em" + ChatColor.BLACK + "]";
	/** First line of a INet Receiver sign */
	private static final String ITEMNETWORK_RECEIVER = "[" + ChatColor.GREEN + "INet-Re" + ChatColor.BLACK + "]";
	/** Prefix of the INet sign owner name */
	private static final String PRIMARY_PREFIX       = ChatColor.GREEN.toString();
	/** Prefix of all other lines */
	private static final String SECONDARY_PREFIX     = ChatColor.GOLD.toString();

	/** First line of an Error sign */
	private static final String ERROR = "[" + ChatColor.DARK_RED + "Error" + ChatColor.RESET + "]";

	/** Set of String that players could write as first lines to say that it's a INet Emitter sign */
	private static Set<String> itemNetworkEmitterStrings;

	/** Static lazy getter for {@link #itemNetworkEmitterStrings} */
	private static Set<String> getItemNetworkEmitterStrings() {
		if (itemNetworkEmitterStrings == null) {
			itemNetworkEmitterStrings = new HashSet<>(3);
			itemNetworkEmitterStrings.add("inet-em");
			itemNetworkEmitterStrings.add("[inet-em]");
			itemNetworkEmitterStrings.add("ine");
		}
		return itemNetworkEmitterStrings;
	}

	/** Set of String that players could write as first lines to say that it's a INet Receiver sign */
	private static Set<String> itemNetworkReceiverStrings;

	/** Static lazy getter for {@link #itemNetworkReceiverStrings} */
	private static Set<String> getItemNetworkReceiverStrings() {
		if (itemNetworkReceiverStrings == null) {
			itemNetworkReceiverStrings = new HashSet<>(3);
			itemNetworkReceiverStrings.add("inet-re");
			itemNetworkReceiverStrings.add("[inet-re]");
			itemNetworkReceiverStrings.add("inr");
		}
		return itemNetworkReceiverStrings;
	}

	private final ItemNetworkFeature feature;

	public ItemNetworkListener(final ItemNetworkFeature feature) {
		this.feature = feature;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (isChest(event.getClickedBlock()) && feature.isLocked(new NLocation(event.getClickedBlock().getLocation()))) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryOpen(final InventoryOpenEvent event) {
		final Inventory inventory = event.getInventory();
		final InventoryHolder holder = inventory.getHolder();
		if (holder instanceof Chest || holder instanceof DoubleChest) {
			final BlockState chest = (BlockState) (holder instanceof Chest ? holder : ((DoubleChest) holder).getLeftSide());
			final Block block = chest.getBlock();
			final NLocation loc = new NLocation(block.getLocation());
			final List<Sign> signs = SignUtils.getSignsForChest(block);
			for (final Sign sign : signs) {
				if (sign.getLine(0).equals(ITEMNETWORK_EMITTER)) {
					feature.lock(loc);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClose(final InventoryCloseEvent event) {
		if (event.getViewers().size() == 1) {
			final Inventory inventory = event.getInventory();
			final InventoryHolder holder = inventory.getHolder();
			if (holder instanceof Chest || holder instanceof DoubleChest) {
				final BlockState chest = (BlockState) (holder instanceof Chest ? holder : ((DoubleChest) holder).getLeftSide());
				final Block block = chest.getBlock();
				final NLocation loc = new NLocation(block.getLocation());
				final List<Sign> signs = SignUtils.getSignsForChest(block);
				for (final Sign sign : signs) {
					if (sign.getLine(0).equals(ITEMNETWORK_EMITTER)) {
						final String networkName = ColorUtils.stripColorCodes(sign.getLine(1));
						final ItemNetwork network = feature.getNetworks().get(networkName.toLowerCase());
						if (network != null) {
							final List<ItemStack> items = new ArrayList<>();
							for (final ItemStack is : inventory.getContents()) {
								if (is != null) {
									items.add(is);
								}
							}
							if (!items.isEmpty()) {
								network.queue(new InventoryContent(loc, items));
								inventory.clear();
								feature.lock(loc);
								return;
							}
							break;
						} else {
							sign.getBlock().breakNaturally();
						}
					}
				}
				feature.unlock(loc);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(final SignChangeEvent event) {
		final String[] lines = event.getLines();
		final Player player = event.getPlayer();
		final String cleanedFirstLine = ColorUtils.stripColorCodes(lines[0].toLowerCase());
		if (getItemNetworkEmitterStrings().contains(cleanedFirstLine)) {
			final String networkName = ColorUtils.stripColorCodes(lines[1].toLowerCase());
			final ItemNetwork network = feature.getNetworks().get(networkName);
			if (network == null) {
				event.setLine(0, ERROR);
				event.setLine(1, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignUnknownNetworkMsgLine1()));
				event.setLine(2, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignUnknownNetworkMsgLine2()));
				event.setLine(3, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignUnknownNetworkMsgLine3()));
			} else if (!Perms.hasItemNetworkAll(player) && !player.getName().equalsIgnoreCase(network.getCreator())) {
				event.setLine(0, ERROR);
				event.setLine(1, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignNotAllowedMsgLine1()));
				event.setLine(2, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignNotAllowedMsgLine2()));
				event.setLine(3, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignNotAllowedMsgLine3()));
			} else if (network.isTooFar(event.getBlock().getLocation())) {
				event.setLine(0, ERROR);
				event.setLine(1, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignTooFarMsgLine1()));
				event.setLine(2, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignTooFarMsgLine2()));
				event.setLine(3, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignTooFarMsgLine3()));
			} else {
				event.setLine(0, ITEMNETWORK_EMITTER);
				event.setLine(1, SECONDARY_PREFIX + network.getName());
				event.setLine(2, "");
				event.setLine(3, PRIMARY_PREFIX + UsernameUtils.getId(player.getName()));
			}
		} else if (getItemNetworkReceiverStrings().contains(cleanedFirstLine)) {
			final String networkName = ColorUtils.stripColorCodes(lines[1].toLowerCase());
			final ItemNetwork network = feature.getNetworks().get(networkName);
			if (network == null) {
				event.setLine(0, ERROR);
				event.setLine(1, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignUnknownNetworkMsgLine1()));
				event.setLine(2, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignUnknownNetworkMsgLine2()));
				event.setLine(3, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignUnknownNetworkMsgLine3()));
			} else if (!Perms.hasItemNetworkAll(player) && !player.getName().equalsIgnoreCase(network.getCreator())) {
				event.setLine(0, ERROR);
				event.setLine(1, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignNotAllowedMsgLine1()));
				event.setLine(2, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignNotAllowedMsgLine2()));
				event.setLine(3, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignNotAllowedMsgLine3()));
			} else if (network.isTooFar(event.getBlock().getLocation())) {
				event.setLine(0, ERROR);
				event.setLine(1, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignTooFarMsgLine1()));
				event.setLine(2, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignTooFarMsgLine2()));
				event.setLine(3, ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignTooFarMsgLine3()));
			} else {
				final String acceptedString = event.getLine(2);
				try {
					network.getReceivers().add(new ReceiverSign(new NLocation(event.getBlock().getLocation()), acceptedString));
				} catch (IllegalArgumentException e) {
					event.setLine(0, ERROR);
					event.setLine(1,
					              ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignInvalidMaterialsMsgLine1()));
					event.setLine(2,
					              ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignInvalidMaterialsMsgLine2()));
					event.setLine(3,
					              ColorUtils.colorize(feature.getPlugin().getPluginConfig().getItemNetworkSignInvalidMaterialsMsgLine3()));
					return;
				}
				event.setLine(0, ITEMNETWORK_RECEIVER);
				event.setLine(1, SECONDARY_PREFIX + network.getName());
				event.setLine(2, SECONDARY_PREFIX + event.getLine(2));
				event.setLine(3, PRIMARY_PREFIX + UsernameUtils.getId(player.getName()));
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(final BlockBreakEvent event) {
		if (SignUtils.isSign(event.getBlock())) {
			final Sign sign = (Sign) event.getBlock().getState();
			final String line1 = sign.getLine(0);
			if (line1.equals(ITEMNETWORK_EMITTER) || line1.equals(ITEMNETWORK_RECEIVER)) {
				final String playerId = ColorUtils.stripColorCodes(sign.getLine(3));
				if (!Perms.isAdmin(event.getPlayer()) && !UsernameUtils.getId(event.getPlayer().getName()).equals(playerId)) {
					feature.getPlugin().sendMessage(event.getPlayer(), MessageId.general_itemnetwork_youNeedToBeCreator);
					event.setCancelled(true);
				} else if (line1.equals(ITEMNETWORK_RECEIVER)) {
					final String networkName = ColorUtils.stripColorCodes(sign.getLine(1));
					final NLocation location = new NLocation(event.getBlock().getLocation());
					final ItemNetwork network = feature.getNetworks().get(networkName);
					if (network != null) {
						final Iterator<ReceiverSign> it = network.getReceivers().iterator();
						while (it.hasNext()) {
							if (it.next().getLocation().equals(location)) {
								it.remove();
								break;
							}
						}
					}
				}
			}
		}
	}

	private boolean isChest(final Block b) {
		return b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST;
	}
}
