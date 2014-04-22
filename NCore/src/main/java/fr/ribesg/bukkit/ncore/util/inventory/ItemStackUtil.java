/***************************************************************************
 * Project file:    NPlugins - NCore - ItemStackUtil.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.util.inventory.ItemStackUtil    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util.inventory;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

/**
 * This lib is used to get some Bukkit Enum / standard values from Strings.
 *
 * @author Ribesg
 */
public class ItemStackUtil {

	/**
	 * Different levels of Separators used for the String representation.
	 * Do not use 2-length separators as it's the length of Random
	 * Separators.
	 */
	static final String[] SEPARATORS = {
			";|;",
			",",
			":"
	};

	/**
	 * Create an ItemStack description String from an ItemStack.
	 * <p>
	 * Item Description String format: [field][;;field]{5}
	 * Every field is mandatory but can be empty. First field obviously
	 * cannot be empty.
	 * <p>
	 * List of available fields:
	 * <p>
	 * First field:
	 * - Material name as defined in Bukkit's Material enum
	 * - Material ID
	 * <p>
	 * Second field:
	 * - Material Data byte
	 * - Durability
	 * - Empty for 0
	 * <p>
	 * Third field:
	 * - Amount
	 * <p>
	 * Fourth field:
	 * - List of Enchantments, separated by ','. ID or name as defined in
	 * Bukkit's Enchantment enum + ':' + level.
	 * <p>
	 * Fifth field:
	 * - Item Name if non-default
	 * <p>
	 * Sixth field:
	 * - Item Lore, list separated by the first 2 chars in the field's String
	 * <p>
	 * Seventh field:
	 * - Special Meta field
	 *
	 * @param is the ItemStack to convert
	 *
	 * @return a String matching the provided ItemStack
	 *
	 * @see #fromString(String) to get an ItemStack from the provided String
	 */
	public static String toString(final ItemStack is) throws InventoryUtilException {
		if (is == null) {
			throw new InventoryUtilException("Null ItemStack");
		}

		final String idString = is.getType().name();
		final String dataString = Short.toString(is.getDurability());
		final String amountString = Integer.toString(Math.min(Math.max(is.getAmount(), 1), 64));
		final String enchantmentsString = EnchantmentUtil.toString(is, SEPARATORS);
		final String nameString = ItemMetaUtil.getNameString(is);
		final String loreString = ItemMetaUtil.getLoreString(is);
		final String specialMetaString = ItemMetaUtil.getSpecialMetaString(is, SEPARATORS);

		return idString + SEPARATORS[0] +
		       dataString + SEPARATORS[0] +
		       amountString + SEPARATORS[0] +
		       enchantmentsString + SEPARATORS[0] +
		       nameString + SEPARATORS[0] +
		       loreString + SEPARATORS[0] +
		       specialMetaString;
	}

	/**
	 * Gets an ItemStack from an Item Description String.
	 *
	 * @param itemString the String representing the ItemStack
	 *
	 * @return an ItemStack matching the provided itemString, or null
	 *
	 * @see #toString(ItemStack) for format
	 */
	public static ItemStack fromString(final String itemString) throws InventoryUtilException {
		final String[] parts = StringUtil.splitKeepEmpty(itemString, SEPARATORS[0]);
		if (parts.length != 7) {
			throw new InventoryUtilException("Invalid amount of fields (parsing '" + itemString + "')");
		}

		final String idString = parts[0];
		final String dataString = parts[1];
		final String amountString = parts[2];
		final String enchantmentsString = parts[3];
		final String nameString = parts[4];
		final String loreString = parts[5];
		final String specialMetaString = parts[6];

		final Material id;
		final Short data;
		final Integer amount;

		if (idString.isEmpty()) {
			throw new InventoryUtilException("Id is mandatory");
		} else {
			id = MaterialUtil.getMaterial(idString);
			if (id == null) {
				throw new InventoryUtilException("Unknown id '" + idString + "' (parsing '" + itemString + "')");
			}
		}

		if (dataString.isEmpty()) {
			data = 0;
		} else {
			try {
				data = Short.parseShort(dataString);
			} catch (final NumberFormatException e) {
				throw new InventoryUtilException("Invalid data value '" + dataString + "' (parsing '" + itemString + "')");
			}
		}

		if (amountString.isEmpty()) {
			amount = 1;
		} else {
			try {
				amount = Math.min(Math.max(Integer.parseInt(amountString), 1), 64);
			} catch (final NumberFormatException e) {
				throw new InventoryUtilException("Invalid amount value '" + amountString + "' (parsing '" + itemString + "')");
			}
		}

		final ItemStack is = new ItemStack(id, amount, data);

		final Map<Enchantment, Integer> enchantments = EnchantmentUtil.fromString(enchantmentsString, SEPARATORS);
		is.addUnsafeEnchantments(enchantments);

		final ItemMeta meta = is.getItemMeta();
		final ItemMeta itemMeta = ItemMetaUtil.fromString(meta, nameString, loreString, specialMetaString, SEPARATORS);

		is.setItemMeta(itemMeta);

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
	 *
	 * @see #loadFromConfig(ConfigurationSection, String)
	 */
	public static void saveToConfigSection(final ConfigurationSection parentSection, final String key, final ItemStack is) throws InventoryUtilException {
		if (is == null) {
			throw new InventoryUtilException("Null ItemStack");
		}

		final ConfigurationSection itemSection = parentSection.createSection(key);

		itemSection.set("id", is.getType().name());
		itemSection.set("data", is.getDurability());
		itemSection.set("amount", Math.min(Math.max(is.getAmount(), 1), 64));

		if (!is.getEnchantments().isEmpty()) {
			EnchantmentUtil.saveToConfigSection(itemSection, is);
		}

		if (is.hasItemMeta()) {
			ItemMetaUtil.saveToConfigSection(itemSection, is);
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
	 * @throws InventoryUtilException if the ItemStack description is malformed
	 * @see #saveToConfigSection(ConfigurationSection, String, ItemStack)
	 */
	public static ItemStack loadFromConfig(final ConfigurationSection parentSection, final String key) throws InventoryUtilException {
		final ConfigurationSection itemSection = parentSection.getConfigurationSection(key);
		final String parsed = " (parsed: Configuration file, under " + parentSection.getCurrentPath() + '.' + key + ')';

		final Material id = MaterialUtil.getMaterial(itemSection.getString("id", ""));
		if (id == null) {
			throw new InventoryUtilException("Id is mandatory" + parsed);
		}

		final short data = (short) itemSection.getInt("data", 0);
		final int amount = Math.min(Math.max(itemSection.getInt("amount", 1), 1), 64);

		final Map<Enchantment, Integer> enchantmentsMap = EnchantmentUtil.loadFromConfigSection(itemSection);

		final ItemStack is = new ItemStack(id, amount, data);

		if (!enchantmentsMap.isEmpty()) {
			is.addUnsafeEnchantments(enchantmentsMap);
		}

		ItemMetaUtil.loadFromConfigSection(itemSection, is);

		return is;
	}
}
