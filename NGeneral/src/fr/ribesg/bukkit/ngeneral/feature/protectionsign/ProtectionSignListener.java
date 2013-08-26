package fr.ribesg.bukkit.ngeneral.feature.protectionsign;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ncore.utils.SignUtils;
import fr.ribesg.bukkit.ncore.utils.UsernameUtils;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProtectionSignListener implements Listener {

	// ############### //
	// ## Constants ## //
	// ############### //

	/** First line of a Protection sign - Exactly 16 chars */
	private static final String PROTECTION       = "[" + ChatColor.GREEN + "Private" + ChatColor.BLACK + "]";
	/** Prefix of the Protection sign Owner name */
	private static final String PRIMARY_PREFIX   = ChatColor.GREEN.toString();
	/** Prefix of a Protection sign additional name */
	private static final String SECONDARY_PREFIX = ChatColor.GRAY.toString();

	/** First line of an Error sign */
	private static final String ERROR = "[" + ChatColor.DARK_RED + "Error" + ChatColor.RESET + "]";

	/** The ID of a Post Sign block */
	private static final int SIGN_POST_ID = Material.SIGN_POST.getId();
	/** The ID of a Wall Sign block */
	private static final int WALL_SIGN_ID = Material.WALL_SIGN.getId();

	/** The ID of a Chest block */
	private static final int CHEST_ID         = Material.CHEST.getId();
	/** The ID of a Trapped Chest block */
	private static final int TRAPPED_CHEST_ID = Material.TRAPPED_CHEST.getId();

	// ################### //
	// ## Constant sets ## //
	// ################### //

	/** Set of String that players could write as first lines to say that it's a Protection sign */
	private static Set<String> protectionStrings;

	/** Static lazy getter for {@link #protectionStrings} */
	private static Set<String> getProtectionStrings() {
		if (protectionStrings == null) {
			protectionStrings = new HashSet<>(5);
			protectionStrings.add("protection");
			protectionStrings.add("[protection]");
			protectionStrings.add("private");
			protectionStrings.add("[private]");
			protectionStrings.add("p");
		}
		return protectionStrings;
	}

	/** Set of block types that can be protected by a Protection sign */
	private static Set<Integer> protectedMaterials;

	/** Static lazy getter for {@link #protectedMaterials} */
	private static Set<Integer> getProtectedMaterials() {
		if (protectedMaterials == null) {
			protectedMaterials = new HashSet<>(11);
			protectedMaterials.add(Material.BEACON.getId());
			protectedMaterials.add(Material.BREWING_STAND.getId());
			protectedMaterials.add(Material.BURNING_FURNACE.getId());
			protectedMaterials.add(Material.CHEST.getId());
			protectedMaterials.add(Material.COMMAND.getId());
			protectedMaterials.add(Material.DISPENSER.getId());
			protectedMaterials.add(Material.DROPPER.getId());
			protectedMaterials.add(Material.FURNACE.getId());
			protectedMaterials.add(Material.HOPPER.getId());
			protectedMaterials.add(Material.JUKEBOX.getId());
			protectedMaterials.add(Material.TRAPPED_CHEST.getId());
		}
		return protectedMaterials;
	}

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
		if (getProtectionStrings().contains(parse(lines[0]))) {
			// This is a Protection sign
			if (!Perms.hasProtectionSign(event.getPlayer())) {
				lines[0] = ERROR;
				lines[1] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine1());
				lines[2] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine2());
				lines[3] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignNoPermMsgLine3());
			} else if (protectsSomething(event.getBlock().getLocation())) {
				if (canProtect(event.getBlock().getLocation(), event.getPlayer().getName())) {
					lines[0] = PROTECTION;
					lines[1] = SECONDARY_PREFIX + UsernameUtils.getId(strip(lines[1]));
					lines[2] = SECONDARY_PREFIX + UsernameUtils.getId(strip(lines[2]));
					lines[3] = PRIMARY_PREFIX + UsernameUtils.getId(event.getPlayer().getName());
				} else {
					lines[0] = ERROR;
					lines[1] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignAlreadyProtectedMsgLine1());
					lines[2] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignAlreadyProtectedMsgLine2());
					lines[3] = ColorUtils.colorize(feature.getPlugin().getPluginConfig().getProtectionSignAlreadyProtectedMsgLine3());
				}
			} else {
				lines[0] = ERROR;
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
		if (!canBreak(event.getBlock(), event.getPlayer())) {
			feature.getPlugin().sendMessage(event.getPlayer(), MessageId.general_protectionsign_breakDenied);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!canUse(event.getClickedBlock(), event.getPlayer())) {
				feature.getPlugin().sendMessage(event.getPlayer(), MessageId.general_protectionsign_accessDenied);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event) {
		final Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
			if (!canBreak(it.next(), null)) {
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
		event.setCancelled(isProtected(b) != null);
	}

	// ############# //
	// ## Methods ## //
	// ############# //

	/**
	 * Detect if a block can be broken by a Player or by something else (Explosion...)
	 *
	 * @param b      Block to be broken
	 * @param player Player that want to break the block, if there is one. Null otherwise
	 *
	 * @return If the block can be broken [by the Player]
	 */
	private boolean canBreak(Block b, Player player) {
		final int blockId = b.getTypeId();
		final String userId = player != null ? UsernameUtils.getId(player.getName()) : null;
		if (blockId == SIGN_POST_ID || blockId == WALL_SIGN_ID) {
			final Sign sign = (Sign) b.getState();
			return !sign.getLine(0).equals(PROTECTION) ||
			       player != null && strip(sign.getLine(3)).equals(userId) ||
			       player != null && Perms.hasProtectionSignBreak(player);
		} else {
			List<String[]> signLines;
			if (blockId == CHEST_ID || blockId == TRAPPED_CHEST_ID) {
				signLines = SignUtils.getSignsForChest(b);
			} else if (protectedMaterials.contains(blockId)) {
				signLines = SignUtils.getSignsForBlock(b);
			} else {
				return true;
			}
			for (String[] lines : signLines) {
				if (lines[0].equals(PROTECTION)) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Detects if a Player can use a "Protectable" block or not
	 *
	 * @param b      Block to be used
	 * @param player Player
	 *
	 * @return True if the player can use the block, false otherwise
	 */
	private boolean canUse(Block b, Player player) {
		if (Perms.hasProtectionSignBypass(player)) {
			return true;
		}
		final int blockId = b.getTypeId();
		final String userId = UsernameUtils.getId(player.getName());
		List<String[]> signLines;
		if (blockId == CHEST_ID || blockId == TRAPPED_CHEST_ID) {
			signLines = SignUtils.getSignsForChest(b);
		} else if (getProtectedMaterials().contains(blockId)) {
			signLines = SignUtils.getSignsForBlock(b);
		} else {
			return true;
		}
		boolean protectedBySign = false;
		boolean explicitlyAllowed = false;
		for (String[] lines : signLines) {
			if (lines[0].equals(PROTECTION)) {
				protectedBySign = true;
				if (strip(lines[1]).equals(userId) || strip(lines[2]).equals(userId) || strip(lines[3]).equals(userId)) {
					explicitlyAllowed = true;
					break;
				}
			}
		}
		return !protectedBySign || explicitlyAllowed;
	}

	/**
	 * Detect if a Protection Sign has something to protect
	 *
	 * @param l Location of the protection sign
	 *
	 * @return True if there is a "protectable" block, otherwise false
	 */
	private boolean protectsSomething(Location l) {
		final World w = l.getWorld();
		final int x = l.getBlockX();
		final int y = l.getBlockY();
		final int z = l.getBlockZ();

		return getProtectedMaterials().contains(w.getBlockTypeIdAt(x - 1, y, z)) ||
		       getProtectedMaterials().contains(w.getBlockTypeIdAt(x + 1, y, z)) ||
		       getProtectedMaterials().contains(w.getBlockTypeIdAt(x, y - 1, z)) ||
		       getProtectedMaterials().contains(w.getBlockTypeIdAt(x, y + 1, z)) ||
		       getProtectedMaterials().contains(w.getBlockTypeIdAt(x, y, z - 1)) ||
		       getProtectedMaterials().contains(w.getBlockTypeIdAt(x, y, z + 1));
	}

	/**
	 * Detects if a Block is protected by a Sign or not.
	 * Returns the Sign owner if protected, null otherwise.
	 *
	 * @param b The block
	 *
	 * @return Sign owner name if protected, null otherwise
	 */
	private String isProtected(Block b) {
		int blockId = b.getTypeId();
		List<String[]> signLines;
		if (blockId == CHEST_ID || blockId == TRAPPED_CHEST_ID) {
			signLines = SignUtils.getSignsForChest(b);
		} else if (getProtectedMaterials().contains(blockId)) {
			signLines = SignUtils.getSignsForBlock(b);
		} else {
			return null;
		}
		for (String[] sign : signLines) {
			if (PROTECTION.equals(sign[0])) {
				return strip(sign[3]);
			}
		}
		return null;
	}

	/**
	 * Checks if this Protection Sign location would interfer with another Protection sign
	 *
	 * @param l
	 *
	 * @return
	 */
	private boolean canProtect(Location l, String playerName) {
		final World w = l.getWorld();
		final int x = l.getBlockX();
		final int y = l.getBlockY();
		final int z = l.getBlockZ();

		final String userId = UsernameUtils.getId(playerName);
		String protecterId;

		protecterId = isProtected(w.getBlockAt(x - 1, y, z));
		if (protecterId != null && !protecterId.equals(userId)) {
			return false;
		}

		protecterId = isProtected(w.getBlockAt(x + 1, y, z));
		if (protecterId != null && !protecterId.equals(userId)) {
			return false;
		}

		protecterId = isProtected(w.getBlockAt(x, y - 1, z));
		if (protecterId != null && !protecterId.equals(userId)) {
			return false;
		}

		protecterId = isProtected(w.getBlockAt(x, y + 1, z));
		if (protecterId != null && !protecterId.equals(userId)) {
			return false;
		}

		protecterId = isProtected(w.getBlockAt(x, y, z - 1));
		if (protecterId != null && !protecterId.equals(userId)) {
			return false;
		}

		protecterId = isProtected(w.getBlockAt(x, y, z + 1));
		if (protecterId != null && !protecterId.equals(userId)) {
			return false;
		}

		return true;
	}

	/** Removes colors and lowercases the provided String */
	private String parse(String string) {
		return strip(string).toLowerCase();
	}

	/** Removes colors (Both Bukkit and custom) of the provided String */
	private String strip(String string) {
		return ChatColor.stripColor(ColorUtils.colorize(string));
	}
}
