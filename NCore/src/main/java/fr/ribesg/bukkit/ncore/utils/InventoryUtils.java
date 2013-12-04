/***************************************************************************
 * Project file:    NPlugins - NCore - InventoryUtils.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.InventoryUtils            *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Utility class to handle Inventories.
 * Allows to stack and sort Inventories.
 */
public class InventoryUtils {

	private static Comparator<ItemStack> comparator;

	private static Comparator<ItemStack> getComparator() {
		if (comparator == null) {
			comparator = new Comparator<ItemStack>() {

				@Override
				public int compare(ItemStack o1, ItemStack o2) {
					if (o2 == null) {
						return -1;
					} else if (o1 == null) {
						return 1;
					} else {
						final int compareIds = Integer.compare(o1.getTypeId(), o2.getTypeId());
						if (compareIds != 0) {
							return compareIds;
						} else {
							return Short.compare(o1.getDurability(), o2.getDurability());
						}
					}
				}
			};
		}
		return comparator;
	}

	/**
	 * Stack and sort an array of ItemStacks.
	 * Note: The size of the returned array is guaranteed to be
	 * exactly the same as the provided array.
	 *
	 * @param items the items
	 *
	 * @return the stacked & sorted items
	 */
	public static ItemStack[] stackKeepSizeAndSort(final ItemStack[] items) {
		return sort(stackKeepSize(items));
	}

	/**
	 * Stack and sort an array of ItemStacks.
	 * WARNING: Does not keep the original size of the array.
	 *
	 * @param items the items
	 *
	 * @return the stacked & sorted items
	 */
	public static ItemStack[] stackAndSort(final ItemStack[] items) {
		return sort(stack(items));
	}

	/**
	 * Stack an array of ItemStacks together.
	 * Note: The size of the returned array is guaranteed to be
	 * exactly the same as the provided array.
	 *
	 * @param items the items
	 *
	 * @return the stacked items
	 */
	public static ItemStack[] stackKeepSize(final ItemStack[] items) {
		final ItemStack[] result = new ItemStack[items.length];
		final ItemStack[] stacked = stack(items);
		System.arraycopy(stacked, 0, result, 0, stacked.length);
		return result;
	}

	/**
	 * Stack an array of ItemStacks together.
	 * WARNING: Does not keep the original size of the array.
	 *
	 * @param items the items
	 *
	 * @return the stacked items
	 */
	public static ItemStack[] stack(final ItemStack[] items) {
		final List<ItemStack> result = new ArrayList<>(items.length);
		final Inventory tmpInventory = Bukkit.createInventory(null, 6);
		Map<Integer, ItemStack> remaining = tmpInventory.addItem(items);
		do {
			Collections.addAll(result, tmpInventory.getContents());
			tmpInventory.clear();
			remaining = tmpInventory.addItem(remaining.values().toArray(new ItemStack[remaining.size()]));
		} while (!remaining.isEmpty());
		return result.toArray(new ItemStack[result.size()]);
	}

	/**
	 * Sort an array of ItemStacks.
	 *
	 * @param items the items
	 *
	 * @return the sorted items
	 */
	public static ItemStack[] sort(final ItemStack[] items) {
		Arrays.sort(items, getComparator());
		return items;
	}

	/**
	 * Stack and sort the ItemStacks in an Inventory.
	 *
	 * @param inventory the Inventory to handle
	 */
	public static void stackAndSort(final Inventory inventory) {
		stack(inventory);
		sort(inventory);
	}

	/**
	 * Stack the ItemStacks in an Inventory.
	 *
	 * @param inventory the Inventory to handle
	 */
	public static void stack(final Inventory inventory) {
		final ItemStack[] items = inventory.getContents();
		inventory.clear();
		inventory.addItem(items);
	}

	/**
	 * Sort the ItemStacks in an Inventory.
	 *
	 * @param inventory the Inventory to handle
	 */
	public static void sort(final Inventory inventory) {
		final ItemStack[] content = inventory.getContents();
		Arrays.sort(content, getComparator());
		inventory.setContents(content);
	}
}
