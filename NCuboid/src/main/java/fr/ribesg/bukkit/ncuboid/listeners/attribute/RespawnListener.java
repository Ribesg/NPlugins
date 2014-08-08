/***************************************************************************
 * Project file:    NPlugins - NCuboid - RespawnListener.java              *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.attribute.RespawnListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.attribute;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener extends AbstractListener {

	private final Map<String, NLocation> deathLocations;

	public RespawnListener(final NCuboid instance) {
		super(instance);
		this.deathLocations = new HashMap<>();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(final PlayerDeathEvent event) {
		this.deathLocations.put(event.getEntity().getName(), new NLocation(event.getEntity().getLocation()));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		final NLocation loc = this.deathLocations.remove(event.getPlayer().getName());
		if (loc != null) {
			final SortedSet<GeneralRegion> regions = this.getPlugin().getDb().getAllByLocation(loc);
			for (final GeneralRegion region : regions) {
				final Location respawnPoint = region.getLocationAttribute(Attribute.RESPAWN_POINT);
				if (respawnPoint != null) {
					event.setRespawnLocation(respawnPoint);
					break;
				}
			}
		}
	}
}
