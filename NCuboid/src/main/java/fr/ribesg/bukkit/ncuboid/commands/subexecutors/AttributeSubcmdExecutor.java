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
        this.setUsage(ChatColor.RED + "Usage : /ncuboid attribute <regionName> <attributeName> [value]");
    }

    @Override
    public boolean exec(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            return false;
        } else if (Perms.hasAttribute(sender)) {
            // Get region, check rights on region
            final GeneralRegion c = this.getPlugin().getDb().getByName(args[0]);
            if (c == null) {
                this.getPlugin().sendMessage(sender, MessageId.cuboid_doesNotExist, args[0]);
                return true;
            } else if (!Perms.hasAdmin(sender) && !(sender instanceof Player && c.isAdmin((Player)sender))) {
                this.getPlugin().sendMessage(sender, MessageId.cuboid_notCuboidAdmin, args[0]);
                return true;
            }

            // Get flag attribute, check rights on flag attribute
            final Attribute att = Attribute.get(args[1]);
            if (att == null) {
                this.getPlugin().sendMessage(sender, MessageId.cuboid_cmdAttUnknownFlagAtt, args[1]);
                return true;
            } else if (!Perms.hasAttribute(sender, att)) {
                this.getPlugin().sendMessage(sender, MessageId.cuboid_cmdAttNoPermission, att.name());
                return true;
            }

            if (args.length == 2) {
                // Show value
                this.getPlugin().sendMessage(sender, MessageId.cuboid_cmdAttValue, att.name(), c.getStringRepresentation(att), c.getRegionName());
                return true;
            } else {
                // Parse and set value
                if (Attribute.isStringAttribute(att)) {
                    final StringBuilder builder = new StringBuilder(args[2]);
                    int i = 3;
                    while (i < args.length) {
                        builder.append(' ').append(args[i]);
                        i++;
                    }
                    final String theString = builder.toString();
                    c.setStringAttribute(att, theString);
                } else if (Attribute.isIntegerAttribute(att)) {
                    if (args.length != 3) {
                        return false;
                    }
                    final int value;
                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (final NumberFormatException e) {
                        return false;
                    }
                    c.setIntegerAttribute(att, value);
                } else if (Attribute.isVectorAttribute(att)) {
                    if (args.length != 3) {
                        return false;
                    }
                    final Vector v = this.parseVector(sender, args[2]);
                    if (v == null) {
                        return false;
                    } else {
                        c.setVectorAttribute(att, v);
                    }
                } else if (Attribute.isLocationAttribute(att)) {
                    if (args.length != 3) {
                        return false;
                    }
                    if (sender instanceof Player) {
                        if ("set".equalsIgnoreCase(args[2])) {
                            final Location loc = ((Player)sender).getLocation();
                            c.setLocationAttribute(att, loc);
                        } else if ("unset".equalsIgnoreCase(args[2])) {
                            c.setLocationAttribute(att, null);
                        } else {
                            return false;
                        }
                    } else {
                        this.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                        return true;
                    }
                } else {
                    // Hello, future
                    throw new UnsupportedOperationException("Not yet implemented for " + att.name());
                }

                // Notice the new value (not necessarily the provided value)
                this.getPlugin().sendMessage(sender, MessageId.cuboid_cmdFlagAttSet, att.name(), c.getStringRepresentation(att), c.getRegionName());
                return true;
            }
        } else {
            this.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
    }

    private Vector parseVector(final CommandSender sender, final String input) {
        if ("set".equalsIgnoreCase(input)) {
            if (sender instanceof Player) {
                return ((Player)sender).getLocation().getDirection();
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
