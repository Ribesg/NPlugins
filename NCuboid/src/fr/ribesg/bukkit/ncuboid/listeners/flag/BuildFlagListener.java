package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedHangingBreakEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class BuildFlagListener extends AbstractListener {

    public BuildFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent ext) {
        final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(ext.getBlockClicked().getRelative(ext.getBlockFace()).getLocation());
        if (cuboid == null) {
            return;
        } else if (cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(ext.getPlayer())) {
            ext.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerBucketFill(final PlayerBucketFillEvent ext) {
        final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(ext.getBlockClicked().getLocation());
        if (cuboid == null) {
            return;
        } else if (cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(ext.getPlayer())) {
            ext.setCancelled(true);
        }
    }

    // We don't care if hasBlock()==false, so ignoreCancelled is true
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(final ExtendedPlayerInteractEvent ext) {
        final PlayerInteractEvent event = (PlayerInteractEvent) ext.getBaseEvent();
        if (event.hasBlock()) {
            if (event.hasItem() && event.getItem().getType() == Material.STICK) {
                // Handled in PlayerStickListener
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem()) {
                // Fire or vehicle
                if (event.getItem().getType() == Material.FLINT_AND_STEEL ||
                    event.getItem().getType() == Material.FIREBALL ||
                    event.getItem().getType() == Material.MINECART ||
                    event.getItem().getType() == Material.BOAT) {
                    final GeneralCuboid cuboid = getPlugin().getDb()
                            .getPriorByLoc(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation());
                    if (cuboid != null && cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(event.getPlayer())) {
                        event.setCancelled(true);
                        return;
                    }
                }
                // Disc
                else if (event.getClickedBlock().getType() == Material.JUKEBOX &&
                         event.getItem().getTypeId() >= 2256 &&
                         event.getItem().getTypeId() <= 2267) {
                    if (ext.getCuboid() != null &&
                        ext.getCuboid().getFlag(Flag.BUILD) &&
                        !ext.getCuboid().isAllowedPlayer(event.getPlayer())) {
                        event.setCancelled(true);
                        return;
                    }
                }
                // Repeater, Comparator
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.DIODE_BLOCK_OFF ||
                                                                      event.getClickedBlock().getType() == Material.DIODE_BLOCK_ON ||
                                                                      event.getClickedBlock().getType() ==
                                                                      Material.REDSTONE_COMPARATOR_OFF ||
                                                                      event.getClickedBlock().getType() ==
                                                                      Material.REDSTONE_COMPARATOR_ON)) {
                    if (ext.getCuboid() != null &&
                        ext.getCuboid().getFlag(Flag.BUILD) &&
                        !ext.getCuboid().isAllowedPlayer(event.getPlayer())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent ext) {
        final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(ext.getBlock().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(ext.getPlayer())) {
            ext.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent ext) {
        final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(ext.getBlock().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(ext.getPlayer())) {
            ext.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockDamage(final BlockDamageEvent ext) {
        final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(ext.getBlock().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(ext.getPlayer())) {
            ext.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHangingBreakByEntity(final ExtendedHangingBreakEvent ext) {
        if (ext.getBaseEvent() instanceof HangingBreakByEntityEvent) {
            final HangingBreakByEntityEvent event = (HangingBreakByEntityEvent) ext.getBaseEvent();
            if (event.getRemover().getType() == EntityType.PLAYER) {
                final Player player = (Player) event.getRemover();
                if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.BUILD) && !ext.getCuboid().isAllowedPlayer(player)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHangingPlace(final HangingPlaceEvent ext) {
        final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(ext.getEntity().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(ext.getPlayer())) {
            ext.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onVehicleDestroy(final VehicleDestroyEvent event) {
        if (event.getAttacker() != null && event.getAttacker().getType() == EntityType.PLAYER) {
            final Player player = (Player) event.getAttacker();
            final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(event.getVehicle().getLocation());
            if (cuboid != null && cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(player)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStructureGrow(final StructureGrowEvent ext) {
        if (ext.isFromBonemeal()) {
            final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(ext.getLocation());
            if (cuboid != null && cuboid.getFlag(Flag.BUILD) && !cuboid.isAllowedPlayer(ext.getPlayer())) {
                ext.setCancelled(true);
                return;
            }
        }
    }
}
