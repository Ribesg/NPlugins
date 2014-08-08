/***************************************************************************
 * Project file:    NPlugins - NWorld - NListener.java                     *
 * Full Class name: fr.ribesg.bukkit.nworld.NListener                      *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nworld.world.AdditionalSubWorld;
import fr.ribesg.bukkit.nworld.world.AdditionalWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld.WorldType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TravelAgent;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Ribesg
 */
public class NListener implements Listener {

    private static final BlockFace[] blockFaces = {
            // Where we should find it
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.UP,
            BlockFace.DOWN,

            // Let's check those too
            BlockFace.NORTH_EAST,
            BlockFace.NORTH_WEST,
            BlockFace.NORTH_NORTH_EAST,
            BlockFace.NORTH_NORTH_WEST,
            BlockFace.SOUTH_EAST,
            BlockFace.SOUTH_WEST,
            BlockFace.SOUTH_SOUTH_EAST,
            BlockFace.SOUTH_SOUTH_WEST,
            BlockFace.EAST_NORTH_EAST,
            BlockFace.EAST_SOUTH_EAST,
            BlockFace.WEST_NORTH_WEST,
            BlockFace.WEST_SOUTH_WEST
    };

    private final NWorld plugin;

    public NListener(final NWorld instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onEntityUsePortal(final EntityPortalEvent event) {
        this.plugin.entering(this.getClass(), "onEntityUsePortal", "entity=" + event.getEntityType() + ";from=" + NLocation.toString(event.getFrom()) + ";to=" + NLocation.toString(event.getTo()));

        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            this.plugin.exiting(this.getClass(), "onEntityUsePortal", "EnderDragon should really stay in the End!");
            event.setCancelled(true);
            return;
        }

        final GeneralWorld world = this.plugin.getWorlds().get(event.getFrom().getWorld().getName());
        if (GeneralWorld.WorldType.isStock(world)) {
            this.plugin.exiting(this.getClass(), "onEntityUsePortal", "Stock world!");
            return;
        }

        // Build a fake TeleportCause based on From and To locations
        PlayerTeleportEvent.TeleportCause cause = null;
        final Block block = event.getFrom().getBlock();
        switch (block.getType()) {
            case PORTAL:
                cause = PlayerTeleportEvent.TeleportCause.NETHER_PORTAL;
                break;
            case ENDER_PORTAL:
                cause = PlayerTeleportEvent.TeleportCause.END_PORTAL;
                break;
            default:
                this.plugin.debug("Strange block found: " + block.getType() + ", trying to find a portal block near the Location");
                for (final BlockFace face : blockFaces) {
                    if (block.getRelative(face).getType() == Material.PORTAL) {
                        cause = PlayerTeleportEvent.TeleportCause.NETHER_PORTAL;
                        this.plugin.debug("Found a Nether Portal block at " + NLocation.toString(block.getRelative(face).getLocation()));
                        break;
                    } else if (block.getRelative(face).getType() == Material.ENDER_PORTAL) {
                        cause = PlayerTeleportEvent.TeleportCause.END_PORTAL;
                        this.plugin.debug("Found an End Portal block at " + NLocation.toString(block.getRelative(face).getLocation()));
                        break;
                    }
                }
                if (cause == null) {
                    cause = PlayerTeleportEvent.TeleportCause.PLUGIN;
                }
                break;
        }

        if (world.getType() == WorldType.ADDITIONAL) {
            final AdditionalWorld additionalWorld = (AdditionalWorld)world;
            if (cause == TeleportCause.NETHER_PORTAL && !additionalWorld.hasNether()) {
                event.setCancelled(true);
                this.plugin.exiting(this.getClass(), "onEntityUsePortal", "doesn't have required subworld (nether)");
                return;
            } else if (cause == TeleportCause.END_PORTAL && !additionalWorld.hasEnd()) {
                event.setCancelled(true);
                this.plugin.exiting(this.getClass(), "onEntityUsePortal", "doesn't have required subworld (end)");
                return;
            }
        }

        final PortalEventResult result = this.handlePortalEvent(event.getFrom(), cause, event.getPortalTravelAgent());
        if (result == null) {
            this.plugin.exiting(this.getClass(), "onEntityUsePortal", "result is null");
            return;
        }
        if (result.destination != null) {
            event.setTo(result.destination);
        }
        if (result.useTravelAgent) {
            event.useTravelAgent(true);
        }
        if (result.cancelEvent) {
            event.setCancelled(true);
        }

        this.plugin.exiting(this.getClass(), "onEntityUsePortal");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerUsePortal(final PlayerPortalEvent event) {
        this.plugin.entering(this.getClass(), "onPlayerUsePortal", "player=" + event.getPlayer().getName() + ";from=" + NLocation.toString(event.getFrom()) + ";to=" + NLocation.toString(event.getTo()));

        final GeneralWorld world = this.plugin.getWorlds().get(event.getFrom().getWorld().getName());
        if (GeneralWorld.WorldType.isStock(world)) {
            return;
        }

        if (world.getType() == WorldType.ADDITIONAL) {
            final AdditionalWorld additionalWorld = (AdditionalWorld)world;
            if (event.getCause() == TeleportCause.NETHER_PORTAL && !additionalWorld.hasNether()) {
                event.setCancelled(true);
                this.plugin.exiting(this.getClass(), "onPlayerUsePortal", "doesn't have required subworld (nether)");
                return;
            } else if (event.getCause() == TeleportCause.END_PORTAL && !additionalWorld.hasEnd()) {
                event.setCancelled(true);
                this.plugin.exiting(this.getClass(), "onPlayerUsePortal", "doesn't have required subworld (end)");
                return;
            }
        }

        final PortalEventResult result = this.handlePortalEvent(event.getFrom(), event.getCause(), event.getPortalTravelAgent());
        if (result == null) {
            return;
        }
        if (result.destination != null) {
            event.setTo(result.destination);
        }
        if (result.useTravelAgent) {
            event.useTravelAgent(true);
        }
        if (result.cancelEvent) {
            event.setCancelled(true);
        }

        this.plugin.exiting(this.getClass(), "onPlayerUsePortal");
    }

    /**
     * Little structure used to return 3 values.
     */
    private class PortalEventResult {

        public final Location destination;
        public final boolean  useTravelAgent;
        public final boolean  cancelEvent;

        private PortalEventResult(final Location destination, final boolean useTravelAgent, final boolean cancelEvent) {
            this.destination = destination;
            this.useTravelAgent = useTravelAgent;
            this.cancelEvent = cancelEvent;
        }
    }

    private PortalEventResult handlePortalEvent(final Location fromLocation, final PlayerTeleportEvent.TeleportCause teleportCause, final TravelAgent portalTravelAgent) {
        this.plugin.entering(this.getClass(), "handlePortalEvent", "fromLocation=" + NLocation.toString(fromLocation) + ";teleportCause=" + teleportCause);

        // In case of error or other good reasons
        final PortalEventResult cancel = new PortalEventResult(null, false, true);

        final World fromWorld = fromLocation.getWorld();
        final String worldName = fromWorld.getName();
        final World.Environment sourceWorldEnvironment = fromWorld.getEnvironment();
        final GeneralWorld world = this.plugin.getWorlds().get(worldName);

        if (GeneralWorld.WorldType.isStock(world)) {
            // Do not override any Bukkit behaviour
            this.plugin.exiting(this.getClass(), "handlePortalEvent", "Source is stock world");
            return null;
        }

        if (teleportCause == TeleportCause.NETHER_PORTAL) {
            if (sourceWorldEnvironment == Environment.NORMAL) {
                // NORMAL => NETHER
                final AdditionalWorld normalWorld = (AdditionalWorld)world;
                final AdditionalSubWorld netherWorld = normalWorld.getNetherWorld();
                if (netherWorld == null) {
                    this.plugin.exiting(this.getClass(), "handlePortalEvent", "NORMAL => NETHER - cancel");
                    return cancel;
                }
                final Location averageDestination = this.normalToNetherLocation(netherWorld.getBukkitWorld(), fromLocation);
                final Location actualDestination = portalTravelAgent.findOrCreate(averageDestination);
                return new PortalEventResult(actualDestination, true, false);
            } else if (sourceWorldEnvironment == Environment.NETHER) {
                // NETHER => NORMAL
                final AdditionalSubWorld netherWorld = (AdditionalSubWorld)world;
                final AdditionalWorld normalWorld = netherWorld.getParentWorld();
                if (normalWorld == null) {
                    this.plugin.exiting(this.getClass(), "handlePortalEvent", "NETHER => NORMAL - cancel");
                    return cancel;
                }
                final Location averageDestination = this.netherToNormalLocation(normalWorld.getBukkitWorld(), fromLocation);
                final Location actualDestination = portalTravelAgent.findOrCreate(averageDestination);
                return new PortalEventResult(actualDestination, true, false);
            } else if (sourceWorldEnvironment == Environment.THE_END) {
                // END => NETHER
                // Buggy in Vanilla, do not handle and prevent bugs.
                this.plugin.exiting(this.getClass(), "handlePortalEvent", "END => NETHER - cancel");
                return cancel;
            }
        } else if (teleportCause == TeleportCause.END_PORTAL) {
            if (sourceWorldEnvironment == Environment.NORMAL) {
                // NORMAL => END
                final AdditionalWorld normalWorld = (AdditionalWorld)world;
                final AdditionalSubWorld endWorld = normalWorld.getEndWorld();
                if (endWorld == null) {
                    this.plugin.exiting(this.getClass(), "handlePortalEvent", "NORMAL => END - cancel");
                    return cancel;
                }
                final Location actualDestination = this.getEndLocation(endWorld.getBukkitWorld());
                portalTravelAgent.createPortal(actualDestination);
                return new PortalEventResult(actualDestination, true, false);
            } else if (sourceWorldEnvironment == Environment.NETHER) {
                // NETHER => END (WTF)
                // Not possible in Vanilla, do not handle and prevent eventual bugs.
                this.plugin.exiting(this.getClass(), "handlePortalEvent", "NETHER => END - cancel");
                return cancel;
            } else if (sourceWorldEnvironment == Environment.THE_END) {
                // END => NORMAL
                // Just teleport to spawn
                final AdditionalSubWorld endWorld = (AdditionalSubWorld)world;
                final AdditionalWorld normalWorld = endWorld.getParentWorld();
                if (normalWorld == null) {
                    this.plugin.exiting(this.getClass(), "handlePortalEvent", "END => NORMAL - cancel");
                    return cancel;
                }
                final Location actualDestination = normalWorld.getSpawnLocation().toBukkitLocation();
                this.plugin.exiting(this.getClass(), "handlePortalEvent");
                return new PortalEventResult(actualDestination, false, false);
            }
        }

        this.plugin.exiting(this.getClass(), "handlePortalEvent", "Not handled");
        return null;
    }

    /**
     * Given a Location in a Normal World and a Nether World, this builds the
     * corresponding Location in the Nether world.
     *
     * @param netherWorld      The destination World
     * @param originalLocation The source Location
     *
     * @return The destination Location
     */
    private Location normalToNetherLocation(final World netherWorld, final Location originalLocation) {
        if (originalLocation == null || netherWorld == null) {
            return null;
        }

        // Get original coordinates
        double x = originalLocation.getX();
        double y = originalLocation.getY();
        double z = originalLocation.getZ();

        // Transform them
        x /= 8;
        y /= 2;
        z /= 8;

        // Create the Location and return it
        return new Location(netherWorld, x, y, z);
    }

    /**
     * Given a Location in a Nether World and a Normal World, this builds the
     * corresponding Location in the Normal world.
     *
     * @param normalWorld      The destination World
     * @param originalLocation The source Location
     *
     * @return The destination Location
     */
    private Location netherToNormalLocation(final World normalWorld, final Location originalLocation) {
        if (normalWorld == null || originalLocation == null) {
            return null;
        }

        // Get original coordinates
        double x = originalLocation.getX();
        double y = originalLocation.getY();
        double z = originalLocation.getZ();

        // Transform them
        x *= 8;
        y *= 2;
        z *= 8;

        // Try to be on the ground !
        y = Math.min(y, normalWorld.getHighestBlockYAt((int)x, (int)z));

        // Create the Location and return it
        return new Location(normalWorld, x, y, z);
    }

    /**
     * Builds a new spawn Location for this End world
     *
     * @param endWorld The End World
     *
     * @return The spawn / warp Location
     */
    private Location getEndLocation(final World endWorld) {
        return endWorld == null ? null : new Location(endWorld, 100, 50, 0);
    }
}
