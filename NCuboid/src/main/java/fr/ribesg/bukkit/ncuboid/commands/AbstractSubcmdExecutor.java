package fr.ribesg.bukkit.ncuboid.commands;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class AbstractSubcmdExecutor {

	private final NCuboid plugin;

	public AbstractSubcmdExecutor(final NCuboid instance) {
		this.plugin = instance;
	}

	public boolean execute(final CommandSender sender, final String[] superCommandArgs) {
		return exec(sender, Arrays.copyOfRange(superCommandArgs, 1, superCommandArgs.length));
	}

	protected abstract boolean exec(final CommandSender sender, final String[] commandArgs);

	public NCuboid getPlugin() {
		return plugin;
	}
}
