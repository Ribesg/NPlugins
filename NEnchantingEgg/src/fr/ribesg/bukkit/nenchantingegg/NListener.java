package fr.ribesg.bukkit.nenchantingegg;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.ActiveToEggProvidedTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.InactiveToActiveTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;

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
    // DEBUG - This is a test handler
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.DRAGON_EGG) {
            final Location loc = event.getClickedBlock().getLocation();
            final ChunkCoord coord = new ChunkCoord(loc.getChunk());
            Altar altar = plugin.getAltarMap().get(coord);
            if (altar == null || !altar.getCenterLocation().equals(loc)) {
                buildAltar(loc);
                event.setCancelled(true);
                altar = new Altar(loc);
                for (final ChunkCoord c : altar.getChunks()) {
                    plugin.getAltarMap().put(c, altar);
                }
            } else if (altar.getState() == AltarState.INACTIVE) {
                InactiveToActiveTransition.getInstance().doTransition(altar);
                event.setCancelled(true);
            } else if (altar.getState() == AltarState.ACTIVE) {
                ActiveToEggProvidedTransition.getInstance().doTransition(altar);
                event.setCancelled(true);
            } else {
                buildAltar(loc);
                altar.setState(AltarState.INACTIVE);
                event.setCancelled(true);
            }
        }
    }

    private void buildAltar(final Location loc) {
        for (final RelativeBlock r : AltarState.getInactiveStateBlocks()) {
            final Block b = loc.clone().add(r.getRelativeLocation()).getBlock();
            b.setType(r.getBlockMaterial());
            b.setData(r.getBlockData());
            if (r.needSpecialWork()) {
                r.doSpecialWork(b);
            }
        }
    }

}
