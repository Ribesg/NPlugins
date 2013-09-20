package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid.CuboidState;
import fr.ribesg.bukkit.ncuboid.beans.RectCuboid;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSubcmdExecutor extends AbstractSubcmdExecutor {

	private static final String USAGE = ChatColor.RED + "Usage : /cuboid create <cuboidName>";

	public CreateSubcmdExecutor(final NCuboid instance) {
		super(instance);
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		} else if (args.length != 1) {
			sender.sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE);
			return true;
		} else if (Perms.hasCreate(sender)) {
			final PlayerCuboid c = getPlugin().getDb().getByName(args[0]);
			if (c != null) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdCreateAlreadyExists);
				return true;
			} else if (args[0].toLowerCase().startsWith("world_")) {
				getPlugin().sendMessage(sender, MessageId.cuboid_cmdCreateForbiddenName);
				return true;
			} else {
				final Player player = (Player) sender;
				final RectCuboid selection = (RectCuboid) getPlugin().getDb().getSelection(player.getName());
				if (selection.getState() == CuboidState.TMPSTATE2) {
					getPlugin().getDb().removeSelection(player.getName());
					selection.create(args[0]);
					getPlugin().getDb().add(selection);
					getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateCreated, selection.getCuboidName());
				} else {
					getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateNoValidSelection);
				}
				return true;
			}
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}
}
