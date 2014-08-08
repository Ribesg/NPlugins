/***************************************************************************
 * Project file:    NPlugins - NCuboid - ReloadSubcmdExecutor.java         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.subexecutors.ReloadSubcmdExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.RegionDbPersistenceHandler;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

public class ReloadSubcmdExecutor extends AbstractSubcmdExecutor {

	public ReloadSubcmdExecutor(final NCuboid instance) {
		super(instance);
		this.setUsage(ChatColor.RED + "Usage : /ncuboid reload <regions|config|messages>");
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		if (args.length != 1) {
			return false;
		} else if (Perms.hasReload(sender)) {
			switch (args[0].toLowerCase()) {
				case "regions":
				case "region":
					final RegionDb db = RegionDbPersistenceHandler.reloadDb(this.getPlugin());
					if (db == this.getPlugin().getDb()) {
						this.getPlugin().sendMessage(sender, MessageId.cmdReloadError, "regionDB.yml");
					} else {
						this.getPlugin().setDb(db);
						this.getPlugin().getDynmapBridge().reinitialize(db);
						this.getPlugin().sendMessage(sender, MessageId.cuboid_cmdReloadRegions);
					}
					return true;
				case "config":
				case "conf":
					try {
						this.getPlugin().getPluginConfig().loadConfig();
						this.getPlugin().sendMessage(sender, MessageId.cmdReloadConfig);
						return true;
					} catch (final IOException | InvalidConfigurationException e) {
						this.getPlugin().error("An error occured when NCuboid tried to reload config.yml", e);
						this.getPlugin().sendMessage(sender, MessageId.cmdReloadError, "config.yml");
						return true;
					}
				case "messages":
				case "mess":
				case "mes":
					try {
						this.getPlugin().loadMessages();
						this.getPlugin().sendMessage(sender, MessageId.cmdReloadMessages);
					} catch (final IOException e) {
						this.getPlugin().error("An error occured when NCuboid tried to reload messages.yml", e);
						this.getPlugin().sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
					}
					return true;
				default:
					return false;
			}
		} else {
			this.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}
}
