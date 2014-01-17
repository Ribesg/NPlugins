/***************************************************************************
 * Project file:    NPlugins - NCore - SignUtils.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.SignUtils                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class containing some tools to play with Signs.
 *
 * @author Ribesg
 */
public class SignUtils {

	/**
	 * Checks if a block is a Sign.
	 *
	 * @param b the block to check
	 *
	 * @return true if the provided block is a sign, false otherwise
	 */
	public static boolean isSign(final Block b) {
		return b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN;
	}

	/**
	 * Returns all signs adjacent to the provided coordinates in the provided world
	 *
	 * @param w World considered
	 * @param x X coordinate of the Location
	 * @param y Y coordinate of the Location
	 * @param z Z coordinate of the Location
	 *
	 * @return List of Signs adjacent to provided Location
	 */
	public static List<Sign> getSigns(final World w, final int x, final int y, final int z) {

		final List<Sign> result = new ArrayList<>(6);

		// Relative < -1 ; 0 ; 0 >
		checkAddSign(result, w, x, y, z, -1, 0, 0);

		// Relative < 1 ; 0 ; 0 >
		checkAddSign(result, w, x, y, z, 1, 0, 0);

		// Relative < 0 ; -1 ; 0 >
		checkAddSign(result, w, x, y, z, 0, -1, 0);

		// Relative < 0 ; 1 ; 0 >
		checkAddSign(result, w, x, y, z, 0, 1, 0);

		// Relative < 0 ; 0 ; -1 >
		checkAddSign(result, w, x, y, z, 0, 0, -1);

		// Relative < 0 ; 0 ; 1 >
		checkAddSign(result, w, x, y, z, 0, 0, 1);

		return result;
	}

	/**
	 * Returns all signs adjacent to the provided Block
	 *
	 * @param b Block considered
	 *
	 * @return List of Signs adjacent to provided Block
	 */
	public static List<Sign> getSignsForBlock(final Block b) {
		return getSignsForLocation(b.getLocation());
	}

	/**
	 * Returns all signs adjacent to the provided Location
	 *
	 * @param l Location considered
	 *
	 * @return List of Signs adjacent to provided Location
	 */
	public static List<Sign> getSignsForLocation(final Location l) {
		return getSigns(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}

	/**
	 * Returns all signs adjacent to the provided Chest, handling the double-chest case
	 *
	 * @param b Block considered. Should be a Chest or a TrappedChest.
	 *
	 * @return List of Signs adjacent to provided Chest
	 */
	public static List<Sign> getSignsForChest(final Block b) {
		final Material blockMat = b.getType();

		if (blockMat != Material.CHEST && blockMat != Material.TRAPPED_CHEST) {
			throw new IllegalArgumentException("Not a Chest: " + b.getType());
		}

		final World w = b.getWorld();
		final int x = b.getLocation().getBlockX();
		final int y = b.getLocation().getBlockY();
		final int z = b.getLocation().getBlockZ();

		final List<Sign> result = new ArrayList<>(10);

		// Relative < -1 ; 0 ; 0 >
		if (checkAddSignChest(result, w, x, y, z, -1, 0, 0, blockMat)) {
			// Relative < -2 ; 0 ; 0 >
			checkAddSign(result, w, x, y, z, -2, 0, 0);

			// Relative < -1 ; -1 ; 0 >
			checkAddSign(result, w, x, y, z, -1, -1, 0);

			// Relative < -1 ; 1 ; 0 >
			checkAddSign(result, w, x, y, z, -1, 1, 0);

			// Relative < -1 ; 0 ; -1 >
			checkAddSign(result, w, x, y, z, -1, 0, -1);

			// Relative < -1 ; 0 ; 1 >
			checkAddSign(result, w, x, y, z, -1, 0, 1);
		}

		// Relative < 1 ; 0 ; 0 >
		if (checkAddSignChest(result, w, x, y, z, 1, 0, 0, blockMat)) {
			// Relative < 2 ; 0 ; 0 >
			checkAddSign(result, w, x, y, z, 2, 0, 0);

			// Relative < 1 ; -1 ; 0 >
			checkAddSign(result, w, x, y, z, 1, -1, 0);

			// Relative < 1 ; 1 ; 0 >
			checkAddSign(result, w, x, y, z, 1, 1, 0);

			// Relative < 1 ; 0 ; -1 >
			checkAddSign(result, w, x, y, z, 1, 0, -1);

			// Relative < 1 ; 0 ; 1 >
			checkAddSign(result, w, x, y, z, 1, 0, 1);
		}

		// Relative < 0 ; -1 ; 0 >
		checkAddSign(result, w, x, y, z, 0, -1, 0);

		// Relative < 0 ; 1 ; 0 >
		checkAddSign(result, w, x, y, z, 0, 1, 0);

		// Relative < 0 ; 0 ; -1 >
		if (checkAddSignChest(result, w, x, y, z, 0, 0, -1, blockMat)) {
			// Relative < 0 ; 0 ; -2 >
			checkAddSign(result, w, x, y, z, 0, 0, -2);

			// Relative < 0 ; -1 ; -1 >
			checkAddSign(result, w, x, y, z, 0, -1, -1);

			// Relative < 0 ; 1 ; -1 >
			checkAddSign(result, w, x, y, z, 0, 1, -1);

			// Relative < -1 ; 0 ; -1 >
			checkAddSign(result, w, x, y, z, -1, 0, -1);

			// Relative < 1 ; 0 ; -1 >
			checkAddSign(result, w, x, y, z, 1, 0, -1);
		}

		// Relative < 0 ; 0 ; 1 >
		if (checkAddSignChest(result, w, x, y, z, 0, 0, 1, blockMat)) {
			// Relative < 0 ; 0 ; -2 >
			checkAddSign(result, w, x, y, z, 0, 0, 2);

			// Relative < 0 ; -1 ; -1 >
			checkAddSign(result, w, x, y, z, 0, -1, 1);

			// Relative < 0 ; 1 ; -1 >
			checkAddSign(result, w, x, y, z, 0, 1, 1);

			// Relative < -1 ; 0 ; -1 >
			checkAddSign(result, w, x, y, z, -1, 0, 1);

			// Relative < 1 ; 0 ; -1 >
			checkAddSign(result, w, x, y, z, 1, 0, 1);
		}

		return result;
	}

	/**
	 * Gets a list of Blocks around the provided Location. Returned blocks
	 * types are contained in the filter Set if not null.
	 * <p/>
	 * This is useful for getting all Chest near a Sign for example, but this
	 * could also be used for anything else.
	 *
	 * @param w      World considered
	 * @param x      X origin
	 * @param y      Y origin
	 * @param z      Z origin
	 * @param filter Materials considered, null if all
	 *
	 * @return a List of Blocks around this Location
	 */
	public static List<Block> getBlocksForLocation(final World w, final int x, final int y, final int z, final Set<Material> filter) {
		final List<Block> result = new ArrayList<>();

		Block b = w.getBlockAt(x - 1, y, z);
		if (filter == null || filter.contains(b.getType())) {
			result.add(b);
		}

		b = w.getBlockAt(x + 1, y, z);
		if (filter == null || filter.contains(b.getType())) {
			result.add(b);
		}

		b = w.getBlockAt(x, y - 1, z);
		if (filter == null || filter.contains(b.getType())) {
			result.add(b);
		}

		b = w.getBlockAt(x, y + 1, z);
		if (filter == null || filter.contains(b.getType())) {
			result.add(b);
		}

		b = w.getBlockAt(x, y, z - 1);
		if (filter == null || filter.contains(b.getType())) {
			result.add(b);
		}

		b = w.getBlockAt(x, y, z + 1);
		if (filter == null || filter.contains(b.getType())) {
			result.add(b);
		}

		return result;
	}

	/**
	 * Gets a list of Blocks around the provided Location. Returned blocks
	 * types are contained in the filter Set if not null.
	 * <p/>
	 * This is useful for getting all Chest near a Sign for example, but this
	 * could also be used for anything else.
	 *
	 * @param block  Block considered
	 * @param filter Materials considered, null if all
	 *
	 * @return a List of Blocks around this Location
	 */
	public static List<Block> getBlocksForBlock(final Block block, final Set<Material> filter) {
		return getBlocksForLocation(block.getLocation(), filter);
	}

	/**
	 * Gets a list of Blocks around the provided Location. Returned blocks
	 * types are contained in the filter Set if not null.
	 * <p/>
	 * This is useful for getting all Chest near a Sign for example, but this
	 * could also be used for anything else.
	 *
	 * @param loc    Location considered
	 * @param filter Materials considered, null if all
	 *
	 * @return a List of Blocks around this Location
	 */
	public static List<Block> getBlocksForLocation(final Location loc, final Set<Material> filter) {
		return getBlocksForLocation(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), filter);
	}

