/***************************************************************************
 * Project file:    NPlugins - NCuboid - Config.java                       *
 * Full Class name: fr.ribesg.bukkit.ncuboid.config.Config                 *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.config;

import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.inventory.InventoryUtilException;
import fr.ribesg.bukkit.ncore.util.inventory.MaterialUtil;
import fr.ribesg.bukkit.ncuboid.NCuboid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
        this.setSelectionItemMaterial(Material.STICK);
        this.groupConfigs = new HashMap<>();

        final GroupConfig adminConfig = new GroupConfig("admin", -1, -1, -1);
        this.groupConfigs.put(adminConfig.getGroupName(), adminConfig);

        final GroupConfig userConfig = new GroupConfig("user", 2, 60, 25000);
        this.groupConfigs.put(userConfig.getGroupName(), userConfig);

        this.defaultGroupConfig = new GroupConfig("", 0, 0, 0);
    }

    @Override
    protected void handleValues(final YamlConfiguration config) {

        // selectionItemMaterial. Default : Stick/280
        Material m;
        try {
            m = MaterialUtil.getMaterial(config.getString("selectionItemMaterial", Material.STICK.name()));
        } catch (final InventoryUtilException e) {
            m = null;
        }
        this.setSelectionItemMaterial(m == null ? Material.STICK : m);

        // groupConfigs.
        if (config.isConfigurationSection("groupConfigs")) {
            this.groupConfigs.clear();
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
                            this.groupConfigs.put(groupName.toLowerCase(), new GroupConfig(groupName.toLowerCase(), maxRegionNb, maxRegion1DSize, maxRegion3DSize));
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
        content.append("selectionItemMaterial: " + this.selectionItemMaterial.name() + "\n\n");

        // groupConfigs
        content.append("# Here, you can configure for each group:\n");
        content.append("#   - The maximum amount of regions a player can have;\n");
        content.append("#   - The maximum length in any direction of a region a player can make, in cube-length;\n");
        content.append("#   - The maximum volume of a region a player can make, in cubes.\n");
        content.append("# Note: The group 'Foo' is defined by 'anybody that has the permission group.foo'\n");
        content.append("# Note: For any of those values, '-1' means 'unlimited'.\n");
        content.append("groupConfigs:\n");
        for (final GroupConfig gc : this.groupConfigs.values()) {
            content.append("  # Group '" + gc.getGroupName() + "'. The permission for this group is " + gc.getGroupPerm() + '\n');
            content.append("  " + gc.getGroupName() + ":\n");
            content.append("    maxRegionNb: " + gc.getMaxRegionNb() + '\n');
            content.append("    maxRegion1DSize: " + gc.getMaxRegion1DSize() + '\n');
            content.append("    maxRegion3DSize: " + gc.getMaxRegion3DSize() + '\n');
        }

        return content.toString();
    }

    public Material getSelectionItemMaterial() {
        return this.selectionItemMaterial;
    }

    private void setSelectionItemMaterial(final Material selectionItemMaterial) {
        this.selectionItemMaterial = selectionItemMaterial;
    }

    public Set<String> getKnownGroups() {
        return this.groupConfigs.keySet();
    }

    public GroupConfig getGroupConfig(final String groupName) {
        return this.groupConfigs.get(groupName);
    }

    public GroupConfig getGroupConfig(final Player player) {
        final GroupConfig result = new GroupConfig("tmp", 0, 0, 0);
        boolean found = false;
        for (final GroupConfig gc : this.groupConfigs.values()) {
            if (player.hasPermission(gc.getGroupPerm())) {
                found = true;
                if (result.getMaxRegionNb() != -1) {
                    if (gc.getMaxRegionNb() == -1) {
                        result.setMaxRegionNb(-1);
                    } else {
                        result.setMaxRegionNb(Math.max(result.getMaxRegionNb(), gc.getMaxRegionNb()));
                    }
                }
                if (result.getMaxRegion1DSize() != -1) {
                    if (gc.getMaxRegion1DSize() == -1) {
                        result.setMaxRegion1DSize(-1);
                    } else {
                        result.setMaxRegion1DSize(Math.max(result.getMaxRegion1DSize(), gc.getMaxRegion1DSize()));
                    }
                }
                if (result.getMaxRegion3DSize() != -1) {
                    if (gc.getMaxRegion3DSize() == -1) {
                        result.setMaxRegion3DSize(-1);
                    } else {
                        result.setMaxRegion3DSize(Math.max(result.getMaxRegion3DSize(), gc.getMaxRegion3DSize()));
                    }
                }
            }
        }
        if (found) {
            return result;
        } else {
            LOGGER.warning("Player '" + player.getName() + "' doesn't have any associated group, he will not be able to make regions!");
            return this.defaultGroupConfig;
        }
    }
}
