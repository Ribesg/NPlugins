/***************************************************************************
 * Project file:    NPlugins - NCore - DataUtil.java                       *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.DataUtil                  *
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * This lib is used to get some Bukkit Enum / standard values from Strings.
 *
 * @author Ribesg
 */
public class DataUtil {

	private static final Logger LOGGER = Logger.getLogger(DataUtil.class.getName());

	/** A comparator to sort Enchantments by name */
	private static final Comparator<Enchantment> ENCHANTMENT_COMPARATOR = new Comparator<Enchantment>() {

		@Override
		public int compare(final Enchantment a, final Enchantment b) {
			return a.getName().compareTo(b.getName());
		}
	};

	/**
	 * An arbitrary list of possible separator characters.
	 * Used to save Lore to String. Two of them are randomly chosen randomly
	 * and the combination of both is used as separator.
	 * The only way to break it would be to have Lore Strings contain every
	 * single possible 2-length combination of those characters.
	 */
	private static final CharSequence SEPARATOR_CHARS = ",;:!?§/.*+-=@_-|#~&$£¤°<>()[]{}";

	/**
	 * Different levels of Separators used for the String representation.
	 * Do not use 2-length separators as it's the length of Random
	 * Separators.
	 */
	private static final String[] SEPARATORS = {
			";|;",
			",",
			":"
	};

	private static final Random RANDOM = new Random();

