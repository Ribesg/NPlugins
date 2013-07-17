package fr.ribesg.bukkit.ncuboid.commands;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class AbstractSubcmdExecutor {

    private final NCuboid       plugin;
    private final CommandSender sender;
    private final String[]      args;

    public AbstractSubcmdExecutor(final NCuboid instance, final CommandSender sender, final String[] superCommandArgs) {
        plugin = instance;
        this.sender = sender;
        args = Arrays.copyOfRange(superCommandArgs, 1, superCommandArgs.length);
    }

    public abstract boolean exec();

    public String[] getArgs() {
        return args;
    }

    public NCuboid getPlugin() {
        return plugin;
    }

    public CommandSender getSender() {
        return sender;
    }
}
