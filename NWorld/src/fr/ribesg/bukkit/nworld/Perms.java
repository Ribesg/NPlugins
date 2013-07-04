package fr.ribesg.bukkit.nworld;

import org.bukkit.command.CommandSender;

public class Perms {

    // World node permissions
    private static final String ADMIN               = "nworld.admin";
    private static final String USER                = "nworld.user";
    private static final String CMD_WORLD           = "nworld.cmd.world";
    private static final String CMD_WORLD_WARP_EDIT = "nworld.cmd.world.edit";
    private static final String CMD_WORLD_WARP_ALL  = "nworld.cmd.world.all";
    private static final String CMD_WORLD_CREATE    = "nworld.cmd.create";
    private static final String CMD_WORLD_LOAD      = "nworld.cmd.load";
    private static final String CMD_WORLD_UNLOAD    = "nworld.cmd.unload";
    private static final String CMD_SPAWN           = "nworld.cmd.spawn";
    private static final String CMD_SETSPAWN        = "nworld.cmd.setspawn";

    public static boolean hasWorld(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_WORLD) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasWorldWarpEdit(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_WORLD_WARP_EDIT) || sender.hasPermission(ADMIN);
    }

    public static boolean hasWorldWarpAll(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_WORLD_WARP_ALL) || sender.hasPermission(ADMIN);
    }

    public static boolean hasWorldCreate(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_WORLD_CREATE) || sender.hasPermission(ADMIN);
    }

    public static boolean hasWorldLoad(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_WORLD_LOAD) || sender.hasPermission(ADMIN);
    }

    public static boolean hasWorldUnload(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_WORLD_UNLOAD) || sender.hasPermission(ADMIN);
    }

    public static boolean hasSpawn(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_SPAWN) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasSetSpawn(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_SETSPAWN) || sender.hasPermission(ADMIN);
    }
}
