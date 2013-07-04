package fr.ribesg.bukkit.ntheendagain;

import org.bukkit.command.CommandSender;

public class Perms {

    // TheEndAgain node permissions
    private static final String ADMIN              = "ntheendagain.admin";
    private static final String USER               = "ntheendagain.user";
    private static final String CMD_HELP           = "ntheendagain.cmd.help";
    private static final String CMD_RELOAD         = "ntheendagain.cmd.reload";
    private static final String CMD_REGEN          = "ntheendagain.cmd.regen";
    private static final String CMD_RESPAWN        = "ntheendagain.cmd.respawn";
    private static final String CMD_NB             = "ntheendagain.cmd.nb";
    private static final String CMD_CHUNKINFO      = "ntheendagain.cmd.chunkinfo";
    private static final String CMD_CHUNKPROTECT   = "ntheendagain.cmd.chunkprotect";
    private static final String CMD_CHUNKUNPROTECT = "ntheendagain.cmd.chunkunprotect";

    public static boolean hasHelp(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_HELP) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasReload(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_RELOAD) || sender.hasPermission(ADMIN);
    }

    public static boolean hasRegen(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_REGEN) || sender.hasPermission(ADMIN);
    }

    public static boolean hasRespawn(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_RESPAWN) || sender.hasPermission(ADMIN);
    }

    public static boolean hasNb(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_NB) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasChunkInfo(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_CHUNKINFO) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
    }

    public static boolean hasChunkProtect(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_CHUNKPROTECT) || sender.hasPermission(ADMIN);
    }

    public static boolean hasChunkUnprotect(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(CMD_CHUNKUNPROTECT) || sender.hasPermission(ADMIN);
    }
}
