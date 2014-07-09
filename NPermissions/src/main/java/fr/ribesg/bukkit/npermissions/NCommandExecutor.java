/***************************************************************************
 * Project file:    NPlugins - NPermissions - NCommandExecutor.java        *
 * Full Class name: fr.ribesg.bukkit.npermissions.NCommandExecutor         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import fr.ribesg.bukkit.npermissions.permission.LegacyPlayerPermissions;
import fr.ribesg.bukkit.npermissions.permission.PlayerPermissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.UUID;

public class NCommandExecutor implements CommandExecutor {

	private final NPermissions plugin;

	public NCommandExecutor(final NPermissions instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		switch (cmd.getName()) {
			case "npermissions":
				return this.cmdPermissions(sender, args);
			case "setgroup":
				return this.cmdSetGroup(sender, args);
			default:
				return false;
		}
	}

	private boolean cmdPermissions(final CommandSender sender, final String[] args) {
		if (args.length < 1) {
			return false;
		}
		switch (args[0].toLowerCase()) {
			case "reload":
			case "rld":
				if (Perms.hasReload(sender)) {
					if (args.length != 2) {
						return false;
					}
					switch (args[1].toLowerCase()) {
						case "messages":
						case "mess":
						case "mes":
							return cmdReloadMessages(sender, args);
						case "groups":
						case "group":
						case "g":
							return cmdReloadGroups(sender, args);
						case "players":
						case "player":
						case "p":
							return cmdReloadPlayers(sender, args);
						default:
							return false;
					}
				} else {
					this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			default:
				return false;
		}
	}

	private boolean cmdReloadMessages(final CommandSender sender, final String[] args) {
		if (Perms.hasReloadMessages(sender)) {
			try {
				this.plugin.loadMessages();
				this.plugin.sendMessage(sender, MessageId.cmdReloadMessages);
			} catch (final IOException e) {
				this.plugin.error("An error occured when NPermissions tried to load messages.yml", e);
				this.plugin.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
			}
		} else {
			this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
		}
		return true;
	}

	private boolean cmdReloadGroups(final CommandSender sender, final String[] args) {
		if (Perms.hasReloadPermissions(sender)) {
			try {
				this.plugin.getGroupsConfig().loadConfig("groups.yml");
				this.plugin.sendMessage(sender, MessageId.cmdReloadGroups);
				this.plugin.getManager().reload();
			} catch (final IOException | InvalidConfigurationException e) {
				this.plugin.error("An error occured when NPermissions tried to load groups.yml", e);
				this.plugin.sendMessage(sender, MessageId.cmdReloadError, "groups.yml");
			}
		} else {
			this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
		}
		return true;
	}

	private boolean cmdReloadPlayers(final CommandSender sender, final String[] args) {
		if (Perms.hasReloadPermissions(sender)) {
			try {
				this.plugin.getPlayersConfig().loadConfig("players.yml");
				this.plugin.sendMessage(sender, MessageId.cmdReloadPlayers);
				this.plugin.getManager().reload();
			} catch (final IOException | InvalidConfigurationException e) {
				this.plugin.error("An error occured when NPermissions tried to load players.yml", e);
				this.plugin.sendMessage(sender, MessageId.cmdReloadError, "players.yml");
			}
		} else {
			this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
		}
		return true;
	}

	private boolean cmdSetGroup(final CommandSender sender, final String[] args) {
		if (Perms.hasSetGroup(sender)) {
			if (args.length < 2 || args.length > 3) {
				return false;
			} else {
				final boolean force;
				if (args.length == 3) {
					if (args[2].equalsIgnoreCase("force")) {
						force = true;
					} else {
						return false;
					}
				} else {
					force = false;
				}
				final String[] input = args[0].contains(",") ? args[0].split(",") : new String[] {args[0]};
				final String groupName = args[1].toLowerCase();
				if (!this.plugin.getManager().getGroups().containsKey(groupName)) {
					this.plugin.sendMessage(sender, MessageId.permissions_unknownGroup, groupName);
					return true;
				} else {
					for (final String id : input) {
						UUID uuid;
						try {
							uuid = UUID.fromString(id);
						} catch (final IllegalArgumentException e1) {
							if (id.length() == 32) {
								// Maybe an UUID without dashes? Add them!
								try {
									uuid = PlayerIdsUtil.shortUuidToUuid(id);
								} catch (final IllegalArgumentException e2) {
									uuid = this.plugin.getManager().getByPlayerName(id);
								}
							} else {
								uuid = this.plugin.getManager().getByPlayerName(id);
							}
						}
						if (uuid != null) {
							PlayerPermissions playerPerms = this.plugin.getManager().getPlayers().get(uuid);
							if (playerPerms == null) {
								if (force) {
									playerPerms = new PlayerPermissions(this.plugin.getManager(), uuid, "_UNKNOWN", 1, groupName);
									this.plugin.getManager().getPlayers().put(uuid, playerPerms);
									this.plugin.sendMessage(sender, MessageId.permissions_newPlayer, uuid.toString(), groupName);
									this.plugin.savePlayers();
								} else {
									this.plugin.sendMessage(sender, MessageId.permissions_unknownUuid, uuid.toString());
								}
							} else if (playerPerms.getMainGroup().equals(groupName)) {
								this.plugin.sendMessage(sender, MessageId.permissions_alreadyMainGroup, playerPerms.getPlayerName(), groupName);
							} else {
								playerPerms.setMainGroup(groupName);
								playerPerms.getGroups().remove(groupName);
								playerPerms.computePermissions();
								this.plugin.getManager().reRegisterPlayer(uuid);
								this.plugin.sendMessage(sender, MessageId.permissions_changedGroup, playerPerms.getPlayerName(), groupName);
								this.plugin.savePlayers();
							}
						} else {
							// At least check that it's a valid username
							if (!PlayerIdsUtil.isValidMinecraftUserName(id)) {
								this.plugin.sendMessage(sender, MessageId.permissions_unknown, id);
							} else {
								LegacyPlayerPermissions legacyPlayerPerms = this.plugin.getManager().getLegacyPlayers().get(id.toLowerCase());
								if (legacyPlayerPerms == null) {
									if (force) {
										legacyPlayerPerms = new LegacyPlayerPermissions(this.plugin.getManager(), id, 1, groupName);
										this.plugin.getManager().getLegacyPlayers().put(id.toLowerCase(), legacyPlayerPerms);
										this.plugin.sendMessage(sender, MessageId.permissions_newLegacyPlayer, id, groupName);
										this.plugin.savePlayers();
									} else {
										this.plugin.sendMessage(sender, MessageId.permissions_unknownPlayer, id);
									}
								} else if (legacyPlayerPerms.getMainGroup().equals(groupName)) {
									this.plugin.sendMessage(sender, MessageId.permissions_alreadyMainGroup, legacyPlayerPerms.getPlayerName(), groupName);
								} else {
									legacyPlayerPerms.setMainGroup(groupName);
									legacyPlayerPerms.getGroups().remove(groupName);
									legacyPlayerPerms.computePermissions();
									this.plugin.sendMessage(sender, MessageId.permissions_changedLegacyGroup, legacyPlayerPerms.getPlayerName(), groupName);
									this.plugin.savePlayers();
								}
							}
						}
					}
					return true;
				}
			}
		} else {
			this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}
}