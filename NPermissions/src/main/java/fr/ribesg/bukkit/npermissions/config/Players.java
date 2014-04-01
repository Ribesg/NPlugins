/***************************************************************************
 * Project file:    NPlugins - NPermissions - Players.java                 *
 * Full Class name: fr.ribesg.bukkit.npermissions.config.Players           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.config;
import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.npermissions.NPermissions;
import fr.ribesg.bukkit.npermissions.permission.PermissionException;
import fr.ribesg.bukkit.npermissions.permission.PermissionsManager;
import fr.ribesg.bukkit.npermissions.permission.PlayerPermissions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/** @author Ribesg */
public class Players extends AbstractConfig<NPermissions> {

	/**
	 * The Permisions Manager
	 */
	private final PermissionsManager manager;

	/**
	 * Players config constructor.
	 *
	 * @param instance the NPermissions plugin instance
	 */
	public Players(final NPermissions instance) {
		super(instance);
		this.manager = instance.getManager();

		final PlayerPermissions notch = new PlayerPermissions(this.manager, UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch", 1, "admin");
		try {
			notch.addAllow("world.rule");
			notch.addDeny("mojang.sell");
		} catch (final PermissionException e) {
			plugin.error(e.getMessage(), e);
		}
		notch.addGroup("example");
		this.manager.getPlayers().put(notch.getPlayerUuid(), notch);
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {
		this.manager.getPlayers().clear();

		for (final String key : config.getKeys(false)) {
			if (!config.isConfigurationSection(key)) {
				plugin.error(Level.WARNING, "Unknown key '" + key + "' found in groups.yml, ignored");
			} else {
				final ConfigurationSection playerSection = config.getConfigurationSection(key);
				final UUID uuid = UUID.fromString(key);
				final String playerName = playerSection.getString("playerName");
				final String mainGroup = playerSection.getString("mainGroup");
				final int priority = playerSection.getInt("priority", 0);
				final List<String> groups = playerSection.getStringList("groups");
				final List<String> allow = playerSection.getStringList("allow");
				final List<String> deny = playerSection.getStringList("deny");

				if (!manager.getGroups().containsKey(mainGroup)) {
					plugin.error("Unknown group '" + mainGroup + "' found in players.yml as main group of player '" + playerName + "' + with UUID '" + key + "', ignored player");
					continue;
				}
				final PlayerPermissions player = new PlayerPermissions(this.manager, uuid, playerName, priority, mainGroup);

				for (final String allowedPermission : allow) {
					try {
						player.addAllow(allowedPermission);
					} catch (final PermissionException e) {
						plugin.error(e.getMessage(), e);
					}
				}

				for (final String deniedPermission : deny) {
					try {
						player.addDeny(deniedPermission);
					} catch (final PermissionException e) {
						plugin.error(e.getMessage(), e);
					}
				}

				for (final String group : groups) {
					if (!manager.getGroups().containsKey(mainGroup)) {
						plugin.error("Unknown group '" + key + "' found in players.yml as secondary group of player '" + playerName + "' + with UUID '" + key + "', ignored group");
					} else {
						player.addGroup(group);
					}
				}

				this.manager.getPlayers().put(uuid, player);
			}
		}

		// Compute group perms
		for (final PlayerPermissions player : this.manager.getPlayers().values()) {
			player.getComputedAllowed();
			player.getComputedDenied();
		}
	}

	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NPermissions plugin PLAYERS", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line).append('\n');
		}
		content.append('\n');

		// TODO print some (commented) example before

		for (final PlayerPermissions player : this.manager.getPlayers().values()) {
			content.append("# The player '" + player.getPlayerName() + "', also have the following permissions:\n");
			content.append("# - maingroup." + player.getMainGroup().toLowerCase() + '\n');
			for (final String group : player.getGroups()) {
				content.append("# - group." + group.toLowerCase() + '\n');
			}
			final YamlConfiguration dummySection = new YamlConfiguration();
			player.save(dummySection);
			content.append(dummySection.saveToString()).append("\n\n");
		}

		return content.toString();
	}
}
