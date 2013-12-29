/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - EnderDragonListener.java     *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.listener.EnderDragonListener
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.listener;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/**
 * Handles EnderDragons spawn, health regain and death
 *
 * @author Ribesg
 */
public class EnderDragonListener implements Listener {

	/**
	 * Players that did less than threshold % of total damages
	 * have no chance to receive the Egg with custom handling
	 */
	private static final float  threshold = 0.15f;
	private static final Random rand      = new Random();

	private final NTheEndAgain plugin;

	public EnderDragonListener(final NTheEndAgain instance) {
		plugin = instance;
	}

	/**
	 * Handles custom XP handling on ED death
	 *
	 * @param event an EntityDeathEvent
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnderDragonDeath(final EntityDeathEvent event) {
		if (event.getEntityType() == EntityType.ENDER_DRAGON) {
			final World endWorld = event.getEntity().getWorld();
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(endWorld.getName()));
			if (handler != null) {
				final Config config = handler.getConfig();
				switch (config.getEdExpHandling()) {
					case 0:
						event.setDroppedExp(config.getEdExpReward());
						break;
					case 1:
						event.setDroppedExp(0);
						final HashMap<String, Double> dmgMap;
						try {
							dmgMap = new HashMap<>(handler.getDragons().get(event.getEntity().getUniqueId()));
						} catch (final NullPointerException e) {
							return;
						}

						// We ignore offline players
						final Iterator<Entry<String, Double>> it = dmgMap.entrySet().iterator();
						Entry<String, Double> e;
						while (it.hasNext()) {
							e = it.next();
							if (plugin.getServer().getPlayerExact(e.getKey()) == null) {
								it.remove();
							}
						}

						// Get total damages done to the ED by Online players
						long totalDamages = 0;
						for (final double v : dmgMap.values()) {
							totalDamages += v;
						}

						// Give exp to players
						for (final Entry<String, Double> entry : dmgMap.entrySet()) {
							final Player p = plugin.getServer().getPlayerExact(entry.getKey());
							final int reward = (int) (config.getEdExpReward() * entry.getValue() / totalDamages);
							p.giveExp(reward);
							plugin.sendMessage(p, MessageId.theEndAgain_receivedXP, Integer.toString(reward));
						}
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	 * Handles:
	 * - Portal creation
	 * - Custom Egg drop handling
	 * - Cleaning the dead EnderDragon
	 *
	 * @param event an EntityCreatePortalEvent
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnderDragonCreatePortal(final EntityCreatePortalEvent event) {
		if (event.getEntityType() == EntityType.ENDER_DRAGON) {
			final World endWorld = event.getEntity().getWorld();
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(endWorld.getName()));
			if (handler != null) {
				final Config config = handler.getConfig();
				final int pH = config.getEdPortalSpawn();
				final int eH = config.getEdEggHandling();
				final Location deathLocation = event.getEntity().getLocation();

                /*
                 * Explanation of algorithm:
                 *
                 * Variables:
                 * - pH is the portalHandling configuration value
                 * - eH is the eggHandling configuration value
                 *
                 * "Things" to do:
                 * - 1a = Cancel Egg spawn (on the portal)
                 * - 1b = Cancel non-Egg portal blocks spawn
                 * - 2  = Distribute Egg correctly, based on Damages done
                 * - 3  = Cancel the Event, it's better to use this when we just want
                 *        no portal at all than setting everything to air later.
                 *
                 * Notes:
                 * - 1a and 1b are "things" that should be done at the same time
                 *
                 *         +-------+------+------+
                 *         | pH=0  | pH=1 | pH=2 |
                 *  +------+-------+------+------+
                 *  | eH=0 |   -   |  1b  |  3   |
                 *  | eH=1 | 1a, 2 | 2, 3 | 2, 3 |
                 *  +------+-------+------+------+
                 */

				// (1a)
				final boolean cancelEgg = pH == 0 && eH == 1 || event.isCancelled();

				// (1b)
				final boolean cancelPortalBlocks = pH == 1 && eH == 0 || event.isCancelled();

				// (2)
				final boolean customEggHandling = eH == 1;

				// (3)
				final boolean cancelEvent = pH == 1 && eH == 1 || pH == 2;

				// 1a & 1b
				if (cancelEgg || cancelPortalBlocks) {
					// Change block types accordingly
					BlockState eggBlock = null;
					for (final BlockState b : event.getBlocks()) {
						if (b.getType() == Material.DRAGON_EGG) {
							if (cancelEgg) {
								b.setType(Material.AIR);
							}
							eggBlock = b;
						} else if (cancelPortalBlocks) {
							b.setType(Material.AIR);
						}
					}

					// Refresh chunks to prevent client-side glitch
					if (eggBlock != null) {
						final Chunk c = eggBlock.getChunk();
						for (int x = c.getX() - 1; x <= c.getX() + 1; x++) {
							for (int z = c.getZ() - 1; z <= c.getZ() + 1; z++) {
								c.getWorld().refreshChunk(x, z);
							}
						}
					}
				}

