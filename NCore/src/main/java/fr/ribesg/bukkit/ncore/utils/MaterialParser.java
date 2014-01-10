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
import org.bukkit.configuration.ConfigurationSection;
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
	 * Gets an ItemStack from an Item Description String.
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
	public static ItemStack fromString(final String itemString) throws MaterialParserException {
		final String[] parts = itemString.split(";;");
		if (parts.length != 6) {
			throw new MaterialParserException(itemString, "Invalid amount of fields");
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
			throw new MaterialParserException(itemString, "Id is mandatory");
		} else {
			id = getMaterial(idString);
			if (id == null) {
				throw new MaterialParserException(itemString, "Unknown id '" + idString + "'");
			}
		}

		if (dataString.isEmpty()) {
			data = 0;
		} else {
			try {
				data = Short.parseShort(dataString);
			} catch (final NumberFormatException e) {
				throw new MaterialParserException(itemString, "Invalid data value '" + dataString + "'");
			}
		}

		if (amountString.isEmpty()) {
			amount = 1;
		} else {
			try {
				amount = Integer.parseInt(amountString);
			} catch (final NumberFormatException e) {
				throw new MaterialParserException(itemString, "Invalid amount value '" + amountString + "'");
			}
		}

		if (!enchantmentsString.isEmpty()) {
			enchantments = new HashMap<>();
			final String[] enchantmentsPairs = enchantmentsString.split(",");
			for (final String enchantmentPair : enchantmentsPairs) {
				final String[] enchantmentPairSplit = enchantmentPair.split(":");
				if (enchantmentPairSplit.length != 2) {
					throw new MaterialParserException(itemString, "Malformed Enchantments field '" + enchantmentsString + "'");
				} else {
					final String enchantmentName = enchantmentPairSplit[0];
					final String enchantmentLevel = enchantmentPairSplit[1];
					final Enchantment enchantment = getEnchantment(enchantmentName);
					if (enchantment == null) {
						throw new MaterialParserException(itemString, "Unknown Enchantment '" + enchantmentName + "'");
					}
					try {
						final int level = Integer.parseInt(enchantmentLevel);
						if (level < 1) {
							throw new MaterialParserException(itemString, "Invalid enchantment level '" +
							                                              level +
							                                              "' for enchantment '" +
							                                              enchantment.getName() +
							                                              "'");
						}
						enchantments.put(enchantment, level);
					} catch (final NumberFormatException e) {
						throw new MaterialParserException(itemString, "Invalid level value '" +
						                                              enchantmentLevel +
						                                              "' for enchantment '" +
						                                              enchantment.getName() +
						                                              "'");
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
			is.addUnsafeEnchantments(enchantments);
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

	/**
	 * Saves an ItemStack description under the provided configuration
	 * section, with the provided key.
	 *
	 * @param parentSection the parent section of the ItemStack description
	 * @param key           the key for the section of the ItemStack
	 *                      description
	 * @param is            the ItemStack to save
	 */
	public static void saveToConfigSection(final ConfigurationSection parentSection, final String key, final ItemStack is) {
		final ConfigurationSection itemSection = parentSection.createSection(key);

		itemSection.set("id", is.getType().name());
		itemSection.set("data", is.getDurability());
		itemSection.set("amount", is.getAmount());

		if (!is.getEnchantments().isEmpty()) {
			final ConfigurationSection enchantmentsSection = itemSection.createSection("enchantments");
			for (final Map.Entry<Enchantment, Integer> e : is.getEnchantments().entrySet()) {
				enchantmentsSection.set(e.getKey().getName(), e.getValue());
			}
		}

		if (is.hasItemMeta()) {
			final ItemMeta meta = is.getItemMeta();
			if (meta.hasDisplayName()) {
				itemSection.set("displayName", meta.getDisplayName());
			}
			if (meta.hasLore()) {
				itemSection.set("lore", meta.getLore());
			}
		}
	}

	/**
	 * Loads an ItemStack from a parent configuration section and the key
	 * for the ItemStack under this parent configuration section.
	 *
	 * @param parentSection the parent section of the ItemStack description
	 * @param key           the key for the section of the ItemStack
	 *                      description
	 *
	 * @return the ItemStack saved under parentSection.key
	 *
	 * @throws MaterialParserException if the ItemStack description is malformed
	 */
	public static ItemStack loadFromConfig(final ConfigurationSection parentSection, final String key) throws MaterialParserException {
		final ConfigurationSection itemSection = parentSection.getConfigurationSection(key);
		final String parsed = "Configuration file, under " + parentSection.getCurrentPath() + '.' + key;

		final Material id = getMaterial(itemSection.getString("id", ""));
		if (id == null) {
			throw new MaterialParserException(parsed, "Id is mandatory");
		}

		final short data = (short) itemSection.getInt("data", 0);

		final int amount = itemSection.getInt("amount", 1);

		Map<Enchantment, Integer> enchantmentsMap = null;
		if (itemSection.isConfigurationSection("enchantments")) {
			final ConfigurationSection enchantmentsSection = itemSection.getConfigurationSection("enchantments");
			enchantmentsMap = new HashMap<>();
			for (final String enchantmentName : enchantmentsSection.getKeys(false)) {
				final Enchantment enchantment = getEnchantment(enchantmentName);
				final int level = enchantmentsSection.getInt(enchantmentName, -1);
				if (level < 1) {
					throw new MaterialParserException(parsed, "Invalid enchantment level '" +
					                                          level +
					                                          "' for enchantment '" +
					                                          enchantment.getName() +
					                                          "'");
				} else {
					enchantmentsMap.put(enchantment, level);
				}
			}
		}

		final String displayName = itemSection.getString("displayName", null);

		final List<String> lore = itemSection.getStringList("lore");

		final ItemStack is = new ItemStack(id, amount, data);

		if (enchantmentsMap != null) {
			is.addUnsafeEnchantments(enchantmentsMap);
		}

		final ItemMeta meta = is.getItemMeta();
		if (displayName != null) {
			meta.setDisplayName(displayName);
		}
		if (lore != null && !lore.isEmpty()) {
			meta.setLore(lore);
		}
		if (meta.hasDisplayName() || meta.hasLore()) {
			is.setItemMeta(meta);
		}

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

	public static class MaterialParserException extends Exception {

		private final String parsed;
		private final String reason;

		public MaterialParserException(final String parsed, final String reason) {
			super("Error while parsing '" + parsed + "', " + reason);
			this.parsed = parsed;
			this.reason = reason;
		}

		public MaterialParserException(final String parsed, final String reason, final Throwable origin) {
			super("Error while parsing '" + parsed + "', " + reason, origin);
			this.parsed = parsed;
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
