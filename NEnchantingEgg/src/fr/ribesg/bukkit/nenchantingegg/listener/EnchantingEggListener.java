package fr.ribesg.bukkit.nenchantingegg.listener;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * TODO
 *
 * @author Ribesg
 */
public class EnchantingEggListener implements Listener {

    private final NEnchantingEgg plugin;

    public EnchantingEggListener(final NEnchantingEgg instance) {
        plugin = instance;
    }

    // TODO Remove this
    // DEBUG - This is a test handler
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.ENDER_PORTAL_FRAME) {
            final Location loc = event.getClickedBlock().getLocation();
            final ChunkCoord coord = new ChunkCoord(loc.getChunk());
            Altar altar = plugin.getAltars().get(coord);
            if (altar == null || !altar.getCenterLocation().equals(loc)) {
                buildAltar(loc);
                event.setCancelled(true);
                altar = new Altar(plugin, loc);
                plugin.getAltars().add(altar);
                altar.setState(AltarState.INACTIVE);
            } else if (altar.getState() == AltarState.INACTIVE) {
                plugin.getInactiveToActiveTransition().doTransition(altar);
                event.setCancelled(true);
            } else if (altar.getState() == AltarState.ACTIVE) {
                plugin.getActiveToEggProvidedTransition().doTransition(altar);
                event.setCancelled(true);
            } else if (altar.getState() == AltarState.EGG_PROVIDED) {
                plugin.getEggProvidedToItemProvidedTransition().doTransition(altar);
                event.setCancelled(true);
            } else if (altar.getState() == AltarState.ITEM_PROVIDED) {
                plugin.getItemProvidedToLockedTransition().doTransition(altar);
                event.setCancelled(true);
            } else {
                buildAltar(loc);
                altar.setState(AltarState.INACTIVE);
                event.setCancelled(true);
            }
        }
    }

    // TODO Remove this (or not? Command?)
    // DEBUG - This is a test method
    private void buildAltar(final Location loc) {
        for (final RelativeBlock r : AltarState.getInactiveStateBlocks()) {
            final Block b = loc.clone().add(r.getRelativeLocation()).getBlock();
            b.setType(r.getBlockMaterial());
            b.setData(r.getBlockData());
            if (r.needAdditionalData()) {
                r.setAdditionalData(b);
            }
        }
    }

}