				// 2
				if (customEggHandling) {
					// % of total damages done to the ED ; Player name
					TreeMap<Float, String> ratioMap = new TreeMap<>();
					long totalDamages = 0;
					for (final Entry<String, Double> e : handler.getDragons().get(event.getEntity().getUniqueId()).entrySet()) {
						totalDamages += e.getValue();
					}
					for (final Entry<String, Double> e : handler.getDragons().get(event.getEntity().getUniqueId()).entrySet()) {
						ratioMap.put((float) (e.getValue() / (double) totalDamages), e.getKey());
					}

					// Remove entries for Players whom done less damages than threshold
					final Iterator<Entry<Float, String>> it = ratioMap.entrySet().iterator();
					while (it.hasNext()) {
						final Entry<Float, String> e = it.next();
						if (e.getKey() <= threshold) {
							it.remove();
						}
					}

					// Update ratio according to removed parts of total (was 1 obviously)
					float remainingRatioTotal = 0f;
					for (final float f : ratioMap.keySet()) {
						// Computing new total (should be <=1)
						remainingRatioTotal += f;
					}

					// Now update what part of the new total damages each player did
					if (remainingRatioTotal != 1) {
						final TreeMap<Float, String> newRatioMap = new TreeMap<>();
						for (final Entry<Float, String> e : ratioMap.entrySet()) {
							newRatioMap.put(e.getKey() * 1 / remainingRatioTotal, e.getValue());
						}
						ratioMap = newRatioMap;
					}

					// Now we will take a random player, the best fighter has the best chance to be choosen
					float rand = new Random().nextFloat();
					String playerName = null;
					for (final Entry<Float, String> e : ratioMap.entrySet()) {
						if (rand < e.getKey()) {
							playerName = e.getValue();
							break;
						}
						rand -= e.getKey();
					}

					// And now we give him a Dragon Egg
					if (playerName == null) {
						// Security
						endWorld.dropItem(deathLocation, new ItemStack(Material.DRAGON_EGG));
					} else {
						final Player p = Bukkit.getServer().getPlayerExact(playerName);
						if (p == null) {
							// Security
							endWorld.dropItem(deathLocation, new ItemStack(Material.DRAGON_EGG));
						} else {
							// Try to give the Egg
							final HashMap<Integer, ItemStack> notGiven = p.getInventory().addItem(new ItemStack(Material.DRAGON_EGG));
							if (notGiven.size() > 0) {
								// Inventory full, drop the egg at Player's foot
								p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.DRAGON_EGG));
								plugin.sendMessage(p, MessageId.theEndAgain_droppedDragonEgg);
							} else {
								plugin.sendMessage(p, MessageId.theEndAgain_receivedDragonEgg);
							}
						}
					}
				}

				// 3
				if (!event.isCancelled()) {
					event.setCancelled(cancelEvent);
				}

				// Forget about this dragon
				handler.getDragons().remove(event.getEntity().getUniqueId());
				handler.getLoadedDragons().remove(event.getEntity().getUniqueId());

				// Handle on-ED-death regen/respawn
				boolean willRespawn = false;
				if (config.getRespawnType() == 1) {
					willRespawn = true;
				} else if (config.getRespawnType() == 2 && handler.getNumberOfAliveEnderDragons() == 0) {
					willRespawn = true;
				}
				if (willRespawn) {
					handler.getRespawnHandler().respawnLater();
				}
			}
		}
	}

	/**
	 * - Prevents natural spawn of EnderDragon
	 * - Prevents too many Dragons in the End world
	 * - Handle custom health and take care of this new Dragon
	 *
	 * @param event a CreatureSpawnEvent
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEnderDragonSpawn(final CreatureSpawnEvent event) {
		if (event.getEntityType() == EntityType.ENDER_DRAGON) {
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(event.getLocation().getWorld().getName()));
			if (handler != null) {
				if (handler.getNumberOfAliveEnderDragons() >= handler.getConfig().getRespawnNumber()) {
					event.setCancelled(true);
				} else {
					if (event.getSpawnReason() != SpawnReason.CUSTOM && event.getSpawnReason() != SpawnReason.SPAWNER_EGG) {
						event.setCancelled(true);
					} else {
						if (!handler.getDragons().containsKey(event.getEntity().getUniqueId())) {
							handler.getDragons().put(event.getEntity().getUniqueId(), new HashMap<String, Double>());
							event.getEntity().setMaxHealth(handler.getConfig().getEdHealth());
							event.getEntity().setHealth(event.getEntity().getMaxHealth());
						}
						handler.getLoadedDragons().add(event.getEntity().getUniqueId());
					}
				}
			}
		}
	}

	/**
	 * Handle EnderDragon regen
	 *
	 * @param event an EntityRegainHealthEvent
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEnderDragonRegainHealth(final EntityRegainHealthEvent event) {
		if (event.getEntityType() == EntityType.ENDER_DRAGON &&
		    event.getRegainReason() == EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL) {
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(event.getEntity()
			                                                                                    .getLocation()
			                                                                                    .getWorld()
			                                                                                    .getName()));
			if (handler != null) {
				final float rate = handler.getConfig().getEcHealthRegainRate();
				if (rate < 1.0) {
					if (rand.nextFloat() >= rate) {
						event.setCancelled(true);
					}
				} else if (rate > 1.0) {
					event.setAmount((int) (rate * event.getAmount()));
				}
			}
		}
	}
}
