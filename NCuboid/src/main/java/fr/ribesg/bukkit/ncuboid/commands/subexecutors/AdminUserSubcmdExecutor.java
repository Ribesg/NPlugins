package fr.ribesg.bukkit.ncuboid.commands.subexecutors;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.PlayerRegion;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AdminUserSubcmdExecutor extends AbstractSubcmdExecutor {

	private static final String USAGE_ADMIN = ChatColor.RED + "Usage : /cuboid admin <regionName> add|del <playerName>[,playerName]";
	private static final String USAGE_USER  = ChatColor.RED + "Usage : /cuboid user <regionName> add|del <playerName>[,playerName]";

	private final boolean adminMode;

	public AdminUserSubcmdExecutor(final NCuboid instance, final boolean adminMode) {
		super(instance);
		this.adminMode = adminMode;
	}

	@Override
	protected boolean exec(final CommandSender sender, final String[] args) {
		if (args.length != 3) {
			sender.sendMessage(getPlugin().getMessages().getMessageHeader() + (adminMode ? USAGE_ADMIN : USAGE_USER));
			return true;
		} else {
			return adminMode ? execAdmin(sender, args) : execUser(sender, args);
		}
	}

	private boolean execAdmin(final CommandSender sender, final String[] args) {
		if (Perms.hasAdmin(sender)) {
			// Get region, check rights on region
			final PlayerRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			}
			if (!Perms.isAdmin(sender) && !c.isOwner(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidOwner, c.getRegionName());
				return true;
			}

			// Get required action
			final Boolean add = getAction(args[1]);
			if (add == null) {
				sender.sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE_ADMIN);
				return true;
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
			final PlayerRegion c = getPlugin().getDb().getByName(args[0]);
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
				sender.sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE_USER);
				return true;
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

	/**
	 * @param actionString a String that represents an action
	 *
	 * @return true if the action is to ADD, false if it's to REMOVE, null if
	 *         it's incorrect
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
