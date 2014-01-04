/***************************************************************************
 * Project file:    NPlugins - NCuboid - FlagSubcmdExecutor.java           *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.subexecutors.FlagSubcmdExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public class FlagSubcmdExecutor extends AbstractSubcmdExecutor {

	private static final Pattern enable  = Pattern.compile("^(on|enabled?|true|1)$");
	private static final Pattern disable = Pattern.compile("^(off|disabled?|false|0)$");

	public FlagSubcmdExecutor(final NCuboid instance) {
		super(instance);
		setUsage(ChatColor.RED + "Usage : /cuboid flag <regionName> <flagName> [value]");
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		if (args.length != 2 && args.length != 3) {
			return false;
		} else if (Perms.hasFlag(sender)) {
			// Get region, check rights on region
			final GeneralRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			} else if (!c.isAdmin(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidAdmin, args[0]);
				return true;
			}

			// Get flag, check rights on flag
			final Flag f = Flag.get(args[1]);
			if (f == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagUnknownFlag, args[1]);
				return true;
			} else if (!Perms.hasFlag(sender, f)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagNoPermission, f.name());
				return true;
			}

			if (args.length == 2) {
				// Show value
				final boolean value = c.getFlag(f);
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagValue, f.name(), value ? "ON" : "OFF", c.getRegionName());
				return true;
			} else {
				// Get provided value
				final String valueString = args[2].toLowerCase();
				final boolean value;
				if (enable.matcher(valueString).matches()) {
					value = true;
				} else if (disable.matcher(valueString).matches()) {
					value = false;
				} else {
					getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagUnknownValue, args[2]);
					return true;
				}
				if (value == c.getFlag(f)) {
					getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagAlreadySet, f.name(), value ? "ON" : "OFF", c.getRegionName());
					return true;
				}

				// Set value
				c.setFlag(f, value);
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagSet, f.name(), Boolean.toString(value), c.getRegionName());
				return true;
			}
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}
}
