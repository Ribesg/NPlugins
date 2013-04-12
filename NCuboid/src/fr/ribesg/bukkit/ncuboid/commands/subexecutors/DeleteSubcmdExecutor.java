package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Permissions;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;

public class DeleteSubcmdExecutor extends AbstractSubcmdExecutor {

    private static final String USAGE = ChatColor.RED + "Usage : /cuboid delete <cuboidName>";

    public DeleteSubcmdExecutor(final NCuboid instance, final CommandSender sender, final String[] superCommandArgs) {
        super(instance, sender, superCommandArgs);
    }

    @Override
    public boolean exec() {
        if (getArgs().length != 1) {
            getSender().sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE);
            return true;
        } else if (getSender().isOp() || getSender().hasPermission(Permissions.CMD_DELETE)) {
            final PlayerCuboid c = getPlugin().getDb().getByName(getArgs()[0]);
            if (c == null) {
                getPlugin().sendMessage(getSender(), MessageId.cuboid_cmdDeleteDoesNotExist);
                return true;
            } else {
                if (getSender().isOp() || getSender() instanceof Player && (((Player) getSender()).getName().equals(c.getOwnerName()) || ((Player) getSender()).hasPermission(Permissions.ADMIN))) {
                    getPlugin().getDb().del(c);
                    getPlugin().sendMessage(getSender(), MessageId.cuboid_cmdDeleteDeleted, c.getCuboidName());
                } else {
                    getPlugin().sendMessage(getSender(), MessageId.cuboid_cmdDeleteNoPermission, c.getCuboidName());
                }
                return true;
            }
        } else {
            getPlugin().sendMessage(getSender(), MessageId.noPermissionForCommand);
            return true;
        }
    }
}
