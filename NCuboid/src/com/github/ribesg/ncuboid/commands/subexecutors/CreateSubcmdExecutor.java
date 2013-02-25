package com.github.ribesg.ncuboid.commands.subexecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ribesg.ncore.Permissions;
import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.beans.PlayerCuboid.CuboidState;
import com.github.ribesg.ncuboid.beans.RectCuboid;
import com.github.ribesg.ncuboid.commands.AbstractSubcmdExecutor;
import com.github.ribesg.ncuboid.lang.Messages;
import com.github.ribesg.ncuboid.lang.Messages.MessageId;

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
