package fr.ribesg.bukkit.ncuboid.commands.subexecutors;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.PlayerRegion;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Set;

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
		} else if (adminMode ? Perms.hasAdmin(sender) : Perms.hasUser(sender)) {
			// Get region, check rights on region
			final PlayerRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			}
			if (adminMode && !Perms.isAdmin(sender) && !c.isOwner(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidOwner, c.getRegionName());
				return true;
			} else if (!adminMode && !Perms.isAdmin(sender) && !c.isAdmin(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidAdmin, c.getRegionName());
				return true;
			}

			// Get appropriate user list
			final Set<String> players = adminMode ? c.getAdmins() : c.getAllowedPlayers();

			// Get required action
			final Boolean add = getAction(args[1]);
			if (add == null) {
				sender.sendMessage(getPlugin().getMessages().getMessageHeader() + (adminMode ? USAGE_ADMIN : USAGE_USER));
				return true;
			}

			// Now for each provided playerName
			for (final String playerName : args[2].toLowerCase().split(",")) {
				if (add) {
					if (players.contains(playerName)) {
						getPlugin().sendMessage(sender,
						                        adminMode ? MessageId.cuboid_cmdAdminAlreadyAdmin : MessageId.cuboid_cmdUserAlreadyUser,
						                        playerName,
						                        c.getRegionName());
					} else {
						players.add(playerName);
						getPlugin().sendMessage(sender,
						                        adminMode ? MessageId.cuboid_cmdAdminAdded : MessageId.cuboid_cmdUserAdded,
						                        playerName,
						                        c.getRegionName());
					}
				} else {
					if (!players.contains(playerName)) {
						getPlugin().sendMessage(sender,
						                        adminMode ? MessageId.cuboid_cmdAdminNotAdmin : MessageId.cuboid_cmdUserNotUser,
						                        playerName,
						                        c.getRegionName());
						return true;
					} else {
						players.remove(playerName);
						getPlugin().sendMessage(sender,
						                        adminMode ? MessageId.cuboid_cmdAdminRemoved : MessageId.cuboid_cmdUserRemoved,
						                        playerName,
						                        c.getRegionName());
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
