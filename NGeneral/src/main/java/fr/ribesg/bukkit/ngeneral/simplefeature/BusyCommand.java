/***************************************************************************
 * Project file:    NPlugins - NGeneral - BusyCommand.java                 *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.BusyCommand    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BusyCommand implements CommandExecutor {

	private static final String BUSY_PREFIX = "" + ChatColor.DARK_RED + ChatColor.ITALIC;
	private static final String COMMAND                 = "busy";

	private final NGeneral plugin;

	public BusyCommand(final NGeneral instance) {
		this.plugin = instance;
		plugin.setCommandExecutor(COMMAND, this);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equals(COMMAND)) {
			if (!Perms.hasBusy(sender)) {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			} else if (!(sender instanceof Player)) {
				plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			} else {
				final Player player = (Player) sender;
				String reason = "";
				for (final String arg : args) {
					reason += arg + ' ';
				}
				if (reason.length() > 0) {
					reason = reason.substring(0, reason.length() - 1);
				}
				if (player.getPlayerListName().startsWith(BUSY_PREFIX)) {
					player.setPlayerListName(player.getName());
					if (plugin.getPluginConfig().hasBroadCastOnBusy()) {
						if (reason.length() > 0) {
							plugin.broadcastMessage(MessageId.general_busy_noLongerBusyBroadcastReason, player.getName(), reason);
						} else {
							plugin.broadcastMessage(MessageId.general_busy_noLongerBusyBroadcast, player.getName());
						}
					}
				} else {
					String newPlayerListName = BUSY_PREFIX + player.getName();
					if (newPlayerListName.length() > 16) {
						newPlayerListName = newPlayerListName.substring(0, 16);
					}
					player.setPlayerListName(newPlayerListName);
					if (plugin.getPluginConfig().hasBroadCastOnBusy()) {
						if (reason.length() > 0) {
							plugin.broadcastMessage(MessageId.general_busy_nowBusyBroadcastReason, player.getName(), reason);
						} else {
							plugin.broadcastMessage(MessageId.general_busy_nowBusyBroadcast, player.getName());
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
