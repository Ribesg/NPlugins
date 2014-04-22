/***************************************************************************
 * Project file:    NPlugins - NGeneral - ProtectionSignFeature.java       *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.protectionsign.ProtectionSignFeature
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.protectionsign;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ncore.utils.PlayerIdsUtils;
import fr.ribesg.bukkit.ncore.utils.SignUtils;
import fr.ribesg.bukkit.ncore.utils.TimeUtils;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import fr.ribesg.bukkit.ngeneral.feature.Feature;
import fr.ribesg.bukkit.ngeneral.feature.FeatureType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProtectionSignFeature extends Feature {

	// ############### //
	// ## Constants ## //
	// ############### //

	/**
	 * First line of a Protection sign - Exactly 16 chars
	 */
	/* package */ static final String PROTECTION       = "[" + ChatColor.GREEN + "Private" + ChatColor.BLACK + "]";
	/**
	 * Prefix of the Protection sign Owner name
	 */
	/* package */ static final String PRIMARY_PREFIX   = ChatColor.GREEN.toString();
	/**
	 * Prefix of a Protection sign additional name
	 */
	/* package */ static final String SECONDARY_PREFIX = ChatColor.GRAY.toString();

	/**
	 * First line of an Error sign
	 */
	/* package */ static final String ERROR = "[" + ChatColor.DARK_RED + "Error" + ChatColor.RESET + "]";

	/**
	 * Amount of time to keep cached results
	 */
	private static final int CACHE_TIME = (int) TimeUtils.getInMilliseconds("1minute");

	// ################### //
	// ## Constant sets ## //
	// ################### //

	/**
	 * Set of String that players could write as first lines to say that it's a Protection sign
	 */
	private static Set<String> protectionStrings;

	/**
	 * Static lazy getter for {@link #protectionStrings}
	 */
	public static Set<String> getProtectionStrings() {
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

	/**
	 * Set of block types that can be protected by a Protection sign
	 */
	private static Set<Material> protectedMaterials;

	/**
	 * Static lazy getter for {@link #protectedMaterials}
	 */
	public static Set<Material> getProtectedMaterials() {
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

	// ############################## //
	// ## Non-static class content ## //
	// ############################## //

	private final Map<NLocation, ProtectionState> isProtectedCache;

	private class ProtectionState {

		public final String protectedBy;
		public final long   timeout;

		private ProtectionState(final String protectedBy, final long timeout) {
			this.protectedBy = protectedBy;
			this.timeout = timeout;
		}
	}

	private BukkitTask cacheFreeTask;

	public ProtectionSignFeature(final NGeneral instance) {
		super(instance, FeatureType.PROTECTION_SIGNS, instance.getPluginConfig().hasProtectionSignFeature());
		this.isProtectedCache = new HashMap<>();
	}

	@Override
	public void initialize() {
		final ProtectionSignListener listener = new ProtectionSignListener(this);

		Bukkit.getPluginManager().registerEvents(listener, getPlugin());

		this.cacheFreeTask = new BukkitRunnable() {

			@Override
			public void run() {
				final long now = System.currentTimeMillis();
				final Iterator<Map.Entry<NLocation, ProtectionState>> it = isProtectedCache.entrySet().iterator();
				while (it.hasNext()) {
					if (it.next().getValue().timeout <= now) {
						it.remove();
					}
				}
			}
		}.runTaskTimer(plugin, 10 * 20L, 10 * 20L);
	}

	@Override
	public void terminate() {
		if (cacheFreeTask != null) {
			cacheFreeTask.cancel();
		}
	}

	/**
	 * Clear the cache for the provided location information.
	 *
	 * @param x         the X coordinate
	 * @param y         the Y coordinate
	 * @param z         the Z coordinate
	 * @param worldName the world this location relates to
	 */
	public void clearCache(final int x, final int y, final int z, final String worldName) {
		for (int i = x - 2; i < x + 2; i++) {
			for (int j = y - 2; j < y + 2; j++) {
				for (int k = z - 2; k < z + 2; k++) {
					this.isProtectedCache.remove(new NLocation(worldName, x, y, z));
				}
			}
		}
	}

	/**
	 * Detect if a Block can be broken by a Player or by something else (Explosion...)
	 *
	 * @param b      the Block to be broken
	 * @param player the Player that want to break the Block, if there is one, null otherwise
	 *
	 * @return true if the block can be broken [by the Player], false otherwise
	 */
	public boolean canBreak(final Block b, final Player player) {
		final Material blockType = b.getType();
		final String userId = player != null ? PlayerIdsUtils.getId(player.getName()) : null;
		if (blockType == Material.SIGN_POST || blockType == Material.WALL_SIGN) {
			final Sign sign = (Sign) b.getState();
			return !sign.getLine(0).equals(PROTECTION) ||
			       player != null && ColorUtils.stripColorCodes(sign.getLine(3)).equals(userId) ||
			       player != null && Perms.hasProtectionSignBreak(player);
		} else {
			final List<Sign> signLines;
			if (blockType == Material.CHEST || blockType == Material.TRAPPED_CHEST) {
				signLines = SignUtils.getSignsForChest(b);
			} else if (getProtectedMaterials().contains(blockType)) {
				signLines = SignUtils.getSignsForBlock(b);
			} else {
				return true;
			}
			for (final Sign sign : signLines) {
				if (sign.getLine(0).equals(PROTECTION)) {
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
		final String userId = PlayerIdsUtils.getId(player.getName());
		final List<Sign> signLines;
		if (blockType == Material.CHEST || blockType == Material.TRAPPED_CHEST) {
			signLines = SignUtils.getSignsForChest(b);
		} else if (getProtectedMaterials().contains(blockType)) {
			signLines = SignUtils.getSignsForBlock(b);
		} else {
			return true;
		}
		boolean protectedBySign = false;
		boolean explicitlyAllowed = false;
		for (final Sign sign : signLines) {
			if (sign.getLine(0).equals(PROTECTION)) {
				protectedBySign = true;
				if (ColorUtils.stripColorCodes(sign.getLine(1)).equals(userId) ||
				    ColorUtils.stripColorCodes(sign.getLine(2)).equals(userId) ||
				    ColorUtils.stripColorCodes(sign.getLine(3)).equals(userId)) {
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
	public boolean protectsSomething(final Location l) {
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
		final NLocation loc = new NLocation(b.getLocation());
		final ProtectionState state = isProtectedCache.get(loc);
		if (state != null && state.timeout > System.currentTimeMillis()) {
			return state.protectedBy;
		} else {
			final Material blockType = b.getType();
			String result = null;
			List<Sign> signLines = null;
			if (blockType == Material.CHEST || blockType == Material.TRAPPED_CHEST) {
				signLines = SignUtils.getSignsForChest(b);
			} else if (getProtectedMaterials().contains(blockType)) {
				signLines = SignUtils.getSignsForBlock(b);
			}
			if (signLines != null) {
				for (final Sign sign : signLines) {
					if (PROTECTION.equals(sign.getLine(0))) {
						result = ColorUtils.stripColorCodes(sign.getLine(3));
						break;
					}
				}
			}
			isProtectedCache.put(loc, new ProtectionState(result, System.currentTimeMillis() + CACHE_TIME));
			return result;
		}
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
	 * otherwise
	 */
	public boolean canPlaceSign(final String playerName, final Location loc) {
		final World w = loc.getWorld();
		final int x = loc.getBlockX();
		final int y = loc.getBlockY();
		final int z = loc.getBlockZ();

		final String userId = PlayerIdsUtils.getId(playerName);
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
