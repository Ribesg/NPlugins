/***************************************************************************
 * Project file:    NPlugins - NCuboid - FlagAttributeSubcmdExecutor.java  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.subexecutors.FlagAttributeSubcmdExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.FlagAtt;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FlagAttributeSubcmdExecutor extends AbstractSubcmdExecutor {

	public FlagAttributeSubcmdExecutor(final NCuboid instance) {
		super(instance);
		setUsage(ChatColor.RED + "Usage : /cuboid flagAttribute <regionName> <flagAttributeName> [value]");
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		if (args.length != 2 && args.length != 3) {
			return false;
		} else if (Perms.hasFlagAttribute(sender)) {
			// Get region, check rights on region
			final GeneralRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
				return true;
			} else if (!c.isAdmin(sender)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidAdmin, args[0]);
				return true;
			}

			// Get flag attribute, check rights on flag attribute
			final FlagAtt fa = FlagAtt.get(args[1]);
			if (fa == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagAttUnknownFlagAtt, args[1]);
				return true;
			} else if (!Perms.hasFlagAttribute(sender, fa)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagAttNoPermission, fa.name());
				return true;
			}

			if (args.length == 2) {
				// Show value
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagAttValue, fa.name(), c.getStringRepresentation(fa), c.getRegionName());
				return true;
			} else {
				// Parse and set value
				if (FlagAtt.isIntFlagAtt(fa)) {
					final int value;
					try {
						value = Integer.parseInt(args[2]);
					} catch (final NumberFormatException e) {
						return false;
					}
					c.setIntFlagAtt(fa, value);
				} else if (FlagAtt.isVectFlagAtt(fa)) {
					final Vector v = parseVector(args[2]);
					if (v == null) {
						return false;
					} else {
						c.setVectFlagAtt(fa, v);
					}
				} else if (FlagAtt.isLocFlagAtt(fa)) {
					if (sender instanceof Player) {
						if ("set".equalsIgnoreCase(args[2])) {
							final Location loc = ((Player) sender).getLocation();
							c.setLocFlagAtt(fa, loc);
						} else {
							return false;
						}
					} else {
						getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
						return true;
					}
				} else {
					// Hello, future
					throw new UnsupportedOperationException("Not yet implemented for " + fa.name());
				}

				// Notice the new value (not necessarily the provided value)
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagAttSet, fa.name(), c.getStringRepresentation(fa), c.getRegionName());
				return true;
			}
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}

	private Vector parseVector(final String input) {
		final String splitChar;
		if (input.contains(";")) {
			splitChar = ";";
		} else if (input.contains(",")) {
			splitChar = ",";
		} else {
			return null;
		}
		final String[] parts = input.split(splitChar);
		if (parts.length != 3) {
			return null;
		}
		try {
			return new Vector(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
		} catch (final NumberFormatException e) {
			return null;
		}
	}
}