	/**
	 * Checks if the block at ( x+rX ; y+rY ; z+rZ ) is a Sign. Adds found Sign block to list.
	 *
	 * @param list List containing Signs
	 * @param w    World considered
	 * @param x    X origin
	 * @param y    Y origin
	 * @param z    Z origin
	 * @param rX   X relative coordinate
	 * @param rY   Y relative coordinate
	 * @param rZ   Z relative coordinate
	 */
	private static void checkAddSign(final List<Sign> list, final World w, final int x, final int y, final int z, final int rX, final int rY, final int rZ) {
		final Material m = w.getBlockAt(x + rX, y + rY, z + rZ).getType();
		if (m == Material.SIGN_POST || m == Material.WALL_SIGN) {
			list.add((Sign) w.getBlockAt(x + rX, y + rY, z + rZ).getState());
		}
	}

	/**
	 * Checks if the block at ( x+rX ; y+rY ; z+rZ ) is a Sign. Adds found Sign block to list.
	 * <p/>
	 * Compared to {@link #checkAddSign(java.util.List, org.bukkit.World, int, int, int, int, int, int)}, this
	 * method returns a boolean that indicates if the block found was of provided type chestId or not.
	 * It's used to consider double chest (a Chest next to a Trapped Chest is not a Double Chest).
	 *
	 * @param list     List containing Signs
	 * @param w        World considered
	 * @param x        X origin
	 * @param y        Y origin
	 * @param z        Z origin
	 * @param rX       X relative coordinate
	 * @param rY       Y relative coordinate
	 * @param rZ       Z relative coordinate
	 * @param blockMat Material of the Chest to find (Chest or Trapped Chest)
	 *
	 * @return True if block found was of provided type chestId, false otherwise
	 */
	private static boolean checkAddSignChest(final List<Sign> list, final World w, final int x, final int y, final int z, final int rX, final int rY, final int rZ, final Material blockMat) {
		final Material m = w.getBlockAt(x + rX, y + rY, z + rZ).getType();
		if (m == Material.SIGN_POST || m == Material.WALL_SIGN) {
			list.add((Sign) w.getBlockAt(x + rX, y + rY, z + rZ).getState());
		} else if (m == blockMat) {
			return true;
		}
		return false;
	}
}
