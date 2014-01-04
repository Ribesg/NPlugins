/***************************************************************************
 * Project file:    NPlugins - NGeneral - GodModeCommandExecutor.java      *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.godmode.GodModeCommandExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.godmode;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GodModeCommandExecutor implements CommandExecutor {

	private static Set<String> enabled;

	private static Set<String> getEnabled() {
		if (enabled == null) {
			enabled = new HashSet<>(2);
			enabled.add("enable");
			enabled.add("on");
		}
		return enabled;
	}

	private static Set<String> disabled;

	private static Set<String> getDisabled() {
		if (disabled == null) {
			disabled = new HashSet<>(2);
			disabled.add("disable");
			disabled.add("off");
		}
		return disabled;
	}

	private final GodModeFeature feature;

	public GodModeCommandExecutor(final GodModeFeature feature) {
		this.feature = feature;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equals("god")) {
			if (!Perms.hasGod(sender)) {
				feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			} else if (args.length == 0) {
				if (!(sender instanceof Player)) {
					feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
				} else if (feature.hasGodMode(sender.getName())) {
					feature.setGodMode((Player) sender, false);
					feature.getPlugin().sendMessage(sender, MessageId.general_god_disabled);
				} else {
					feature.setGodMode((Player) sender, true);
					feature.getPlugin().sendMessage(sender, MessageId.general_god_enabled);
				}
				return true;
			} else if (args.length == 1) {
				final String arg0 = args[0].toLowerCase();
				if (getEnabled().contains(arg0)) {
					if (!(sender instanceof Player)) {
						feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					} else {
						feature.setGodMode((Player) sender, true);
						feature.getPlugin().sendMessage(sender, MessageId.general_god_enabled);
					}
				} else if (getDisabled().contains(arg0)) {
					if (!(sender instanceof Player)) {
						feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					} else {
						feature.setGodMode((Player) sender, false);
						feature.getPlugin().sendMessage(sender, MessageId.general_god_disabled);
					}
				} else if (Perms.hasGodOthers(sender)) {
					final String[] names = arg0.split(",");
					setAll(sender, names, null);
				} else {
					feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
				}
				return true;
			} else if (args.length == 2) {
				if (!Perms.hasGodOthers(sender)) {
					feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
				} else {
					final String arg0 = args[0].toLowerCase();
					final Boolean value;
					final String arg1 = args[1].toLowerCase();
					if (getEnabled().contains(arg0)) {
						value = true;
					} else if (getDisabled().contains(arg0)) {
						value = false;
					} else {
						return false;
					}
					final String[] names = arg1.split(",");
					setAll(sender, names, value);
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Change GodMode state of all players provided.
	 *
	 * @param playerNames Names of players
	 * @param value       Null for toggle, actual value for a set
	 */
	private void setAll(final CommandSender sender, final String[] playerNames, final Boolean value) {
		for (final String name : playerNames) {
			final Player p = Bukkit.getPlayer(name);
			if (p == null) {
				feature.getPlugin().sendMessage(sender, MessageId.noPlayerFoundForGivenName, name);
			} else {
				final boolean actualValue = value == null ? !feature.hasGodMode(p.getName()) : value;
				feature.setGodMode(p, actualValue);
				if (actualValue) {
					feature.getPlugin().sendMessage(sender, MessageId.general_god_enabledFor, p.getName());
					feature.getPlugin().sendMessage(p, MessageId.general_god_enabledBy, sender.getName());
				} else {
					feature.getPlugin().sendMessage(sender, MessageId.general_god_disabledFor, p.getName());
					feature.getPlugin().sendMessage(p, MessageId.general_god_disabledBy, sender.getName());
				}
			}
		}
	}
}
