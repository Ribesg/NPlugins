/***************************************************************************
 * Project file:    NPlugins - NCore - EnchantmentUtil.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.util.inventory.EnchantmentUtil  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util.inventory;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EnchantmentUtil {

	/**
	 * A comparator to sort Enchantments by name
	 */
	static final Comparator<Enchantment> ENCHANTMENT_COMPARATOR = new Comparator<Enchantment>() {

		@Override
		public int compare(final Enchantment a, final Enchantment b) {
			return a.getName().compareTo(b.getName());
		}
	};

	/**
	 * Creates a String representing this ItemStack's enchantments.
	 *
	 * @param is         the ItemStack
	 * @param separators the separators to use
	 *
	 * @return a String representing the provided ItemStack's enchantments
	 */
	public static String toString(final ItemStack is, final String[] separators) {
		if (is.getEnchantments().isEmpty()) {
			return "";
		} else {
			final StringBuilder enchantmentsStringBuilder = new StringBuilder();
			final Map<Enchantment, Integer> sortedEnchantmentMap = new TreeMap<>(ENCHANTMENT_COMPARATOR);
			sortedEnchantmentMap.putAll(is.getEnchantments());
			for (final Map.Entry<Enchantment, Integer> e : sortedEnchantmentMap.entrySet()) {
				enchantmentsStringBuilder.append(e.getKey().getName());
				enchantmentsStringBuilder.append(separators[2]);
				enchantmentsStringBuilder.append(e.getValue());
				enchantmentsStringBuilder.append(separators[1]);
			}
			return enchantmentsStringBuilder.substring(0, enchantmentsStringBuilder.length() - separators[1].length());
		}
	}

	/**
	 * Parses a String representing a map of enchantments.
	 *
	 * @param string     a String representing a map of enchantments
	 * @param separators the separators to use
	 *
	 * @return a map of enchantments and their associated levels
	 *
	 * @throws InventoryUtilException
	 */
	public static Map<Enchantment, Integer> fromString(final String string, final String[] separators) throws InventoryUtilException {
		final Map<Enchantment, Integer> enchantments = new TreeMap<>(ENCHANTMENT_COMPARATOR);
		if (!string.isEmpty()) {
			final String[] enchantmentsPairs = StringUtil.splitKeepEmpty(string, separators[1]);
			for (final String enchantmentPair : enchantmentsPairs) {
				final String[] enchantmentPairSplit = StringUtil.splitKeepEmpty(enchantmentPair, separators[2]);
				if (enchantmentPairSplit.length != 2) {
					throw new InventoryUtilException("Malformed Enchantments field '" + string + "'");
				} else {
					final String enchantmentName = enchantmentPairSplit[0];
					final String enchantmentLevel = enchantmentPairSplit[1];
					final Enchantment enchantment = getEnchantment(enchantmentName);
					if (enchantment == null) {
						throw new InventoryUtilException("Unknown Enchantment '" + enchantmentName + "' (parsing '\" + string + \"')\"");
					}
					try {
						final int level = Integer.parseInt(enchantmentLevel);
						if (level < 1) {
							throw new InventoryUtilException("Invalid enchantment level '" + level + "' for enchantment '" + enchantment.getName() + "' (parsing '\" + string + \"')\"");
						}
						enchantments.put(enchantment, level);
					} catch (final NumberFormatException e) {
						throw new InventoryUtilException("Invalid level value '" + enchantmentLevel + "' for enchantment '" + enchantment.getName() + "' (parsing '" + string + "')");
					}
				}
			}
		}
		return enchantments;
	}

	public static void saveToConfigSection(final ConfigurationSection itemSection, final ItemStack is) {
		final ConfigurationSection enchantmentsSection = itemSection.createSection("enchantments");
		for (final Map.Entry<Enchantment, Integer> e : is.getEnchantments().entrySet()) {
			enchantmentsSection.set(e.getKey().getName(), e.getValue());
		}
	}

	public static Map<Enchantment, Integer> loadFromConfigSection(final ConfigurationSection itemSection) throws InventoryUtilException {
		final Map<Enchantment, Integer> result = new HashMap<>();
		if (itemSection.isConfigurationSection("enchantments")) {
			final ConfigurationSection enchantmentsSection = itemSection.getConfigurationSection("enchantments");
			for (final String enchantmentName : enchantmentsSection.getKeys(false)) {
				final Enchantment enchantment = getEnchantment(enchantmentName);
				final int level = enchantmentsSection.getInt(enchantmentName, -1);
				if (level < 1) {
					throw new InventoryUtilException("Invalid enchantment level '" + level + "' for enchantment '" + enchantment.getName() + "'");
				} else {
					result.put(enchantment, level);
				}
			}
		}
		return result;
	}

	/**
	 * Gets an Enchantment from a String, if able to recognize anything in
	 * the String. Checks for ID and Enchantment name value.
	 *
	 * @param enchantmentName the String representing an Enchantment
	 *
	 * @return the associated Enchantment or null if not found
	 */
	public static Enchantment getEnchantment(final String enchantmentName) {
		Enchantment result = Enchantment.getByName(enchantmentName);
		if (result == null) {
			try {
				result = Enchantment.getById(Integer.parseInt(enchantmentName));
			} catch (final NumberFormatException e) {
				return null;
			}
		}
		return result;
	}

}
