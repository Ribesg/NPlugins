package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncore.Permissions;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid.CuboidState;
import fr.ribesg.bukkit.ncuboid.beans.RectCuboid;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.lang.Messages;
import fr.ribesg.bukkit.ncuboid.lang.Messages.MessageId;

public class CreateSubcmdExecutor extends AbstractSubcmdExecutor {

    private static final String USAGE = ChatColor.RED + "Usage : /cuboid create <cuboidName>";

    public CreateSubcmdExecutor(final NCuboid instance, final CommandSender sender, final String[] superCommandArgs) {
        super(instance, sender, superCommandArgs);
    }

    @Override
    public boolean exec() {
        if (!(getSender() instanceof Player)) {
            getPlugin().sendMessage(getSender(), MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else if (getArgs().length != 1) {
            getSender().sendMessage(Messages.MESSAGE_HEADER + USAGE);
            return true;
        } else if (getSender().isOp() || getSender().hasPermission(Permissions.CMD_CREATE)) {
            final PlayerCuboid c = CuboidDB.getInstance().getByName(getArgs()[0]);
            if (c != null) {
                getPlugin().sendMessage(getSender(), MessageId.cmdCreateAlreadyExists);
                return true;
            } else {
                final Player player = (Player) getSender();
                final RectCuboid selection = (RectCuboid) CuboidDB.getInstance().delTmp(player.getName());
                if (selection.getState() == CuboidState.TMPSTATE2) {
                    selection.create(getArgs()[0]);
                    CuboidDB.getInstance().add(selection);
                    getPlugin().sendMessage(player, MessageId.cmdCreateCreated, selection.getCuboidName());
                } else {
                    getPlugin().sendMessage(player, MessageId.cmdCreateNoValidSelection);
                }
                return true;
            }
        } else {
            getPlugin().sendMessage(getSender(), MessageId.noPermissionForCommand);
            return true;
        }
    }
}
