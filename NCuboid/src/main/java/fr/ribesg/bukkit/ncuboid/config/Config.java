/***************************************************************************
 * Project file:    NPlugins - NCuboid - Config.java                       *
 * Full Class name: fr.ribesg.bukkit.ncuboid.config.Config                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.config;

import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The config for the NCuboid node
 *
 * @author Ribesg
 */
public class Config extends AbstractConfig<NCuboid> {

	private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

	private       Material                 selectionItemMaterial;
	private final Map<String, GroupConfig> groupConfigs;
	private final GroupConfig              defaultGroupConfig;

	/**
	 * Constructor
	 *
	 * @param instance Linked plugin instance
	 */
	public Config(final NCuboid instance) {
		super(instance);
		setSelectionItemMaterial(Material.STICK);
		this.groupConfigs = new HashMap<>();

		final GroupConfig adminConfig = new GroupConfig("admin", -1, -1, -1);
		this.groupConfigs.put(adminConfig.getGroupName(), adminConfig);

		final GroupConfig userConfig = new GroupConfig("user", 2, 60, 25000);
		this.groupConfigs.put(userConfig.getGroupName(), userConfig);

		this.defaultGroupConfig = new GroupConfig("", 0, 0, 0);
	}

	/** @see AbstractConfig#handleValues(YamlConfiguration) */
	@Override
	protected void handleValues(final YamlConfiguration config) {

		// selectionItemMaterial. Default : Stick/280
		final Material m = Material.getMaterial(config.getInt("selectionItemMaterial", Material.STICK.getId()));
		setSelectionItemMaterial(m == null ? Material.STICK : m);

		// groupConfigs.
		if (config.isConfigurationSection("groupConfigs")) {
			groupConfigs.clear();
			final ConfigurationSection groupConfigSection = config.getConfigurationSection("groupConfigs");
			for (final String groupName : groupConfigSection.getKeys(false)) {
				try {
					if (groupConfigSection.isConfigurationSection(groupName)) {
						final ConfigurationSection groupSection = groupConfigSection.getConfigurationSection(groupName);
						final int def = Integer.MIN_VALUE;
						final int maxRegionNb = groupSection.getInt("maxRegionNb", def);
						final int maxRegion1DSize = groupSection.getInt("maxRegion1DSize", def);
						final int maxRegion3DSize = groupSection.getInt("maxRegion3DSize", def);
						if (maxRegionNb == def || maxRegion1DSize == def || maxRegion3DSize == def) {
							LOGGER.severe("Missing config value for '" + groupName + "' in config");
						} else {
							groupConfigs.put(groupName.toLowerCase(), new GroupConfig(groupName.toLowerCase(), maxRegionNb, maxRegion1DSize, maxRegion3DSize));
						}
					} else {
						LOGGER.severe("Invalid config value '" + groupName + "' ignored in config");
					}
				} catch (final NumberFormatException e) {
					LOGGER.severe("Failed to load group " + groupName + ", invalid value set in config");
				}
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
		frame.addLine("Config file for NCuboid plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		// selectionItemMaterial. Default : Stick/280
		content.append("# The tool used to select points and get informations about blocks protection. Default : 180 (Stick)\n");
		content.append("selectionItemMaterial: " + getSelectionItemMaterial().getId() + "\n\n");

		// groupConfigs
		content.append("# Here, you can configure for each group:\n");
		content.append("#   - The maximum amount of regions a player can have;\n");
		content.append("#   - The maximum length in any direction of a region a player can make, in cube-length;\n");
		content.append("#   - The maximum volume of a region a player can make, in cubes.\n");
		content.append("# Note: The group 'foo' is defined by 'anybody that has the permission ncuboid.group.foo'\n");
		content.append("# Note: For any of those values, '-1' means 'unlimited'.\n");
		content.append("groupConfigs:\n");
		for (final GroupConfig gc : this.groupConfigs.values()) {
			content.append("  # Group '" + gc.getGroupName() + "'. The permission for this group is " + gc.getGroupPerm() + "\n");
			content.append("  " + gc.getGroupName() + ":\n");
			content.append("    maxRegionNb: " + gc.getMaxRegionNb() + "\n");
			content.append("    maxRegion1DSize: " + gc.getMaxRegion1DSize() + "\n");
			content.append("    maxRegion3DSize: " + gc.getMaxRegion3DSize() + "\n");
		}

		return content.toString();
	}

	public Material getSelectionItemMaterial() {
		return selectionItemMaterial;
	}

	private void setSelectionItemMaterial(final Material selectionItemMaterial) {
		this.selectionItemMaterial = selectionItemMaterial;
	}

	public Set<String> getKnownGroups() {
		return groupConfigs.keySet();
	}

	public GroupConfig getGroupConfig(final String groupName) {
		return groupConfigs.get(groupName);
	}

	public GroupConfig getGroupConfig(final Player player) {
		for (final GroupConfig gc : groupConfigs.values()) {
			if (player.hasPermission(gc.getGroupPerm())) {
				return gc;
			}
		}
		LOGGER.warning("Player '" + player.getName() + "' doesn't have any associated group, he will not be able to make cuboids!");
		return defaultGroupConfig;
	}
}
