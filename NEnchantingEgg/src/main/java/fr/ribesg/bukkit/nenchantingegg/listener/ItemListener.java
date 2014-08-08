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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemListener implements Listener {

	private final NEnchantingEgg    plugin;
	private final Map<Item, String> itemMap;

	public ItemListener(final NEnchantingEgg instance) {
		this.plugin = instance;
		this.itemMap = new HashMap<>();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onItemPortal(final EntityPortalEvent event) {
		this.plugin.entering(this.getClass(), "onItemPortal");
		final Location loc = event.getEntity().getLocation();
		final Altar altar = this.plugin.getAltars().get(new ChunkCoord(loc.getChunk()));
		if (altar != null && altar.getState() == AltarState.EGG_PROVIDED && event.getEntity().isValid()) {
			this.plugin.debug("Teleportation caused by an Altar.");
			if (event.getEntityType() == EntityType.DROPPED_ITEM) {
				this.plugin.debug("Teleporting entity is a Dropped Item, handling it...");
				altar.getBuilder().addItem(((Item)event.getEntity()).getItemStack());
				event.getEntity().remove();
				this.plugin.debug("Entity handled by an Altar. Location=" + altar.getCenterLocation());
			}
			this.plugin.debug("Entity not an item, not handled");
			event.setCancelled(true);
		} else {
			this.plugin.debug("Entity not handled");
		}
		this.plugin.exiting(this.getClass(), "onItemPortal");
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
		this.plugin.entering(this.getClass(), "onPlayerPickupItem");
		final Item i = event.getItem();
		if (this.itemMap.containsKey(i)) {
			this.plugin.debug("Item is handled by an Altar");
			final String playerName = event.getPlayer().getName();
			final String awaitedPlayerName = this.itemMap.get(i);
			if (!playerName.equals(awaitedPlayerName)) {
				if (this.plugin.isDebugEnabled()) {
					this.plugin.debug("Not the right player (" + playerName + "), cancel pickup. Awaited '" + awaitedPlayerName + '\'');
				}
				event.setCancelled(true);
			} else {
				this.plugin.debug("Right player, allow pickup");
				this.itemMap.remove(i);
				final Altar altar = this.plugin.getAltars().get(new ChunkCoord(i.getLocation().getChunk()));
				if (altar != null) {
					this.plugin.getItemProvidedToLockedTransition().doTransition(altar);
				}
			}
		}
		this.plugin.exiting(this.getClass(), "onPlayerPickupItem");
	}

	public Map<Item, String> getItemMap() {
		return this.itemMap;
	}
}
