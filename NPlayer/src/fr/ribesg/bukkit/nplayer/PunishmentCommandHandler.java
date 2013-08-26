package fr.ribesg.bukkit.nplayer;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class PunishmentCommandHandler implements CommandExecutor, Listener {

	private final NPlayer plugin;

	public PunishmentCommandHandler(NPlayer instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		switch (cmd.getName()) {
			case "ban":
				if (Perms.hasBan(sender)) {
					return cmdBan(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "jail":
				if (Perms.hasJail(sender)) {
					return cmdJail(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "mute":
				if (Perms.hasMute(sender)) {
					return cmdMute(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "kick":
				if (Perms.hasKick(sender)) {
					return cmdKick(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			default:
				return false;
		}
	}

	private boolean cmdBan(CommandSender sender, String[] args) {
		return false;  // TODO Implement method
	}

	private boolean cmdJail(CommandSender sender, String[] args) {
		return false;  // TODO Implement method
	}

	private boolean cmdMute(CommandSender sender, String[] args) {
		return false;  // TODO Implement method
	}

	private boolean cmdKick(CommandSender sender, String[] args) {
		return false;  // TODO Implement method
	}
}
