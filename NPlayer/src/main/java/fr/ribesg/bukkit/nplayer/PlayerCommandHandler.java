/***************************************************************************
 * Project file:    NPlugins - NPlayer - PlayerCommandHandler.java         *
 * Full Class name: fr.ribesg.bukkit.nplayer.PlayerCommandHandler          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.nplayer.security.Security;
import fr.ribesg.bukkit.nplayer.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayerCommandHandler implements CommandExecutor, Listener {

	private final NPlayer plugin;

	private final Map<String, Integer> loginAttempts;

	public PlayerCommandHandler(final NPlayer plugin) {
		this.plugin = plugin;
		this.loginAttempts = new HashMap<>();
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerCommandPreProcess(final PlayerCommandPreprocessEvent event) {
		final String firstWord = event.getMessage().contains(" ") ? event.getMessage().split(" ")[0].toLowerCase() : event.getMessage().toLowerCase();
		switch (firstWord) {
			case "/login":
				event.setCancelled(true);
				if (Perms.hasLogin(event.getPlayer())) {
					loginCommand(event.getPlayer(), event.getMessage().substring(6).trim().split(" "));
				} else {
					plugin.sendMessage(event.getPlayer(), MessageId.noPermissionForCommand);
				}
				break;
			case "login":
			case "/ogin":
			case "/lgin":
			case "/loin":
			case "/logn":
			case "/logi":
			case ":login":
			case ":ogin":
			case ":lgin":
			case ":loin":
			case ":logn":
			case ":logi":
				// Typo on /login command, do not output the password in console or ingame
				event.setCancelled(true);
				break;
			case "/register":
				event.setCancelled(true);
				if (Perms.hasRegister(event.getPlayer())) {
					registerCommand(event.getPlayer(), event.getMessage().substring(9).trim().split(" "));
				} else {
					plugin.sendMessage(event.getPlayer(), MessageId.noPermissionForCommand);
				}
				break;
			case "register":
			case "/egister":
			case "/rgister":
			case "/reister":
			case "/regster":
			case "/regiter":
			case "/regiser":
			case "/registr":
			case "/registe":
			case ":register":
			case ":egister":
			case ":rgister":
			case ":reister":
			case ":regster":
			case ":regiter":
			case ":regiser":
			case ":registr":
			case ":registe":
				// Typo on /register command, do not output the password in console or ingame
				event.setCancelled(true);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		switch (command.getName()) {
			case "login":
				if (sender instanceof Player) {
					if (Perms.hasLogin(sender)) {
						return loginCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						return true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					return true;
				}
			case "register":
				if (sender instanceof Player) {
					if (Perms.hasRegister(sender)) {
						return registerCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						return true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					return true;
				}
			case "logout":
				if (sender instanceof Player) {
					if (Perms.hasLogout(sender)) {
						return logoutCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						return true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					return true;
				}
			case "info":
				if (Perms.hasInfo(sender)) {
					return infoCommand(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "home":
				if (sender instanceof Player) {
					if (Perms.hasHome(sender)) {
						return homeCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						return true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					return true;
				}
			case "sethome":
				if (sender instanceof Player) {
					if (Perms.hasSetHome(sender)) {
						return setHomeCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						return true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					return true;
				}
			case "forcelogin":
				if (Perms.hasForceLogin(sender)) {
					return forceLoginCommand(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
		}
		return false;
	}

	private boolean loginCommand(final Player player, final String[] args) {
		final User user = plugin.getUserDb().get(player.getName());
		if (user == null) {
			plugin.sendMessage(player, MessageId.player_registerFirst);
			return true;
		} else {
			final String password = StringUtils.joinStrings(args);
			final boolean isCorrect = Security.isUserPassword(password, user);
			if (isCorrect) {
				plugin.sendMessage(player, MessageId.player_welcomeBack);
				user.setLoggedIn(true);
				user.newIp(player.getAddress().getAddress().getHostAddress());
				return true;
			} else {
				plugin.sendMessage(player, MessageId.player_wrongPassword);
				return true;
			}
		}
	}

	private boolean registerCommand(final Player player, final String[] args) {
		User user = plugin.getUserDb().get(player.getName());
		final String password = StringUtils.joinStrings(args);
		if (user == null) {
			user = plugin.getUserDb().newUser(player.getName(), Security.hash(password), player.getAddress().getAddress().getHostAddress());
			user.setLoggedIn(true);
			plugin.sendMessage(player, MessageId.player_welcomeToTheServer);
			return true;
		} else if (user.isLoggedIn()) {
			user.setPasswordHash(Security.hash(password));
			plugin.sendMessage(player, MessageId.player_passwordChanged);
			return true;
		} else {
			plugin.sendMessage(player, MessageId.player_alreadyRegistered);
			return true;
		}
	}

	private boolean logoutCommand(final Player player, final String[] args) {
		final User user = plugin.getUserDb().get(player.getName());
		if (user == null) {
			plugin.sendMessage(player, MessageId.player_registerFirst);
			return true;
		} else if (!user.isLoggedIn()) {
			plugin.sendMessage(player, MessageId.player_loginFirst);
			return true;
		}
		boolean autoLogout = false;
		boolean toggle = false;
		boolean enable = false;
		boolean disable = false;
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].toLowerCase();
			}
			if (args.length == 1) {
				if (args[0].equals("autologout") || args[0].equals("auto")) {
					autoLogout = true;
					toggle = true;
				}
			} else if (args.length == 2) {
				if (args[0].equals("autologout") || args[0].equals("auto")) {
					autoLogout = true;
					if (args[1].equals("enable")) {
						enable = true;
					} else if (args[1].equals("disable")) {
						disable = true;
					}
				}
			}
		}
		if (autoLogout) {
			if (toggle) {
				user.setAutoLogout(!user.hasAutoLogout());
			} else if (enable) {
				user.setAutoLogout(true);
			} else if (disable) {
				user.setAutoLogout(false);
			}
			if (user.hasAutoLogout()) {
				plugin.sendMessage(player, MessageId.player_autoLogoutEnabled);
			} else {
				plugin.sendMessage(player, MessageId.player_autoLogoutDisabled);
			}
			return true;
		} else {
			user.setLoggedIn(false);
			plugin.sendMessage(player, MessageId.player_loggedOut);
			return true;
		}
	}

	private boolean infoCommand(final CommandSender sender, final String[] args) {
		final boolean isAdmin = Perms.hasInfoAdmin(sender);
		sender.sendMessage("/info command STILL TODO");
		return false; // TODO
	}

	private boolean homeCommand(final Player player, final String[] args) {
		if (args.length > 0) {
			if (!Perms.hasHomeOthers(player)) {
				plugin.sendMessage(player, MessageId.noPermissionForCommand);
				return true;
			}
			String userName = args[0];
			final Player p = plugin.getServer().getPlayer(userName);
			if (p != null) {
				userName = p.getName();
			}
			final User user = plugin.getUserDb().get(userName);
			if (user == null) {
				plugin.sendMessage(player, MessageId.player_unknownUser, userName);
				return true;
			}
			final Location dest = user.getHome();
			if (dest == null) {
				plugin.sendMessage(player, MessageId.player_userHasNoHome, userName);
				return true;
			} else {
				plugin.sendMessage(player, MessageId.player_teleportingToUserHome, userName);
				dest.getChunk().load(true);
				plugin.getServer().getScheduler().runTask(plugin, new BukkitRunnable() {

					@Override
					public void run() {
						player.teleport(dest);
					}
				});
				return true;
			}
		} else {
			final User user = plugin.getUserDb().get(player.getName());
			if (user == null) {
				plugin.getLogger().severe("Unknown error while executing command /home : user does not exists but still managed to use " + "the command.");
				player.sendMessage("§cUnknown error, see console.");
				return true;
			}
			final Location dest = user.getHome();
			if (dest == null) {
				plugin.sendMessage(player, MessageId.player_youHaveNoHome);
				return true;
			} else {
				plugin.sendMessage(player, MessageId.player_teleportingToYourHome);
				dest.getChunk().load(true);
				plugin.getServer().getScheduler().runTask(plugin, new BukkitRunnable() {

					@Override
					public void run() {
						player.teleport(dest);
					}
				});
				return true;
			}
		}
	}

	private boolean setHomeCommand(final Player player, final String[] args) {
		if (args.length > 0) {
			if (!Perms.hasSetHomeOthers(player)) {
				plugin.sendMessage(player, MessageId.noPermissionForCommand);
				return true;
			}
			String userName = args[0];
			final Player p = plugin.getServer().getPlayer(userName);
			if (p != null) {
				userName = p.getName();
			}
			final User user = plugin.getUserDb().get(userName);
			if (user == null) {
				plugin.sendMessage(player, MessageId.player_unknownUser, userName);
				return true;
			}
			user.setHome(player.getLocation());
			plugin.sendMessage(player, MessageId.player_userHomeSet, userName);
			return true;
		} else {
			final User user = plugin.getUserDb().get(player.getName());
			if (user == null) {
				plugin.getLogger().severe("Unknown error while executing command /home : user does not exists but still managed to use " + "the command.");
				player.sendMessage("§cUnknown error, see console.");
				return true;
			}
			user.setHome(player.getLocation());
			plugin.sendMessage(player, MessageId.player_yourHomeSet);
			return true;
		}
	}

	private boolean forceLoginCommand(final CommandSender sender, final String[] args) {
		if (args.length != 1) {
			return false;
		}
		final Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, args[0]);
			return true;
		} else {
			final User user = plugin.getUserDb().get(player.getName());
			if (user == null) {
				plugin.sendMessage(sender, MessageId.player_unknownUser, player.getName());
				return true;
			} else {
				user.setLoggedIn(true);
				plugin.getLoggedOutUserHandler().notifyLogin(user);
				plugin.sendMessage(sender, MessageId.player_youForcedLogin, player.getName());
				plugin.sendMessage(player, MessageId.player_somebodyForcedLoginYou, sender.getName());
				return true;
			}
		}
	}

	private void loginAttempt(final String userName) {
		int nb = 0;
		if (loginAttempts.containsKey(userName)) {
			nb = loginAttempts.get(userName);
		}
		nb++;
		if (nb > plugin.getPluginConfig().getMaximumLoginAttempts()) {
			final Player target = Bukkit.getPlayerExact(userName);
			switch (plugin.getPluginConfig().getTooManyAttemptsPunishment()) {
				case 0:
					target.kickPlayer(plugin.getMessages().get(MessageId.player_loginAttemptsKickMessage)[0]);
					plugin.getPunishmentDb().getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedKickMessage, userName)[0]);
					break;
				case 1:
					target.kickPlayer(plugin.getMessages().get(MessageId.player_loginAttemptsTempBanMessage)[0]);
					plugin.getPunishmentDb().getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedTempBanMessage, userName)[0]);
					break;
				case 2:
					target.kickPlayer(plugin.getMessages().get(MessageId.player_loginAttemptsPermBanMessage)[0]);
					plugin.getPunishmentDb().getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedPermBanMessage, userName)[0]);
					break;
				default:
					break;
			}
			loginAttempts.put(userName, plugin.getPluginConfig().getMaximumLoginAttempts() - 1);
		}
		loginAttempts.put(userName, nb);
	}
}
