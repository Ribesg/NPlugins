/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Config.java                *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.Config                 *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.Altars;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class Config extends AbstractConfig<NEnchantingEgg> {

	private int    minimumDistanceBetweenTwoAltars;
	private double repairBoostMultiplier;
	private double enchantmentBoostMultiplier;

	private final Altars altars;

	public Config(final NEnchantingEgg instance) {
		super(instance);

		altars = instance.getAltars();

		setMinimumDistanceBetweenTwoAltars(500);
		setRepairBoostMultiplier(1.0);
		setEnchantmentBoostMultiplier(1.0);
	}

	/** @see AbstractConfig#handleValues(YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) {

		// minimumDistanceBetweenTwoAltars. Default: 500.
		// Possible values: Positive integer >= 35
		setMinimumDistanceBetweenTwoAltars(config.getInt("minimumDistanceBetweenTwoAltars", 500));
		if (getMinimumDistanceBetweenTwoAltars() < 35) {
			setMinimumDistanceBetweenTwoAltars(500);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "minimumDistanceBetweenTwoAltars",
			                   "500");
		}

		// repairBoostMultiplier. Default: 1.0.
		// Possible values: Positive double
		setRepairBoostMultiplier(config.getDouble("repairBoostMultiplier", 1.0));
		if (getRepairBoostMultiplier() <= 0.0) {
			setRepairBoostMultiplier(1.0);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "repairBoostMultiplier",
			                   "1.0");
		}

		// enchantmentBoostMultiplier. Default: 1.0.
		// Possible values: Positive double
		setEnchantmentBoostMultiplier(config.getDouble("enchantmentBoostMultiplier", 1.0));
		if (getEnchantmentBoostMultiplier() <= 0.0) {
			setEnchantmentBoostMultiplier(1.0);
			plugin.sendMessage(plugin.getServer().getConsoleSender(),
			                   MessageId.incorrectValueInConfiguration,
			                   "config.yml",
			                   "enchantmentBoostMultiplier",
			                   "1.0");
		}

		if (config.isList("altars")) {
			final List<String> list = config.getStringList("altars");
			for (final String s : list) {
				final NLocation loc = NLocation.toNLocation(s);
				if (loc == null) {
					plugin.getLogger().severe("Incorrect altar location (Malformed): \"" + s + "\"");
					break;
				}
				final Altar a = new Altar(plugin, loc);
				if (!altars.canAdd(a, getMinimumDistanceBetweenTwoAltars())) {
					plugin.getLogger().severe("Incorrect altar location (Too close): \"" + s + "\"");
					break;
				}
				altars.add(a);
			}
		}
	}

	/** @see AbstractConfig#getConfigString() */
	@Override
	protected String getConfigString() {
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

		// Altars
		content.append("# This stores created altars\n");
		content.append("altars:\n");
		for (final Altar a : altars.getAltars()) {
			content.append("- " + a.getCenterLocation().toString() + '\n');
		}

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
}
