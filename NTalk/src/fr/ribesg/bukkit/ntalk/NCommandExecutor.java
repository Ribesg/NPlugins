package fr.ribesg.bukkit.ntalk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.ribesg.bukkit.ncore.lang.MessageId;

/**
 * @author ribes
 */
public class NCommandExecutor implements CommandExecutor {
    private final NTalk plugin;
    
    public NCommandExecutor(final NTalk instance) {
        plugin = instance;
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
        if (args.length <= 1) {
            return false;
        } else {
            // TODO
            return false;
        }
    }
    
    private boolean cmdPr(CommandSender sender, String[] args) {
        // TODO
        return false;
    }
}
