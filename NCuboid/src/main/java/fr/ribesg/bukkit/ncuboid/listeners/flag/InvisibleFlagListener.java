/***************************************************************************
 * Project file:    NPlugins - NCuboid - InvisibleFlagListener.java        *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.InvisibleFlagListener
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.Perms;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerJoinEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InvisibleFlagListener extends AbstractListener {

    private final Set<String> invisiblePlayers;

    public InvisibleFlagListener(final NCuboid instance) {
        super(instance);
        this.invisiblePlayers = new HashSet<>();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
        final PlayerGridMoveEvent event = (PlayerGridMoveEvent)ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            if (this.invisiblePlayers.contains(event.getPlayer().getName())) {
                if (ext.getToRegion() == null || !ext.getToRegion().getFlag(Flag.INVISIBLE)) {
                    this.showToAll(event.getPlayer());
                    this.invisiblePlayers.remove(event.getPlayer().getName());
                }
            } else if (ext.getToRegion() != null && ext.getToRegion().getFlag(Flag.INVISIBLE)) {
                this.hideToAll(event.getPlayer());
                this.invisiblePlayers.add(event.getPlayer().getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(final ExtendedPlayerJoinEvent ext) {
        final PlayerJoinEvent event = (PlayerJoinEvent)ext.getBaseEvent();
        if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.INVISIBLE)) {
            this.invisiblePlayers.add(event.getPlayer().getName());
            this.hideToAll(event.getPlayer());
        }
        for (final String p : this.invisiblePlayers) {
            event.getPlayer().hidePlayer(Bukkit.getPlayerExact(p));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.invisiblePlayers.remove(event.getPlayer().getName());
    }

    private void showToAll(final Player p) {
        if (this.getPlugin().shouldShow(p)) {
            for (final Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p)) {
                    other.showPlayer(p);
                }
            }
        }
    }

    private void hideToAll(final Player p) {
        for (final Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(p) && !Perms.hasSeeInvisibleCuboid(other)) {
                other.hidePlayer(p);
            }
        }
    }
}
