/***************************************************************************
 * Project file:    NPlugins - NCuboid - EventExtensionListener.java       *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.EventExtensionListener
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityChangeBlockEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityExplodeEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityInteractEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedHangingBreakEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerDropItemEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEntityEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerInteractEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerJoinEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerTeleportEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPotionSplashEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventExtensionListener extends AbstractListener {

    public EventExtensionListener(final NCuboid instance) {
        super(instance);
    }

    // PlayerMoveEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerGridMove(final PlayerGridMoveEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedPlayerGridMoveEvent(this.getPlugin().getDb(), event));
    }

    // PlayerTeleportEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final Location from = event.getFrom(), to = event.getTo();
        if (from.getBlockX() != to.getBlockX() ||
            from.getBlockY() != to.getBlockY() ||
            from.getBlockZ() != to.getBlockZ() ||
            !from.getWorld().getName().equals(to.getWorld().getName())) {
            Bukkit.getPluginManager().callEvent(new ExtendedPlayerTeleportEvent(this.getPlugin().getDb(), event));
        }
    }

    // PlayerInteractEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedPlayerInteractEvent(this.getPlugin().getDb(), event));
    }

    // PlayerInteractEntityEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEntityEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedPlayerInteractEntityEvent(this.getPlugin().getDb(), event));
    }

    // HangingBreakEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHangingBreak(final HangingBreakEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedHangingBreakEvent(this.getPlugin().getDb(), event));
    }

    // EntityExplodeEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplode(final EntityExplodeEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedEntityExplodeEvent(this.getPlugin().getDb(), event));
    }

    // PlayerDropItemEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedPlayerDropItemEvent(this.getPlugin().getDb(), event));
    }

    // EntityDamageEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedEntityDamageEvent(this.getPlugin().getDb(), event));
    }

    // EntityInteractEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityInteract(final EntityInteractEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedEntityInteractEvent(this.getPlugin().getDb(), event));
    }

    // PlayerJoinEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedPlayerJoinEvent(this.getPlugin().getDb(), event));
    }

    // EntityChangeBlockEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedEntityChangeBlockEvent(this.getPlugin().getDb(), event));
    }

    // PotionSplashEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPotionSplash(final PotionSplashEvent event) {
        Bukkit.getPluginManager().callEvent(new ExtendedPotionSplashEvent(this.getPlugin().getDb(), event));
    }
}
