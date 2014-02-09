/***************************************************************************
 * Project file:    NPlugins - NCuboid - AbstractSubcmdExecutor.java       *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class AbstractSubcmdExecutor {

	private final NCuboid  plugin;
	private       String[] usage;

	public AbstractSubcmdExecutor(final NCuboid instance) {
		this.plugin = instance;
	}

	public NCuboid getPlugin() {
		return plugin;
	}

	/** Should be called in sub-classes' constructors */
	protected void setUsage(final String... usage) {
		this.usage = usage;
	}

	public boolean execute(final CommandSender sender, final String[] superCommandArgs) {
		final boolean result = exec(sender, Arrays.copyOfRange(superCommandArgs, 1, superCommandArgs.length));
		if (!result) {
			for (final String line : this.usage) {
				sender.sendMessage(getPlugin().getMessages().getMessageHeader() + line);
			}
		}
		return true;
	}

	protected abstract boolean exec(final CommandSender sender, final String[] commandArgs);
}
