/***************************************************************************
 * Project file:    NPlugins - NCuboid - FireFlagListener.java             *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.FireFlagListener
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
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
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

public class FireFlagListener extends AbstractListener {

	public FireFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBurn(final BlockBurnEvent event) {
		final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlock().getLocation());
		if (region != null && region.getFlag(Flag.FIRE)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockIgnite(final BlockIgniteEvent event) {
		if (event.getCause() != IgniteCause.FLINT_AND_STEEL) {
			final GeneralRegion region = getPlugin().getDb().getPriorByLocation(event.getBlock().getLocation());
			if (region != null && region.getFlag(Flag.FIRE)) {
				event.setCancelled(true);
			}
		}
	}
}
