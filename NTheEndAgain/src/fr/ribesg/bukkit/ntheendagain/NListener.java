package fr.ribesg.bukkit.ntheendagain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

public class NListener implements Listener {

    /**
     * Players that did less than threshold % of total damages
     * have no chance to receive the Egg with custom handling
     */
    private final static float threshold = 0.15f;

    private final NTheEndAgain plugin;

    public NListener(final NTheEndAgain instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderDragonDeath(final EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            final World endWorld = event.getEntity().getWorld();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(endWorld.getName()));
            if (handler == null) {
                return;
            } else {
                final Config config = handler.getConfig();
                switch (config.getXpHandling()) {
                    case 0:
                        event.setDroppedExp(config.getXpReward());
                        break;
                    case 1:
                        event.setDroppedExp(0);
                        HashMap<String, Long> dmgMap = null;
                        try {
                            dmgMap = new HashMap<String, Long>(handler.getDragons().get(event.getEntity().getUniqueId()));
                        } catch (final NullPointerException e) {
                            return;
                        }

                        // We ignore offline players
                        final Iterator<Entry<String, Long>> it = dmgMap.entrySet().iterator();
                        Entry<String, Long> e;
                        while (it.hasNext()) {
                            e = it.next();
                            if (plugin.getServer().getPlayerExact(e.getKey()) == null) {
                                it.remove();
                            }
                        }

                        // Get total damages done to the ED by Online players
                        long totalDamages = 0;
                        for (final Long v : dmgMap.values()) {
                            totalDamages += v;
                        }

                        // Give exp to players
                        for (final Entry<String, Long> entry : dmgMap.entrySet()) {
                            final Player p = plugin.getServer().getPlayerExact(entry.getKey());
                            final int reward = (int) (config.getXpReward() * entry.getValue() / totalDamages);
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        // EnderDragon damaged by Player
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            Player player = null;
            if (event.getDamager().getType() == EntityType.PLAYER) {
                player = (Player) event.getDamager();
            } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter().getType() == EntityType.PLAYER) {
                player = (Player) ((Projectile) event.getDamager()).getShooter();
            } else {
                return;
            }
            final EnderDragon dragon = (EnderDragon) event.getEntity();
            final World endWorld = dragon.getWorld();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(endWorld.getName()));
            if (handler != null) {
                handler.playerHitED(dragon.getUniqueId(), player.getName(), event.getDamage());
            }
        }

        // Player damaged by EnderDragon
        else if (event.getEntityType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.ENDER_DRAGON) {
            final World endWorld = event.getEntity().getWorld();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(endWorld.getName()));
            if (handler != null) {
                event.setDamage(Math.round(event.getDamage() * handler.getConfig().getEnderDragonDamageMultiplier()));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEnderDragonCreatePortal(final EntityCreatePortalEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            final World endWorld = event.getEntity().getWorld();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(endWorld.getName()));
            if (handler != null) {
                final int pH = handler.getConfig().getPortalHandling();
                final int eH = handler.getConfig().getDragonEggHandling();
                final Location deathLocation = event.getEntity().getLocation();

                /* Explanation of algorithm (1a and 1b are likely to be the same loop)
                 *   if (pH == 0 && eH == 0) {
                 *       Nothing to do
                 *   } else if (pH == 0 && eH == 1) {
                 *       (1a) Cancel Egg spawn
                 *       (2)  Distribute egg correctly
                 *   } else if (pH == 1 && eH == 0) {
                 *       (1b) Cancel every block but egg
                 *   } else if (pH == 1 && eH == 1) {
                 *       (2)  Distribute egg correctly
                 *       (3)  Cancel event
                 *   } else if (pH == 2 && eH == 0) {
                 *       (3)  Cancel event
                 *   } else if (pH == 2 && eH == 1) {
                 *       (2)  Distribute egg correctly
                 *       (3)  Cancel event
                 *   }
                 */

                // (1a)
                final boolean cancelEgg = pH == 0 && eH == 1;

                // (1b)
                final boolean cancelPortalBlocks = pH == 1 && eH == 0;

                // (2)
                final boolean customEggHandling = eH == 1;

                // (3)
                final boolean cancelEvent = pH == 1 && eH == 1 || pH == 2;

                if (cancelEgg || cancelPortalBlocks) {
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
                    if (eggBlock != null) {
                        final Chunk c = eggBlock.getChunk();
                        for (int x = c.getX() - 2; x <= c.getX() + 2; x++) {
                            for (int z = c.getZ() - 2; z <= c.getZ() + 2; z++) {
                                c.getWorld().refreshChunk(x, z);
                            }
                        }
                    }
                }

                if (customEggHandling) {
                    // % of total damages done to the ED ; Player name
                    TreeMap<Float, String> ratioMap = new TreeMap<Float, String>();
                    long totalDamages = 0;
                    for (final Entry<String, Long> e : handler.getDragons().get(event.getEntity().getUniqueId()).entrySet()) {
                        totalDamages += e.getValue();
                    }
                    for (final Entry<String, Long> e : handler.getDragons().get(event.getEntity().getUniqueId()).entrySet()) {
                        ratioMap.put((float) ((double) e.getValue() / (double) totalDamages), e.getKey());
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
                    if (remainingRatioTotal != 1) {
                        final TreeMap<Float, String> newRatioMap = new TreeMap<Float, String>();
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
                    if (playerName == null) { // Security
                        endWorld.dropItem(deathLocation, new ItemStack(Material.DRAGON_EGG));
                    } else {
                        final Player p = Bukkit.getServer().getPlayerExact(playerName);
                        if (p == null) {
                            endWorld.dropItem(deathLocation, new ItemStack(Material.DRAGON_EGG));
                        } else {
                            final HashMap<Integer, ItemStack> notGiven = p.getInventory().addItem(new ItemStack(Material.DRAGON_EGG));
                            if (notGiven.size() > 0) {
                                p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.DRAGON_EGG));
                                plugin.sendMessage(p, MessageId.theEndAgain_droppedDragonEgg);
                            } else {
                                plugin.sendMessage(p, MessageId.theEndAgain_receivedDragonEgg);
                            }
                        }
                    }
                }

                event.setCancelled(cancelEvent);

                // Forget about this dragon
                handler.getDragons().remove(event.getEntity().getUniqueId());
                handler.setNumberOfAliveEDs(handler.getNumberOfAliveEDs() - 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEndChunkLoad(final ChunkLoadEvent event) {
        if (event.getWorld().getEnvironment() == Environment.THE_END) {
            final String worldName = event.getWorld().getName();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(worldName));
            if (handler != null) {
                final EndChunks chunks = handler.getChunks();
                final Chunk chunk = event.getChunk();
                final EndChunk endChunk = chunks.getChunk(worldName, chunk.getX(), chunk.getZ());
                if (endChunk == null) {
                    chunks.addChunk(chunk);
                } else if (endChunk.hasToBeRegen()) {
                    event.getWorld().regenerateChunk(endChunk.getX(), endChunk.getZ());
                    endChunk.setToBeRegen(false);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEnderDragonSpawn(final CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            if (event.getSpawnReason() != SpawnReason.CUSTOM && event.getSpawnReason() != SpawnReason.SPAWNER_EGG) {
                // Prevent an additional ED to be spawned
                event.setCancelled(true);
            } else {
                final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(event.getLocation().getWorld().getName()));
                if (handler == null) {
                    return;
                } else {
                    handler.getDragons().put(event.getEntity().getUniqueId(), new HashMap<String, Long>());
                    handler.setNumberOfAliveEDs(handler.getNumberOfAliveEDs() + 1);
                    event.getEntity().setMaxHealth(handler.getConfig().getEnderDragonHealth());
                    event.getEntity().setHealth(event.getEntity().getMaxHealth());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEnderDragonRegainHealth(final EntityRegainHealthEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            event.setCancelled(true);
        }
    }
}
