package fr.ribesg.bukkit.ntalk;

import org.bukkit.command.CommandSender;

public class Perms {

	// Talk node permissions
	private static final String ADMIN    = "ntalk.admin";
	private static final String USER     = "ntalk.user";
	private static final String SPY      = "ntalk.spy";
	private static final String CMD_PM   = "ntalk.cmd.pm";
	private static final String CMD_PR   = "ntalk.cmd.pr";
	private static final String CMD_NICK = "ntalk.cmd.nick";

	public static boolean hasPrivateMessage(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_PM) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasPrivateResponse(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_PR) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasNick(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_NICK) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSpy(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(SPY) || sender.hasPermission(ADMIN);
	}
}
