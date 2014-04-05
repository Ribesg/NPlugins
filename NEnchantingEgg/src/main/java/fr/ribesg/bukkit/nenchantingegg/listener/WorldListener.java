/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - WorldListener.java         *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.listener.WorldListener *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.listener;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WorldListener implements Listener {

	private final NEnchantingEgg plugin;

	public WorldListener(final NEnchantingEgg instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(final EntityExplodeEvent event) {
		plugin.entering(getClass(), "onEntityExplode");

		// Get the chunks considered in this event
		final Set<Chunk> chunks = new HashSet<>();
		Chunk c;
		for (final Block b : event.blockList()) {
			c = b.getLocation().getChunk();
			if (!chunks.contains(c)) {
				if (plugin.isDebugEnabled()) {
					plugin.debug("Considering chunk (" + c.getX() + ";" + c.getZ() + ")");
				}
				chunks.add(c);
			}
		}

		// Get the altars considered from the chunks
		final Set<Altar> altars = new HashSet<>();
		ChunkCoord coord;
		Altar altar;
		for (final Chunk chunk : chunks) {
			coord = new ChunkCoord(chunk);
			altar = plugin.getAltars().get(coord);
			if (altar != null) {
				if (plugin.isDebugEnabled()) {
					plugin.debug("Considering altar at location " + altar.getCenterLocation().toString());
				}
				altars.add(altar);
			}
		}

		// Remove blocks that are part of an altar
		final Iterator<Block> it = event.blockList().iterator();
		Block b;
		while (it.hasNext()) {
			b = it.next();
			for (final Altar a : altars) {
				if (a.isAltarXYZ(b.getX() - a.getCenterLocation().getBlockX(), b.getY() - a.getCenterLocation().getBlockY(), b.getZ() - a.getCenterLocation().getBlockZ())) {
					if (plugin.isDebugEnabled()) {
						plugin.debug("Protecting block at location " + NLocation.toString(b.getLocation()));
					}
					it.remove();
					break;
				}
			}
		}

		plugin.exiting(getClass(), "onEntityExplode");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onChunkLoad(final ChunkLoadEvent event) {
		plugin.getAltars().chunkLoad(new ChunkCoord(event.getChunk()));
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onSnowForm(final BlockFormEvent event) {
		if (event.getNewState().getType() == Material.SNOW) {
			final Location loc = event.getBlock().getLocation();
			final Altar altar = plugin.getAltars().get(new ChunkCoord(loc.getChunk()));
			if (altar.preventsBlockPlacement(loc)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onFlow(final BlockFromToEvent event) {
		final Location loc = event.getToBlock().getLocation();
		final Altar altar = plugin.getAltars().get(new ChunkCoord(loc.getChunk()));
		if (altar != null && altar.preventsBlockPlacement(loc)) {
			event.setCancelled(true);
		}
	}
}
