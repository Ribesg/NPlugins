/***************************************************************************
 * Project file:    NPlugins - NPlayer - LoggedOutUserHandler.java         *
 * Full Class name: fr.ribesg.bukkit.nplayer.user.LoggedOutUserHandler     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.user;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncore.event.PlayerJoinedEvent;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.TimeUtil;
import fr.ribesg.bukkit.nplayer.NPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LoggedOutUserHandler implements Listener {

	private final NPlayer             plugin;
	private final Map<UUID, Location> loggedOutPlayers;

	public LoggedOutUserHandler(final NPlayer plugin) {
		this.plugin = plugin;
		this.loggedOutPlayers = new HashMap<>();

		Bukkit.getScheduler().runTaskTimer(plugin, new BukkitRunnable() {

			@Override
			public void run() {
				fr.ribesg.bukkit.nplayer.user.LoggedOutUserHandler.this.poisonLoggedOutPlayers();
			}
		}, 20 * 5, 20 * 5);
	}

	public void notifyConnect(final Player player) {
		this.loggedOutPlayers.put(player.getUniqueId(), player.getLocation());
		this.lockPlayer(player);
	}

	public void notifyDisconnect(final Player player) {
		this.unlockPlayer(player);
		this.loggedOutPlayers.remove(player.getUniqueId());
	}

	public void notifyLogin(final Player player) {
		this.loggedOutPlayers.remove(player.getUniqueId());
		this.unlockPlayer(player);
	}

	public void notifyLogout(final Player player) {
		this.loggedOutPlayers.put(player.getUniqueId(), player.getLocation());
		this.lockPlayer(player);
	}

	public void poisonLoggedOutPlayers() {
		for (final UUID id : this.loggedOutPlayers.keySet()) {
			this.lockPlayer(Bukkit.getPlayer(id));
		}
	}

	public void lockPlayer(final Player player) {
		if (player != null) {
			player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect((int)TimeUtil.getInSeconds("1month"), 9));
		}
	}

	public void unlockPlayer(final Player player) {
		if (player != null) {
			player.removePotionEffect(PotionEffectType.BLINDNESS);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoined(final PlayerJoinedEvent event) {
		final Player player = event.getPlayer();
		this.notifyConnect(player);
		final User user = this.plugin.getUserDb().get(player.getUniqueId());
		if (user == null) {
			// Unknown, should /register
			this.plugin.sendMessage(player, MessageId.player_pleaseRegister);
		} else if (user.getLastIp().equals(player.getAddress().getAddress().getHostAddress()) && !user.hasAutoLogout()) {
			// Auto-login
			user.setLoggedIn(true);
			this.notifyLogin(player);
			this.plugin.sendMessage(player, MessageId.player_autoLogged);
		} else {
			// Should /login
			this.plugin.sendMessage(player, MessageId.player_pleaseLogin);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		final User user = this.plugin.getUserDb().get(player.getUniqueId());
		if (user != null && user.hasAutoLogout()) {
			user.setLoggedIn(false);
			this.notifyLogout(player);
		}
		this.notifyDisconnect(player);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerGridMove(final PlayerGridMoveEvent event) {
		final Player player = event.getPlayer();
		final Location from = event.getFrom();
		final Location to = event.getTo();
		if (this.loggedOutPlayers.containsKey(player.getUniqueId())) {
			final double x = from.getBlockX() + 0.5;
			final double y = from.getBlockY();
			final double z = from.getBlockZ() + 0.5;
			final float yaw = to.getYaw();
			final float pitch = to.getPitch();
			event.getPlayer().teleport(new Location(from.getWorld(), x, y, z, yaw, pitch));
			this.lockPlayer(player);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		synchronized (this.loggedOutPlayers) {
			if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId()) &&
		    !event.getMessage().startsWith("/login") &&
		    !event.getMessage().startsWith("/register")) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDropItem(final PlayerDropItemEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerPortal(final PlayerPortalEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInventoryClick(final InventoryClickEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getWhoClicked().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInventoryOpen(final InventoryOpenEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerExpChange(final PlayerExpChangeEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setAmount(0);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerEditBook(final PlayerEditBookEvent event) {
		if (this.loggedOutPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDamage(final EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER && this.loggedOutPlayers.containsKey(event.getEntity().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityTarget(final EntityTargetLivingEntityEvent event) {
		if (event.getTarget().getType() == EntityType.PLAYER && this.loggedOutPlayers.containsKey(event.getTarget().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onFoodLevelChange(final FoodLevelChangeEvent event) {
		if (event.getEntityType() == EntityType.PLAYER && this.loggedOutPlayers.containsKey(event.getEntity().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onRegainHealth(final EntityRegainHealthEvent event) {
		if (event.getEntityType() == EntityType.PLAYER && this.loggedOutPlayers.containsKey(event.getEntity().getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
