/***************************************************************************
 * Project file:    NPlugins - NPermissions - WorldGroups.java             *
 * Full Class name: fr.ribesg.bukkit.npermissions.config.WorldGroups       *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.config;

import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.npermissions.NPermissions;
import fr.ribesg.bukkit.npermissions.permission.GroupPermissions;
import fr.ribesg.bukkit.npermissions.permission.PermissionException;
import fr.ribesg.bukkit.npermissions.permission.PermissionsManager;
import fr.ribesg.bukkit.npermissions.permission.WorldGroupPermissions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Ribesg
 */
public class WorldGroups extends AbstractConfig<NPermissions> {

    /**
     * The Permissions Manager
     */
    private final PermissionsManager manager;

    /**
     * The name of the world this file relates to
     */
    private final String worldName;

    /**
     * Groups config constructor.
     *
     * @param instance the NPermissions plugin instance
     */
    public WorldGroups(final NPermissions instance, final String worldName) {
        super(instance);
        this.manager = instance.getManager();
        this.worldName = worldName;
    }

    @Override
    protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {
        this.manager.getWorldGroups().get(this.worldName).clear();

        final Map<WorldGroupPermissions, List<String>> inheritanceMap = new LinkedHashMap<>();

        for (final String key : config.getKeys(false)) {
            if (!config.isConfigurationSection(key)) {
                this.plugin.error(Level.WARNING, "Unknown key '" + key + "' found in groups.yml, ignored");
            } else {
                final ConfigurationSection groupSection = config.getConfigurationSection(key);
                final String groupName = key.toLowerCase();
                final GroupPermissions groupPermissions = this.manager.getGroups().get(groupName);
                final int priority = groupSection.getInt("priority", 0);
                final List<String> extendsList = groupSection.getStringList("extends");
                final List<String> allow = groupSection.getStringList("allow");
                final List<String> deny = groupSection.getStringList("deny");
                final WorldGroupPermissions worldGroup = new WorldGroupPermissions(this.worldName, groupPermissions, priority);

                for (final String allowedPermission : allow) {
                    try {
                        worldGroup.add(allowedPermission, true);
                    } catch (final PermissionException e) {
                        this.plugin.error("Error while loading group '" + groupName + "' for world '" + this.worldName + "': " + e.getMessage(), e);
                    }
                }

                for (final String deniedPermission : deny) {
                    try {
                        worldGroup.add(deniedPermission, false);
                    } catch (final PermissionException e) {
                        this.plugin.error("Error while loading group '" + groupName + "' for world '" + this.worldName + "': " + e.getMessage(), e);
                    }
                }

                inheritanceMap.put(worldGroup, extendsList);
                this.manager.getWorldGroups().get(this.worldName).put(groupName, worldGroup);
            }
        }

        for (final Entry<WorldGroupPermissions, List<String>> worldGroupPermissionsListEntry : inheritanceMap.entrySet()) {
            final List<String> extendsList = worldGroupPermissionsListEntry.getValue();
            for (final String superGroupName : extendsList) {
                final WorldGroupPermissions superWorldGroup = this.manager.getWorldGroups().get(this.worldName).get(superGroupName.toLowerCase());
                if (superWorldGroup == null) {
                    this.plugin.error("World Group '" + worldGroupPermissionsListEntry.getKey().getGroupName() + "' references unknown super World Group '" + superGroupName.toLowerCase() + '\'');
                } else {
                    worldGroupPermissionsListEntry.getKey().addSuperGroup(superGroupName.toLowerCase());
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
        frame.addLine("Config file for NPermissions plugin WORLD GROUPS", FrameBuilder.Option.CENTER);
        frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
        frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
        for (final String line : frame.build()) {
            content.append(line).append('\n');
        }
        content.append('\n');

        content.append("# This file contains permissions for world '" + this.worldName + "'\n\n");

        // TODO print some (commented) example before

        for (final WorldGroupPermissions worldGroup : this.manager.getWorldGroups().get(this.worldName).values()) {
            final String groupPermission = "group." + worldGroup.getGroupName().toLowerCase();
            content.append("# The group '" + worldGroup.getGroupName() + "', also has all permissions defined at the plugin folder's root\n");
            final SortedSet<String> groupPerms = worldGroup.getAllGroupPerms();
            if (!groupPerms.isEmpty()) {
                content.append("# Members of this group also have the following permissions:\n");
                for (final String groupPerm : groupPerms) {
                    if (!groupPermission.equals(groupPerm) && !this.manager.getGroups().get(worldGroup.getGroupName()).getAllGroupPerms().contains(groupPerm)) {
                        content.append("# - " + groupPerm + '\n');
                    }
                }
            }
            final YamlConfiguration dummySection = new YamlConfiguration();
            worldGroup.save(dummySection);
            content.append(dummySection.saveToString()).append('\n');
        }

        return content.toString();
    }
}
