/***************************************************************************
 * Project file:    NPlugins - NGeneral - AutoAfkCommandExecutor.java      *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.autoafk.AutoAfkCommandExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.autoafk;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.Perms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoAfkCommandExecutor implements CommandExecutor {

    private final AutoAfkFeature feature;

    public AutoAfkCommandExecutor(final AutoAfkFeature feature) {
        this.feature = feature;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if ("afk".equals(command.getName())) {
            if (!Perms.hasAfk(sender)) {
                this.feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
            } else if (!(sender instanceof Player)) {
                this.feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            } else {
                final Player player = (Player)sender;
                String reason = "";
                for (final String arg : args) {
                    reason += arg + ' ';
                }
                if (!reason.isEmpty()) {
                    reason = reason.substring(0, reason.length() - 1);
                }
                this.feature.setAfk(player.getName(), !this.feature.isAfk(player), reason);
            }
            return true;
        } else {
            return false;
        }
    }
}