	/**
	 * Create an ItemStack description String from an ItemStack.
	 * <p/>
	 * Item Description String format: [field][;;field]{5}
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
	 * @param is the ItemStack to convert
	 *
	 * @return a String matching the provided ItemStack
	 *
	 * @see #fromString(String) to get an ItemStack from the provided String
	 */
	public static String toString(final ItemStack is) {
		final String idString = is.getType().name();
		final String dataString = Short.toString(is.getDurability());
		final String amountString = Integer.toString(is.getAmount());

		final String enchantmentsString;
		if (is.getEnchantments().isEmpty()) {
			enchantmentsString = "";
		} else {
			final StringBuilder enchantmentsStringBuilder = new StringBuilder();
			final Map<Enchantment, Integer> sortedEnchantmentMap = new TreeMap<>(ENCHANTMENT_COMPARATOR);
			sortedEnchantmentMap.putAll(is.getEnchantments());
			for (final Map.Entry<Enchantment, Integer> e : sortedEnchantmentMap.entrySet()) {
				enchantmentsStringBuilder.append(e.getKey().getName());
				enchantmentsStringBuilder.append(SEPARATORS[2]);
				enchantmentsStringBuilder.append(e.getValue());
				enchantmentsStringBuilder.append(SEPARATORS[1]);
			}
			enchantmentsString = enchantmentsStringBuilder.substring(0, enchantmentsStringBuilder.length() - 1);
		}

		final ItemMeta meta = is.getItemMeta();
		final String nameString;
		if (meta.hasDisplayName()) {
			nameString = meta.getDisplayName();
		} else {
			nameString = "";
		}

		final String loreString;
		if (meta.hasLore()) {
			final List<String> lore = meta.getLore();
			final String separator = getPossibleSeparator(lore);
			final StringBuilder loreStringBuilder = new StringBuilder();
			for (final String loreLine : lore) {
				loreStringBuilder.append(separator).append(loreLine);
			}
			loreString = loreStringBuilder.toString();
		} else {
			loreString = "";
		}

		final StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append(idString).append(SEPARATORS[0]);
		resultBuilder.append(dataString).append(SEPARATORS[0]);
		resultBuilder.append(amountString).append(SEPARATORS[0]);
		resultBuilder.append(enchantmentsString).append(SEPARATORS[0]);
		resultBuilder.append(nameString).append(SEPARATORS[0]);
		resultBuilder.append(loreString);

		return resultBuilder.toString();
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
	public static ItemStack fromString(final String itemString) throws DataUtilParserException {
		final String[] parts = itemString.split(SEPARATORS[0]);
		if (parts.length != 6) {
			throw new DataUtilParserException(itemString, "Invalid amount of fields");
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
			throw new DataUtilParserException(itemString, "Id is mandatory");
		} else {
			id = getMaterial(idString);
			if (id == null) {
				throw new DataUtilParserException(itemString, "Unknown id '" + idString + "'");
			}
		}

		if (dataString.isEmpty()) {
			data = 0;
		} else {
			try {
				data = Short.parseShort(dataString);
			} catch (final NumberFormatException e) {
				throw new DataUtilParserException(itemString, "Invalid data value '" + dataString + "'");
			}
		}

		if (amountString.isEmpty()) {
			amount = 1;
		} else {
			try {
				amount = Integer.parseInt(amountString);
			} catch (final NumberFormatException e) {
				throw new DataUtilParserException(itemString, "Invalid amount value '" + amountString + "'");
			}
		}

		if (!enchantmentsString.isEmpty()) {
			enchantments = new TreeMap<>(ENCHANTMENT_COMPARATOR);
			final String[] enchantmentsPairs = enchantmentsString.split(SEPARATORS[1]);
			for (final String enchantmentPair : enchantmentsPairs) {
				final String[] enchantmentPairSplit = enchantmentPair.split(SEPARATORS[3]);
				if (enchantmentPairSplit.length != 2) {
					throw new DataUtilParserException(itemString, "Malformed Enchantments field '" + enchantmentsString + "'");
				} else {
					final String enchantmentName = enchantmentPairSplit[0];
					final String enchantmentLevel = enchantmentPairSplit[1];
					final Enchantment enchantment = getEnchantment(enchantmentName);
					if (enchantment == null) {
						throw new DataUtilParserException(itemString, "Unknown Enchantment '" + enchantmentName + "'");
					}
					try {
						final int level = Integer.parseInt(enchantmentLevel);
						if (level < 1) {
							throw new DataUtilParserException(itemString, "Invalid enchantment level '" +
							                                              level +
							                                              "' for enchantment '" +
							                                              enchantment.getName() +
							                                              "'");
						}
						enchantments.put(enchantment, level);
					} catch (final NumberFormatException e) {
						throw new DataUtilParserException(itemString, "Invalid level value '" +
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
	 *
	 * @see #loadFromConfig(ConfigurationSection, String)
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
	 * @throws fr.ribesg.bukkit.ncore.utils.DataUtil.DataUtilParserException
	 *          if the ItemStack description is malformed
	 * @see #saveToConfigSection(ConfigurationSection, String, ItemStack)
	 */
	public static ItemStack loadFromConfig(final ConfigurationSection parentSection, final String key) throws DataUtilParserException {
		final ConfigurationSection itemSection = parentSection.getConfigurationSection(key);
		final String parsed = "Configuration file, under " + parentSection.getCurrentPath() + '.' + key;

		final Material id = getMaterial(itemSection.getString("id", ""));
		if (id == null) {
			throw new DataUtilParserException(parsed, "Id is mandatory");
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
					throw new DataUtilParserException(parsed, "Invalid enchantment level '" +
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

	/**
	 * Gets a Material from a String, if able to recognize anything in the
	 * String. For now, only checks for ID and Material enum value.
	 * Note: For now, there is no real gain of this over using
	 * {@link Material#matchMaterial(String)}.
	 *
	 * @param idString the String representing a Material
	 *
	 * @return the associated Material or null if not found
	 */
	public static Material getMaterial(final String idString) {
		try {
			final int id = Integer.parseInt(idString);
			for (final Material m : Material.values()) {
				if (m.getId() == id && !isMaterialDeprecated(m)) {
					return m;
				}
			}
			return null;
		} catch (final NumberFormatException e) {
			final String filtered = idString.toUpperCase().replaceAll("\\s+", "_").replaceAll("\\W", "");
			return Material.getMaterial(filtered);
		}
	}

	/**
	 * Checks if a Material is deprecated.
	 *
	 * @param material the Material to check
	 *
	 * @return true if deprecated, false otherwise
	 */
	public static boolean isMaterialDeprecated(final Material material) {
		try {
			final Field f = Material.class.getField(material.name());
			return f.isAnnotationPresent(Deprecated.class);
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException("Material not found: " + material.name(), e);
		}
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

	private static String getPossibleSeparator(final List<String> lore) {
		int i = 1337; // Maximum tries
		String separator;
		boolean notContained;
		while (i-- > 0) {
			notContained = true;
			separator = getRandomSeparator();
			for (final String s : lore) {
				if (s.contains(separator)) {
					notContained = false;
					break;
				}
			}
			if (notContained) {
				return separator;
			}
		}
		throw new IllegalStateException("Cannot find a separator for provided list of Strings, it's a trap!");
	}

	private static String getRandomSeparator() {
		return Character.toString(getRandomCharacterSeparator()) + getRandomCharacterSeparator();
	}

	private static char getRandomCharacterSeparator() {
		return SEPARATOR_CHARS.charAt(RANDOM.nextInt(SEPARATOR_CHARS.length()));
	}

	public static class DataUtilParserException extends Exception {

		private final String parsed;
		private final String reason;

		public DataUtilParserException(final String parsed, final String reason) {
			super("Error while parsing '" + parsed + "', " + reason);
			this.parsed = parsed;
			this.reason = reason;
		}

		public DataUtilParserException(final String parsed, final String reason, final Throwable origin) {
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
