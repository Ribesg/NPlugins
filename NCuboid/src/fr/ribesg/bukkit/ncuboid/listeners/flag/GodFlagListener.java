package fr.ribesg.bukkit.ncuboid.listeners.flag;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerJoinEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

public class GodFlagListener extends AbstractListener {

    private final Set<Player> godPlayers;

    public GodFlagListener(final NCuboid instance) {
        super(instance);
        godPlayers = new HashSet<Player>();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(final ExtendedPlayerMoveEvent ext) {
        final PlayerMoveEvent event = (PlayerMoveEvent) ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            if (godPlayers.contains(event.getPlayer())) {
                if (ext.getToCuboid() == null || ext.getToCuboid() != null && !ext.getToCuboid().getFlag(Flag.GOD)) {
                    godPlayers.remove(event.getPlayer());
                }
            } else if (ext.getToCuboid() != null && ext.getToCuboid().getFlag(Flag.GOD)) {
                godPlayers.add(event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(final ExtendedEntityDamageEvent ext) {
        final EntityDamageEvent event = (EntityDamageEvent) ext.getBaseEvent();
        if (event.getEntityType() == EntityType.PLAYER) {
            if (godPlayers.contains(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(final ExtendedPlayerJoinEvent ext) {
        final PlayerJoinEvent event = (PlayerJoinEvent) ext.getBaseEvent();
        if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.INVISIBLE)) {
            godPlayers.add(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (godPlayers.contains(event.getPlayer())) {
            godPlayers.remove(event.getPlayer());
        }
    }
}
