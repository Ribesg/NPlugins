package fr.ribesg.bukkit.nplayer;

import org.bukkit.command.CommandSender;

public class Perms {

    // World node permissions
    public static final String ADMIN          = "nplayer.admin";
    public static final String USER           = "nplayer.user";
    public static final String CMD_LOGIN      = "nplayer.cmd.login";
    public static final String CMD_LOGOUT     = "nplayer.cmd.logout";
    public static final String CMD_REGISTER   = "nplayer.cmd.register";
    public static final String CMD_INFO       = "nplayer.cmd.info";
    public static final String CMD_INFO_ADMIN = "nplayer.cmd.info.admin";

    public static boolean hasLogin(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_LOGIN) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasLogout(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_LOGOUT) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasRegister(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_REGISTER) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasInfo(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_INFO) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasInfoAdmin(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_INFO_ADMIN) || sender.hasPermission(ADMIN);
    }
}
