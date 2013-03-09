package fr.ribesg.bukkit.ntheendagain;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.ribesg.bukkit.ntheendagain.lang.Messages.MessageId;

public class NCommandExecutor implements CommandExecutor {

    private final NTheEndAgain plugin;

    public NCommandExecutor(final NTheEndAgain instance) {
        // Link the main plugin and his CommandExecutor
        plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        // This method handle commands
        if (commandLabel.equalsIgnoreCase("end")) {
            if (args.length == 0 || args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h"))) {
                if (sender.hasPermission(Permissions.CMD_HELP) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                    return cmdHelp(sender);
                } else {
                    plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return false;
                }
            } else {
                switch (args[0].toLowerCase()) {
                    case "regen":
                        if (sender.hasPermission(Permissions.CMD_REGEN) || sender.hasPermission(Permissions.ADMIN)) {
                            return cmdRegen(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return false;
                        }
                        break;
                    case "respawnenderdragon":
                    case "respawned":
                        if (sender.hasPermission(Permissions.CMD_RESPAWN) || sender.hasPermission(Permissions.ADMIN)) {
                            return cmdRespawn(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return false;
                        }
                        break;
                    case "nbenderdragon":
                    case "nbed":
                        if (sender.hasPermission(Permissions.CMD_NB) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                            return cmdNb(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return false;
                        }
                        break;
                    case "chunk":
                        // TODO Check next level or args
                        if (sender.hasPermission(Permissions.CMD_CHUNK) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                            return cmdChunk(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return false;
                        }
                        break;
                    default:
                        return false;

                }
            }
        } else {
            return false;
        }
    }
}
