/***************************************************************************
 * Project file:    NPlugins - NCuboid - PassFlagListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.PassFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
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
		final PlayerGridMoveEvent event = (PlayerGridMoveEvent) ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			if (ext.getFromRegion() != null && ext.getFromRegion().getFlag(Flag.PASS) && !ext.getFromRegion().equals(ext.getToRegion())) {
				event.setTo(new Location(event.getFrom().getWorld(), event.getFrom().getBlockX() + 0.5, event.getFrom().getBlockY() + 0.25, event.getFrom().getBlockZ() + 0.5, event.getTo().getYaw(), event.getTo().getPitch()));
				ext.setCustomCancelled(true);
			}
		}
	}
}
