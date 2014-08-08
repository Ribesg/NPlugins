/***************************************************************************
 * Project file:    NPlugins - NPlayer - PlayerCommandHandler.java         *
 * Full Class name: fr.ribesg.bukkit.nplayer.PlayerCommandHandler          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ncore.util.TimeUtil;
import fr.ribesg.bukkit.nplayer.security.Security;
import fr.ribesg.bukkit.nplayer.user.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerCommandHandler implements CommandExecutor, Listener {

    private final NPlayer plugin;

    private final Map<String, Integer> loginAttempts;

    public PlayerCommandHandler(final NPlayer plugin) {
        this.plugin = plugin;
        this.loginAttempts = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        this.plugin.entering(this.getClass(), "onPlayerChat");

        final Player player = event.getPlayer();
        final String[] split = event.getMessage().split(" ");

        // Check if Player's password after first arg
        if (split.length > 1) {
            final User user = this.plugin.getUserDb().get(player.getUniqueId());
            if (user != null) {
                final String password = StringUtil.joinStrings(split, 1);
                final boolean isCorrect = Security.isUserPassword(password, user);
                if (isCorrect) {
                    this.plugin.debug("Player typed his password, don't output it");
                    event.setMessage(split[0] + " ****************");
                }
            }
        }

        this.plugin.exiting(this.getClass(), "onPlayerChat");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        this.plugin.entering(this.getClass(), "onCommand");
        boolean result = false;

        this.plugin.debug("Executing command " + command.getName() + " with arguments " + Arrays.toString(args));
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
                            result = this.reloadCommand(sender, args);
                        } else {
                            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
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
                        result = this.loginCommand((Player)sender, args);
                    } else {
                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        result = true;
                    }
                } else {
                    this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                    result = true;
                }
                break;
            case "register":
                if (sender instanceof Player) {
                    if (Perms.hasRegister(sender)) {
                        result = this.registerCommand((Player)sender, args);
                    } else {
                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        result = true;
                    }
                } else {
                    this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                    result = true;
                }
                break;
            case "logout":
                if (sender instanceof Player) {
                    if (Perms.hasLogout(sender)) {
                        result = this.logoutCommand((Player)sender, args);
                    } else {
                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        result = true;
                    }
                } else {
                    this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                    result = true;
                }
                break;
            case "info":
                if (Perms.hasInfo(sender)) {
                    result = this.infoCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "home":
                if (sender instanceof Player) {
                    if (Perms.hasHome(sender)) {
                        result = this.homeCommand((Player)sender, args);
                    } else {
                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        result = true;
                    }
                } else {
                    this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                    result = true;
                }
                break;
            case "sethome":
                if (sender instanceof Player) {
                    if (Perms.hasSetHome(sender)) {
                        result = this.setHomeCommand((Player)sender, args);
                    } else {
                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        result = true;
                    }
                } else {
                    this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                    result = true;
                }
                break;
            case "forcelogin":
                if (Perms.hasForceLogin(sender)) {
                    result = this.forceLoginCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
        }

        this.plugin.debug("Command execution result: " + result);

        this.plugin.exiting(this.getClass(), "onCommand");
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
                    this.plugin.loadMessages();
                    this.plugin.sendMessage(sender, MessageId.cmdReloadMessages);
                } catch (final IOException e) {
                    this.plugin.error("An error occured when NPlayer tried to load messages.yml", e);
                    this.plugin.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
                }
                return true;
            default:
                return false;
        }
    }

    private boolean loginCommand(final Player player, final String[] args) {
        this.plugin.entering(this.getClass(), "loginCommand");

        final User user = this.plugin.getUserDb().get(player.getUniqueId());
        if (user == null) {
            this.plugin.debug("Unregistered user");
            this.plugin.sendMessage(player, MessageId.player_registerFirst);
        } else {
            this.plugin.debug("Registered user");
            final String password = StringUtil.joinStrings(args);
            final boolean isCorrect = Security.isUserPassword(password, user);
            if (isCorrect) {
                this.plugin.debug("Correct password provided");
                this.plugin.sendMessage(player, MessageId.player_welcomeBack);
                user.setLoggedIn(true);
                user.newIp(player.getAddress().getAddress().getHostAddress());
                this.plugin.getUserDb().updateIp(user, player.getAddress().getAddress().getHostAddress());
            } else {
                this.plugin.debug("Incorrect password provided");
                this.plugin.sendMessage(player, MessageId.player_wrongPassword);
                this.loginAttempt(player.getName());
            }
        }

        this.plugin.exiting(this.getClass(), "loginCommand");
        return true;
    }

    private boolean registerCommand(final Player player, final String[] args) {
        this.plugin.entering(this.getClass(), "registerCommand");

        User user = this.plugin.getUserDb().get(player.getUniqueId());
        final String password = StringUtil.joinStrings(args);
        if (user == null) {
            this.plugin.debug("Unregistered user");
            user = this.plugin.getUserDb().newUser(player.getUniqueId(), Security.hash(password), player.getAddress().getAddress().getHostAddress());
            user.setLoggedIn(true);
            this.plugin.sendMessage(player, MessageId.player_welcomeToTheServer);
        } else if (user.isLoggedIn()) {
            this.plugin.debug("Registered and logged-in user, change password");
            user.setPasswordHash(Security.hash(password));
            this.plugin.sendMessage(player, MessageId.player_passwordChanged);
        } else {
            this.plugin.debug("Registered non-logged-in user");
            this.plugin.sendMessage(player, MessageId.player_alreadyRegistered);
        }

        this.plugin.exiting(this.getClass(), "registerCommand");
        return true;
    }

    private boolean logoutCommand(final Player player, final String[] args) {
        this.plugin.entering(this.getClass(), "logoutCommand");

        final User user = this.plugin.getUserDb().get(player.getUniqueId());
        if (user == null) {
            this.plugin.debug("Unregistered user");
            this.plugin.sendMessage(player, MessageId.player_registerFirst);
        } else if (!user.isLoggedIn()) {
            this.plugin.debug("Registered non-logged-in user");
            this.plugin.sendMessage(player, MessageId.player_loginFirst);
        } else {
            this.plugin.debug("Registered logged-in user");
            boolean autoLogout = false;
            boolean toggle = false;
            boolean enable = false;
            boolean disable = false;
            if (args != null && args.length > 0) {
                this.plugin.debug("Additional arguments");
                for (int i = 0; i < args.length; i++) {
                    args[i] = args[i].toLowerCase();
                }
                if (args.length == 1) {
                    if ("autologout".equals(args[0]) || "auto".equals(args[0])) {
                        this.plugin.debug("Toggle auto-logout");
                        autoLogout = true;
                        toggle = true;
                    }
                } else if (args.length == 2) {
                    if ("autologout".equals(args[0]) || "auto".equals(args[0])) {
                        autoLogout = true;
                        if ("enable".equals(args[1])) {
                            this.plugin.debug("Enable auto-logout");
                            enable = true;
                        } else if ("disable".equals(args[1])) {
                            this.plugin.debug("Disable auto-logout");
                            disable = true;
                        }
                    }
                }
            }
            if (autoLogout) {
                this.plugin.debug("Modifying auto-logout state");
                if (toggle) {
                    user.setAutoLogout(!user.hasAutoLogout());
                } else if (enable) {
                    user.setAutoLogout(true);
                } else if (disable) {
                    user.setAutoLogout(false);
                }
                if (user.hasAutoLogout()) {
                    this.plugin.sendMessage(player, MessageId.player_autoLogoutEnabled);
                } else {
                    this.plugin.sendMessage(player, MessageId.player_autoLogoutDisabled);
                }
            } else {
                this.plugin.debug("Logging out");
                user.setLoggedIn(false);
                this.plugin.sendMessage(player, MessageId.player_loggedOut);
            }
        }

        this.plugin.exiting(this.getClass(), "logoutCommand");
        return true;
    }

    private boolean infoCommand(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "infoCommand");

        final boolean isAdmin = Perms.hasInfoAdmin(sender);
        sender.sendMessage("/info command STILL TODO");

        this.plugin.exiting(this.getClass(), "infoCommand");
        return false; // TODO
    }

    private boolean homeCommand(final Player player, final String[] args) {
        this.plugin.entering(this.getClass(), "homeCommand");

        if (args.length > 0) {
            if (!Perms.hasHomeOthers(player)) {
                this.plugin.sendMessage(player, MessageId.noPermissionForCommand);
            } else {
                final String userName = args[0];
                final User user = this.plugin.getUserDb().get(UuidDb.getId(Node.PLAYER, userName));
                if (user == null) {
                    this.plugin.sendMessage(player, MessageId.player_unknownUser, userName);
                } else {
                    final String realUserName = UuidDb.getName(user.getUserId());
                    final Location dest = user.getHome();
                    if (dest == null) {
                        this.plugin.sendMessage(player, MessageId.player_userHasNoHome, realUserName);
                    } else {
                        this.plugin.sendMessage(player, MessageId.player_teleportingToUserHome, realUserName);
                        dest.getChunk().load(true);
                        this.plugin.getServer().getScheduler().runTask(this.plugin, new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.teleport(dest);
                            }
                        });
                    }
                }
            }
        } else {
            final User user = this.plugin.getUserDb().get(player.getUniqueId());
            if (user == null) {
                this.plugin.getLogger().severe("Unknown error while executing command /home : user does not exists but still managed to use the command.");
                player.sendMessage("§cUnknown error, see console.");
            } else {
                final Location dest = user.getHome();
                if (dest == null) {
                    this.plugin.sendMessage(player, MessageId.player_youHaveNoHome);
                } else {
                    this.plugin.sendMessage(player, MessageId.player_teleportingToYourHome);
                    dest.getChunk().load(true);
                    this.plugin.getServer().getScheduler().runTask(this.plugin, new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.teleport(dest);
                        }
                    });
                }
            }
        }

        this.plugin.exiting(this.getClass(), "homeCommand");
        return true;
    }

    private boolean setHomeCommand(final Player player, final String[] args) {
        this.plugin.entering(this.getClass(), "setHomeCommand");

        if (args.length > 0) {
            if (!Perms.hasSetHomeOthers(player)) {
                this.plugin.sendMessage(player, MessageId.noPermissionForCommand);
            } else {
                final String userName = args[0];
                final User user = this.plugin.getUserDb().get(UuidDb.getId(Node.PLAYER, userName));
                if (user == null) {
                    this.plugin.sendMessage(player, MessageId.player_unknownUser, userName);
                } else {
                    user.setHome(player.getLocation());
                    this.plugin.sendMessage(player, MessageId.player_userHomeSet, UuidDb.getName(user.getUserId()));
                }
            }
        } else {
            final User user = this.plugin.getUserDb().get(player.getUniqueId());
            if (user == null) {
                this.plugin.getLogger().severe("Unknown error while executing command /home : user does not exists but still managed to use the command.");
                player.sendMessage("§cUnknown error, see console.");
            } else {
                user.setHome(player.getLocation());
                this.plugin.sendMessage(player, MessageId.player_yourHomeSet);
            }
        }

        this.plugin.exiting(this.getClass(), "setHomeCommand");
        return true;
    }

    private boolean forceLoginCommand(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "forceLoginCommand");
        boolean result = false;

        if (args.length == 1) {
            final Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                this.plugin.sendMessage(sender, MessageId.player_unknownUser, args[0]);
            } else {
                final User user = this.plugin.getUserDb().get(player.getUniqueId());
                if (user == null) {
                    this.plugin.sendMessage(sender, MessageId.player_unknownUser, player.getName());
                } else {
                    user.setLoggedIn(true);
                    this.plugin.getLoggedOutUserHandler().notifyLogin(player);
                    this.plugin.sendMessage(sender, MessageId.player_youForcedLogin, player.getName());
                    this.plugin.sendMessage(player, MessageId.player_somebodyForcedLoginYou, sender.getName());
                }
            }

            result = true;
        }

        this.plugin.exiting(this.getClass(), "forceLoginCommand");
        return result;
    }

    private void loginAttempt(final String userName) {
        this.plugin.entering(this.getClass(), "loginAttempt");

        int nb = 0;
        if (this.loginAttempts.containsKey(userName)) {
            nb = this.loginAttempts.get(userName);
        }
        nb++;
        if (nb > this.plugin.getPluginConfig().getMaximumLoginAttempts()) {
            this.plugin.debug("Reached maximum allowed login attempts");
            final Player target = Bukkit.getPlayerExact(userName);
            switch (this.plugin.getPluginConfig().getTooManyAttemptsPunishment()) {
                case 0:
                    this.plugin.getPunishmentDb().getLeaveMessages().put(target.getUniqueId(), this.plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedKickMessage, userName)[0]);
                    target.kickPlayer(this.plugin.getMessages().get(MessageId.player_loginAttemptsKickMessage)[0]);
                    break;
                case 1:
                    final int duration = this.plugin.getPluginConfig().getTooManyAttemptsPunishmentDuration();
                    final String durationString = TimeUtil.toString(duration);
                    this.plugin.getPunishmentDb().getLeaveMessages().put(target.getUniqueId(), this.plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedTempBanMessage, userName, durationString)[0]);
                    target.kickPlayer(this.plugin.getMessages().get(MessageId.player_loginAttemptsTempBanMessage, durationString)[0]);
                    this.plugin.getPunishmentDb().tempBanIp(target.getAddress().getAddress().getHostAddress(), this.plugin.getPluginConfig().getTooManyAttemptsPunishmentDuration(), this.plugin.getMessages().get(MessageId.player_loginAttemptsTooMany)[0]);
                    break;
                case 2:
                    this.plugin.getPunishmentDb().getLeaveMessages().put(target.getUniqueId(), this.plugin.getMessages().get(MessageId.player_loginAttemptsBroadcastedPermBanMessage, userName)[0]);
                    target.kickPlayer(this.plugin.getMessages().get(MessageId.player_loginAttemptsPermBanMessage)[0]);
                    this.plugin.getPunishmentDb().permBanIp(target.getAddress().getAddress().getHostAddress(), this.plugin.getMessages().get(MessageId.player_loginAttemptsTooMany)[0]);
                    break;
                default:
                    break;
            }
            this.loginAttempts.put(userName, this.plugin.getPluginConfig().getMaximumLoginAttempts() - 1);
        }
        this.loginAttempts.put(userName, nb);

        this.plugin.exiting(this.getClass(), "loginAttempt");
    }
}
