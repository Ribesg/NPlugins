/***************************************************************************
 * Project file:    NPlugins - NGeneral - WalkSpeedCommand.java            *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.WalkSpeedCommand
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class WalkSpeedCommand implements CommandExecutor, Listener {

	private static final float  DEFAULT_BUKKIT_WALKSPEED = 0.2f;
	private static final String COMMAND                  = "walkspeed";

	private final NGeneral plugin;

	public WalkSpeedCommand(final NGeneral instance) {
		this.plugin = instance;
		plugin.setCommandExecutor(COMMAND, this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		event.getPlayer().setWalkSpeed(DEFAULT_BUKKIT_WALKSPEED);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equals(COMMAND)) {
			if (!Perms.hasWalkSpeed(sender)) {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
			} else if (args.length == 1) {
				if (!(sender instanceof Player)) {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
				} else {
					final Player player = (Player) sender;
					try {
						final float value = Float.parseFloat(args[0]);
						if (value < -1 || value > 1) {
							return false;
						}
						player.setWalkSpeed(value);
						plugin.sendMessage(player, MessageId.general_walkSpeed_set, Float.toString(value));
					} catch (NumberFormatException e) {
						player.setWalkSpeed(DEFAULT_BUKKIT_WALKSPEED);
						plugin.sendMessage(player, MessageId.general_walkSpeed_reset);
					}
				}
			} else if (args.length == 2) {
				if (!Perms.hasWalkSpeedOthers(sender)) {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				} else {
					final String arg0 = args[0].toLowerCase();
					float value = DEFAULT_BUKKIT_WALKSPEED;
					try {
						value = Float.parseFloat(arg0);
						if (value < -1 || value > 1) {
							return false;
						}
					} catch (NumberFormatException ignored) {
					}
					final String arg1 = args[1].toLowerCase();
					final String[] names = arg1.split(",");
					setAll(sender, names, value);
				}
				return true;
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Change Speed of all players provided.
	 *
	 * @param playerNames Names of players
	 * @param value       Null for toggle, actual value for a set
	 */
	private void setAll(final CommandSender sender, final String[] playerNames, final float value) {
		for (final String name : playerNames) {
			final Player p = Bukkit.getPlayer(name);
			if (p == null) {
				plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, name);
			} else {
				p.setWalkSpeed(value);
				plugin.sendMessage(sender, MessageId.general_walkSpeed_setFor, p.getName(), Float.toString(value));
				plugin.sendMessage(p, MessageId.general_walkSpeed_setBy, sender.getName(), Float.toString(value));
			}
		}
	}
}
