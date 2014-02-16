/***************************************************************************
 * Project file:    NPlugins - NCuboid - AdminUserGroupJailSubcmdExecutor.java
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.subexecutors.AdminUserGroupJailSubcmdExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands.subexecutors;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.Jail;
import fr.ribesg.bukkit.ncuboid.beans.PlayerRegion;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminUserGroupJailSubcmdExecutor extends AbstractSubcmdExecutor {

	public enum Mode {
		ADMIN,
		USER,
		GROUP,
		JAIL
	}

	private final Mode mode;

	public AdminUserGroupJailSubcmdExecutor(final NCuboid instance, final Mode mode) {
		super(instance);
		this.mode = mode;
		switch (mode) {
			case ADMIN:
				setUsage(ChatColor.RED + "Usage : /cuboid admin <regionName> add|del <playerName>[,playerName]");
				break;
			case USER:
				setUsage(ChatColor.RED + "Usage : /cuboid user <regionName> add|del <playerName>[,playerName]");
				break;
			case GROUP:
				setUsage(ChatColor.RED + "Usage : /cuboid group <regionName> add|del <groupName>[,groupName]");
				break;
			case JAIL:
				setUsage(ChatColor.RED + "Usage : /cuboid jail <regionName> add|del <jailName>");
				break;
		}
	}

	@Override
	protected boolean exec(final CommandSender sender, final String[] args) {
		if (args.length != 3) {
			return false;
		} else {
			switch (mode) {
				case ADMIN:
					return execAdmin(sender, args);
				case USER:
					return execUser(sender, args);
				case GROUP:
					return execGroup(sender, args);
				case JAIL:
					return execJail(sender, args);
				default:
					return false; // Dead code
			}
		}
	}

	private boolean execAdmin(final CommandSender sender, final String[] args) {
		if (Perms.hasAdmin(sender)) {
			// Get region, check rights on region
			final GeneralRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			}
			if (!Perms.isAdmin(sender) && (c.getType() == GeneralRegion.RegionType.WORLD || !((PlayerRegion) c).isOwner(sender))) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidOwner, c.getRegionName());
				return true;
			}

			// Get required action
			final Boolean add = getAction(args[1]);
			if (add == null) {
				return false;
			}

			// Now for each provided playerName
			for (final String name : args[2].toLowerCase().split(",")) {
				if (add) {
					if (c.isAdminName(name)) {
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdAdminAlreadyAdmin, name, c.getRegionName());
					} else {
						c.addAdmin(name);
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdAdminAdded, name, c.getRegionName());
					}
				} else {
					if (!c.isAdminName(name)) {
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdAdminNotAdmin, name, c.getRegionName());
						return true;
					} else {
						c.removeAdmin(name);
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdAdminRemoved, name, c.getRegionName());
					}
				}
			}
			return true;
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}

	private boolean execUser(final CommandSender sender, final String[] args) {
		if (Perms.hasUser(sender)) {
			// Get region, check rights on region
			final GeneralRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			}
			if (!Perms.isAdmin(sender) && !c.isAdmin(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidOwner, c.getRegionName());
				return true;
			}

			// Get required action
			final Boolean add = getAction(args[1]);
			if (add == null) {
				return false;
			}

			// Now for each provided playerName
			for (final String name : args[2].toLowerCase().split(",")) {
				if (add) {
					if (c.isUserName(name)) {
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdUserAlreadyUser, name, c.getRegionName());
					} else {
						c.addUser(name);
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdUserAdded, name, c.getRegionName());
					}
				} else {
					if (!c.isUserName(name)) {
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdUserNotUser, name, c.getRegionName());
						return true;
					} else {
						c.removeUser(name);
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdUserRemoved, name, c.getRegionName());
					}
				}
			}
			return true;
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}

	private boolean execGroup(final CommandSender sender, final String[] args) {
		if (Perms.hasGroup(sender)) {
			// Get region, check rights on region
			final GeneralRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			}
			if (!Perms.isAdmin(sender) && !c.isAdmin(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidOwner, c.getRegionName());
				return true;
			}

			// Get required action
			final Boolean add = getAction(args[1]);
			if (add == null) {
				return false;
			}

			// Now for each provided groupName
			for (final String name : args[2].toLowerCase().split(",")) {
				if (add) {
					if (c.isAllowedGroup(name)) {
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdGroupAlreadyGroup, name, c.getRegionName());
					} else {
						c.allowGroup(name);
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdGroupAdded, name, c.getRegionName());
					}
				} else {
					if (!c.isAllowedGroup(name)) {
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdGroupNotGroup, name, c.getRegionName());
						return true;
					} else {
						c.denyGroup(name);
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdGroupRemoved, name, c.getRegionName());
					}
				}
			}
			return true;
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}

	private boolean execJail(final CommandSender sender, final String[] args) {
		if (Perms.hasJail(sender)) {
			// Get region, check rights on region
			final GeneralRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			}
			if (!Perms.isAdmin(sender) && !c.isAdmin(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidAdmin, c.getRegionName());
				return true;
			}
			if (!c.getFlag(Flag.JAIL)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdJailNotJailCuboid, c.getRegionName());
				return true;
			}

			// Get required action
			final Boolean add = getAction(args[1]);
			if (add == null) {
				return false;
			}

			// Do the stuff
			if (add) {
				if (!(sender instanceof Player)) {
					getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
				} else {
					final Player player = (Player) sender;
					final String jailName = args[2];
					final NLocation location = new NLocation(player.getLocation());
					if (!c.contains(location)) {
						getPlugin().sendMessage(player, MessageId.cuboid_cmdJailNotInRegion, location.toString(), c.getRegionName());
					} else {
						if (getPlugin().getJails().add(new Jail(jailName, location, c))) {
							getPlugin().sendMessage(player, MessageId.cuboid_cmdJailCreated, jailName, c.getRegionName(), location.toString());
						} else {
							getPlugin().sendMessage(player, MessageId.cuboid_cmdJailAlreadyExists, jailName);
						}
					}
				}
			} else {
				final String jailName = args[2];
				if (getPlugin().getJails().remove(jailName)) {
					getPlugin().sendMessage(sender, MessageId.cuboid_cmdJailRemoved, jailName);
				} else {
					getPlugin().sendMessage(sender, MessageId.cuboid_cmdJailUnknown, jailName);
				}
			}
			return true;
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}

	/**
	 * @param actionString a String that represents an action
	 *
	 * @return true if the action is to ADD, false if it's to REMOVE, null if
	 * it's incorrect
	 */
	private Boolean getAction(final String actionString) {
		switch (actionString.toLowerCase()) {
			case "add":
				return true;
			case "rm":
			case "remove":
			case "del":
			case "delete":
				return false;
			default:
				return null;
		}
	}
}
