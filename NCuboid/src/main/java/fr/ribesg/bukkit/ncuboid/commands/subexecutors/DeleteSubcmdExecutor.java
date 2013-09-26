package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.PlayerRegion;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DeleteSubcmdExecutor extends AbstractSubcmdExecutor {

	private static final String USAGE = ChatColor.RED + "Usage : /cuboid delete <regionName>";

	public DeleteSubcmdExecutor(final NCuboid instance) {
		super(instance);
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		if (args.length != 1) {
			sender.sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE);
			return true;
		} else if (Perms.hasDelete(sender)) {
			final PlayerRegion region = getPlugin().getDb().getByName(args[0]);
			if (region == null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdDeleteDoesNotExist);
				return true;
			} else {
				if (Perms.isAdmin(sender) || region.isOwner(sender)) {
					getPlugin().getDb().remove(region);
					getPlugin().sendMessage(sender, MessageId.cuboid_cmdDeleteDeleted, region.getRegionName());
				} else {
					getPlugin().sendMessage(sender, MessageId.cuboid_cmdDeleteNoPermission, region.getRegionName());
				}
				return true;
			}
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}
}
