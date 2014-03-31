/***************************************************************************
 * Project file:    NPlugins - NPermissions - Groups.java                  *
 * Full Class name: fr.ribesg.bukkit.npermissions.config.Groups            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.config;
import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.npermissions.NPermissions;
import fr.ribesg.bukkit.npermissions.permission.GroupPermissions;
import fr.ribesg.bukkit.npermissions.permission.PermissionException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/** @author Ribesg */
public class Groups extends AbstractConfig<NPermissions> {

	private final Map<String, GroupPermissions> groups;

	public Groups(final NPermissions instance) {
		super(instance);
		this.groups = new LinkedHashMap<>();
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {
		final Map<GroupPermissions, List<String>> inheritanceMap = new LinkedHashMap<>();

		for (final String key : config.getKeys(false)) {
			if (!config.isConfigurationSection(key)) {
				plugin.error(Level.WARNING, "Unknown key '" + key + "' found in groups.yml, ignored");
			} else {
				final int priority = config.getInt("priority", 0);
				final List<String> extendsList = config.getStringList("extends");
				final List<String> allow = config.getStringList("allow");
				final List<String> deny = config.getStringList("deny");
				final GroupPermissions group = new GroupPermissions(key, priority);

				for (final String allowedPermission : allow) {
					try {
						group.addAllow(allowedPermission);
					} catch (final PermissionException e) {
						plugin.error(e.getMessage(), e);
					}
				}

				for (final String deniedPermission : deny) {
					try {
						group.addDeny(deniedPermission);
					} catch (final PermissionException e) {
						plugin.error(e.getMessage(), e);
					}
				}

				inheritanceMap.put(group, extendsList);
				this.groups.put(key, group);
			}
		}

		for (final GroupPermissions group : inheritanceMap.keySet()) {
			final List<String> extendsList = inheritanceMap.get(group);
			for (final String superGroupName : extendsList) {
				final GroupPermissions superGroup = this.groups.get(superGroupName);
				if (superGroup == null) {
					plugin.error("Group '" + group.getGroupName() + "' references unknown supergroup '" + superGroupName + "'");
				} else {
					group.addSuperGroup(superGroup);
				}
			}
		}

		// Compute group perms
		for (final GroupPermissions group : this.groups.values()) {
			group.getComputedAllowed();
			group.getComputedDenied();
		}
	}

	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NPermissions plugin GROUPS", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line).append('\n');
		}

		final YamlConfiguration dummySection = new YamlConfiguration();
		for (final GroupPermissions group : this.groups.values()) {
			group.save(dummySection);
		}
		content.append(dummySection.saveToString()).append('\n');

		return content.toString();
	}
}
