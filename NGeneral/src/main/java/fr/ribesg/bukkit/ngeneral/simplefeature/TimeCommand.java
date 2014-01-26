/***************************************************************************
 * Project file:    NPlugins - NGeneral - TimeCommand.java                 *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.TimeCommand    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {

	private static final String COMMAND = "time";

	private final NGeneral plugin;

	public TimeCommand(final NGeneral instance) {
		this.plugin = instance;
		plugin.setCommandExecutor(COMMAND, this);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equals(COMMAND)) {
			if (!Perms.hasTime(sender)) {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			} else if (args.length == 1) {
				if (!(sender instanceof Player)) {
					plugin.sendMessage(sender, MessageId.missingWorldArg);
					return true;
				}
				final Player player = (Player) sender;
				final World world = player.getWorld();
				final long value = parseValue(args[0]);
				if (value == -1) {
					return false;
				}
				world.setTime(value);
				plugin.broadcastMessage(MessageId.general_timeSet, args[0], world.getName(), player.getName());
				return true;
			} else if (args.length == 2) {
				final World world = Bukkit.getWorld(args[1]);
				if (world == null) {
					plugin.sendMessage(sender, MessageId.unknownWorld, args[1]);
					return true;
				}
				final long value = parseValue(args[0]);
				if (value == -1) {
					return false;
				}
				world.setTime(value);
				plugin.broadcastMessage(MessageId.general_timeSet, args[0], world.getName(), sender.getName());
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private long parseValue(final String value) {
		switch (value.toLowerCase()) {
			case "d":
			case "day":
				return MinecraftTime.DAY.start();
			case "n":
			case "night":
				return MinecraftTime.NIGHT.start();
			default:
				try {
					final long l = Long.parseLong(value);
					return l % MinecraftTime.DAY_LENGTH;
				} catch (NumberFormatException e) {
					return -1;
				}
		}
	}

}
