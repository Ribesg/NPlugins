package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class FlagSubcmdExecutor extends AbstractSubcmdExecutor {

	private static final String USAGE = ChatColor.RED + "Usage : /cuboid flag <regionName> <flagName> [value]";

	public FlagSubcmdExecutor(final NCuboid instance) {
		super(instance);
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		/* TODO WIP Copy-pasted code
		if (!(sender instanceof Player)) {
			getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		} else if (args.length != 2 && args.length != 1) {
			sender.sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE);
			return true;
		} else if (Perms.hasFlag(sender)) {
			final PlayerRegion c = getPlugin().getDb().getByName(args[0]);
			if (c == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdCreateAlreadyExists);
				return true;
			} else {
				return false;
			}
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
		END TODO */
		return false; // TODO
	}
}
