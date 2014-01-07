/***************************************************************************
 * Project file:    NPlugins - NWorld - NListener.java                     *
 * Full Class name: fr.ribesg.bukkit.nworld.NListener                      *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.nworld.world.AdditionalSubWorld;
import fr.ribesg.bukkit.nworld.world.AdditionalWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld;
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.logging.Logger;

/** @author Ribesg */
public class NListener implements Listener {

	private static final Logger LOGGER = Logger.getLogger(NListener.class.getName());

	private final NWorld plugin;

	public NListener(final NWorld instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onEntityUsePortal(final EntityPortalEvent event) {
		final World.Environment from = event.getFrom().getWorld().getEnvironment();
		final World.Environment to = event.getTo().getWorld().getEnvironment();

		// Build a fake TeleportCause based on From and To locations
		final PlayerTeleportEvent.TeleportCause cause;
		switch (event.getFrom().getBlock().getType()) {
			case PORTAL:
				cause = PlayerTeleportEvent.TeleportCause.NETHER_PORTAL;
				break;
			case ENDER_PORTAL:
				cause = PlayerTeleportEvent.TeleportCause.END_PORTAL;
				break;
			default:
				// An entity should not be able to call this event if there's no portal
				cause = PlayerTeleportEvent.TeleportCause.PLUGIN;
				break;
		}

		final PortalEventResult result = handlePortalEvent(event.getFrom(), cause, event.getPortalTravelAgent());
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
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPlayerUsePortal(final PlayerPortalEvent event) {
		final PortalEventResult result = handlePortalEvent(event.getFrom(), event.getCause(), event.getPortalTravelAgent());
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
	}

	/** Little structure used to return 3 values. */
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

	private PortalEventResult handlePortalEvent(final Location fromLocation,
	                                            final PlayerTeleportEvent.TeleportCause teleportCause,
	                                            final TravelAgent portalTravelAgent) {
		// In case of error or other good reasons
		final PortalEventResult cancel = new PortalEventResult(null, false, true);

		final World fromWorld = fromLocation.getWorld();
		final String worldName = fromWorld.getName();
		final World.Environment sourceWorldEnvironment = fromWorld.getEnvironment();
		final GeneralWorld world = plugin.getWorlds().get(worldName);

		if (GeneralWorld.WorldType.isStock(world)) {
			// Do not override any Bukkit behaviour
			return null;
		}

		if (teleportCause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
			if (sourceWorldEnvironment == World.Environment.NORMAL) {
				// NORMAL => NETHER
				final AdditionalWorld normalWorld = (AdditionalWorld) world;
				final AdditionalSubWorld netherWorld = normalWorld.getNetherWorld();
				if (netherWorld == null) {
					return cancel;
				}
				final Location averageDestination = normalToNetherLocation(netherWorld.getBukkitWorld(), fromLocation);
				final Location actualDestination = portalTravelAgent.findOrCreate(averageDestination);
				return new PortalEventResult(actualDestination, true, false);
			} else if (sourceWorldEnvironment == World.Environment.NETHER) {
				// NETHER => NORMAL
				final AdditionalSubWorld netherWorld = (AdditionalSubWorld) world;
				final AdditionalWorld normalWorld = netherWorld.getParentWorld();
				if (normalWorld == null) {
					return cancel;
				}
				final Location averageDestination = netherToNormalLocation(normalWorld.getBukkitWorld(), fromLocation);
				final Location actualDestination = portalTravelAgent.findOrCreate(averageDestination);
				return new PortalEventResult(actualDestination, true, false);
			} else if (sourceWorldEnvironment == World.Environment.THE_END) {
				// END => NETHER
				// Buggy in Vanilla, do not handle and prevent bugs.
				return cancel;
			}
		} else if (teleportCause == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
			if (sourceWorldEnvironment == World.Environment.NORMAL) {
				// NORMAL => END
				final AdditionalWorld normalWorld = (AdditionalWorld) world;
				final AdditionalSubWorld endWorld = normalWorld.getEndWorld();
				if (endWorld == null) {
					return cancel;
				}
				final Location actualDestination = getEndLocation(endWorld.getBukkitWorld());
				portalTravelAgent.createPortal(actualDestination);
				return new PortalEventResult(actualDestination, true, false);
			} else if (sourceWorldEnvironment == World.Environment.NETHER) {
				// NETHER => END (WTF)
				// Not possible in Vanilla, do not handle and prevent eventual bugs.
				return cancel;
			} else if (sourceWorldEnvironment == World.Environment.THE_END) {
				// END => NORMAL
				// Just teleport to spawn
				final AdditionalSubWorld endWorld = (AdditionalSubWorld) world;
				final AdditionalWorld normalWorld = endWorld.getParentWorld();
				if (normalWorld == null) {
					return cancel;
				}
				final Location actualDestination = normalWorld.getSpawnLocation().toBukkitLocation();
				return new PortalEventResult(actualDestination, false, false);
			}
		}
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
		y = Math.min(y, normalWorld.getHighestBlockYAt((int) x, (int) z));

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
