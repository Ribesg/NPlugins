/***************************************************************************
 * Project file:    NPlugins - NCore - ItemMetaUtils.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.inventory.ItemMetaUtils   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils.inventory;

import fr.ribesg.bukkit.ncore.utils.ColorUtils;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
	public static String getSpecialMetaString(final ItemStack is, final String[] separators) throws InventoryUtilException {
		final ItemMeta meta = is.getItemMeta();

		if (meta instanceof BookMeta) {
			return getBookMetaString((BookMeta) meta);
		} else if (meta instanceof EnchantmentStorageMeta) {
			return getEnchantmentStorageMetaString((EnchantmentStorageMeta) meta, separators);
		} else if (meta instanceof FireworkEffectMeta) {
			return getFireworkEffectMetaString((FireworkEffectMeta) meta);
		} else if (meta instanceof FireworkMeta) {
			return getFireworkMetaString((FireworkMeta) meta, separators);
		} else if (meta instanceof LeatherArmorMeta) {
			return getLeatherArmorMetaString((LeatherArmorMeta) meta);
		} else if (meta instanceof MapMeta) {
			return getMapMetaString((MapMeta) meta);
		} else if (meta instanceof PotionMeta) {
			return getPotionMetaString((PotionMeta) meta, separators);
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
	public static ItemMeta fromString(final ItemMeta meta, final String nameString, final String loreString, final String specialMetaString, final String[] separators) throws InventoryUtilException {
		if (meta instanceof BookMeta) {
			parseBookMetaString(specialMetaString, (BookMeta) meta);
		} else if (meta instanceof EnchantmentStorageMeta) {
			parseEnchantmentStorageMetaString(specialMetaString, (EnchantmentStorageMeta) meta, separators);
		} else if (meta instanceof FireworkEffectMeta) {
			parseFireworkEffectMetaString(specialMetaString, (FireworkEffectMeta) meta);
		} else if (meta instanceof FireworkMeta) {
			parseFireworkMetaString(specialMetaString, (FireworkMeta) meta, separators);
		} else if (meta instanceof LeatherArmorMeta) {
			parseLeatherArmorMetaString(specialMetaString, (LeatherArmorMeta) meta);
		} else if (meta instanceof MapMeta) {
			parseMapMetaString(specialMetaString, (MapMeta) meta);
		} else if (meta instanceof PotionMeta) {
			parsePotionMetaString(specialMetaString, (PotionMeta) meta, separators);
		} else if (meta instanceof SkullMeta) {
			parseSkullMetaString(specialMetaString, (SkullMeta) meta);
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
			createAndGetSection(itemSection, "meta").set("name", ColorUtils.decolorize(meta.getDisplayName()));
		}

		if (meta.hasLore()) {
			createAndGetSection(itemSection, "meta").set("lore", ColorUtils.decolorize(meta.getLore()));
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
	public static void loadFromConfigSection(final ConfigurationSection itemSection, final ItemStack is) throws InventoryUtilException {
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

			final String displayName = metaSection.getString("name", "");
			if (!displayName.isEmpty()) {
				meta.setDisplayName(ColorUtils.colorize(displayName));
			}

			final List<String> lore = metaSection.getStringList("lore");
			if (!lore.isEmpty()) {
				meta.setLore(ColorUtils.colorize(lore));
			}

			is.setItemMeta(meta);
		}
	}

	// ############## //
	// ## BookMeta ## //
	// ############## //

	private static String getBookMetaString(final BookMeta meta) {
		final String author = meta.getAuthor();
		final String title = meta.getTitle();
		final List<String> pages = meta.getPages();

		final List<String> stringList = new ArrayList<>();
		stringList.add(title);
		stringList.add(author);
		stringList.addAll(pages);

		final String separator = StringUtils.getPossibleSeparator(stringList, 2);
		final StringBuilder builder = new StringBuilder();
		for (final String string : stringList) {
			builder.append(separator).append(string);
		}
		return builder.toString();
	}

	private static void parseBookMetaString(final String string, final BookMeta meta) {
		final String separator = string.substring(0, 2);
		final List<String> stringList = Arrays.asList(StringUtils.splitKeepEmpty(string, separator));

		final String title = stringList.remove(0);
		final String author = stringList.remove(0);

		meta.setTitle(title);
		meta.setAuthor(author);
		meta.setPages(stringList);
	}

	private static void saveBookMetaToConfigSection(final ConfigurationSection metaSection, final BookMeta meta) {
		metaSection.set("title", ColorUtils.decolorize(meta.getTitle()));
		metaSection.set("author", ColorUtils.decolorize(meta.getAuthor()));
		metaSection.set("pages", ColorUtils.decolorize(meta.getPages()));
	}

	private static void loadBookMetaFromConfigSection(final ConfigurationSection metaSection, final BookMeta meta) {
		final String title = ColorUtils.colorize(metaSection.getString("title"));
		final String author = ColorUtils.colorize(metaSection.getString("author"));
		final List<String> pages = ColorUtils.colorize(metaSection.getStringList("pages"));

		meta.setTitle(title);
		meta.setAuthor(author);
		meta.setPages(pages);
	}

	// ############################ //
	// ## EnchantmentStorageMeta ## //
	// ############################ //

	private static String getEnchantmentStorageMetaString(final EnchantmentStorageMeta meta, final String[] separators) {
		if (meta.getStoredEnchants().isEmpty()) {
			return "";
		} else {
			final StringBuilder enchantmentsStringBuilder = new StringBuilder();
			final Map<Enchantment, Integer> sortedEnchantmentMap = new TreeMap<>(EnchantmentUtils.ENCHANTMENT_COMPARATOR);
			sortedEnchantmentMap.putAll(meta.getStoredEnchants());
			for (final Map.Entry<Enchantment, Integer> e : sortedEnchantmentMap.entrySet()) {
				enchantmentsStringBuilder.append(e.getKey().getName());
				enchantmentsStringBuilder.append(separators[2]);
				enchantmentsStringBuilder.append(e.getValue());
				enchantmentsStringBuilder.append(separators[1]);
			}
			return enchantmentsStringBuilder.substring(0, enchantmentsStringBuilder.length() - separators[1].length());
		}
	}

	private static void parseEnchantmentStorageMetaString(final String string, final EnchantmentStorageMeta meta, final String[] separators) throws InventoryUtilException {
		if (!string.isEmpty()) {
			final String[] enchantmentsPairs = StringUtils.splitKeepEmpty(string, separators[1]);
			for (final String enchantmentPair : enchantmentsPairs) {
				final String[] enchantmentPairSplit = StringUtils.splitKeepEmpty(enchantmentPair, separators[2]);
				if (enchantmentPairSplit.length != 2) {
					throw new InventoryUtilException("Malformed Enchantments field '" + string + "'");
				} else {
					final String enchantmentName = enchantmentPairSplit[0];
					final String enchantmentLevel = enchantmentPairSplit[1];
					final Enchantment enchantment = EnchantmentUtils.getEnchantment(enchantmentName);
					if (enchantment == null) {
						throw new InventoryUtilException("Unknown Enchantment '" + enchantmentName + "' (parsing '\" + string + \"')\"");
					}
					try {
						final int level = Integer.parseInt(enchantmentLevel);
						if (level < 1) {
							throw new InventoryUtilException("Invalid enchantment level '" + level + "' for enchantment '" + enchantment.getName() + "' (parsing '\" + string + \"')\"");
						}
						meta.addStoredEnchant(enchantment, level, true);
					} catch (final NumberFormatException e) {
						throw new InventoryUtilException("Invalid level value '" + enchantmentLevel + "' for enchantment '" + enchantment.getName() + "' (parsing '" + string + "')");
					}
				}
			}
		}
	}

	private static void saveEnchantmentStorageMetaToConfigSection(final ConfigurationSection metaSection, final EnchantmentStorageMeta meta) {
		final ConfigurationSection enchantmentsSection = metaSection.createSection("storedEnchantments");
		for (final Map.Entry<Enchantment, Integer> e : meta.getStoredEnchants().entrySet()) {
			enchantmentsSection.set(e.getKey().getName(), e.getValue());
		}
	}

	private static void loadEnchantmentStorageMetaFromConfigSection(final ConfigurationSection metaSection, final EnchantmentStorageMeta meta) throws InventoryUtilException {
		if (metaSection.isConfigurationSection("storedEnchantments")) {
			final ConfigurationSection enchantmentsSection = metaSection.getConfigurationSection("storedEnchantments");
			for (final String enchantmentName : enchantmentsSection.getKeys(false)) {
				final Enchantment enchantment = EnchantmentUtils.getEnchantment(enchantmentName);
				final int level = enchantmentsSection.getInt(enchantmentName, -1);
				if (level < 1) {
					throw new InventoryUtilException("Invalid enchantment level '" + level + "' for enchantment '" + enchantment.getName() + "'");
				} else {
					meta.addStoredEnchant(enchantment, level, true);
				}
			}
		}
	}

	// ######################## //
	// ## FireworkEffectMeta ## //
	// ######################## //

	private static String getFireworkEffectMetaString(final FireworkEffectMeta meta) {
		return getFireworkEffectString(meta.getEffect());
	}

	private static String getFireworkEffectString(final FireworkEffect effect) {
		final String effectType = effect.getType().name();

		final List<String> effectColors = new ArrayList<>();
		for (final Color color : effect.getColors()) {
			effectColors.add(Integer.toString(color.asRGB()));
		}

		final List<String> effectFadeColors = new ArrayList<>();
		for (final Color color : effect.getFadeColors()) {
			effectFadeColors.add(Integer.toString(color.asRGB()));
		}

		final List<String> allStrings = new ArrayList<>();
		allStrings.addAll(effectColors);
		allStrings.addAll(effectFadeColors);

		final String separator2 = StringUtils.getPossibleSeparator(allStrings, 2);

		allStrings.add(effectType);

		String separator1;
		do {
			separator1 = StringUtils.getPossibleSeparator(allStrings, 2);
		} while (separator1.equals(separator2));

		final StringBuilder builder = new StringBuilder();
		builder.append(separator1);
		builder.append(effectType).append(separator1);
		if (effectColors.size() > 0) {
			builder.append(separator2).append(effectColors.get(0));
			for (int i = 1; i < effectColors.size(); i++) {
				builder.append(separator2).append(effectColors.get(i));
			}
		}
		builder.append(separator1);
		if (effectFadeColors.size() > 0) {
			builder.append(separator2).append(effectFadeColors.get(0));
			for (int i = 1; i < effectFadeColors.size(); i++) {
				builder.append(separator2).append(effectFadeColors.get(i));
			}
		}
		builder.append(effect.hasFlicker());
		builder.append(separator1);
		builder.append(effect.hasTrail());

		return builder.toString();
	}

	private static void parseFireworkEffectMetaString(final String string, final FireworkEffectMeta meta) {
		meta.setEffect(parseFireworkEffectString(string));
	}

	private static FireworkEffect parseFireworkEffectString(final String string) {
		final String separator1 = string.substring(0, 2);
		final String[] split = StringUtils.splitKeepEmpty(string.substring(2), separator1);

		final FireworkEffect.Type type = FireworkEffect.Type.valueOf(split[0]);
		final List<Color> colors = new ArrayList<>();
		final List<Color> fadeColors = new ArrayList<>();

		if (!split[1].isEmpty()) {
			final String separator2 = split[1].substring(0, 2);
			final String[] colorStrings = StringUtils.splitKeepEmpty(split[1].substring(2), separator2);
			for (final String colorString : colorStrings) {
				colors.add(Color.fromRGB(Integer.parseInt(colorString)));
			}
		}

		if (!split[2].isEmpty()) {
			final String separator2 = split[1].substring(0, 2);
			final String[] fadeColorStrings = StringUtils.splitKeepEmpty(split[2].substring(2), separator2);
			for (final String fadeColorString : fadeColorStrings) {
				colors.add(Color.fromRGB(Integer.parseInt(fadeColorString)));
			}
		}

		final boolean withFlicker = Boolean.parseBoolean(split[3]);
		final boolean withTrail = Boolean.parseBoolean(split[4]);

		final FireworkEffect.Builder builder = FireworkEffect.builder();
		builder.with(type);
		builder.withColor(colors);
		builder.withFade(fadeColors);
		if (withFlicker) {
			builder.withFlicker();
		}
		if (withTrail) {
			builder.withTrail();
		}
		return builder.build();
	}

	private static void saveFireworkEffectMetaToConfigSection(final ConfigurationSection metaSection, final FireworkEffectMeta meta) {
		saveFireworkEffectMetaToConfigSection(metaSection, meta, "FireworkEffect");
	}

	private static void saveFireworkEffectMetaToConfigSection(final ConfigurationSection metaSection, final FireworkEffectMeta meta, final String sectionName) {
		saveFireworkEffectToConfigSection(metaSection, meta.getEffect(), sectionName);
	}

	private static void saveFireworkEffectToConfigSection(final ConfigurationSection metaSection, final FireworkEffect effect, final String sectionName) {
		final ConfigurationSection fireworkEffectSection = metaSection.createSection(sectionName);

		fireworkEffectSection.set("type", effect.getType().name());

		final List<String> colors = new ArrayList<>();
		for (final Color color : effect.getColors()) {
			colors.add(Integer.toString(color.asRGB()));
		}
		fireworkEffectSection.set("colors", colors);

		final List<String> fadeColors = new ArrayList<>();
		for (final Color fadeColor : effect.getFadeColors()) {
			fadeColors.add(Integer.toString(fadeColor.asRGB()));
		}
		fireworkEffectSection.set("fadeColors", fadeColors);

		fireworkEffectSection.set("hasFlicker", effect.hasFlicker());
		fireworkEffectSection.set("hasTrail", effect.hasTrail());
	}

	private static void loadFireworkEffectMetaFromConfigSection(final ConfigurationSection metaSection, final FireworkEffectMeta meta) {
		loadFireworkEffectMetaFromConfigSection(metaSection, meta, "fireworkEffect");
	}

	private static void loadFireworkEffectMetaFromConfigSection(final ConfigurationSection metaSection, final FireworkEffectMeta meta, final String sectionName) {
		if (metaSection.isConfigurationSection(sectionName)) {
			meta.setEffect(loadFireworkEffectFromConfigSection(metaSection, sectionName));
		}
	}

	private static FireworkEffect loadFireworkEffectFromConfigSection(final ConfigurationSection metaSection, final String sectionName) {
		final ConfigurationSection fireworkEffectSection = metaSection.getConfigurationSection(sectionName);

		final String typeString = fireworkEffectSection.getString("type");
		final FireworkEffect.Type type = FireworkEffect.Type.valueOf(typeString);

		final List<String> colorsStringList = fireworkEffectSection.getStringList("colors");
		final List<Color> colors = new ArrayList<>();
		for (final String colorString : colorsStringList) {
			colors.add(Color.fromRGB(Integer.parseInt(colorString)));
		}

		final List<String> fadeColorsStringList = fireworkEffectSection.getStringList("fadeColors");
		final List<Color> fadeColors = new ArrayList<>();
		for (final String fadeColorString : fadeColorsStringList) {
			fadeColors.add(Color.fromRGB(Integer.parseInt(fadeColorString)));
		}

		final boolean hasFlicker = fireworkEffectSection.getBoolean("hasFlicker");
		final boolean hasTrail = fireworkEffectSection.getBoolean("hasTrail");

		final FireworkEffect.Builder builder = FireworkEffect.builder();
		builder.with(type);
		builder.withColor(colors);
		builder.withFade(fadeColors);
		if (hasFlicker) {
			builder.withFlicker();
		}
		if (hasTrail) {
			builder.withTrail();
		}

		return builder.build();
	}

	// ################## //
	// ## FireworkMeta ## //
	// ################## //

	private static String getFireworkMetaString(final FireworkMeta meta, final String[] separators) {
		final int power = meta.getPower();
		final List<FireworkEffect> effects = meta.getEffects();

		final StringBuilder builder = new StringBuilder();
		builder.append(Integer.toString(power)).append(separators[1]);
		if (!effects.isEmpty()) {
			builder.append(getFireworkEffectString(effects.get(0)));
			for (int i = 1; i < effects.size(); i++) {
				builder.append(separators[1]).append(getFireworkEffectString(effects.get(i)));
			}
		}

		return builder.toString();
	}

	private static void parseFireworkMetaString(final String string, final FireworkMeta meta, final String[] separators) {
		final String[] split = StringUtils.splitKeepEmpty(string, separators[1]);

		final int power = Integer.parseInt(split[0]);
		final List<FireworkEffect> effects = new ArrayList<>();
		for (int i = 1; i < split.length; i++) {
			effects.add(parseFireworkEffectString(split[i]));
		}

		meta.setPower(power);
		meta.addEffects(effects);
	}

	private static void saveFireworkMetaToConfigSection(final ConfigurationSection metaSection, final FireworkMeta meta) {
		final ConfigurationSection fireworkSection = metaSection.createSection("firework");
		fireworkSection.set("power", meta.getPower());
		for (int i = 1; i <= meta.getEffects().size(); i++) {
			saveFireworkEffectToConfigSection(fireworkSection, meta.getEffects().get(i - 1), "fireworkEffect" + i);
		}
	}

	private static void loadFireworkMetaFromConfigSection(final ConfigurationSection metaSection, final FireworkMeta meta) {
		if (metaSection.isConfigurationSection("firework")) {
			final ConfigurationSection fireworkSection = metaSection.getConfigurationSection("firework");

			meta.setPower(fireworkSection.getInt("power"));
			for (final String key : fireworkSection.getKeys(false)) {
				if (key.startsWith("fireworkEffect") && fireworkSection.isConfigurationSection(key)) {
					meta.addEffect(loadFireworkEffectFromConfigSection(fireworkSection, key));
				}
			}
		}
	}

	// ###################### //
	// ## LeatherArmorMeta ## //
	// ###################### //

	private static String getLeatherArmorMetaString(final LeatherArmorMeta meta) {
		return Integer.toString(meta.getColor().asRGB());
	}

	private static void parseLeatherArmorMetaString(final String string, final LeatherArmorMeta meta) {
		meta.setColor(Color.fromRGB(Integer.parseInt(string)));
	}

	private static void saveLeatherArmorMetaToConfigSection(final ConfigurationSection metaSection, final LeatherArmorMeta meta) {
		metaSection.set("leatherArmorColor", meta.getColor().asRGB());
	}

	private static void loadLeatherArmorMetaFromConfigSection(final ConfigurationSection metaSection, final LeatherArmorMeta meta) {
		meta.setColor(Color.fromRGB(metaSection.getInt("leatherArmorColor")));
	}

	// ############# //
	// ## MapMeta ## //
	// ############# //

	private static String getMapMetaString(final MapMeta meta) {
		return Boolean.toString(meta.isScaling());
	}

	private static void parseMapMetaString(final String string, final MapMeta meta) {
		meta.setScaling(Boolean.parseBoolean(string));
	}

	private static void saveMapMetaToConfigSection(final ConfigurationSection metaSection, final MapMeta meta) {
		metaSection.set("isMapScaling", meta.isScaling());
	}

	private static void loadMapMetaFromConfigSection(final ConfigurationSection metaSection, final MapMeta meta) {
		meta.setScaling(metaSection.getBoolean("isMapScaling"));
	}

	// ################ //
	// ## PotionMeta ## //
	// ################ //

	private static String getPotionMetaString(final PotionMeta meta, final String[] separators) {
		final List<PotionEffect> effects = meta.getCustomEffects();

		final StringBuilder builder = new StringBuilder();
		if (!effects.isEmpty()) {
			PotionEffect effect = effects.get(0);
			builder.append(effect.getType().getName()).append(separators[2]);
			builder.append(effect.getDuration()).append(separators[2]);
			builder.append(effect.getAmplifier());
			for (int i = 1; i < effects.size(); i++) {
				builder.append(separators[1]);
				effect = effects.get(i);
				builder.append(effect.getType().getName()).append(separators[2]);
				builder.append(effect.getDuration()).append(separators[2]);
				builder.append(effect.getAmplifier());
			}
		}

		return builder.toString();
	}

	private static void parsePotionMetaString(final String string, final PotionMeta meta, final String[] separators) {
		final String[] split = StringUtils.splitKeepEmpty(string, separators[1]);

		for (final String effectString : split) {
			final String[] effectSplit = StringUtils.splitKeepEmpty(effectString, separators[2]);
			final PotionEffectType type = PotionEffectType.getByName(effectSplit[0]);
			final int duration = Integer.parseInt(effectSplit[1]);
			final int amplifier = Integer.parseInt(effectSplit[2]);
			meta.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
		}
	}

	private static void savePotionMetaToConfigSection(final ConfigurationSection metaSection, final PotionMeta meta) {
		final ConfigurationSection potionSection = metaSection.createSection("potion");
		int i = 1;
		for (final PotionEffect effect : meta.getCustomEffects()) {
			final ConfigurationSection potionEffectSection = potionSection.createSection("potionEffect" + i++);
			potionEffectSection.set("type", effect.getType().getName());
			potionEffectSection.set("duration", effect.getDuration());
			potionEffectSection.set("amplifier", effect.getAmplifier());
		}
	}

	private static void loadPotionMetaFromConfigSection(final ConfigurationSection metaSection, final PotionMeta meta) {
		if (metaSection.isConfigurationSection("potion")) {
			final ConfigurationSection potionSection = metaSection.getConfigurationSection("potion");
			for (final String key : potionSection.getKeys(false)) {
				if (key.startsWith("potionEffect") && potionSection.isConfigurationSection(key)) {
					final ConfigurationSection potionEffectSection = metaSection.getConfigurationSection(key);
					final PotionEffectType type = PotionEffectType.getByName(potionEffectSection.getString("type"));
					final int duration = potionEffectSection.getInt("duration");
					final int amplifier = potionEffectSection.getInt("amplifier");
					meta.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
				}
			}
		}
	}

	// ############### //
	// ## SkullMeta ## //
	// ############### //

	private static String getSkullMetaString(final SkullMeta meta) {
		return meta.getOwner();
	}

	private static void parseSkullMetaString(final String string, final SkullMeta meta) {
		meta.setOwner(string);
	}

	private static void saveSkullMetaToConfigSection(final ConfigurationSection metaSection, final SkullMeta meta) {
		metaSection.set("skullOwner", ColorUtils.decolorize(meta.getOwner()));
	}

	private static void loadSkullMetaFromConfigSection(final ConfigurationSection metaSection, final SkullMeta meta) {
		meta.setOwner(ColorUtils.colorize(metaSection.getString("skullOwner")));
	}
}
