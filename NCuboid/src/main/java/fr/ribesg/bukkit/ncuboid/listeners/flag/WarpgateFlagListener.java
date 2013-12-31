/***************************************************************************
 * Project file:    NPlugins - NCuboid - WarpgateFlagListener.java         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.WarpgateFlagListener
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.FlagAtt;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class WarpgateFlagListener extends AbstractListener {

	public WarpgateFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerMoveBlock(final ExtendedPlayerMoveEvent ext) {
		final PlayerMoveEvent event = (PlayerMoveEvent) ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			if (ext.getToRegion() != null && ext.getToRegion().getFlag(Flag.WARPGATE)) {
				event.getPlayer().teleport(ext.getToRegion().getLocFlagAtt(FlagAtt.EXTERNAL_POINT));
				ext.setCustomCancelled(true);
				event.setCancelled(true);
			}
		}
	}
}
