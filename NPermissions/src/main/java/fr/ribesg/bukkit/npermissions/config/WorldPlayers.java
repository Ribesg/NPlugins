/***************************************************************************
 * Project file:    NPlugins - NPermissions - WorldPlayers.java            *
 * Full Class name: fr.ribesg.bukkit.npermissions.config.WorldPlayers      *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.config;

import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.npermissions.NPermissions;
import fr.ribesg.bukkit.npermissions.permission.LegacyPlayerPermissions;
import fr.ribesg.bukkit.npermissions.permission.PermissionException;
import fr.ribesg.bukkit.npermissions.permission.PermissionsManager;
import fr.ribesg.bukkit.npermissions.permission.PlayerPermissions;
import fr.ribesg.bukkit.npermissions.permission.WorldLegacyPlayerPermissions;
import fr.ribesg.bukkit.npermissions.permission.WorldPlayerPermissions;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Ribesg
 */
public class WorldPlayers extends AbstractConfig<NPermissions> {

    /**
     * The Permisions Manager
     */
    private final PermissionsManager manager;

    /**
     * The name of the world this file relates to
     */
    private final String worldName;

    /**
     * Players config constructor.
     *
     * @param instance the NPermissions plugin instance
     */
    public WorldPlayers(final NPermissions instance, final String worldName) {
        super(instance);
        this.manager = instance.getManager();
        this.worldName = worldName;
    }

    @Override
    protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {
        this.manager.getWorldPlayers().get(this.worldName).clear();
        this.manager.getWorldLegacyPlayers().get(this.worldName).clear();

        for (final String key : config.getKeys(false)) {
            if (!config.isConfigurationSection(key)) {
                this.plugin.error(Level.WARNING, "Unknown key '" + key + "' found in " + this.worldName + "/players.yml, ignored");
            } else if ("_legacy".equals(key)) {
                final ConfigurationSection legacyPlayersSection = config.getConfigurationSection(key);
                for (final String legacyKey : legacyPlayersSection.getKeys(false)) {
                    if (!legacyPlayersSection.isConfigurationSection(legacyKey)) {
                        this.plugin.error(Level.WARNING, "Unknown key '" + legacyKey + "' found in " + this.worldName + "/players.yml under _legacy, ignored");
                    } else {
                        final ConfigurationSection legacyPlayerSection = legacyPlayersSection.getConfigurationSection(legacyKey);
                        final String mainGroup = legacyPlayerSection.getString("mainGroup", this.plugin.getPluginConfig().getDefaultGroup()).toLowerCase();
                        final int priority = legacyPlayerSection.getInt("priority", 1);
                        final List<String> groups = legacyPlayerSection.getStringList("groups");
                        final List<String> allow = legacyPlayerSection.getStringList("allow");
                        final List<String> deny = legacyPlayerSection.getStringList("deny");

                        if (!this.manager.getGroups().containsKey(mainGroup)) {
                            this.plugin.error("Unknown group '" + mainGroup + "' found in " + this.worldName + "/players.yml as main group of legacy player '" + legacyKey + "', ignored player");
                            continue;
                        }
                        final LegacyPlayerPermissions legacyPlayerPermissions = this.manager.getLegacyPlayers().get(legacyKey.toLowerCase());
                        if (legacyPlayerPermissions == null) {
                            this.plugin.error("Unknown legacy player '" + legacyKey + "' found in " + this.worldName + "/players.yml, ignored player. It has to be defined in the general Legacy Players list first.");
                            continue;
                        }
                        final WorldLegacyPlayerPermissions worldLegacyPlayer = new WorldLegacyPlayerPermissions(this.worldName, legacyPlayerPermissions, priority);

                        for (final String allowedPermission : allow) {
                            try {
                                worldLegacyPlayer.add(allowedPermission, true);
                            } catch (final PermissionException e) {
                                this.plugin.error("Error while loading player '" + legacyKey + "': " + e.getMessage(), e);
                            }
                        }

                        for (final String deniedPermission : deny) {
                            try {
                                worldLegacyPlayer.add(deniedPermission, false);
                            } catch (final PermissionException e) {
                                this.plugin.error("Error while loading player '" + legacyKey + "': " + e.getMessage(), e);
                            }
                        }

                        for (final String group : groups) {
                            if (!this.manager.getGroups().containsKey(group.toLowerCase())) {
                                this.plugin.error("Unknown group '" + group + "' found in " + this.worldName + "/players.yml as secondary group of player '" + legacyKey + "', ignored group");
                            } else {
                                worldLegacyPlayer.addGroup(group);
                            }
                        }

                        this.manager.getWorldLegacyPlayers().get(this.worldName).put(legacyKey.toLowerCase(), worldLegacyPlayer);
                    }
                }
            } else {
                final ConfigurationSection playerSection = config.getConfigurationSection(key);
                final String playerName = playerSection.getString("playerName");
                final UUID uuid;
                try {
                    uuid = UUID.fromString(key);
                } catch (final IllegalArgumentException e) {
                    this.plugin.error("Malformed UUID '" + key + "' in " + this.worldName + "/players.yml (for player '" + playerName + "')");
                    continue;
                }
                final String mainGroup = playerSection.getString("mainGroup");
                final int priority = playerSection.getInt("priority", 1);
                final List<String> groups = playerSection.getStringList("groups");
                final List<String> allow = playerSection.getStringList("allow");
                final List<String> deny = playerSection.getStringList("deny");

                if (!this.manager.getGroups().containsKey(mainGroup)) {
                    this.plugin.error("Unknown group '" + mainGroup + "' found in " + this.worldName + "/players.yml as main group of player '" + playerName + "' with UUID '" + key + "', ignored player");
                    continue;
                }
                final PlayerPermissions playerPermissions = this.manager.getPlayers().get(uuid);
                final WorldPlayerPermissions worldPlayer = new WorldPlayerPermissions(this.worldName, playerPermissions, priority);

                for (final String allowedPermission : allow) {
                    try {
                        worldPlayer.add(allowedPermission, true);
                    } catch (final PermissionException e) {
                        this.plugin.error("Error while loading player '" + playerName + "' (" + key + "): " + e.getMessage(), e);
                    }
                }

                for (final String deniedPermission : deny) {
                    try {
                        worldPlayer.add(deniedPermission, false);
                    } catch (final PermissionException e) {
                        this.plugin.error("Error while loading player '" + playerName + "' (" + key + "): " + e.getMessage(), e);
                    }
                }

                for (final String group : groups) {
                    if (!this.manager.getGroups().containsKey(group.toLowerCase())) {
                        this.plugin.error("Unknown group '" + group + "' found in " + this.worldName + "/players.yml as secondary group of player '" + playerName + "' with UUID '" + key + "', ignored group");
                    } else {
                        worldPlayer.addGroup(group);
                    }
                }

                this.manager.getWorldPlayers().get(this.worldName).put(uuid, worldPlayer);
                this.manager.addPlayerByName(playerName, uuid);
            }
        }
    }

    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        final FrameBuilder frame;

