/***************************************************************************
 * Project file:    NPlugins - NGeneral - AfkCommand.java                  *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.AfkCommand     *
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

public class AfkCommand implements CommandExecutor {

	private static final String AFK_PREFIX = "" + ChatColor.GRAY + ChatColor.ITALIC;

	private final NGeneral plugin;

	public AfkCommand(final NGeneral instance) {
		this.plugin = instance;
		plugin.getCommand("afk").setExecutor(this);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equals("afk")) {
			if (!Perms.hasAfk(sender)) {
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
				if (player.getPlayerListName().startsWith(AFK_PREFIX)) {
					player.setPlayerListName(player.getName());
					if (plugin.getPluginConfig().hasBroadCastOnAfk()) {
						if (reason.length() > 0) {
							plugin.broadcastMessage(MessageId.general_afk_noLongerAfkBroadcastReason, player.getName(), reason);
						} else {
							plugin.broadcastMessage(MessageId.general_afk_noLongerAfkBroadcast, player.getName());
						}
					}
				} else {
					String newPlayerListName = AFK_PREFIX + player.getName();
					if (newPlayerListName.length() > 16) {
						newPlayerListName = newPlayerListName.substring(0, 16);
					}
					player.setPlayerListName(newPlayerListName);
					if (plugin.getPluginConfig().hasBroadCastOnAfk()) {
						if (reason.length() > 0) {
							plugin.broadcastMessage(MessageId.general_afk_nowAfkBroadcastReason, player.getName(), reason);
						} else {
							plugin.broadcastMessage(MessageId.general_afk_nowAfkBroadcast, player.getName());
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
