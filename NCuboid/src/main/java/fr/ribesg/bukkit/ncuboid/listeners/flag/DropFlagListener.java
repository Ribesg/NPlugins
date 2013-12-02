/***************************************************************************
 * Project file:    NPlugins - NCuboid - DropFlagListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.DropFlagListener
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerDropItemEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropFlagListener extends AbstractListener {

	public DropFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDropItem(final ExtendedPlayerDropItemEvent ext) {
		final PlayerDropItemEvent event = (PlayerDropItemEvent) ext.getBaseEvent();
		if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.DROP) && !ext.getRegion().isUser(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
