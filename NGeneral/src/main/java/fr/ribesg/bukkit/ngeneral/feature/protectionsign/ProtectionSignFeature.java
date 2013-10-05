package fr.ribesg.bukkit.ngeneral.feature.protectionsign;
import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ncore.utils.SignUtils;
import fr.ribesg.bukkit.ncore.utils.UsernameUtils;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.GeneralFeature;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProtectionSignFeature extends GeneralFeature {

	// ############### //
	// ## Constants ## //
	// ############### //

	/** First line of a Protection sign - Exactly 16 chars */
	/* package */ static final String PROTECTION       = "[" + ChatColor.GREEN + "Private" + ChatColor.BLACK + "]";
	/** Prefix of the Protection sign Owner name */
	/* package */ static final String PRIMARY_PREFIX   = ChatColor.GREEN.toString();
	/** Prefix of a Protection sign additional name */
	/* package */ static final String SECONDARY_PREFIX = ChatColor.GRAY.toString();

	/** First line of an Error sign */
	/* package */ static final String ERROR = "[" + ChatColor.DARK_RED + "Error" + ChatColor.RESET + "]";

	/** The ID of a Post Sign block */
	/* package */ static final Material SIGN_POST = Material.SIGN_POST;
	/** The ID of a Wall Sign block */
	/* package */ static final Material WALL_SIGN = Material.WALL_SIGN;

	/** The ID of a Chest block */
	/* package */ static final Material CHEST         = Material.CHEST;
	/** The ID of a Trapped Chest block */
	/* package */ static final Material TRAPPED_CHEST = Material.TRAPPED_CHEST;

	// ################### //
	// ## Constant sets ## //
	// ################### //

	/** Set of String that players could write as first lines to say that it's a Protection sign */
	private static Set<String> protectionStrings;

	/** Static lazy getter for {@link #protectionStrings} */
	/* package */
	static Set<String> getProtectionStrings() {
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
	private static Set<Material> protectedMaterials;

	/** Static lazy getter for {@link #protectedMaterials} */
	/* package */
	static Set<Material> getProtectedMaterials() {
		if (protectedMaterials == null) {
			protectedMaterials = new HashSet<>(11);
			protectedMaterials.add(Material.BEACON);
			protectedMaterials.add(Material.BREWING_STAND);
			protectedMaterials.add(Material.BURNING_FURNACE);
			protectedMaterials.add(Material.CHEST);
			protectedMaterials.add(Material.COMMAND);
			protectedMaterials.add(Material.DISPENSER);
			protectedMaterials.add(Material.DROPPER);
			protectedMaterials.add(Material.FURNACE);
			protectedMaterials.add(Material.HOPPER);
			protectedMaterials.add(Material.JUKEBOX);
			protectedMaterials.add(Material.TRAPPED_CHEST);
		}
		return protectedMaterials;
	}

	// ############ //
	// ## Fields ## //
	// ############ //

	private final ProtectionSignListener listener;

	public ProtectionSignFeature(NGeneral instance) {
		super(instance);
		listener = new ProtectionSignListener(this);
	}

	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
	}

	/**
	 * Detect if a Block can be broken by a Player or by something else (Explosion...)
	 *
	 * @param b      the Block to be broken
	 * @param player the Player that want to break the Block, if there is one, null otherwise
	 *
	 * @return true if the block can be broken [by the Player], false otherwise
	 */
	public boolean canBreak(Block b, Player player) {
		final Material blockType = b.getType();
		final String userId = player != null ? UsernameUtils.getId(player.getName()) : null;
		if (blockType == SIGN_POST || blockType == WALL_SIGN) {
			final Sign sign = (Sign) b.getState();
			return !sign.getLine(0).equals(PROTECTION) ||
			       player != null && ColorUtils.stripColorCodes(sign.getLine(3)).equals(userId) ||
			       player != null && Perms.hasProtectionSignBreak(player);
		} else {
			List<String[]> signLines;
			if (blockType == CHEST || blockType == TRAPPED_CHEST) {
				signLines = SignUtils.getSignsForChest(b);
			} else if (getProtectedMaterials().contains(blockType)) {
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
	 * Detects if a Player can use a protectable Block or not
	 *
	 * @param player the Player involved
	 * @param b      the Block to be used
	 *
	 * @return true if the Player can use the Block, false otherwise
	 */
	public boolean canUse(final Player player, final Block b) {
		if (Perms.hasProtectionSignBypass(player)) {
			return true;
		}
		final Material blockType = b.getType();
		final String userId = UsernameUtils.getId(player.getName());
		List<String[]> signLines;
		if (blockType == CHEST || blockType == TRAPPED_CHEST) {
			signLines = SignUtils.getSignsForChest(b);
		} else if (getProtectedMaterials().contains(blockType)) {
			signLines = SignUtils.getSignsForBlock(b);
		} else {
			return true;
		}
		boolean protectedBySign = false;
		boolean explicitlyAllowed = false;
		for (String[] lines : signLines) {
			if (lines[0].equals(PROTECTION)) {
				protectedBySign = true;
				if (ColorUtils.stripColorCodes(lines[1]).equals(userId) ||
				    ColorUtils.stripColorCodes(lines[2]).equals(userId) ||
				    ColorUtils.stripColorCodes(lines[3]).equals(userId)) {
					explicitlyAllowed = true;
					break;
				}
			}
		}
		return !protectedBySign || explicitlyAllowed;
	}

	/**
	 * Detect if a Protection Sign has something to protect.
	 * Note: This does not check if a protectable block is already protected.
	 *
	 * @param l Location of the protection sign
	 *
	 * @return True if there is a protectable block, otherwise false
	 */
	public boolean protectsSomething(Location l) {
		final World w = l.getWorld();
		final int x = l.getBlockX();
		final int y = l.getBlockY();
		final int z = l.getBlockZ();

		return getProtectedMaterials().contains(w.getBlockAt(x - 1, y, z).getType()) ||
		       getProtectedMaterials().contains(w.getBlockAt(x + 1, y, z).getType()) ||
		       getProtectedMaterials().contains(w.getBlockAt(x, y - 1, z).getType()) ||
		       getProtectedMaterials().contains(w.getBlockAt(x, y + 1, z).getType()) ||
		       getProtectedMaterials().contains(w.getBlockAt(x, y, z - 1).getType()) ||
		       getProtectedMaterials().contains(w.getBlockAt(x, y, z + 1).getType());
	}

	/**
	 * Detects if a Block is protected by a Sign or not.
	 * Returns the Sign owner if protected, null otherwise.
	 *
	 * @param b the block to check
	 *
	 * @return the Sign owner name if protected, null otherwise
	 */
	public String isProtected(final Block b) {
		final Material blockType = b.getType();
		List<String[]> signLines;
		if (blockType == CHEST || blockType == TRAPPED_CHEST) {
			signLines = SignUtils.getSignsForChest(b);
		} else if (getProtectedMaterials().contains(blockType)) {
			signLines = SignUtils.getSignsForBlock(b);
		} else {
			return null;
		}
		for (String[] sign : signLines) {
			if (PROTECTION.equals(sign[0])) {
				return ColorUtils.stripColorCodes(sign[3]);
			}
		}
		return null;
	}

	/**
	 * This method checks if a Player with the provided playerName can place
	 * a Special Sign at the given Location.
	 * <p/>
	 * This will check every Block that would be affected by this Sign and
	 * see if at least one of them is protected, or not.
	 *
	 * @param loc        the Location of the future Sign
	 * @param playerName the name of the Player that is placing the Sign
	 *
	 * @return true if none of the considered Blocks is protected, false
	 *         otherwise
	 */
	public boolean canPlaceSign(final String playerName, final Location loc) {
		final World w = loc.getWorld();
		final int x = loc.getBlockX();
		final int y = loc.getBlockY();
		final int z = loc.getBlockZ();

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
		return !(protecterId != null && !protecterId.equals(userId));

	}
}
