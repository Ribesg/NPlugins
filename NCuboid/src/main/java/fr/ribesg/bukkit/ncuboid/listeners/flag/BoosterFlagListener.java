/***************************************************************************
 * Project file:    NPlugins - NCuboid - BoosterFlagListener.java          *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.BoosterFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class BoosterFlagListener extends AbstractListener {

	public BoosterFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
		final PlayerGridMoveEvent event = (PlayerGridMoveEvent) ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			if (ext.getToRegion() != null && ext.getToRegion().getFlag(Flag.BOOSTER)) {
				event.getPlayer().setVelocity(ext.getToRegion().getVectAttribute(Attribute.BOOSTER_VECTOR));
			}
		}
	}
}
