/***************************************************************************
 * Project file:    NPlugins - NCore - ItemMetaUtils.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.inventory.ItemMetaUtils   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils.inventory;

import fr.ribesg.bukkit.ncore.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemMetaUtils {

	// ############ //
	// ## Global ## //
	// ############ //

	/**
	 * Get the custom name of this ItemStack, if any.
	 *
	 * @param is the ItemStack
	 *
	 * @return the custom name of this ItemStack or an empty String
	 */
	public static String getNameString(final ItemStack is) {
		final ItemMeta meta = is.getItemMeta();
		if (meta.hasDisplayName()) {
			return meta.getDisplayName();
		} else {
			return "";
		}
	}

	/**
	 * Get a String representing the lore of this ItemStack, if any.
	 *
	 * @param is the ItemStack
	 *
	 * @return a String representing this ItemStack's lore, or an empty String
	 */
	public static String getLoreString(final ItemStack is) {
		final ItemMeta meta = is.getItemMeta();
		if (meta.hasLore()) {
			final List<String> lore = meta.getLore();
			final String separator = StringUtils.getPossibleSeparator(lore, 2);
			final StringBuilder loreStringBuilder = new StringBuilder();
			for (final String loreLine : lore) {
				loreStringBuilder.append(separator).append(loreLine);
			}
			return loreStringBuilder.toString();
		} else {
			return "";
		}
	}

	/**
	 * Gets a String representing all special meta of this ItemStack, if any.
	 *
	 * @param is the ItemStack
	 *
	 * @return a String representing this ItemStack's special meta or an empty String
	 */
	public static String getSpecialMetaString(final ItemStack is) throws InventoryUtilException {
		final ItemMeta meta = is.getItemMeta();

		if (meta instanceof BookMeta) {
			return getBookMetaString((BookMeta) meta);
		} else if (meta instanceof EnchantmentStorageMeta) {
			return getEnchantmentStorageMetaString((EnchantmentStorageMeta) meta);
		} else if (meta instanceof FireworkEffectMeta) {
			return getFireworkEffectMetaString((FireworkEffectMeta) meta);
		} else if (meta instanceof FireworkMeta) {
			return getFireworkMetaString((FireworkMeta) meta);
		} else if (meta instanceof LeatherArmorMeta) {
			return getLeatherArmorMetaString((LeatherArmorMeta) meta);
		} else if (meta instanceof MapMeta) {
			return getMapMetaString((MapMeta) meta);
		} else if (meta instanceof PotionMeta) {
			return getPotionMetaString((PotionMeta) meta);
		} else if (meta instanceof SkullMeta) {
			return getSkullMetaString((SkullMeta) meta);
		} else {
			throw new InventoryUtilException("Unknown Meta type '" + meta.getClass().getName() + "', please report this to the author (Ribesg)!");
		}
	}

	/**
	 * Parses 3 strings into an ItemMeta.
	 *
	 * @param meta              the ItemMeta to complete
	 * @param nameString        the DisplayName String
	 * @param loreString        the Lore String representation
	 * @param specialMetaString the Special Meta part String representation
	 *
	 * @return the same ItemMeta, completed
	 */
	public static ItemMeta fromString(final ItemMeta meta, final String nameString, final String loreString, final String specialMetaString) {
		if (meta instanceof BookMeta) {
			parseBookMeta(specialMetaString, (BookMeta) meta);
		} else if (meta instanceof EnchantmentStorageMeta) {
			parseEnchantmentStorageMeta(specialMetaString, (EnchantmentStorageMeta) meta);
		} else if (meta instanceof FireworkEffectMeta) {
			parseFireworkEffectMeta(specialMetaString, (FireworkEffectMeta) meta);
		} else if (meta instanceof FireworkMeta) {
			parseFireworkMeta(specialMetaString, (FireworkMeta) meta);
		} else if (meta instanceof LeatherArmorMeta) {
			parseLeatherArmorMeta(specialMetaString, (LeatherArmorMeta) meta);
		} else if (meta instanceof MapMeta) {
			parseMapMeta(specialMetaString, (MapMeta) meta);
		} else if (meta instanceof PotionMeta) {
			parsePotionMeta(specialMetaString, (PotionMeta) meta);
		} else if (meta instanceof SkullMeta) {
			parseSkullMeta(specialMetaString, (SkullMeta) meta);
		}

		if (!nameString.isEmpty()) {
			meta.setDisplayName(nameString);
		}

		if (loreString.length() > 1) {
			final List<String> lore = new ArrayList<>();
			final String separator = loreString.substring(0, 2);
			Collections.addAll(lore, StringUtils.splitKeepEmpty(loreString.substring(2), separator));
			meta.setLore(lore);
		}

		return meta;
	}

	/**
	 * Saves an ItemMeta to a ConfigurationSection
	 *
	 * @param itemSection the parent section of the to-be-created meta section
	 * @param is          the ItemStack
	 */
	public static void saveToConfigSection(final ConfigurationSection itemSection, final ItemStack is) {
		final ItemMeta meta = is.getItemMeta();

		if (meta instanceof BookMeta) {
			saveBookMetaToConfigSection(itemSection, (BookMeta) meta);
		} else if (meta instanceof EnchantmentStorageMeta) {
			saveEnchantmentStorageMetaToConfigSection(itemSection, (EnchantmentStorageMeta) meta);
		} else if (meta instanceof FireworkEffectMeta) {
			saveFireworkEffectMetaToConfigSection(itemSection, (FireworkEffectMeta) meta);
		} else if (meta instanceof FireworkMeta) {
			saveFireworkMetaToConfigSection(itemSection, (FireworkMeta) meta);
		} else if (meta instanceof LeatherArmorMeta) {
			saveLeatherArmorMetaToConfigSection(itemSection, (LeatherArmorMeta) meta);
		} else if (meta instanceof MapMeta) {
			saveMapMetaToConfigSection(itemSection, (MapMeta) meta);
		} else if (meta instanceof PotionMeta) {
			savePotionMetaToConfigSection(itemSection, (PotionMeta) meta);
		} else if (meta instanceof SkullMeta) {
			saveSkullMetaToConfigSection(itemSection, (SkullMeta) meta);
		}

		if (meta.hasDisplayName()) {
			createAndGetSection(itemSection, "meta").set("name", meta.getDisplayName());
		}

		if (meta.hasLore()) {
			createAndGetSection(itemSection, "meta").set("lore", meta.getLore());
		}
	}

	/**
	 * Gets a ConfigurationSection, eventually creating it.
	 *
	 * @param parent the parent ConfigurationSection
	 * @param key    the key under which the ConfigurationSection will be created
	 *
	 * @return the ConfigurationSection
	 */
	private static ConfigurationSection createAndGetSection(final ConfigurationSection parent, final String key) {
		if (!parent.isConfigurationSection(key)) {
			parent.createSection(key);
		}
		return parent.getConfigurationSection(key);
	}

	/**
	 * Loads an ItemMeta from a ConfigurationSection.
	 *
	 * @param itemSection the parent section of the meta section
	 * @param is          the ItemStack to complete
	 */
	public static void loadFromConfigSection(final ConfigurationSection itemSection, final ItemStack is) {
		if (itemSection.isConfigurationSection("meta")) {
			final ItemMeta meta = is.getItemMeta();
			final ConfigurationSection metaSection = itemSection.getConfigurationSection("meta");

			if (meta instanceof BookMeta) {
				loadBookMetaFromConfigSection(metaSection, (BookMeta) meta);
			} else if (meta instanceof EnchantmentStorageMeta) {
				loadEnchantmentStorageMetaFromConfigSection(metaSection, (EnchantmentStorageMeta) meta);
			} else if (meta instanceof FireworkEffectMeta) {
				loadFireworkEffectMetaFromConfigSection(metaSection, (FireworkEffectMeta) meta);
			} else if (meta instanceof FireworkMeta) {
				loadFireworkMetaFromConfigSection(metaSection, (FireworkMeta) meta);
			} else if (meta instanceof LeatherArmorMeta) {
				loadLeatherArmorMetaFromConfigSection(metaSection, (LeatherArmorMeta) meta);
			} else if (meta instanceof MapMeta) {
				loadMapMetaFromConfigSection(metaSection, (MapMeta) meta);
			} else if (meta instanceof PotionMeta) {
				loadPotionMetaFromConfigSection(metaSection, (PotionMeta) meta);
			} else if (meta instanceof SkullMeta) {
				loadSkullMetaFromConfigSection(metaSection, (SkullMeta) meta);
			}

			final String displayName = itemSection.getString("name", "");
			if (!displayName.isEmpty()) {
				meta.setDisplayName(displayName);
			}

			final List<String> lore = itemSection.getStringList("lore");
			if (!lore.isEmpty()) {
				meta.setLore(lore);
			}

			is.setItemMeta(meta);
		}
	}

	// ############## //
	// ## BookMeta ## //
	// ############## //

	private static String getBookMetaString(final BookMeta meta) {
		return ""; // TODO
	}

	private static void parseBookMeta(final String string, final BookMeta meta) {
		// TODO
	}

	private static void saveBookMetaToConfigSection(final ConfigurationSection metaSection, final BookMeta meta) {
		// TODO
	}

	private static void loadBookMetaFromConfigSection(final ConfigurationSection metaSection, final BookMeta meta) {
		// TODO
	}

	// ############################ //
	// ## EnchantmentStorageMeta ## //
	// ############################ //

	private static String getEnchantmentStorageMetaString(final EnchantmentStorageMeta meta) {
		return ""; // TODO
	}

	private static void parseEnchantmentStorageMeta(final String string, final EnchantmentStorageMeta meta) {
		// TODO
	}

	private static void saveEnchantmentStorageMetaToConfigSection(final ConfigurationSection metaSection, final EnchantmentStorageMeta meta) {
		// TODO
	}

	private static void loadEnchantmentStorageMetaFromConfigSection(final ConfigurationSection metaSection, final EnchantmentStorageMeta meta) {
		// TODO
	}

	// ######################## //
	// ## FireworkEffectMeta ## //
	// ######################## //

	private static String getFireworkEffectMetaString(final FireworkEffectMeta meta) {
		return ""; // TODO
	}

	private static void parseFireworkEffectMeta(final String string, final FireworkEffectMeta meta) {
		// TODO
	}

	private static void saveFireworkEffectMetaToConfigSection(final ConfigurationSection metaSection, final FireworkEffectMeta meta) {
		// TODO
	}

	private static void loadFireworkEffectMetaFromConfigSection(final ConfigurationSection metaSection, final FireworkEffectMeta meta) {
		// TODO
	}

	// ################## //
	// ## FireworkMeta ## //
	// ################## //

	private static String getFireworkMetaString(final FireworkMeta meta) {
		return ""; // TODO
	}

	private static void parseFireworkMeta(final String string, final FireworkMeta meta) {
		// TODO
	}

	private static void saveFireworkMetaToConfigSection(final ConfigurationSection metaSection, final FireworkMeta meta) {
		// TODO
	}

	private static void loadFireworkMetaFromConfigSection(final ConfigurationSection metaSection, final FireworkMeta meta) {
		// TODO
	}

	// ###################### //
	// ## LeatherArmorMeta ## //
	// ###################### //

	private static String getLeatherArmorMetaString(final LeatherArmorMeta meta) {
		return ""; // TODO
	}

	private static void parseLeatherArmorMeta(final String string, final LeatherArmorMeta meta) {
		// TODO
	}

	private static void saveLeatherArmorMetaToConfigSection(final ConfigurationSection metaSection, final LeatherArmorMeta meta) {
		// TODO
	}

	private static void loadLeatherArmorMetaFromConfigSection(final ConfigurationSection metaSection, final LeatherArmorMeta meta) {
		// TODO
	}

	// ############# //
	// ## MapMeta ## //
	// ############# //

	private static String getMapMetaString(final MapMeta meta) {
		return ""; // TODO
	}

	private static void parseMapMeta(final String string, final MapMeta meta) {
		// TODO
	}

	private static void saveMapMetaToConfigSection(final ConfigurationSection metaSection, final MapMeta meta) {
		// TODO
	}

	private static void loadMapMetaFromConfigSection(final ConfigurationSection metaSection, final MapMeta meta) {
		// TODO
	}

	// ################ //
	// ## PotionMeta ## //
	// ################ //

	private static String getPotionMetaString(final PotionMeta meta) {
		return ""; // TODO
	}

	private static void parsePotionMeta(final String string, final PotionMeta meta) {
		// TODO
	}

	private static void savePotionMetaToConfigSection(final ConfigurationSection metaSection, final PotionMeta meta) {
		// TODO
	}

	private static void loadPotionMetaFromConfigSection(final ConfigurationSection metaSection, final PotionMeta meta) {
		// TODO
	}

	// ############### //
	// ## SkullMeta ## //
	// ############### //

	private static String getSkullMetaString(final SkullMeta meta) {
		return ""; // TODO
	}

	private static void parseSkullMeta(final String string, final SkullMeta meta) {
		// TODO
	}

	private static void saveSkullMetaToConfigSection(final ConfigurationSection metaSection, final SkullMeta meta) {
		// TODO
	}

	private static void loadSkullMetaFromConfigSection(final ConfigurationSection metaSection, final SkullMeta meta) {
		// TODO
	}
}
