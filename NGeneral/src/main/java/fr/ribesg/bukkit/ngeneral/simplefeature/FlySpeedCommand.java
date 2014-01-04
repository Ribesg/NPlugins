/***************************************************************************
 * Project file:    NPlugins - NGeneral - FlySpeedCommand.java             *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.FlySpeedCommand*
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

public class FlySpeedCommand implements CommandExecutor, Listener {

	private static final float  DEFAULT_BUKKIT_FLYSPEED = 0.1f;
	private static final String COMMAND                 = "flyspeed";

	private final NGeneral plugin;

	public FlySpeedCommand(final NGeneral instance) {
		this.plugin = instance;
		plugin.getCommand(COMMAND).setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		event.getPlayer().setFlySpeed(DEFAULT_BUKKIT_FLYSPEED);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equals(COMMAND)) {
			if (!Perms.hasFlySpeed(sender)) {
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
						player.setFlySpeed(value);
						plugin.sendMessage(player, MessageId.general_flySpeed_set, Float.toString(value));
					} catch (NumberFormatException e) {
						player.setFlySpeed(DEFAULT_BUKKIT_FLYSPEED);
						plugin.sendMessage(player, MessageId.general_flySpeed_reset);
					}
				}
			} else if (args.length == 2) {
				if (!Perms.hasFlySpeedOthers(sender)) {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				} else {
					final String arg0 = args[0].toLowerCase();
					float value = DEFAULT_BUKKIT_FLYSPEED;
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
				p.setFlySpeed(value);
				plugin.sendMessage(sender, MessageId.general_flySpeed_setFor, Float.toString(value), p.getName());
				plugin.sendMessage(p, MessageId.general_flySpeed_setBy, Float.toString(value), sender.getName());
			}
		}
	}
}
