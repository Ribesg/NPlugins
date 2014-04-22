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
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.npermissions.NPermissions;
import fr.ribesg.bukkit.npermissions.permission.GroupPermissions;
import fr.ribesg.bukkit.npermissions.permission.PermissionException;
import fr.ribesg.bukkit.npermissions.permission.PermissionsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Level;

/**
 * @author Ribesg
 */
public class Groups extends AbstractConfig<NPermissions> {

	/**
	 * Default NPlugins simple permissions: admin and user prefixes
	 */
	private static final String[] DEFAULT_PERMISSIONS_PREFIXES = new String[] {
			"ncuboid.",
			"nenchantingegg.",
			"ngeneral.",
			"npermissions.",
			"nplayer.",
			"ntalk.",
			"ntheendagain.",
			"nworld."
	};

	/**
	 * The Permissions Manager
	 */
	private final PermissionsManager manager;

	/**
	 * Groups config constructor.
	 *
	 * @param instance the NPermissions plugin instance
	 */
	public Groups(final NPermissions instance) {
		super(instance);
		this.manager = instance.getManager();

		final GroupPermissions user = new GroupPermissions(this.manager, "user", 0);
		final GroupPermissions admin = new GroupPermissions(this.manager, "admin", 0);
		final GroupPermissions example = new GroupPermissions(this.manager, "example", 0);
		try {
			for (final String permPrefix : DEFAULT_PERMISSIONS_PREFIXES) {
				user.add(permPrefix + "user", true);
				admin.add(permPrefix + "admin", true);
			}
		} catch (final PermissionException e) {
			plugin.error(e.getMessage(), e);
		}
		admin.addSuperGroup("user");

		this.manager.getGroups().put("user", user);
		this.manager.getGroups().put("admin", admin);
		this.manager.getGroups().put("example", example);
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {
		this.manager.getGroups().clear();

		final Map<GroupPermissions, List<String>> inheritanceMap = new LinkedHashMap<>();

		for (final String key : config.getKeys(false)) {
			if (!config.isConfigurationSection(key)) {
				plugin.error(Level.WARNING, "Unknown key '" + key + "' found in groups.yml, ignored");
			} else {
				final ConfigurationSection groupSection = config.getConfigurationSection(key);
				final String groupName = key.toLowerCase();
				final int priority = groupSection.getInt("priority", 0);
				final List<String> extendsList = groupSection.getStringList("extends");
				final List<String> allow = groupSection.getStringList("allow");
				final List<String> deny = groupSection.getStringList("deny");
				final GroupPermissions group = new GroupPermissions(this.manager, groupName, priority);

				for (final String allowedPermission : allow) {
					try {
						group.add(allowedPermission, true);
					} catch (final PermissionException e) {
						plugin.error("Error while loading group '" + groupName + "': " + e.getMessage(), e);
					}
				}

				for (final String deniedPermission : deny) {
					try {
						group.add(deniedPermission, false);
					} catch (final PermissionException e) {
						plugin.error("Error while loading group '" + groupName + "': " + e.getMessage(), e);
					}
				}

				inheritanceMap.put(group, extendsList);
				this.manager.getGroups().put(groupName, group);
			}
		}

		for (final GroupPermissions group : inheritanceMap.keySet()) {
			final List<String> extendsList = inheritanceMap.get(group);
			for (final String superGroupName : extendsList) {
				final GroupPermissions superGroup = this.manager.getGroups().get(superGroupName.toLowerCase());
				if (superGroup == null) {
					plugin.error("Group '" + group.getGroupName() + "' references unknown supergroup '" + superGroupName.toLowerCase() + "'");
				} else {
					group.addSuperGroup(superGroupName.toLowerCase());
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
		frame.addLine("Config file for NPermissions plugin GROUPS", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line).append('\n');
		}
		content.append('\n');

		// TODO print some (commented) example before

		for (final GroupPermissions group : this.manager.getGroups().values()) {
			final String groupPermission = "group." + group.getGroupName().toLowerCase();
			final String mainPermission = "main" + groupPermission;
			content.append("# The group '" + group.getGroupName() + "', also defines the following permissions:\n");
			content.append("# - " + mainPermission + " - For players for whom this group is the main group (unique per player)\n");
			content.append("# - " + groupPermission + " - For members of this group AND members of subgroups\n");
			final SortedSet<String> groupPerms = group.getAllGroupPerms();
			if (groupPerms.size() > 0) {
				content.append("# Members of this group also have the following permissions:\n");
				for (final String groupPerm : groupPerms) {
					if (!groupPermission.equals(groupPerm)) {
						content.append("# - " + groupPerm + "\n");
					}
				}
			}
			final YamlConfiguration dummySection = new YamlConfiguration();
			group.save(dummySection);
			content.append(dummySection.saveToString()).append("\n");
		}

		return content.toString();
	}
}
