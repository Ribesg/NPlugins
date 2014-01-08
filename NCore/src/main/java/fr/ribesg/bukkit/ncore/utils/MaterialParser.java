/***************************************************************************
 * Project file:    NPlugins - NCore - MaterialParser.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.MaterialParser            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This little lib is used to get an ItemStack from a String.
 *
 * @author Ribesg
 */
public class MaterialParser {

	private static final Logger LOGGER = Logger.getLogger(MaterialParser.class.getName());

	/**
	 * Gets an ItemStack from a Item Description String.
	 * <p/>
	 * Item Description String format: [field][;;field]*
	 * Every field is mandatory but can be empty. First field obviously
	 * cannot be empty.
	 * <p/>
	 * List of available fields:
	 * <p/>
	 * First field:
	 * - Material name as defined in Bukkit's Material enum
	 * - Material ID
	 * <p/>
	 * Second field:
	 * - Material Data byte
	 * - Durability
	 * - Empty for 0
	 * <p/>
	 * Third field:
	 * - Amount
	 * <p/>
	 * Fourth field:
	 * - List of Enchantments, separated by ','. ID or name as defined in
	 * Bukkit's Enchantment enum + ':' + level.
	 * <p/>
	 * Fifth field:
	 * - Item Name if non-default
	 * <p/>
	 * Sixth field:
	 * - Item Lore, list separated by the first 2 chars in the field's String
	 *
	 * @param itemString the String representing the ItemStack
	 *
	 * @return an ItemStack matching the provided itemString, or null
	 */
	public static ItemStack get(final String itemString) {
		final String[] parts = itemString.split(";;");
		if (parts.length != 6) {
			error(itemString, "Invalid amount of fields");
			return null;
		}

		final String idString = parts[0];
		final String dataString = parts[1];
		final String amountString = parts[2];
		final String enchantmentsString = parts[3];
		final String nameString = parts[4];
		final String loreString = parts[5];

		final Material id;
		final Short data;
		final Integer amount;
		Map<Enchantment, Integer> enchantments = null;
		String name = null;
		List<String> lore = null;

		if (idString.isEmpty()) {
			error(itemString, "Id is mandatory");
			return null;
		} else {
			id = getMaterial(idString);
			if (id == null) {
				error(itemString, "Unknown id '" + idString + "'");
				return null;
			}
		}

		if (dataString.isEmpty()) {
			data = 0;
		} else {
			try {
				data = Short.parseShort(dataString);
			} catch (final NumberFormatException e) {
				error(itemString, "Invalid data value '" + dataString + "'");
				return null;
			}
		}

		if (amountString.isEmpty()) {
			amount = 1;
		} else {
			try {
				amount = Integer.parseInt(amountString);
			} catch (final NumberFormatException e) {
				error(itemString, "Invalid amount value '" + amountString + "'");
				return null;
			}
		}

		if (!enchantmentsString.isEmpty()) {
			enchantments = new HashMap<>();
			final String[] enchantmentsPairs = enchantmentsString.split(",");
			for (final String enchantmentPair : enchantmentsPairs) {
				final String[] enchantmentPairSplit = enchantmentPair.split(":");
				if (enchantmentPairSplit.length != 2) {
					error(itemString, "Malformed Enchantments field '" + enchantmentsString + "'");
					return null;
				} else {
					final String enchantmentName = enchantmentPairSplit[0];
					final String enchantmentLevel = enchantmentPairSplit[1];
					final Enchantment enchantment = getEnchantment(enchantmentName);
					if (enchantment == null) {
						error(itemString, "Unknown Enchantment '" + enchantmentName + "'");
						return null;
					}
					try {
						final int level = Integer.parseInt(enchantmentLevel);
						if (level < 1) {
							error(itemString, "Invalid enchantment level '" + level + "' for enchantment '" + enchantment.getName() + "'");
							return null;
						}
						enchantments.put(enchantment, level);
					} catch (final NumberFormatException e) {
						error(itemString, "Invalid level value '" + enchantmentLevel + "' for enchantment '" + enchantment.getName() + "'");
						return null;
					}
				}
			}
		}

		if (!nameString.isEmpty()) {
			name = nameString;
		}

		if (loreString.length() > 1) {
			lore = new ArrayList<>();
			final String separator = loreString.substring(0, 2);
			Collections.addAll(lore, loreString.split(separator));
		}

		final ItemStack is = new ItemStack(id, amount, data);
		if (enchantments != null) {
			is.getEnchantments().putAll(enchantments);
		}
		final ItemMeta meta = is.getItemMeta();
		if (name != null) {
			meta.setDisplayName(name);
		}
		if (lore != null) {
			meta.setLore(lore);
		}
		is.setItemMeta(meta);

		return is;
	}

	private static Material getMaterial(final String idString) {
		Material result = Material.matchMaterial(idString);
		if (result == null) {
			try {
				result = Material.getMaterial(Integer.parseInt(idString));
			} catch (final NumberFormatException e) {
				return null;
			}
		}
		return result;
	}

	private static Enchantment getEnchantment(final String enchantmentName) {
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

	private static void error(final String itemString, final String reason) {
		LOGGER.severe("Error while parsing itemString:");
		LOGGER.severe(itemString);
		LOGGER.severe(reason);
	}
}
