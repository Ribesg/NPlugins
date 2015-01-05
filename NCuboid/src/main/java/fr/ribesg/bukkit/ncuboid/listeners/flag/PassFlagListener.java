/***************************************************************************
 * Project file:    NPlugins - NCuboid - PassFlagListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.PassFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PassFlagListener extends AbstractListener {

    public PassFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
        final PlayerGridMoveEvent event = (PlayerGridMoveEvent)ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            final GeneralRegion to = ext.getToRegion();
            if (to != null && to.getFlag(Flag.PASS) && !ext.getFromRegions().contains(to)) {
                Location loc = to.getLocationAttribute(Attribute.EXTERNAL_POINT);
                if (loc == null) {
                    loc = event.getFrom();
                }
                event.setTo(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.1, loc.getBlockZ() + 0.5, event.getTo().getYaw(), event.getTo().getPitch()));
                ext.setCustomCancelled(true);
            }
        }
    }
}
