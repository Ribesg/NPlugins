/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - PlayerListener.java        *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.listener.PlayerListener*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.listener;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.AltarState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

	private final NEnchantingEgg plugin;

	public PlayerListener(final NEnchantingEgg instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPlaceBlock(final BlockPlaceEvent event) {
		final Block block = event.getBlockPlaced();
		final Material mat = block.getType();
		final Location loc = block.getLocation();
		final ChunkCoord coord;
		final Altar altar;
		switch (mat) {
			case DRAGON_EGG: // Placing a Dragon Egg on the center of an activated Altar
				coord = new ChunkCoord(event.getBlockPlaced().getLocation().getChunk());
				altar = plugin.getAltars().get(coord);
				if (altar != null) {
					if (altar.getState() == AltarState.ACTIVE && altar.isEggPosition(loc)) {
						plugin.getActiveToEggProvidedTransition().doTransition(altar);
						plugin.sendMessage(event.getPlayer(), MessageId.egg_altarEggProvided);
						altar.setPlayerName(event.getPlayer().getName());
					} else if (altar.preventsBlockPlacement(event)) {
						plugin.sendMessage(event.getPlayer(), MessageId.egg_cantPlaceOnAltar);
						event.setCancelled(true);
					}
				}
				break;
			case SKULL: // Creating an Altar by placing the Wither Skull last
				final Player player = event.getPlayer();
				Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

					@Override
					public void run() {
						final Skull skullState = (Skull) block.getState();
						if (skullState.getSkullType() == SkullType.WITHER) {
							// Create the altar, then check if it's valid
							final Altar altar = new Altar(plugin, Altar.getCenterFromSkullLocation(loc));
							final boolean minimumDistanceCheck = plugin.getAltars()
							                                           .canAdd(altar,
							                                                   plugin.getPluginConfig()
							                                                         .getMinimumDistanceBetweenTwoAltars());
							if (altar.isInactiveAltarValid() && minimumDistanceCheck) {
								if (player.isOnline()) {
									plugin.sendMessage(player, MessageId.egg_altarCreated);
								}
								altar.setState(AltarState.INACTIVE);
								plugin.getAltars().add(altar);
								if (MinecraftTime.isNightTime(loc.getWorld().getTime())) {
									plugin.getInactiveToActiveTransition().doTransition(altar);
								}
							} else {
								if (!minimumDistanceCheck) {
									if (player.isOnline()) {
										plugin.sendMessage(player, MessageId.egg_altarTooClose);
									}
								} else {
									// TODO: Don't know if we should do something
								}
								altar.destroy();
							}
						}
					}
				}, 1L);
				break;
			default:
				coord = new ChunkCoord(event.getBlockPlaced().getLocation().getChunk());
				altar = plugin.getAltars().get(coord);
				if (altar != null) {
					if (altar.preventsBlockPlacement(event)) {
						plugin.sendMessage(event.getPlayer(), MessageId.egg_cantPlaceOnAltar);
						event.setCancelled(true);
					}
				}
				break;
		}

	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBreakBlock(final BlockBreakEvent event) {
		final Block block = event.getBlock();
		final Material mat = block.getType();
		final Location loc = block.getLocation();
		final ChunkCoord coord = new ChunkCoord(loc.getChunk());
		final Altar altar = plugin.getAltars().get(coord);
		if (altar != null) {
			switch (mat) {
				case SKULL: // Destroying an Altar by destroying the Wither skull
					if (altar.isSkullPosition(loc)) {
						if (altar.getState() == AltarState.INACTIVE) {
							plugin.sendMessage(event.getPlayer(), MessageId.egg_altarDestroyed);
							altar.destroy();
						} else {
							plugin.sendMessage(event.getPlayer(), MessageId.egg_altarProtectedSkullAtNight);
							event.setCancelled(true);
						}
						break;
					} // else enter default section
				default:
					if (altar.preventsBlockDestruction(event)) {
						plugin.sendMessage(event.getPlayer(), MessageId.egg_altarProtectedBlock);
						event.setCancelled(true);
					}
					break;
			}
		}

	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPortalEnter(final PlayerPortalEvent event) {
		final Location from = event.getFrom();
		final ChunkCoord coord = new ChunkCoord(from.getChunk());
		final Altar altar = plugin.getAltars().get(coord);
		if (altar != null) {
			final Location teleportLocation = altar.getCenterLocation().clone().toBukkitLocation().add(2.5, 1, 0.5);
			teleportLocation.setPitch(6f);
			teleportLocation.setYaw(90f);
			event.getPlayer().teleport(teleportLocation);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.hasBlock() && event.getClickedBlock().getType() == Material.DRAGON_EGG) {
			final Location eggLocation = event.getClickedBlock().getLocation();
			final ChunkCoord chunkCoord = new ChunkCoord(eggLocation.getChunk());
			final Altar altar = plugin.getAltars().get(chunkCoord);
			if (altar != null && altar.isEggPosition(eggLocation)) {
				event.setCancelled(true);
			}
		}
	}
}
