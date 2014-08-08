/***************************************************************************
 * Project file:    NPlugins - NCuboid - GodFlagListener.java              *
 * Full Class name: fr.ribesg.bukkit.ncuboid.listeners.flag.GodFlagListener*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.listeners.flag;

import fr.ribesg.bukkit.ncore.event.PlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityDamageEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerGridMoveEvent;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedPlayerJoinEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GodFlagListener extends AbstractListener {

    private final Set<String> godPlayers;

    public GodFlagListener(final NCuboid instance) {
        super(instance);
        this.godPlayers = new HashSet<>();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerGridMove(final ExtendedPlayerGridMoveEvent ext) {
        final PlayerGridMoveEvent event = (PlayerGridMoveEvent)ext.getBaseEvent();
        if (!ext.isCustomCancelled()) {
            if (this.godPlayers.contains(event.getPlayer().getName())) {
                if (ext.getToRegion() == null || !ext.getToRegion().getFlag(Flag.GOD)) {
                    this.godPlayers.remove(event.getPlayer().getName());
                }
            } else if (ext.getToRegion() != null && ext.getToRegion().getFlag(Flag.GOD)) {
                this.godPlayers.add(event.getPlayer().getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(final ExtendedEntityDamageEvent ext) {
        final EntityDamageEvent event = (EntityDamageEvent)ext.getBaseEvent();
        if (event.getEntityType() == EntityType.PLAYER) {
            if (this.godPlayers.contains(((Player)event.getEntity()).getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(final ExtendedPlayerJoinEvent ext) {
        final PlayerJoinEvent event = (PlayerJoinEvent)ext.getBaseEvent();
        if (ext.getRegion() != null && ext.getRegion().getFlag(Flag.INVISIBLE)) {
            this.godPlayers.add(event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.godPlayers.remove(event.getPlayer().getName());
    }
}
