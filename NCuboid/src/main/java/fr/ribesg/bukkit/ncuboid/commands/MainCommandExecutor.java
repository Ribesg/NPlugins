/***************************************************************************
 * Project file:    NPlugins - NCuboid - MainCommandExecutor.java          *
 * Full Class name: fr.ribesg.bukkit.ncuboid.commands.MainCommandExecutor  *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.commands;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.AdminUserGroupJailSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.AttributeSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.CreateSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.DeleteSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.FlagSubcmdExecutor;
import fr.ribesg.bukkit.ncuboid.commands.subexecutors.ReloadSubcmdExecutor;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommandExecutor implements CommandExecutor {

    private final NCuboid plugin;

    private final Map<String, String>                 aliasesMap;
    private final Map<String, AbstractSubcmdExecutor> executorsMap;

    public MainCommandExecutor(final NCuboid instance) {
        this.plugin = instance;

        this.aliasesMap = new HashMap<>(14);
        this.aliasesMap.put("rld", "reload");
        this.aliasesMap.put("c", "create");
        this.aliasesMap.put("d", "delete");
        this.aliasesMap.put("del", "delete");
        this.aliasesMap.put("rm", "delete");
        this.aliasesMap.put("remove", "delete");
        this.aliasesMap.put("a", "admin");
        this.aliasesMap.put("u", "user");
        this.aliasesMap.put("g", "group");
        this.aliasesMap.put("j", "jail");
        this.aliasesMap.put("f", "flag");
        this.aliasesMap.put("at", "attribute");
        this.aliasesMap.put("att", "attribute");
        this.aliasesMap.put("attributes", "attribute");

        this.executorsMap = new HashMap<>(9);
        this.executorsMap.put("create", new CreateSubcmdExecutor(instance));
        this.executorsMap.put("delete", new DeleteSubcmdExecutor(instance));
        this.executorsMap.put("reload", new ReloadSubcmdExecutor(instance));
        this.executorsMap.put("admin", new AdminUserGroupJailSubcmdExecutor(instance, AdminUserGroupJailSubcmdExecutor.Mode.ADMIN));
        this.executorsMap.put("user", new AdminUserGroupJailSubcmdExecutor(instance, AdminUserGroupJailSubcmdExecutor.Mode.USER));
        this.executorsMap.put("group", new AdminUserGroupJailSubcmdExecutor(instance, AdminUserGroupJailSubcmdExecutor.Mode.GROUP));
        this.executorsMap.put("jail", new AdminUserGroupJailSubcmdExecutor(instance, AdminUserGroupJailSubcmdExecutor.Mode.JAIL));
        this.executorsMap.put("flag", new FlagSubcmdExecutor(instance));
        this.executorsMap.put("attribute", new AttributeSubcmdExecutor(instance));
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
        if ("ncuboid".equals(cmd.getName())) {
            if (!Perms.hasGeneral(sender)) {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            } else {
                if (args.length == 0) {
                    return this.cmdDefault(sender);
                } else {
                    final AbstractSubcmdExecutor executor = this.getExecutor(args[0].toLowerCase());
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
        return this.executorsMap.get(this.getFromAlias(providedName));
    }

    private String getFromAlias(final String providedName) {
        final String command = this.aliasesMap.get(providedName);
        return command == null ? providedName : command;
    }
}
