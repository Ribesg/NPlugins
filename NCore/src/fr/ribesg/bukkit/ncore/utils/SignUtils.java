package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

/** Some tools to play with Signs */
public class SignUtils {

	/** The ID of a Post Sign block */
	private static final int SIGN_POST_ID = Material.SIGN_POST.getId();
	/** The ID of a Wall Sign block */
	private static final int WALL_SIGN_ID = Material.WALL_SIGN.getId();

	/** The ID of a Chest block */
	private static final int CHEST_ID         = Material.CHEST.getId();
	/** The ID of a Trapped Chest block */
	private static final int TRAPPED_CHEST_ID = Material.TRAPPED_CHEST.getId();

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
	public static List<String[]> getSigns(final World w, final int x, final int y, final int z) {

		List<String[]> result = new ArrayList<>(6);

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
	public static List<String[]> getSignsForBlock(Block b) {
		return getSignsForLocation(b.getLocation());
	}

	/**
	 * Returns all signs adjacent to the provided Location
	 *
	 * @param l Location considered
	 *
	 * @return List of Signs adjacent to provided Location
	 */
	public static List<String[]> getSignsForLocation(Location l) {
		return getSigns(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}

	/**
	 * Returns all signs adjacent to the provided Chest, handling the double-chest case
	 *
	 * @param b Block considered. Should be a Chest or a TrappedChest.
	 *
	 * @return List of Signs adjacent to provided Chest
	 */
	public static List<String[]> getSignsForChest(Block b) {
		int blockId = b.getTypeId();

		if (blockId != CHEST_ID && blockId != TRAPPED_CHEST_ID) {
			throw new IllegalArgumentException("Not a Chest: " + b.getType());
		}

		final World w = b.getWorld();
		final int x = b.getLocation().getBlockX();
		final int y = b.getLocation().getBlockY();
		final int z = b.getLocation().getBlockZ();

		List<String[]> result = new ArrayList<>(10);

		// Relative < -1 ; 0 ; 0 >
		if (checkAddSignChest(result, w, x, y, z, -1, 0, 0, blockId)) {
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
		if (checkAddSignChest(result, w, x, y, z, 1, 0, 0, blockId)) {
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
		if (checkAddSignChest(result, w, x, y, z, 0, 0, -1, blockId)) {
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
		if (checkAddSignChest(result, w, x, y, z, 0, 0, 1, blockId)) {
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
	private static void checkAddSign(List<String[]> list, World w, int x, int y, int z, int rX, int rY, int rZ) {
		int id = w.getBlockTypeIdAt(x + rX, y + rY, z + rZ);
		if (id == SIGN_POST_ID || id == WALL_SIGN_ID) {
			list.add(((Sign) w.getBlockAt(x + rX, y + rY, z + rZ).getState()).getLines());
		}
	}

	/**
	 * Checks if the block at ( x+rX ; y+rY ; z+rZ ) is a Sign. Adds found Sign block to list.
	 * <p/>
	 * Compared to {@link #checkAddSign(java.util.List, org.bukkit.World, int, int, int, int, int, int)}, this
	 * method returns a boolean that indicates if the block found was of provided type chestId or not.
	 * It's used to consider double chest (a Chest next to a Trapped Chest is not a Double Chest).
	 *
	 * @param list    List containing Signs
	 * @param w       World considered
	 * @param x       X origin
	 * @param y       Y origin
	 * @param z       Z origin
	 * @param rX      X relative coordinate
	 * @param rY      Y relative coordinate
	 * @param rZ      Z relative coordinate
	 * @param chestId Id of the type of Chest to find (Chest or Trapped Chest)
	 *
	 * @return True if block found was of provided type chestId, false otherwise
	 */
	private static boolean checkAddSignChest(List<String[]> list, World w, int x, int y, int z, int rX, int rY, int rZ, int chestId) {
		int id = w.getBlockTypeIdAt(x + rX, y + rY, z + rZ);
		if (id == SIGN_POST_ID || id == WALL_SIGN_ID) {
			list.add(((Sign) w.getBlockAt(x + rX, y + rY, z + rZ).getState()).getLines());
		} else if (id == chestId) {
			return true;
		}
		return false;
	}
}
