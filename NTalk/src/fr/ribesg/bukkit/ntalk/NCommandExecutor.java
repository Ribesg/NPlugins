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
    
    private final static String     CONSOLE_NAME = Bukkit.getConsoleSender().getName();
    
    private final NTalk             plugin;
    private HashMap<String, String> lastReceivedPmMap;
    
    public NCommandExecutor(final NTalk instance) {
        plugin = instance;
        lastReceivedPmMap = new HashMap<String, String>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (command.getName().equalsIgnoreCase("pm")) {
            if (sender.hasPermission(Permissions.CMD_PM) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                return cmdPm(sender, args);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("pr")) {
            if (sender.hasPermission(Permissions.CMD_PR) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                return cmdPr(sender, args);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean cmdPm(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        } else {
            String[] targetsName = args[0].split(",");
            HashSet<CommandSender> targets = new HashSet<CommandSender>();
            for (String target : targetsName) {
                Player p = plugin.getServer().getPlayer(target);
                if (p != null) {
                    targets.add(p);
                } else if (target.equalsIgnoreCase("CONSOLE") || target.equalsIgnoreCase("SERVER")) {
                    targets.add(plugin.getServer().getConsoleSender());
                } else {
                    plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, target);
                }
            }
            if (targets.size() == 0) return true;
            
            StringBuilder messageBuilder = new StringBuilder(args[1]);
            for (int i = 2; i < args.length; i++) {
                messageBuilder.append(' ').append(args[i]);
            }
            
            sendMessage(sender, targets, messageBuilder.toString());
            return true;
        }
    }
    
    private boolean cmdPr(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        } else if (lastReceivedPmMap.containsKey(sender.getName())) {
            String targetName = lastReceivedPmMap.get(sender.getName());
            CommandSender target = null;
            if (CONSOLE_NAME.equals(targetName)) {
                target = plugin.getServer().getConsoleSender();
            } else {
                target = plugin.getServer().getPlayerExact(targetName);
            }
            if (target != null) {
                StringBuilder messageBuilder = new StringBuilder(args[0]);
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
    
    private void sendMessage(CommandSender from, Set<CommandSender> toSet, String message) {
        for (CommandSender to : toSet) {
            sendMessage(from, to, message);
        }
    }
    
    private void sendMessage(CommandSender from, CommandSender to, String message) {
        String formattedMessage = plugin.getFormater().parsePM(from, to, message);
        from.sendMessage(formattedMessage);
        to.sendMessage(formattedMessage);
        lastReceivedPmMap.put(to.getName(), from.getName());
    }
}
