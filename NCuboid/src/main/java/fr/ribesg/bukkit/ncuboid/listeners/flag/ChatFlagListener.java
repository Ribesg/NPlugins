/***************************************************************************
 * Project file:    NPlugins - NCuboid - ChatFlagListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.ChatFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFlagListener extends AbstractListener {

    public ChatFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        final GeneralRegion region = this.getPlugin().getDb().getPriorByLocation(event.getPlayer().getLocation());
        if (region != null && region.getFlag(Flag.CHAT) && !region.isUser(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
