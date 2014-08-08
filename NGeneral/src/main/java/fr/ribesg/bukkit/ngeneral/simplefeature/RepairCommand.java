/***************************************************************************
 * Project file:    NPlugins - NGeneral - RepairCommand.java               *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.RepairCommand  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairCommand implements CommandExecutor {

    private static final String COMMAND = "repair";

    private final NGeneral plugin;

    public RepairCommand(final NGeneral instance) {
        this.plugin = instance;
        this.plugin.setCommandExecutor(COMMAND, this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (command.getName().equals(COMMAND)) {
            if (!Perms.hasRepair(sender)) {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            } else if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            } else {
                final Player player = (Player)sender;
                final ItemStack is = player.getItemInHand();
                if (is == null || is.getType().getMaxDurability() == 0) {
                    this.plugin.sendMessage(sender, MessageId.general_repair_cannot);
                } else {
                    is.setDurability((short)0);
                    this.plugin.sendMessage(sender, MessageId.general_repair_done);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
