/***************************************************************************
 * Project file:    NPlugins - NCuboid - CreateSubcmdExecutor.java         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.subexecutors.CreateSubcmdExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.CuboidRegion;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSubcmdExecutor extends AbstractSubcmdExecutor {

	public CreateSubcmdExecutor(final NCuboid instance) {
		super(instance);
		setUsage(ChatColor.RED + "Usage : /ncuboid create <regionName>");
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		} else if (args.length != 1) {
			return false;
		} else if (Perms.hasCreate(sender)) {
			final GeneralRegion region = getPlugin().getDb().getByName(args[0]);
			if (region != null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdCreateAlreadyExists);
				return true;
			} else if (args[0].toLowerCase().startsWith("world_")) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdCreateForbiddenName);
				return true;
			} else {
				final Player player = (Player) sender;
				final RegionDb.CreationResult result = getPlugin().getDb().canCreate(player);
				switch (result.getResult()) {
					case DENIED_NO_SELECTION:
						getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateNoValidSelection);
						break;
					case DENIED_TOO_MUCH:
						getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateTooMuchRegions, Integer.toString(result.getMaxValue()), Long.toString(result.getValue()));
						break;
					case DENIED_TOO_LONG:
						getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateRegionTooLong, Integer.toString(result.getMaxValue()), Long.toString(result.getValue()));
						break;
					case DENIED_TOO_BIG:
						getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateRegionTooBig, Integer.toString(result.getMaxValue()), Long.toString(result.getValue()));
						break;
					case DENIED_OVERLAP:
						getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateOverlap, result.getRegion().getRegionName());
						break;
					default:
						final CuboidRegion selection = (CuboidRegion) getPlugin().getDb().removeSelection(player.getUniqueId());
						selection.create(args[0]);
						getPlugin().getDb().add(selection);
						getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateCreated, selection.getRegionName());
						break;
				}
				return true;
			}
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}
}
