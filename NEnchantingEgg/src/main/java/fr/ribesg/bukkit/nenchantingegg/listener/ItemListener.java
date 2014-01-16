/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - ItemListener.java          *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.listener.ItemListener  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.listener;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;
import java.util.Map;

public class ItemListener implements Listener {

	private final NEnchantingEgg    plugin;
	private final Map<Item, String> itemMap;

	public ItemListener(final NEnchantingEgg instance) {
		plugin = instance;
		itemMap = new HashMap<>();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onItemPortal(final EntityPortalEvent event) {
		plugin.entering(getClass(), "onItemPortal");
		if (event.getEntityType() == EntityType.DROPPED_ITEM) {
			plugin.debug("Teleporting entity is a Dropped Item, trying to handle it...");
			final Location loc = event.getEntity().getLocation();
			final Altar altar = plugin.getAltars().get(new ChunkCoord(loc.getChunk()));
			if (altar != null && altar.getState() == AltarState.EGG_PROVIDED && event.getEntity().isValid()) {
				altar.getBuilder().addItem(((Item) event.getEntity()).getItemStack());
				event.setCancelled(true);
				event.getEntity().remove();
				plugin.debug("Entity handled by an Altar. Location=" + altar.getCenterLocation().toString());
			} else {
				plugin.debug("Entity not handled");
			}
		}
		plugin.exiting(getClass(), "onItemPortal");
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
		plugin.entering(getClass(), "onPlayerPickupItem");
		final Item i = event.getItem();
		if (itemMap.containsKey(i)) {
			plugin.debug("Item is handled by an Altar");
			final String playerName = event.getPlayer().getName();
			final String awaitedPlayerName = itemMap.get(i);
			if (!playerName.equals(awaitedPlayerName)) {
				if (plugin.isDebugEnabled()) {
					plugin.debug("Not the right player (" + playerName + "), cancel pickup. Awaited '" + awaitedPlayerName + "'");
				}
				event.setCancelled(true);
			} else {
				plugin.debug("Right player, allow pickup");
				itemMap.remove(i);
				final Altar altar = plugin.getAltars().get(new ChunkCoord(i.getLocation().getChunk()));
				if (altar != null) {
					plugin.getItemProvidedToLockedTransition().doTransition(altar);
				}
			}
		}
		plugin.exiting(getClass(), "onPlayerPickupItem");
	}

	public Map<Item, String> getItemMap() {
		return itemMap;
	}
}
