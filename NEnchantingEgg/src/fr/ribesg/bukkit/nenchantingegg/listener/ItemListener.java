package fr.ribesg.bukkit.nenchantingegg.listener;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
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
        if (event.getEntityType() == EntityType.DROPPED_ITEM) {
            final Location loc = event.getEntity().getLocation();
            final Altar altar = plugin.getAltars().get(new ChunkCoord(loc.getChunk()));
            if (altar != null && altar.getState() == AltarState.EGG_PROVIDED && event.getEntity().isValid()) {
                altar.getBuilder().addItem(((Item) event.getEntity()).getItemStack());
                event.setCancelled(true);
                event.getEntity().remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        final Item i = event.getItem();
        if (itemMap.containsKey(i)) {
            final String awaitedPlayerName = itemMap.get(i);
            if (!event.getPlayer().getName().equals(awaitedPlayerName)) {
                event.setCancelled(true);
            } else {
                itemMap.remove(i);
                final Altar altar = plugin.getAltars().get(new ChunkCoord(i.getLocation().getChunk()));
                if (altar != null) {
                    plugin.getItemProvidedToLockedTransition().doTransition(altar);
                }
            }
        }
    }

    public Map<Item, String> getItemMap() {
        return itemMap;
    }
}
