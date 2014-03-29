/***************************************************************************
 * Project file:    NPlugins - NCore - InventoryUtils.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.inventory.InventoryUtils  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils.inventory;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
	 * @return the stacked and sorted items
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
	 * @return the stacked and sorted items
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
	public static String toString(final ItemStack[] itemStacks) throws InventoryUtilException {
		final List<String> strings = new ArrayList<>(itemStacks.length);
		for (final ItemStack is : itemStacks) {
			if (is == null) {
				strings.add((""));
			} else {
				try {
					strings.add(ItemStackUtils.toString(is));
				} catch (final InventoryUtilException e) {
					throw new InventoryUtilException("Invalid item in provided array", e);
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
	public static ItemStack[] fromString(final String string) throws InventoryUtilException {
		final String separator = string.substring(0, 4);
		final String[] items = StringUtils.splitKeepEmpty(string, separator);
		final ItemStack[] result = new ItemStack[items.length - 1];
		for (int i = 1; i < items.length; i++) {
			try {
				result[i - 1] = items[i].isEmpty() ? null : ItemStackUtils.fromString(items[i]);
			} catch (final InventoryUtilException e) {
				throw new InventoryUtilException("Invalid item string provided ('" + string + "')", e);
			}
		}
		return result;
	}

	/**
	 * Saves an array of ItemStacks to a Configuration file
	 *
	 * @param parent     the parent section under which another section for
	 *                   the ItemStacks will be created
	 * @param key        the key for the ItemStacks section
	 * @param itemStacks the ItemStack array to save
	 *
	 * @throws InventoryUtilException if something goes wrong
	 */
	public static void saveToConfigSection(final ConfigurationSection parent, final String key, final ItemStack[] itemStacks) throws InventoryUtilException {
		final ConfigurationSection itemsSection = parent.createSection(key);
		itemsSection.set("size", itemStacks.length);
		for (int i = 0; i < itemStacks.length; i++) {
			final ItemStack is = itemStacks[i];
			if (is != null) {
				ItemStackUtils.saveToConfigSection(itemsSection, Integer.toString(i), is);
			}
		}
	}

	/**
	 * Loads an array of ItemStacks from a Configuration File
	 *
	 * @param parent the parent section under which another section for
	 *               the ItemStacks exists
	 * @param key    the key of the ItemStacks section
	 *
	 * @return an array of ItemStacks found in this Configuration File
	 *
	 * @throws InventoryUtilException if something goes wrong
	 */
	public static ItemStack[] loadFromConfigSection(final ConfigurationSection parent, final String key) throws InventoryUtilException {
		final ConfigurationSection itemsSection = parent.getConfigurationSection(key);
		final ItemStack[] result = new ItemStack[itemsSection.getInt("size")];
		for (final String isKey : itemsSection.getKeys(false)) {
			final ItemStack is = ItemStackUtils.loadFromConfig(itemsSection, isKey);
			result[Integer.parseInt(isKey)] = is;
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
	public static String toString(final Inventory inventory) throws InventoryUtilException {
		return toString(inventory.getContents());
	}

	/**
	 * Sets the provided inventory's content to the provided Inventory String
	 * representation.
	 *
	 * @param inventoryToSet the inventory to modify
	 * @param string         the inventory representation to deserialize
	 *
	 * @throws InventoryUtilException if the provided String deserialization does not match the provided
	 *                                Inventory's size
	 */
	public static void setFromString(final Inventory inventoryToSet, final String string) throws InventoryUtilException {
		final ItemStack[] fromString = fromString(string);
		if (inventoryToSet.getSize() != fromString.length) {
			throw new InventoryUtilException("String size (" + fromString.length + ") does not match inventory size (" + inventoryToSet.getSize() + ")");
		}
		inventoryToSet.setContents(fromString);
	}

	/**
	 * Saves an Inventory to a Configuration file
	 *
	 * @param parent    the parent section under which another section for
	 *                  the Inventory will be created
	 * @param key       the key for the Inventory section
	 * @param inventory the Inventory to save
	 *
	 * @throws InventoryUtilException if something goes wrong
	 */
	public static void saveToConfigSection(final ConfigurationSection parent, final String key, final Inventory inventory) throws InventoryUtilException {
		saveToConfigSection(parent, key, inventory.getContents());
	}

	/**
	 * Loads an Inventory from a Configuration File
	 *
	 * @param parent the parent section under which another section for
	 *               the Inventory exists
	 * @param key    the key of the Inventory section
	 *
	 * @throws InventoryUtilException if something goes wrong
	 */
	public static void setFromConfigSection(final ConfigurationSection parent, final String key, final Inventory inventory) throws InventoryUtilException {
		final ItemStack[] items = loadFromConfigSection(parent, key);
		inventory.setContents(items);
	}
}
