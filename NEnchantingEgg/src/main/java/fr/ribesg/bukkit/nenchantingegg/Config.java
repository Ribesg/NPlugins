/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Config.java                *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.Config                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.inventory.EnchantmentUtil;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.Altars;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Config extends AbstractConfig<NEnchantingEgg> {

	private int                       minimumDistanceBetweenTwoAltars;
	private double                    repairBoostMultiplier;
	private double                    enchantmentBoostMultiplier;
	private Map<Enchantment, Integer> enchantmentsMaxLevels;

	private final Altars altars;

	public Config(final NEnchantingEgg instance) {
		super(instance);

		altars = instance.getAltars();

		setMinimumDistanceBetweenTwoAltars(500);
		setRepairBoostMultiplier(1.0);
		setEnchantmentBoostMultiplier(1.0);

		enchantmentsMaxLevels = new HashMap<>();
		for (final Enchantment enchantment : Enchantment.values()) {
			enchantmentsMaxLevels.put(enchantment, 10);
		}
	}

	/**
	 * @see AbstractConfig#handleValues(YamlConfiguration)
	 */
	@Override
	protected void handleValues(final YamlConfiguration config) {
		plugin.entering(getClass(), "handleValues");

		// minimumDistanceBetweenTwoAltars. Default: 500.
		// Possible values: Positive integer >= 35
		setMinimumDistanceBetweenTwoAltars(config.getInt("minimumDistanceBetweenTwoAltars", 500));
		if (getMinimumDistanceBetweenTwoAltars() < 35) {
			wrongValue("config.yml", "minimumDistanceBetweenTwoAltars", getMinimumDistanceBetweenTwoAltars(), 500);
			setMinimumDistanceBetweenTwoAltars(500);
		}

		// repairBoostMultiplier. Default: 1.0.
		// Possible values: Positive double
		setRepairBoostMultiplier(config.getDouble("repairBoostMultiplier", 1.0));
		if (getRepairBoostMultiplier() <= 0.0) {
			wrongValue("config.yml", "repairBoostMultiplier", getRepairBoostMultiplier(), 1.0);
			setRepairBoostMultiplier(1.0);
		}

		// enchantmentBoostMultiplier. Default: 1.0.
		// Possible values: Positive double
		setEnchantmentBoostMultiplier(config.getDouble("enchantmentBoostMultiplier", 1.0));
		if (getEnchantmentBoostMultiplier() <= 0.0) {
			wrongValue("config.yml", "enchantmentBoostMultiplier", getEnchantmentBoostMultiplier(), 1.0);
			setEnchantmentBoostMultiplier(1.0);
		}

		// enchantmentMaxLevels.
		if (config.isConfigurationSection("enchantmentMaxLevels")) {
			final ConfigurationSection section = config.getConfigurationSection("enchantmentMaxLevels");
			for (final String key : section.getKeys(false)) {
				final int level = section.getInt(key, 10);
				final Enchantment enchantment = EnchantmentUtil.getEnchantment(key);
				if (enchantment == null) {
					plugin.error(Level.WARNING, "Ignored unknown enchantment name or id: " + key);
				} else if (level > 10) {
					plugin.error(Level.WARNING, "Ignored too high level for enchantment '" + key + "': " + level);
				} else {
					enchantmentsMaxLevels.put(enchantment, level);
				}
			}
		}

		if (config.isList("altars")) {
			final List<String> list = config.getStringList("altars");
			for (final String s : list) {
				final NLocation loc = NLocation.toNLocation(s);
				if (loc == null) {
					plugin.error("Incorrect altar location (Malformed): \"" + s + "\"");
					break;
				}
				final Altar a = new Altar(plugin, loc);
				if (a.getCenterLocation().getWorld() == null) {
					plugin.error("Incorrect altar location (Unknown world '" + loc.getWorldName() + "'): \"" + s + "\"");
					plugin.error("Has this world been disabled?");
					break;
				} else if (!altars.canAdd(a, getMinimumDistanceBetweenTwoAltars())) {
					plugin.error("Incorrect altar location (Too close): \"" + s + "\"");
					break;
				} else if (a.isInactiveAltarValid()) {
					altars.add(a);
				}
			}
		}

		plugin.exiting(getClass(), "handleValues");
	}

	/**
	 * @see AbstractConfig#getConfigString()
	 */
	@Override
	protected String getConfigString() {
		plugin.entering(getClass(), "getConfigString");

		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NEnchantingEgg plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		// Minimum distance between 2 altars
		content.append("# The minimum distance between 2 altars. Default: 500\n");
		content.append("# Note: You can't use a value under 35.\n");
		content.append("minimumDistanceBetweenTwoAltars: " + getMinimumDistanceBetweenTwoAltars() + "\n\n");

		// Repair boost multiplier
		content.append("# The coefficient applied to durability boost on repair. Default: 1.0\n");
		content.append("# Note: You can't use a value equals to or under 0.0\n");
		content.append("repairBoostMultiplier: " + getRepairBoostMultiplier() + "\n\n");

		// Enchantment boost multiplier
		content.append("# The coefficient applied to probabilities of enchantment boost. Default: 1.0\n");
		content.append("# Note: You can't use a value equals to or under 0.0, and you may prefer\n");
		content.append("#       to use values close to 1 to prevent breaking everything.\n");
		content.append("#       Example: 1.1 is an IMPORTANT increase!\n");
		content.append("enchantmentBoostMultiplier: " + getEnchantmentBoostMultiplier() + "\n\n");

		// Enchantments max levels
		content.append("# Maximum allowed levels for each enchantment.\n");
		content.append("# You can use the enchantment id for the key if you're not sure.\n");
		content.append("# Notes: - Any Enchantment not specified here has a max level of 10.\n");
		content.append("#        - Any value greater than 10 will be ignored.\n");
		content.append("enchantmentMaxLevels:\n");
		for (final Map.Entry<Enchantment, Integer> e : enchantmentsMaxLevels.entrySet()) {
			content.append("  " + e.getKey().getName() + ": " + e.getValue() + "\n");
		}

		// Altars
		content.append("# This stores created altars\n");
		content.append("altars:\n");
		for (final Altar a : altars.getAltars()) {
			content.append("- " + a.getCenterLocation().toString() + '\n');
		}

		plugin.exiting(getClass(), "getConfigString");
		return content.toString();
	}

	public int getMinimumDistanceBetweenTwoAltars() {
		return minimumDistanceBetweenTwoAltars;
	}

	public void setMinimumDistanceBetweenTwoAltars(final int minimumDistanceBetweenTwoAltars) {
		this.minimumDistanceBetweenTwoAltars = minimumDistanceBetweenTwoAltars;
	}

	public double getRepairBoostMultiplier() {
		return repairBoostMultiplier;
	}

	public void setRepairBoostMultiplier(final double repairBoostMultiplier) {
		this.repairBoostMultiplier = repairBoostMultiplier;
	}

	public double getEnchantmentBoostMultiplier() {
		return enchantmentBoostMultiplier;
	}

	public void setEnchantmentBoostMultiplier(final double enchantmentBoostMultiplier) {
		this.enchantmentBoostMultiplier = enchantmentBoostMultiplier;
	}

	public int getEnchantmentMaxLevel(final Enchantment enchantment) {
		return enchantmentsMaxLevels.get(enchantment);
	}
}
