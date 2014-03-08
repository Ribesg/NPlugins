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
import fr.ribesg.bukkit.ncore.utils.TimeUtils;
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

import java.io.IOException;
import java.util.Arrays;
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
		plugin.entering(getClass(), "onPlayerCommandPreProcess");

		final String firstWord = event.getMessage().contains(" ") ? event.getMessage().split(" ")[0].toLowerCase() : event.getMessage().toLowerCase();
		switch (firstWord) {
			case "/login":
				plugin.debug("/login command");
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
				plugin.debug("Typo on /login");
				event.setCancelled(true);
				break;
			case "/register":
				plugin.debug("/register command");
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
				plugin.debug("Typo on /register");
				event.setCancelled(true);
				break;
			default:
				break;
		}

		plugin.exiting(getClass(), "onPlayerCommandPreProcess");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		plugin.entering(getClass(), "onCommand");
		boolean result = false;

		plugin.debug("Executing command " + command.getName() + " with arguments " + Arrays.toString(args));
		switch (command.getName()) {
			case "nplayer":
				if (args.length < 1) {
					result = false;
					break;
				}
				switch (args[0].toLowerCase()) {
					case "reload":
					case "rld":
						if (Perms.hasReload(sender)) {
							result = reloadCommand(sender, args);
						} else {
							plugin.sendMessage(sender, MessageId.noPermissionForCommand);
							result = true;
						}
						break;
					default:
						result = false;
				}
				break;
			case "login":
				if (sender instanceof Player) {
					if (Perms.hasLogin(sender)) {
						result = loginCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						result = true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					result = true;
				}
				break;
			case "register":
				if (sender instanceof Player) {
					if (Perms.hasRegister(sender)) {
						result = registerCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						result = true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					result = true;
				}
				break;
			case "logout":
				if (sender instanceof Player) {
					if (Perms.hasLogout(sender)) {
						result = logoutCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						result = true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					result = true;
				}
				break;
			case "info":
				if (Perms.hasInfo(sender)) {
					result = infoCommand(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "home":
				if (sender instanceof Player) {
					if (Perms.hasHome(sender)) {
						result = homeCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						result = true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					result = true;
				}
				break;
			case "sethome":
				if (sender instanceof Player) {
					if (Perms.hasSetHome(sender)) {
						result = setHomeCommand((Player) sender, args);
					} else {
						plugin.sendMessage(sender, MessageId.noPermissionForCommand);
						result = true;
					}
				} else {
					plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
					result = true;
				}
				break;
			case "forcelogin":
				if (Perms.hasForceLogin(sender)) {
					result = forceLoginCommand(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
		}

		plugin.debug("Command execution result: " + result);

		plugin.exiting(getClass(), "onCommand");
		return result;
	}

	private boolean reloadCommand(final CommandSender sender, final String[] args) {
		if (args.length != 2) {
			return false;
		}
		switch (args[1].toLowerCase()) {
			case "messages":
			case "mess":
			case "mes":
				try {
					plugin.loadMessages();
					plugin.sendMessage(sender, MessageId.cmdReloadMessages);
				} catch (final IOException e) {
					plugin.error("An error occured when NPlayer tried to load messages.yml", e);
					plugin.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
				}
				return true;
			default:
				return false;
		}
	}

	private boolean loginCommand(final Player player, final String[] args) {
		plugin.entering(getClass(), "loginCommand");

		final User user = plugin.getUserDb().get(player.getName());
		if (user == null) {
			plugin.debug("Unregistered user");
			plugin.sendMessage(player, MessageId.player_registerFirst);
		} else {
			plugin.debug("Registered user");
			final String password = StringUtils.joinStrings(args);
			final boolean isCorrect = Security.isUserPassword(password, user);
			if (isCorrect) {
				plugin.debug("Correct password provided");
				plugin.sendMessage(player, MessageId.player_welcomeBack);
				user.setLoggedIn(true);
				user.newIp(player.getAddress().getAddress().getHostAddress());
			} else {
				plugin.debug("Incorrect password provided");
				plugin.sendMessage(player, MessageId.player_wrongPassword);
				loginAttempt(player.getName());
			}
		}

		plugin.exiting(getClass(), "loginCommand");
		return true;
	}

	private boolean registerCommand(final Player player, final String[] args) {
		plugin.entering(getClass(), "registerCommand");

		User user = plugin.getUserDb().get(player.getName());
		final String password = StringUtils.joinStrings(args);
		if (user == null) {
			plugin.debug("Unregistered user");
			user = plugin.getUserDb().newUser(player.getName(), Security.hash(password), player.getAddress().getAddress().getHostAddress());
			user.setLoggedIn(true);
			plugin.sendMessage(player, MessageId.player_welcomeToTheServer);
		} else if (user.isLoggedIn()) {
			plugin.debug("Registered and logged-in user, change password");
			user.setPasswordHash(Security.hash(password));
			plugin.sendMessage(player, MessageId.player_passwordChanged);
		} else {
			plugin.debug("Registered non-logged-in user");
			plugin.sendMessage(player, MessageId.player_alreadyRegistered);
		}

		plugin.exiting(getClass(), "registerCommand");
		return true;
	}

	private boolean logoutCommand(final Player player, final String[] args) {
		plugin.entering(getClass(), "logoutCommand");

		final User user = plugin.getUserDb().get(player.getName());
		if (user == null) {
			plugin.debug("Unregistered user");
			plugin.sendMessage(player, MessageId.player_registerFirst);
		} else if (!user.isLoggedIn()) {
			plugin.debug("Registered non-logged-in user");
			plugin.sendMessage(player, MessageId.player_loginFirst);
		} else {
			plugin.debug("Registered logged-in user");
			boolean autoLogout = false;
			boolean toggle = false;
			boolean enable = false;
			boolean disable = false;
			if (args != null && args.length > 0) {
				plugin.debug("Additional arguments");
				for (int i = 0; i < args.length; i++) {
					args[i] = args[i].toLowerCase();
				}
				if (args.length == 1) {
					if (args[0].equals("autologout") || args[0].equals("auto")) {
						plugin.debug("Toggle auto-logout");
						autoLogout = true;
						toggle = true;
					}
				} else if (args.length == 2) {
					if (args[0].equals("autologout") || args[0].equals("auto")) {
						autoLogout = true;
						if (args[1].equals("enable")) {
							plugin.debug("Enable auto-logout");
							enable = true;
						} else if (args[1].equals("disable")) {
							plugin.debug("Disable auto-logout");
							disable = true;
						}
					}
				}
			}
			if (autoLogout) {
				plugin.debug("Modifying auto-logout state");
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
			} else {
				plugin.debug("Logging out");
				user.setLoggedIn(false);
				plugin.sendMessage(player, MessageId.player_loggedOut);
			}
		}

		plugin.exiting(getClass(), "logoutCommand");
		return true;
	}

	private boolean infoCommand(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "infoCommand");

		final boolean isAdmin = Perms.hasInfoAdmin(sender);
		sender.sendMessage("/info command STILL TODO");

		plugin.exiting(getClass(), "infoCommand");
		return false; // TODO
	}

	private boolean homeCommand(final Player player, final String[] args) {
		plugin.entering(getClass(), "homeCommand");

		if (args.length > 0) {
			if (!Perms.hasHomeOthers(player)) {
				plugin.sendMessage(player, MessageId.noPermissionForCommand);
			} else {
				final String userName = args[0];
				final User user = plugin.getUserDb().get(userName);
				if (user == null) {
					plugin.sendMessage(player, MessageId.player_unknownUser, userName);
				} else {
					final Location dest = user.getHome();
					if (dest == null) {
						plugin.sendMessage(player, MessageId.player_userHasNoHome, user.getUserName());
					} else {
						plugin.sendMessage(player, MessageId.player_teleportingToUserHome, user.getUserName());
						dest.getChunk().load(true);
						plugin.getServer().getScheduler().runTask(plugin, new BukkitRunnable() {

							@Override
							public void run() {
								player.teleport(dest);
							}
						});
					}
				}
			}
		} else {
			final User user = plugin.getUserDb().get(player.getName());
			if (user == null) {
				plugin.getLogger().severe("Unknown error while executing command /home : user does not exists but still managed to use the command.");
				player.sendMessage("§cUnknown error, see console.");
			} else {
				final Location dest = user.getHome();
				if (dest == null) {
					plugin.sendMessage(player, MessageId.player_youHaveNoHome);
				} else {
					plugin.sendMessage(player, MessageId.player_teleportingToYourHome);
					dest.getChunk().load(true);
					plugin.getServer().getScheduler().runTask(plugin, new BukkitRunnable() {

						@Override
						public void run() {
							player.teleport(dest);
						}
					});
				}
			}
		}

		plugin.exiting(getClass(), "homeCommand");
		return true;
	}

	private boolean setHomeCommand(final Player player, final String[] args) {
		plugin.entering(getClass(), "setHomeCommand");

		if (args.length > 0) {
			if (!Perms.hasSetHomeOthers(player)) {
				plugin.sendMessage(player, MessageId.noPermissionForCommand);
			} else {
				final String userName = args[0];
				final User user = plugin.getUserDb().get(userName);
				if (user == null) {
					plugin.sendMessage(player, MessageId.player_unknownUser, userName);
				} else {
					user.setHome(player.getLocation());
					plugin.sendMessage(player, MessageId.player_userHomeSet, user.getUserName());
				}
			}
		} else {
			final User user = plugin.getUserDb().get(player.getName());
			if (user == null) {
				plugin.getLogger().severe("Unknown error while executing command /home : user does not exists but still managed to use the command.");
				player.sendMessage("§cUnknown error, see console.");
			} else {
				user.setHome(player.getLocation());
				plugin.sendMessage(player, MessageId.player_yourHomeSet);
			}
		}

		plugin.exiting(getClass(), "setHomeCommand");
		return true;
	}

	private boolean forceLoginCommand(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "forceLoginCommand");
		boolean result = false;

		if (args.length == 1) {
			final Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				plugin.sendMessage(sender, MessageId.player_unknownUser, args[0]);
			} else {
				final User user = plugin.getUserDb().get(player.getName());
				if (user == null) {
					plugin.sendMessage(sender, MessageId.player_unknownUser, player.getName());
				} else {
					user.setLoggedIn(true);
					plugin.getLoggedOutUserHandler().notifyLogin(user);
					plugin.sendMessage(sender, MessageId.player_youForcedLogin, player.getName());
					plugin.sendMessage(player, MessageId.player_somebodyForcedLoginYou, sender.getName());
				}
			}

			result = true;
		}

		plugin.exiting(getClass(), "forceLoginCommand");
		return result;
	}

	private void loginAttempt(final String userName) {
		plugin.entering(getClass(), "loginAttempt");

		int nb = 0;
		if (loginAttempts.containsKey(userName)) {
			nb = loginAttempts.get(userName);
		}
		nb++;
		if (nb > plugin.getPluginConfig().getMaximumLoginAttempts()) {
			plugin.debug("Reached maximum allowed login attempts");
			final Player target = Bukkit.getPlayerExact(userName);
			switch (plugin.getPluginConfig().getTooManyAttemptsPunishment()) {
				case 0:
					plugin.getPunishmentDb().getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedKickMessage, userName)[0]);
					target.kickPlayer(plugin.getMessages().get(MessageId.player_loginAttemptsKickMessage)[0]);
					break;
				case 1:
					final int duration = plugin.getPluginConfig().getTooManyAttemptsPunishmentDuration();
					final String durationString = TimeUtils.toString(duration);
					plugin.getPunishmentDb().getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedTempBanMessage, userName, durationString)[0]);
					target.kickPlayer(plugin.getMessages().get(MessageId.player_loginAttemptsTempBanMessage, durationString)[0]);
					plugin.getPunishmentDb().tempBanIp(target.getAddress().getAddress().getHostAddress(), plugin.getPluginConfig().getTooManyAttemptsPunishmentDuration(), plugin.getMessages().get(MessageId.player_loginAttemptsTooMany)[0]);
					break;
				case 2:
					plugin.getPunishmentDb().getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedPermBanMessage, userName)[0]);
					target.kickPlayer(plugin.getMessages().get(MessageId.player_loginAttemptsPermBanMessage)[0]);
					plugin.getPunishmentDb().permBanIp(target.getAddress().getAddress().getHostAddress(), plugin.getMessages().get(MessageId.player_loginAttemptsTooMany)[0]);
					break;
				default:
					break;
			}
			loginAttempts.put(userName, plugin.getPluginConfig().getMaximumLoginAttempts() - 1);
		}
		loginAttempts.put(userName, nb);

		plugin.exiting(getClass(), "loginAttempt");
	}
}
