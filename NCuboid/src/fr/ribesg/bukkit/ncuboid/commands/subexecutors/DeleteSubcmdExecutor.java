package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
        } else if (Perms.hasDelete(getSender())) {
            final PlayerCuboid c = getPlugin().getDb().getByName(getArgs()[0]);
            if (c == null) {
                getPlugin().sendMessage(getSender(), MessageId.cuboid_cmdDeleteDoesNotExist);
                return true;
            } else {
                if (Perms.isAdmin(getSender()) || c.isOwner(getSender())) {
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
