/***************************************************************************
 * Project file:    NPlugins - NGeneral - SpyModeCommandExecutor.java      *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.spymode.SpyModeCommandExecutor
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.spymode;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.Perms;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyModeCommandExecutor implements CommandExecutor {

    private final SpyModeFeature feature;

    public SpyModeCommandExecutor(final SpyModeFeature feature) {
        this.feature = feature;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if ("spy".equals(command.getName())) {
            if (!Perms.hasSpy(sender)) {
                this.feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            } else if (!(sender instanceof Player)) {
                this.feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                return true;
            } else {
                final Player player = (Player)sender;
                if (this.feature.hasSpyMode(player.getUniqueId())) {
                    this.feature.unSetSpyMode(player);
                    this.feature.getPlugin().sendMessage(player, MessageId.general_spy_disabled);
                } else if (args.length == 0) {
                    this.feature.setSpyMode(player, null);
                    this.feature.getPlugin().sendMessage(player, MessageId.general_spy_enabled);
                } else {
                    final Player spied = Bukkit.getPlayer(args[0]);
                    if (spied == null) {
                        this.feature.getPlugin().sendMessage(player, MessageId.noPlayerFoundForGivenName, args[0]);
                    } else {
                        this.feature.setSpyMode(player, spied);
                        this.feature.getPlugin().sendMessage(player, MessageId.general_spy_enabledPlayer, spied.getName());
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }
}
