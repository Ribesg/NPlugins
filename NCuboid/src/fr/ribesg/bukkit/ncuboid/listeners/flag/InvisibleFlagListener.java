package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerJoinEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerMoveEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public class InvisibleFlagListener extends AbstractListener {

    private final Set<Player> invisiblePlayers;

    public InvisibleFlagListener(final NCuboid instance) {
        super(instance);
        invisiblePlayers = new HashSet<Player>();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(final ExtendedPlayerMoveEvent ext) {
        final PlayerMoveEvent event = (PlayerMoveEvent) ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            if (invisiblePlayers.contains(event.getPlayer())) {
                if (ext.getToCuboid() == null || ext.getToCuboid() != null && !ext.getToCuboid().getFlag(Flag.INVISIBLE)) {
                    showToAll(event.getPlayer());
                    invisiblePlayers.remove(event.getPlayer());
                }
            } else if (ext.getToCuboid() != null && ext.getToCuboid().getFlag(Flag.INVISIBLE)) {
                hideToAll(event.getPlayer());
                invisiblePlayers.add(event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(final ExtendedPlayerJoinEvent ext) {
        final PlayerJoinEvent event = (PlayerJoinEvent) ext.getBaseEvent();
        if (ext.getCuboid() != null && ext.getCuboid().getFlag(Flag.INVISIBLE)) {
            invisiblePlayers.add(event.getPlayer());
            hideToAll(event.getPlayer());
        }
        for (final Player p : invisiblePlayers) {
            event.getPlayer().hidePlayer(p);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (invisiblePlayers.contains(event.getPlayer())) {
            invisiblePlayers.remove(event.getPlayer());
        }
    }

    private void showToAll(final Player p) {
        for (final Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(p)) {
                other.showPlayer(p);
            }
        }
    }

    private void hideToAll(final Player p) {
        for (final Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(p) && !Perms.hasSeeInvisibleCuboid(other)) {
                other.showPlayer(p);
            }
        }
    }
}