        // Header
        frame = new FrameBuilder();
        frame.addLine("Config file for NPermissions plugin WORLD PLAYERS", FrameBuilder.Option.CENTER);
        frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
        frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
        for (final String line : frame.build()) {
            content.append(line).append('\n');
        }
        content.append('\n');

        // TODO print some (commented) example before

        for (final WorldPlayerPermissions worldPlayer : this.manager.getWorldPlayers().get(this.worldName).values()) {
            content.append("# The player '" + worldPlayer.getPlayerName() + "' also has all permissions defined at the plugin folder's root\n");
            for (final String groupPerm : worldPlayer.getAllGroupPerms()) {
                content.append("# - " + groupPerm + '\n');
            }
            final YamlConfiguration dummySection = new YamlConfiguration();
            worldPlayer.save(dummySection);
            content.append(dummySection.saveToString()).append('\n');
        }

        content.append("_legacy:\n");
        for (final WorldLegacyPlayerPermissions worldLegacyPlayer : this.manager.getWorldLegacyPlayers().get(this.worldName).values()) {
            content.append("  # The player '" + worldLegacyPlayer.getPlayerName() + "' will also have all permissions defined at the plugin folder's root\n");
            for (final String groupPerm : worldLegacyPlayer.getAllGroupPerms()) {
                content.append("  # - " + groupPerm + '\n');
            }
            final YamlConfiguration dummySection = new YamlConfiguration();
            worldLegacyPlayer.save(dummySection);
            content.append(StringUtil.prependLines(dummySection.saveToString(), "  ")).append('\n');
        }

        return content.toString();
    }
}
