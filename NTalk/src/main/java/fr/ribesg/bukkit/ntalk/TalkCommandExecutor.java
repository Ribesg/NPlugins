/***************************************************************************
 * Project file:    NPlugins - NTalk - TalkCommandExecutor.java            *
 * Full Class name: fr.ribesg.bukkit.ntalk.TalkCommandExecutor             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.UsernameUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ribes
 */
public class TalkCommandExecutor implements CommandExecutor {

	private static final String CONSOLE_NAME = Bukkit.getConsoleSender().getName();

	private final NTalk                   plugin;
	private final HashMap<String, String> lastReceivedPmMap;

	public TalkCommandExecutor(final NTalk instance) {
		plugin = instance;
		lastReceivedPmMap = new HashMap<>();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		if (command.getName().equalsIgnoreCase("pm")) {
			if (Perms.hasPrivateMessage(sender)) {
				return cmdPrivateMessage(sender, args);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else if (command.getName().equalsIgnoreCase("pr")) {
			if (Perms.hasPrivateResponse(sender)) {
				return cmdPrivateResponse(sender, args);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else if (command.getName().equalsIgnoreCase("nick")) {
			if (Perms.hasNick(sender)) {
				return cmdNick(sender, args);
			} else {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cmdPrivateMessage(final CommandSender sender, final String[] args) {
		if (args.length < 2) {
			return false;
		} else {
			final String[] targetsName = args[0].split(",");
			final HashSet<CommandSender> targets = new HashSet<>();
			final HashSet<CommandSender> spies = new HashSet<>();
			for (final String target : targetsName) {
				final Player p = plugin.getServer().getPlayer(target);
				if (p != null) {
					targets.add(p);
				} else if (target.equalsIgnoreCase("CONSOLE") || target.equalsIgnoreCase("SERVER")) {
					targets.add(plugin.getServer().getConsoleSender());
				} else {
					plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, target);
				}
			}
			for (final Player p : plugin.getServer().getOnlinePlayers()) {
				if (Perms.hasSpy(p)) {
					spies.add(p);
				}
			}
			spies.add(plugin.getServer().getConsoleSender());
			if (targets.size() == 0) {
				return true;
			}

			final StringBuilder messageBuilder = new StringBuilder(args[1]);
			for (int i = 2; i < args.length; i++) {
				messageBuilder.append(' ').append(args[i]);
			}

			sendMessages(sender, targets, spies, messageBuilder.toString());
			return true;
		}
	}

	private boolean cmdPrivateResponse(final CommandSender sender, final String[] args) {
		if (args.length < 1) {
			return false;
		} else if (lastReceivedPmMap.containsKey(sender.getName())) {
			final String targetName = lastReceivedPmMap.get(sender.getName());
			final CommandSender target;
			if (CONSOLE_NAME.equals(targetName)) {
				target = plugin.getServer().getConsoleSender();
			} else {
				target = plugin.getServer().getPlayerExact(targetName);
			}
			if (target != null) {
				final StringBuilder messageBuilder = new StringBuilder(args[0]);
				for (int i = 1; i < args.length; i++) {
					messageBuilder.append(' ').append(args[i]);
				}
				final String formattedMessage = sendMessage(sender, target, messageBuilder.toString());
				for (final Player p : plugin.getServer().getOnlinePlayers()) {
					if (Perms.hasSpy(p) && p != target && p != sender) {
						p.sendMessage(formattedMessage);
					}
				}
				if (target != plugin.getServer().getConsoleSender()) {
					plugin.getServer().getConsoleSender().sendMessage(formattedMessage);
				}
				return true;
			} else {
				plugin.sendMessage(sender, MessageId.talk_nobodyToRespond);
				return true;
			}
		} else {
			plugin.sendMessage(sender, MessageId.talk_nobodyToRespond);
			return true;
		}
	}

	private boolean cmdNick(final CommandSender sender, final String[] args) {
		if (args.length < 1 || args.length > 2) {
			return false;
		} else if (args.length == 1) {
			final String realName = args[0];
			if (!UsernameUtils.isValidMinecraftUserName(realName)) {
				plugin.sendMessage(sender, MessageId.talk_invalidUsername, realName);
				return true;
			}
			plugin.getPluginConfig().getPlayerNicknames().remove(realName);
			plugin.sendMessage(sender, MessageId.talk_youDeNickNamed, realName);
			if (plugin.getServer().getPlayerExact(realName) != null) {
				plugin.sendMessage(plugin.getServer().getPlayerExact(realName), MessageId.talk_youWereDeNickNamed, sender.getName());
			}
			return true;
		} else {
			final String realName = args[0];
			final String nick = args[1];
			if (!UsernameUtils.isValidMinecraftUserName(realName)) {
				plugin.sendMessage(sender, MessageId.talk_invalidUsername, realName);
				return true;
			} else if (!UsernameUtils.isValidNickName(nick)) {
				plugin.sendMessage(sender, MessageId.talk_invalidNickname, nick);
				return true;
			}
			plugin.getPluginConfig().getPlayerNicknames().put(realName, nick);
			plugin.sendMessage(sender, MessageId.talk_youNickNamed, realName, nick);
			if (plugin.getServer().getPlayerExact(realName) != null) {
				plugin.sendMessage(plugin.getServer().getPlayerExact(realName), MessageId.talk_youWereNickNamed, nick, sender.getName());
			}
			return true;
		}
	}

	private void sendMessages(final CommandSender from, final Set<CommandSender> toSet, final Set<CommandSender> spySet, final String message) {
		for (final CommandSender to : toSet) {
			final String formattedMessage = sendMessage(from, to, message);
			for (final CommandSender spy : spySet) {
				if (spy != from && spy != to) {
					spy.sendMessage(formattedMessage);
				}
			}
		}
	}

	private String sendMessage(final CommandSender from, final CommandSender to, final String message) {
		final String formattedMessage = plugin.getFormater().parsePM(from, to, message);
		from.sendMessage(formattedMessage);
		to.sendMessage(formattedMessage);
		lastReceivedPmMap.put(to.getName(), from.getName());
		return formattedMessage;
	}
}
