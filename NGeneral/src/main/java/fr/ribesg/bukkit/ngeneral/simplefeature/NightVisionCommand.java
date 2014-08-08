/***************************************************************************
 * Project file:    NPlugins - NGeneral - NightVisionCommand.java          *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.NightVisionCommand
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.TimeUtil;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionCommand implements CommandExecutor {

    private static final String COMMAND = "nightvision";

    private final NGeneral plugin;

    public NightVisionCommand(final NGeneral instance) {
        this.plugin = instance;
        this.plugin.setCommandExecutor(COMMAND, this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (command.getName().equals(COMMAND)) {
            if (!Perms.hasNightVision(sender)) {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            } else if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            } else {
                final Player player = (Player)sender;
                if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    this.plugin.sendMessage(player, MessageId.general_nightvision_disabled);
                } else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (int)TimeUtil.getInSeconds("1month"), 9, true), true);
                    this.plugin.sendMessage(player, MessageId.general_nightvision_enabled);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
