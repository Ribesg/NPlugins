/***************************************************************************
 * Project file:    NPlugins - NGeneral - NicknameFilter.java              *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.NicknameFilter *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import fr.ribesg.bukkit.ngeneral.NGeneral;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class NicknameFilter implements Listener {

    private final NGeneral plugin;

    public NicknameFilter(final NGeneral plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        if (event.getResult() == Result.ALLOWED) {
            final String playerName = event.getPlayer().getName();
            if (!PlayerIdsUtil.isValidMinecraftUserName(playerName)) {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(this.plugin.getMessages().get(MessageId.general_nicknameFilter_invalid, playerName)[0]);
            }
        }
    }
}
