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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

public class Config extends AbstractConfig<NEnchantingEgg> {

    private       int                       minimumDistanceBetweenTwoAltars;
    private       double                    repairBoostMultiplier;
    private       double                    enchantmentBoostMultiplier;
    private final Map<Enchantment, Integer> enchantmentsMaxLevels;

    private final Altars altars;

    public Config(final NEnchantingEgg instance) {
        super(instance);

        this.altars = instance.getAltars();

        this.setMinimumDistanceBetweenTwoAltars(500);
        this.setRepairBoostMultiplier(1.0);
        this.setEnchantmentBoostMultiplier(1.0);

        this.enchantmentsMaxLevels = new HashMap<>();
        for (final Enchantment enchantment : Enchantment.values()) {
            this.enchantmentsMaxLevels.put(enchantment, 10);
        }
    }

    @Override
    protected void handleValues(final YamlConfiguration config) {
        this.plugin.entering(this.getClass(), "handleValues");

        // minimumDistanceBetweenTwoAltars. Default: 500.
        // Possible values: Positive integer >= 35
        this.setMinimumDistanceBetweenTwoAltars(config.getInt("minimumDistanceBetweenTwoAltars", 500));
        if (this.minimumDistanceBetweenTwoAltars < 35) {
            this.wrongValue("config.yml", "minimumDistanceBetweenTwoAltars", this.minimumDistanceBetweenTwoAltars, 500);
            this.setMinimumDistanceBetweenTwoAltars(500);
        }

        // repairBoostMultiplier. Default: 1.0.
        // Possible values: Positive double
        this.setRepairBoostMultiplier(config.getDouble("repairBoostMultiplier", 1.0));
        if (this.repairBoostMultiplier <= 0.0) {
            this.wrongValue("config.yml", "repairBoostMultiplier", this.repairBoostMultiplier, 1.0);
            this.setRepairBoostMultiplier(1.0);
        }

        // enchantmentBoostMultiplier. Default: 1.0.
        // Possible values: Positive double
        this.setEnchantmentBoostMultiplier(config.getDouble("enchantmentBoostMultiplier", 1.0));
        if (this.enchantmentBoostMultiplier <= 0.0) {
            this.wrongValue("config.yml", "enchantmentBoostMultiplier", this.enchantmentBoostMultiplier, 1.0);
            this.setEnchantmentBoostMultiplier(1.0);
        }

        // enchantmentMaxLevels.
        if (config.isConfigurationSection("enchantmentMaxLevels")) {
            final ConfigurationSection section = config.getConfigurationSection("enchantmentMaxLevels");
            for (final String key : section.getKeys(false)) {
                final int level = section.getInt(key, 10);
                final Enchantment enchantment = EnchantmentUtil.getEnchantment(key);
                if (enchantment == null) {
                    this.plugin.error(Level.WARNING, "Ignored unknown enchantment name or id: " + key);
                } else if (level > 10) {
                    this.plugin.error(Level.WARNING, "Ignored too high level for enchantment '" + key + "': " + level);
                } else {
                    this.enchantmentsMaxLevels.put(enchantment, level);
                }
            }
        }

        if (config.isList("altars")) {
            final List<String> list = config.getStringList("altars");
            for (final String s : list) {
                final NLocation loc = NLocation.toNLocation(s);
                if (loc == null) {
                    this.plugin.error("Incorrect altar location (Malformed): \"" + s + '"');
                    break;
                }
                final Altar a = new Altar(this.plugin, loc);
                if (a.getCenterLocation().getWorld() == null) {
                    this.plugin.error("Incorrect altar location (Unknown world '" + loc.getWorldName() + "'): \"" + s + '"');
                    this.plugin.error("Has this world been disabled?");
                    break;
                } else if (!this.altars.canAdd(a, this.minimumDistanceBetweenTwoAltars)) {
                    this.plugin.error("Incorrect altar location (Too close): \"" + s + '"');
                    break;
                } else if (a.isInactiveAltarValid()) {
                    this.altars.add(a);
                }
            }
        }

        this.plugin.exiting(this.getClass(), "handleValues");
    }

    @Override
    protected String getConfigString() {
        this.plugin.entering(this.getClass(), "getConfigString");

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
        content.append("minimumDistanceBetweenTwoAltars: " + this.minimumDistanceBetweenTwoAltars + "\n\n");

        // Repair boost multiplier
        content.append("# The coefficient applied to durability boost on repair. Default: 1.0\n");
        content.append("# Note: You can't use a value equals to or under 0.0\n");
        content.append("repairBoostMultiplier: " + this.repairBoostMultiplier + "\n\n");

        // Enchantment boost multiplier
        content.append("# The coefficient applied to probabilities of enchantment boost. Default: 1.0\n");
        content.append("# Note: You can't use a value equals to or under 0.0, and you may prefer\n");
        content.append("#       to use values close to 1 to prevent breaking everything.\n");
        content.append("#       Example: 1.1 is an IMPORTANT increase!\n");
        content.append("enchantmentBoostMultiplier: " + this.enchantmentBoostMultiplier + "\n\n");

        // Enchantments max levels
        content.append("# Maximum allowed levels for each enchantment.\n");
        content.append("# You can use the enchantment id for the key if you're not sure.\n");
        content.append("# Notes: - Any Enchantment not specified here has a max level of 10.\n");
        content.append("#        - Any value greater than 10 will be ignored.\n");
        content.append("enchantmentMaxLevels:\n");
        for (final Map.Entry<Enchantment, Integer> e : this.enchantmentsMaxLevels.entrySet()) {
            content.append("  " + e.getKey().getName() + ": " + e.getValue() + '\n');
        }

        // Altars
        content.append("# This stores created altars\n");
        content.append("altars:\n");
        for (final Altar a : this.altars.getAltars()) {
            content.append("- " + a.getCenterLocation() + '\n');
        }

        this.plugin.exiting(this.getClass(), "getConfigString");
        return content.toString();
    }

    public int getMinimumDistanceBetweenTwoAltars() {
        return this.minimumDistanceBetweenTwoAltars;
    }

    public void setMinimumDistanceBetweenTwoAltars(final int minimumDistanceBetweenTwoAltars) {
        this.minimumDistanceBetweenTwoAltars = minimumDistanceBetweenTwoAltars;
    }

    public double getRepairBoostMultiplier() {
        return this.repairBoostMultiplier;
    }

    public void setRepairBoostMultiplier(final double repairBoostMultiplier) {
        this.repairBoostMultiplier = repairBoostMultiplier;
    }

    public double getEnchantmentBoostMultiplier() {
        return this.enchantmentBoostMultiplier;
    }

    public void setEnchantmentBoostMultiplier(final double enchantmentBoostMultiplier) {
        this.enchantmentBoostMultiplier = enchantmentBoostMultiplier;
    }

    public int getEnchantmentMaxLevel(final Enchantment enchantment) {
        return this.enchantmentsMaxLevels.get(enchantment);
    }
}
