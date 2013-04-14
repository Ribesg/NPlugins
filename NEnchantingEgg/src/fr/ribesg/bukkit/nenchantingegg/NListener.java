package fr.ribesg.bukkit.nenchantingegg;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transitions.beans.RelativeBlock;

/**
 * TODO
 * 
 * @author Ribesg
 */
public class NListener implements Listener {

    @Getter private final NEnchantingEgg plugin;

    public NListener(final NEnchantingEgg instance) {
        plugin = instance;
    }

    // TODO Remove this
    // DEBUG - This is a test handler, to check that AltarState.getInactiveStateBlocks() is ok
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.DRAGON_EGG) {
            final Location loc = event.getClickedBlock().getLocation();
            for (final RelativeBlock r : AltarState.getInactiveStateBlocks()) {
                final Block b = loc.clone().add(r.getRelativeLocation()).getBlock();
                b.setType(r.getBlockMaterial());
                b.setData(r.getBlockData());
                if (r.needSpecialWork()) {
                    r.doSpecialWork(b);
                }
            }
            event.setCancelled(true);
        }
    }

}
