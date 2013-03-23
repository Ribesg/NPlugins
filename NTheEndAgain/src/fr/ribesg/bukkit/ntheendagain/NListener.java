package fr.ribesg.bukkit.ntheendagain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

public class NListener implements Listener {

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
                        final HashMap<String, Long> dmgMap = handler.getDragons().get(event.getEntity().getUniqueId());
                        if (dmgMap == null) {
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
                            p.giveExp((int) (config.getXpReward() * entry.getValue() / totalDamages));
                            // TODO Send message to the player
                        }

                        // Forget about this dragon
                        handler.getDragons().remove(event.getEntity().getUniqueId());
                        handler.setNumberOfAliveEDs(handler.getNumberOfAliveEDs() - 1);
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
        if (event.getEntityType() == EntityType.ENDER_DRAGON && event.getDamager().getType() == EntityType.PLAYER) {
            final EnderDragon dragon = (EnderDragon) event.getEntity();
            final Player player = (Player) event.getDamager();
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
                    // TODO Custom egg give
                }

                event.setCancelled(cancelEvent);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEndChunkLoad(final ChunkLoadEvent event) {
        if (event.getWorld().getEnvironment() == Environment.THE_END) {
            // TODO Check if as to be regen, and regen
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEnderDragonSpawn(final CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            // TODO Store him, change health
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEnderDragonRegainHealth(final EntityRegainHealthEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            // TODO Eh... I don't know. Do we have to do something here ?
        }
    }
}
