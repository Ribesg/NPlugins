package fr.ribesg.bukkit.ncuboid.commands.subexecutors;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.RegionDbPersistenceHandler;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.commands.AbstractSubcmdExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class ReloadSubcmdExecutor extends AbstractSubcmdExecutor {

	private static final String USAGE = ChatColor.RED + "Usage : /cuboid reload <regions|config|messages>";

	public ReloadSubcmdExecutor(final NCuboid instance) {
		super(instance);
	}

	@Override
	public boolean exec(final CommandSender sender, final String[] args) {
		if (args.length != 1) {
			sender.sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE);
			return true;
		} else if (Perms.hasReload(sender)) {
			switch (args[0]) {
				case "regions":
				case "region":
					final RegionDb db = RegionDbPersistenceHandler.reloadDb(getPlugin());
					if (db == getPlugin().getDb()) {
						getPlugin().sendMessage(sender, MessageId.cmdReloadError, "regionDB.yml");
					} else {
						getPlugin().setDb(db);
						getPlugin().sendMessage(sender, MessageId.cuboid_cmdReloadRegions);
					}
					return true;
				case "config":
				case "conf":
					try {
						getPlugin().getPluginConfig().loadConfig();
						getPlugin().sendMessage(sender, MessageId.cmdReloadConfig);
						return true;
					} catch (final IOException | InvalidConfigurationException e) {
						getPlugin().getLogger().severe("An error occured, stacktrace follows:");
						e.printStackTrace();
						getPlugin().getLogger().severe("This error occured when NCuboid tried to reload config.yml");
						getPlugin().sendMessage(sender, MessageId.cmdReloadError, "config.yml");
						return true;
					}
				case "messages":
				case "mess":
				case "mes":
					try {
						getPlugin().getMessages().loadMessages(getPlugin());
						getPlugin().sendMessage(sender, MessageId.cmdReloadMessages);
						return true;
					} catch (final IOException e) {
						getPlugin().getLogger().severe("An error occured, stacktrace follows:");
						e.printStackTrace();
						getPlugin().getLogger().severe("This error occured when NCuboid tried to reload messages.yml");
						getPlugin().sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
						return true;
					}
				default:
					sender.sendMessage(getPlugin().getMessages().getMessageHeader() + USAGE);
					return true;
			}
		} else {
			getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
			return true;
		}
	}

}
