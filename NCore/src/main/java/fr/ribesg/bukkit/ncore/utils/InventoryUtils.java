/***************************************************************************
 * Project file:    NPlugins - NCore - InventoryUtils.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.InventoryUtils            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
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
 *
 * @author Ribesg
 */
public class InventoryUtils {

	private static Comparator<ItemStack> comparator;

	private static Comparator<ItemStack> getComparator() {
		if (comparator == null) {
			comparator = new Comparator<ItemStack>() {

				@Override
				public int compare(final ItemStack o1, final ItemStack o2) {
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

	// #################
	// ## ItemStack[] ##
	// #################

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
	 * Serializes this array of ItemStacks to a single String.
	 * The first 4 chars of the String have to represent the separator
	 * between the different ItemStacks
	 *
	 * @param itemStacks the ItemStack array to serialize
	 *
	 * @return a single String representing this ItemStack array
	 */
	public static String toString(final ItemStack[] itemStacks) throws InventoryUtilParserException {
		final List<String> strings = new ArrayList<>(itemStacks.length);
		for (final ItemStack is : itemStacks) {
			if (is == null) {
				strings.add((""));
			} else {
				try {
					strings.add(DataUtils.toString(is));
				} catch (final DataUtils.DataUtilParserException e) {
					throw new InventoryUtilParserException(itemStacks, "Invalid item in provided array", e);
				}
			}
		}

		final String separator = StringUtils.getPossibleSeparator(strings, 4);
		final StringBuilder builder = new StringBuilder();
		for (final String s : strings) {
			builder.append(separator).append(s);
		}
		return builder.toString();
	}

	/**
	 * Deserializes this Sring as an array of ItemStacks.
	 *
	 * @param string the String to deserialize
	 *
	 * @return an array of ItemStacks
	 *
	 * @see #toString(org.bukkit.inventory.ItemStack[])
	 */
	public static ItemStack[] fromString(final String string) throws InventoryUtilParserException {
		final String separator = string.substring(0, 4);
		final String[] items = StringUtils.splitKeepEmpty(string, separator);
		final ItemStack[] result = new ItemStack[items.length - 1];
		for (int i = 1; i < items.length; i++) {
			try {
				result[i - 1] = items[i].isEmpty() ? null : DataUtils.fromString(items[i]);
			} catch (final DataUtils.DataUtilParserException e) {
				throw new InventoryUtilParserException(string, "Invalid item string provided", e);
			}
		}
		return result;
	}

	// ###############
	// ## Inventory ##
	// ###############

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

	/**
	 * Gets a String representing the provided inventory's content.
	 *
	 * @param inventory the inventory to serialize
	 *
	 * @return a String representing the provided inventory
	 */
	public static String toString(final Inventory inventory) throws InventoryUtilParserException {
		return toString(inventory.getContents());
	}

	/**
	 * Sets the provided inventory's content to the provided Inventory String
	 * representation.
	 *
	 * @param inventoryToSet the inventory to modify
	 * @param string         the inventory representation to deserialize
	 *
	 * @throws IllegalArgumentException if the provided String deserialization does not match the provided
	 *                                  Inventory's size
	 */
	public static void setFromString(final Inventory inventoryToSet, final String string) throws InventoryUtilParserException {
		final ItemStack[] fromString = fromString(string);
		if (inventoryToSet.getSize() != fromString.length) {
			throw new IllegalArgumentException("String size (" +
			                                   fromString.length +
			                                   ") does not match inventory size (" +
			                                   inventoryToSet.getSize() +
			                                   ")");
		}
		inventoryToSet.setContents(fromString);
	}

	public static class InventoryUtilParserException extends Exception {

		private final String parsed;
		private final String reason;

		public InventoryUtilParserException(final Object parsed, final String reason) {
			super("Error while parsing '" + (parsed == null ? "null" : parsed.toString()) + "', " + reason);
			this.parsed = parsed == null ? "null" : parsed.toString();
			this.reason = reason;
		}

		public InventoryUtilParserException(final Object parsed, final String reason, final Throwable origin) {
			super("Error while parsing '" + (parsed == null ? "null" : parsed.toString()) + "', " + reason, origin);
			this.parsed = parsed == null ? "null" : parsed.toString();
			this.reason = reason;
		}

		public String getParsed() {
			return parsed;
		}

		public String getReason() {
			return reason;
		}
	}
}
