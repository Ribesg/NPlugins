package fr.ribesg.bukkit.nenchantingegg.listener;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.ncore.utils.Time;
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
        ChunkCoord coord;
        Altar altar;
        switch (mat) {
            case DRAGON_EGG: // Placing a Dragon Egg on the center of an activated Altar
                coord = new ChunkCoord(event.getBlockPlaced().getLocation().getChunk());
                altar = plugin.getAltars().get(coord);
                if (altar != null) {
                    if (altar.getState() == AltarState.ACTIVE && altar.isEggPosition(loc)) {
                        plugin.getActiveToEggProvidedTransition().doTransition(altar);
                        altar.setPlayerName(event.getPlayer().getName());
                    } else if (altar.preventsBlockPlacement(event)) {
                        // TODO Send message? Damage player? Do something?
                        event.getPlayer().sendMessage("§cDEBUG: §aYou can't place a block on an Altar");
                        event.setCancelled(true);
                    }
                }
                break;
            case SKULL: // Creating an Altar by placing the Wither Skull last
                final Player player = event.getPlayer();
                Bukkit.getScheduler().runTask(plugin, new BukkitRunnable() {

                    @Override
                    public void run() {
                        final Skull skullState = (Skull) block.getState();
                        if (skullState.getSkullType() == SkullType.WITHER) {
                            // Create the altar, then check if it's valid
                            final Altar altar = new Altar(plugin, Altar.getCenterFromSkullLocation(loc));
                            if (altar.isInactiveAltarValid()) {
                                if (player.isOnline()) {
                                    player.sendMessage("§cDEBUG: §aYou created an Altar");
                                }
                                altar.setState(AltarState.INACTIVE);
                                plugin.getAltars().add(altar);
                                if (Time.isNightTime(loc.getWorld().getTime())) {
                                    plugin.getInactiveToActiveTransition().doTransition(altar);
                                }
                            } else {
                                altar.destroy();
                                // TODO: Message? Maybe a different message if there are missing block
                                //       than if there are blocks on top of the altar.
                                //       Also: be sure not to send a message for every Wither block placed
                                //             Maybe a minimum amount of corresponding blocks?
                                if (player.isOnline()) {
                                    event.getPlayer().sendMessage("§cDEBUG: §aNo Altar Found / Missing Blocks");
                                }
                            }
                        }
                    }
                });
                break;
            default:
                coord = new ChunkCoord(event.getBlockPlaced().getLocation().getChunk());
                altar = plugin.getAltars().get(coord);
                if (altar != null) {
                    if (altar.preventsBlockPlacement(event)) {
                        // TODO Send message? Damage player? Do something?
                        event.getPlayer().sendMessage("§cDEBUG: §aYou can't place a block on an Altar");
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
                            event.getPlayer().sendMessage("§cDEBUG: §aYou broke an Altar");
                            altar.destroy();
                        } else {
                            event.getPlayer().sendMessage("§cDEBUG: §aYou can't break an activated Altar");
                            event.setCancelled(true);
                        }
                        break;
                    } // else enter default section
                default:
                    if (altar.preventsBlockDestruction(event)) {
                        // TODO Send message? Damage player? Do something?
                        event.getPlayer().sendMessage("§cDEBUG: §aYou can't break a block part of an Altar");
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
            final Location teleportLocation = altar.getCenterLocation().clone().add(2.5, 1, 0.5);
            teleportLocation.setPitch(6f);
            teleportLocation.setYaw(90f);
            event.getPlayer().teleport(teleportLocation);
            event.setCancelled(true);
        }
    }
}
