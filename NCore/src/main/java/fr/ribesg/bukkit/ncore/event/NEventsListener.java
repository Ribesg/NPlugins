/***************************************************************************
 * Project file:    NPlugins - NCore - NEventsListener.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.event.NEventsListener           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.event;
import fr.ribesg.bukkit.ncore.NCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NEventsListener implements Listener {

	private final NCore plugin;

	public NEventsListener(final NCore instance) {
		plugin = instance;
	}

	/**
	 * Throws a PlayerJoinedEvent AFTER a Player joined the game.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.getPluginManager().callEvent(new PlayerJoinedEvent(event.getPlayer()));
			}
		}, 1L);
	}

	/**
	 * Throws a PlayerGridMoveEvent if a Player change block-location
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final Location from = event.getFrom();
		final Location to = event.getTo();
		if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
			final PlayerGridMoveEvent gridMoveEvent = new PlayerGridMoveEvent(player, from, to);
			gridMoveEvent.setCancelled(event.isCancelled());
			Bukkit.getPluginManager().callEvent(gridMoveEvent);
			event.setCancelled(gridMoveEvent.isCancelled());
			event.setFrom(gridMoveEvent.getFrom());
			event.setTo(gridMoveEvent.getTo());
		}
	}

	/**
	 * Throws a PlayerChunkMoveEvent if a Player change chunk-location
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerGridMove(final PlayerGridMoveEvent event) {
		final Player player = event.getPlayer();
		final Location from = event.getFrom();
		final Location to = event.getTo();
		if (from.getBlockX() / 16 != to.getBlockX() / 16 || from.getBlockZ() / 16 != to.getBlockZ() / 16) {
			final PlayerChunkMoveEvent chunkMoveEvent = new PlayerChunkMoveEvent(player, from, to);
			chunkMoveEvent.setCancelled(event.isCancelled());
			Bukkit.getPluginManager().callEvent(chunkMoveEvent);
			event.setCancelled(chunkMoveEvent.isCancelled());
			event.setFrom(chunkMoveEvent.getFrom());
			event.setTo(chunkMoveEvent.getTo());
		}
	}
}
