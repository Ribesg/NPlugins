/***************************************************************************
 * Project file:    NPlugins - NCuboid - MainCommandExecutor.java          *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.MainCommandExecutor  *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.AdminUserSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.CreateSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.DeleteSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.FlagAttributeSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.FlagSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.ReloadSubcmdExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MainCommandExecutor implements CommandExecutor {

	private final NCuboid plugin;

	private final Map<String, String>                 aliasesMap;
	private final Map<String, AbstractSubcmdExecutor> executorsMap;

	public MainCommandExecutor(final NCuboid instance) {
		this.plugin = instance;

		this.aliasesMap = new HashMap<>(5);
		this.aliasesMap.put("rld", "reload");
		this.aliasesMap.put("c", "create");
		this.aliasesMap.put("d", "delete");
		this.aliasesMap.put("del", "delete");
		this.aliasesMap.put("rm", "delete");
		this.aliasesMap.put("remove", "delete");
		this.aliasesMap.put("a", "admin");
		this.aliasesMap.put("u", "user");
		this.aliasesMap.put("f", "flag");
		this.aliasesMap.put("fa", "flagattribute");
		this.aliasesMap.put("flagatt", "flagattribute");
		this.aliasesMap.put("flagattributes", "flagattribute");

		this.executorsMap = new HashMap<>(3);
		this.executorsMap.put("create", new CreateSubcmdExecutor(instance));
		this.executorsMap.put("delete", new DeleteSubcmdExecutor(instance));
		this.executorsMap.put("reload", new ReloadSubcmdExecutor(instance));
		this.executorsMap.put("admin", new AdminUserSubcmdExecutor(instance, true));
		this.executorsMap.put("user", new AdminUserSubcmdExecutor(instance, false));
		this.executorsMap.put("flag", new FlagSubcmdExecutor(instance));
		this.executorsMap.put("flagattribute", new FlagAttributeSubcmdExecutor(instance));
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
		if (cmd.getName().equals("cuboid")) {
			if (!Perms.hasGeneral(sender)) {
				plugin.sendMessage(sender, MessageId.noPermissionForCommand);
				return true;
			} else {
				if (args.length == 0) {
					return cmdDefault(sender);
				} else {
					final AbstractSubcmdExecutor executor = getExecutor(args[0].toLowerCase());
					return executor != null && executor.execute(sender, args);
				}
			}
		} else {
			return false;
		}
	}

	private boolean cmdDefault(final CommandSender sender) {
		// TODO
		return false;
	}

	private AbstractSubcmdExecutor getExecutor(final String providedName) {
		return this.executorsMap.get(getFromAlias(providedName));
	}

	private String getFromAlias(final String providedName) {
		final String command = this.aliasesMap.get(providedName);
		return command == null ? providedName : command;
	}
}
