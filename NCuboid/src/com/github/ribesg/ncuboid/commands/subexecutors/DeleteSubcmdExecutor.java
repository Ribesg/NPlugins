package com.github.ribesg.ncuboid.commands.subexecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ribesg.ncore.Permissions;
import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.commands.AbstractSubcmdExecutor;
import com.github.ribesg.ncuboid.lang.Messages;
import com.github.ribesg.ncuboid.lang.Messages.MessageId;

public class DeleteSubcmdExecutor extends AbstractSubcmdExecutor {

    private static final String USAGE = ChatColor.RED + "Usage : /cuboid delete <cuboidName>";

    public DeleteSubcmdExecutor(final NCuboid instance, final CommandSender sender, final String[] superCommandArgs) {
        super(instance, sender, superCommandArgs);
    }

    @Override
    public boolean exec() {
        if (getArgs().length != 1) {
            getSender().sendMessage(Messages.MESSAGE_HEADER + USAGE);
            return true;
        } else if (getSender().isOp() || getSender().hasPermission(Permissions.CMD_DELETE)) {
            final PlayerCuboid c = CuboidDB.getInstance().getByName(getArgs()[0]);
            if (c == null) {
                getPlugin().sendMessage(getSender(), MessageId.cmdDeleteDoesNotExist);
                return true;
            } else {
                if (getSender().isOp() || getSender() instanceof Player && (((Player) getSender()).getName().equals(c.getOwnerName()) || ((Player) getSender()).hasPermission(Permissions.ADMIN))) {
                    CuboidDB.getInstance().del(c);
                    getPlugin().sendMessage(getSender(), MessageId.cmdDeleteDeleted, c.getCuboidName());
                } else {
                    getPlugin().sendMessage(getSender(), MessageId.cmdDeleteNoPermission, c.getCuboidName());
                }
                return true;
            }
        } else {
            getPlugin().sendMessage(getSender(), MessageId.noPermissionForCommand);
            return true;
        }
    }
}
