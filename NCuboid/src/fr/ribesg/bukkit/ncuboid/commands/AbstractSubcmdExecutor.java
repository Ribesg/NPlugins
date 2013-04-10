package fr.ribesg.bukkit.ncuboid.commands;

import java.util.Arrays;

import lombok.Getter;

import org.bukkit.command.CommandSender;

import fr.ribesg.bukkit.ncuboid.NCuboid;

public abstract class AbstractSubcmdExecutor {

    @Getter private final NCuboid       plugin;
    @Getter private final CommandSender sender;
    @Getter private final String[]      args;

    public AbstractSubcmdExecutor(final NCuboid instance, final CommandSender sender, final String[] superCommandArgs) {
        plugin = instance;
        this.sender = sender;
        args = Arrays.copyOfRange(superCommandArgs, 1, superCommandArgs.length);
    }

    public abstract boolean exec();
}
