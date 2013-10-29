package fr.ribesg.bukkit.ncuboid.commands;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class AbstractSubcmdExecutor {

	private final NCuboid plugin;
	private       String  usage;

	public AbstractSubcmdExecutor(final NCuboid instance) {
		this.plugin = instance;
	}

	public NCuboid getPlugin() {
		return plugin;
	}

	/** Should be called in sub-classes' constructors */
	protected void setUsage(final String usage) {
		this.usage = usage;
	}

	public boolean execute(final CommandSender sender, final String[] superCommandArgs) {
		final boolean result = exec(sender, Arrays.copyOfRange(superCommandArgs, 1, superCommandArgs.length));
		if (!result) {
			sender.sendMessage(getPlugin().getMessages().getMessageHeader() + this.usage);
		}
		return true;
	}

	protected abstract boolean exec(final CommandSender sender, final String[] commandArgs);
}
