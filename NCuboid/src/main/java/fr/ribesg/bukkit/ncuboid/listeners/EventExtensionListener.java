package fr.ribesg.bukkit.ncuboid.listeners;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.events.extensions.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventExtensionListener extends AbstractListener {

	public EventExtensionListener(final NCuboid instance) {
		super(instance);
	}

	// PlayerMoveEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerMove(final PlayerMoveEvent event) {
		final Location from = event.getFrom(), to = event.getTo();
		if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
			Bukkit.getPluginManager().callEvent(new ExtendedPlayerMoveEvent(getPlugin().getDb(), event));
		}
	}

	// PlayerTeleportEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		final Location from = event.getFrom(), to = event.getTo();
		if (from.getBlockX() != to.getBlockX() ||
		    from.getBlockY() != to.getBlockY() ||
		    from.getBlockZ() != to.getBlockZ() ||
		    !from.getWorld().getName().equals(to.getWorld().getName())) {
			Bukkit.getPluginManager().callEvent(new ExtendedPlayerTeleportEvent(getPlugin().getDb(), event));
		}
	}

	// PlayerInteractEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedPlayerInteractEvent(getPlugin().getDb(), event));
	}

	// PlayerInteractEntityEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEntityEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedPlayerInteractEntityEvent(getPlugin().getDb(), event));
	}

	// HangingBreakEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onHangingBreak(final HangingBreakEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedHangingBreakEvent(getPlugin().getDb(), event));
	}

	// EntityExplodeEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityExplode(final EntityExplodeEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedEntityExplodeEvent(getPlugin().getDb(), event));
	}

	// PlayerDropItemEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDropItem(final PlayerDropItemEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedPlayerDropItemEvent(getPlugin().getDb(), event));
	}

	// EntityDamageEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedEntityDamageEvent(getPlugin().getDb(), event));
	}

	// EntityInteractEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityInteract(final EntityInteractEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedEntityInteractEvent(getPlugin().getDb(), event));
	}

	// PlayerJoinEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedPlayerJoinEvent(getPlugin().getDb(), event));
	}

	// EntityChangeBlockEvent
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
		Bukkit.getPluginManager().callEvent(new ExtendedEntityChangeBlockEvent(getPlugin().getDb(), event));
	}
}
