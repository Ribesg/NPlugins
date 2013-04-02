package fr.ribesg.bukkit.ntalk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncore.lang.MessageId;

/**
 * @author ribes
 */
public class NCommandExecutor implements CommandExecutor {

    private final static String           CONSOLE_NAME = Bukkit.getConsoleSender().getName();

    private final NTalk                   plugin;
    private final HashMap<String, String> lastReceivedPmMap;

    public NCommandExecutor(final NTalk instance) {
        plugin = instance;
        lastReceivedPmMap = new HashMap<String, String>();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (command.getName().equalsIgnoreCase("pm")) {
            if (sender.hasPermission(Permissions.CMD_PM) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN) || sender.isOp()) {
                return cmdPm(sender, args);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("pr")) {
            if (sender.hasPermission(Permissions.CMD_PR) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN) || sender.isOp()) {
                return cmdPr(sender, args);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("nick")) {
            if (sender.hasPermission(Permissions.CMD_NICK) || sender.hasPermission(Permissions.ADMIN) || sender.isOp()) {
                return cmdNick(sender, args);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean cmdPm(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            return false;
        } else {
            final String[] targetsName = args[0].split(",");
            final HashSet<CommandSender> targets = new HashSet<CommandSender>();
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
            if (targets.size() == 0) {
                return true;
            }

            final StringBuilder messageBuilder = new StringBuilder(args[1]);
            for (int i = 2; i < args.length; i++) {
                messageBuilder.append(' ').append(args[i]);
            }

            sendMessage(sender, targets, messageBuilder.toString());
            return true;
        }
    }

    private boolean cmdPr(final CommandSender sender, final String[] args) {
        if (args.length < 1) {
            return false;
        } else if (lastReceivedPmMap.containsKey(sender.getName())) {
            final String targetName = lastReceivedPmMap.get(sender.getName());
            CommandSender target = null;
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
                sendMessage(sender, target, messageBuilder.toString());
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
            plugin.getPluginConfig().getPlayerNicknames().remove(realName);
            plugin.sendMessage(sender, MessageId.talk_youDeNickNamed, realName);
            if (plugin.getServer().getPlayerExact(realName) != null) {
                plugin.sendMessage(plugin.getServer().getPlayerExact(realName), MessageId.talk_youWereDeNickNamed, sender.getName());
            }
            return true;
        } else {
            final String realName = args[0];
            final String nick = args[1];
            plugin.getPluginConfig().getPlayerNicknames().put(realName, nick);
            plugin.sendMessage(sender, MessageId.talk_youNickNamed, realName, nick);
            if (plugin.getServer().getPlayerExact(realName) != null) {
                plugin.sendMessage(plugin.getServer().getPlayerExact(realName), MessageId.talk_youWereNickNamed, nick, sender.getName());
            }
            return true;
        }
    }

    private void sendMessage(final CommandSender from, final Set<CommandSender> toSet, final String message) {
        for (final CommandSender to : toSet) {
            sendMessage(from, to, message);
        }
    }

    private void sendMessage(final CommandSender from, final CommandSender to, final String message) {
        final String formattedMessage = plugin.getFormater().parsePM(from, to, message);
        from.sendMessage(formattedMessage);
        to.sendMessage(formattedMessage);
        lastReceivedPmMap.put(to.getName(), from.getName());
    }
}
