package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Permissions;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid.CuboidState;
import fr.ribesg.bukkit.ncuboid.beans.RectCuboid;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;

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
            getSender().sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE);
            return true;
        } else if (getSender().isOp() || getSender().hasPermission(Permissions.CMD_CREATE)) {
            final PlayerCuboid c = getPlugin().getDb().getByName(getArgs()[0]);
            if (c != null) {
                getPlugin().sendMessage(getSender(), MessageId.cuboid_cmdCreateAlreadyExists);
                return true;
            } else {
                final Player player = (Player) getSender();
                final RectCuboid selection = (RectCuboid) getPlugin().getDb().delTmp(player.getName());
                if (selection.getState() == CuboidState.TMPSTATE2) {
                    selection.create(getArgs()[0]);
                    getPlugin().getDb().add(selection);
                    getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateCreated, selection.getCuboidName());
                } else {
                    getPlugin().sendMessage(player, MessageId.cuboid_cmdCreateNoValidSelection);
                }
                return true;
            }
        } else {
            getPlugin().sendMessage(getSender(), MessageId.noPermissionForCommand);
            return true;
        }
    }
}
