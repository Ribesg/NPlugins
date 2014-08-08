/***************************************************************************
 * Project file:    NPlugins - NCuboid - CreativeFlagListener.java         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.CreativeFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerDropItemEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEntityEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CreativeFlagListener extends AbstractListener {

	public CreativeFlagListener(final NCuboid instance) {
		super(instance);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
		final PlayerGridMoveEvent event = (PlayerGridMoveEvent)ext.getBaseEvent();
		if (!ext.isCustomCancelled()) {
			// TODO Handle player GameMode
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
		final PlayerInteractEvent event = (PlayerInteractEvent)ext.getBaseEvent();
		if (event.hasBlock()) {
			if (ext.getClickedRegion() != null && ext.getClickedRegion().getFlag(Flag.CREATIVE)) {
				switch (event.getClickedBlock().getType()) {
					case CHEST:
					case TRAPPED_CHEST:
					case DISPENSER:
					case DROPPER:
					case HOPPER:
					case FURNACE:
					case BURNING_FURNACE:
					case BREWING_STAND:
					case BEACON:
						event.setCancelled(true);
						break;
					default:
						break;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteractEntity(final ExtendedPlayerInteractEntityEvent ext) {
		final PlayerInteractEntityEvent event = (PlayerInteractEntityEvent)ext.getBaseEvent();
		if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.CREATIVE)) {
			final Player p = event.getPlayer();
			switch (event.getRightClicked().getType()) {
				case ITEM_FRAME:
				case MINECART_CHEST:
				case MINECART_FURNACE:
				case MINECART_HOPPER:
					event.setCancelled(!Perms.isAdmin(p));
					break;
				default:
					break;
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDropItem(final ExtendedPlayerDropItemEvent ext) {
		final PlayerDropItemEvent event = (PlayerDropItemEvent)ext.getBaseEvent();
		if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.CREATIVE)) {
			event.setCancelled(true);
		}
	}
}
