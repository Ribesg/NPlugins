package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Permissions;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.lang.Messages;

public class ReloadSubcmdExecutor extends AbstractSubcmdExecutor {

    private static final String USAGE = ChatColor.RED + "Usage : /cuboid reload <cuboids|config|messages>";

    public ReloadSubcmdExecutor(final NCuboid instance, final CommandSender sender, final String[] superCommandArgs) {
        super(instance, sender, superCommandArgs);
    }

    @Override
    public boolean exec() {
        if (getArgs().length != 1) {
            getSender().sendMessage(Messages.MESSAGE_HEADER + USAGE);
            return true;
        } else if (getSender().isOp() || getSender().hasPermission(Permissions.CMD_RELOAD)) {
            switch (getArgs()[0]) {
                case "cuboids":
                case "cuboid":
                case "cubo":
                    try {
                        getPlugin().getMessages().loadMessages(getPlugin()); // TODO Wrong config file
                        getPlugin().sendMessage(getSender(), MessageId.cuboid_cmdReloadCuboids);
                        return true;
                    } catch (final IOException e) {
                        getPlugin().getLogger().severe("An error occured, stacktrace follows:");
                        e.printStackTrace();
                        getPlugin().getLogger().severe("This error occured when NCuboid tried to reload cuboidDB.yml");
                        return true;
                    }
                case "config":
                case "conf":
                    try {
                        getPlugin().getPluginConfig().loadConfig(getPlugin());
                        getPlugin().sendMessage(getSender(), MessageId.cmdReloadConfig);
                        return true;
                    } catch (final IOException e) {
                        getPlugin().getLogger().severe("An error occured, stacktrace follows:");
                        e.printStackTrace();
                        getPlugin().getLogger().severe("This error occured when NCuboid tried to reload config.yml");
                        return true;
                    }
                case "messages":
                case "mess":
                case "mes":
                    try {
                        getPlugin().getMessages().loadMessages(getPlugin());
                        getPlugin().sendMessage(getSender(), MessageId.cmdReloadMessages);
                        return true;
                    } catch (final IOException e) {
                        getPlugin().getLogger().severe("An error occured, stacktrace follows:");
                        e.printStackTrace();
                        getPlugin().getLogger().severe("This error occured when NCuboid tried to reload messages.yml");
                        return true;
                    }
                default:
                    getSender().sendMessage(Messages.MESSAGE_HEADER + USAGE);
                    return true;
            }
        } else {
            getPlugin().sendMessage(getSender(), MessageId.noPermissionForCommand);
            return true;
        }
    }

}
