/***************************************************************************
 * Project file:    NPlugins - NCuboid - TeleportFlagListener.java         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.TeleportFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerTeleportEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportFlagListener extends AbstractListener {

    public TeleportFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerTeleport(final ExtendedPlayerTeleportEvent ext) {
        final PlayerTeleportEvent event = (PlayerTeleportEvent)ext.getBaseEvent();
        if (ext.getFromRegion() != null && ext.getFromRegion().getFlag(Flag.TELEPORT)) {
            event.setCancelled(true);
        } else if (ext.getToRegion() != null && ext.getToRegion().getFlag(Flag.TELEPORT)) {
            event.setCancelled(true);
        }
    }
}
