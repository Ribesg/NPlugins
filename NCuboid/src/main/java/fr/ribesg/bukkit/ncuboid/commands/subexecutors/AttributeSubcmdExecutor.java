/***************************************************************************
 * Project file:    NPlugins - NCuboid - AttributeSubcmdExecutor.java      *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.subexecutors.AttributeSubcmdExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AttributeSubcmdExecutor extends AbstractSubcmdExecutor {

	public AttributeSubcmdExecutor(final NCuboid instance) {
		super(instance);
		setUsage(ChatColor.RED + "Usage : /cuboid attribute <regionName> <attributeName> [value]");
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
			final Attribute fa = Attribute.get(args[1]);
			if (fa == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdAttUnknownFlagAtt, args[1]);
				return true;
			} else if (!Perms.hasFlagAttribute(sender, fa)) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdAttNoPermission, fa.name());
				return true;
			}

			if (args.length == 2) {
				// Show value
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdAttValue, fa.name(), c.getStringRepresentation(fa), c.getRegionName());
				return true;
			} else {
				// Parse and set value
				if (Attribute.isIntFlagAtt(fa)) {
					final int value;
					try {
						value = Integer.parseInt(args[2]);
					} catch (final NumberFormatException e) {
						return false;
					}
					c.setIntAttribute(fa, value);
				} else if (Attribute.isVectFlagAtt(fa)) {
					final Vector v = parseVector(sender, args[2]);
					if (v == null) {
						return false;
					} else {
						c.setVectAttribute(fa, v);
					}
				} else if (Attribute.isLocFlagAtt(fa)) {
					if (sender instanceof Player) {
						if ("set".equalsIgnoreCase(args[2])) {
							final Location loc = ((Player) sender).getLocation();
							c.setLocAttribute(fa, loc);
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

	private Vector parseVector(final CommandSender sender, final String input) {
		if ("set".equalsIgnoreCase(input)) {
			if (sender instanceof Player) {
				return ((Player) sender).getLocation().getDirection();
			} else {
				return null;
			}
		} else {
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
}
