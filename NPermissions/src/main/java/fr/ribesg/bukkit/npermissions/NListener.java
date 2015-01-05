/***************************************************************************
 * Project file:    NPlugins - NPermissions - NListener.java               *
 * Full Class name: fr.ribesg.bukkit.npermissions.NListener                *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

/**
 * NPermissions Listener. Binds PermissionAttachments to
 * connecting players and clear disconnecting players.
 *
 * @author Ribesg
 */
public class NListener implements Listener {

    /**
     * The NPermissions plugin instance
     */
    private final NPermissions plugin;

    /**
     * NPermissions Listener constructor.
     *
     * @param instance the NPermissions plugin instance
     */
    public NListener(final NPermissions instance) {
        this.plugin = instance;
    }

    /**
     * Registers the player as soon as he logins to be able to do some
     * Permissions checks in the PlayerLoginEvent.
     *
     * @param event the PlayerLoginEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLoginFirst(final PlayerLoginEvent event) {
        this.plugin.getManager().registerPlayer(event.getPlayer());
    }

    /**
     * Unregisters the player if the login failed.
     *
     * @param event the PlayerLoginEvent
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginLast(final PlayerLoginEvent event) {
        if (event.getResult() != Result.ALLOWED) {
            this.plugin.getManager().unRegisterPlayer(event.getPlayer());
        }
    }

    /**
     * Update permissions for Player now that we now in which world he is.
     *
     * @param event the PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.plugin.getManager().registerPlayerForWorld(event.getPlayer());
    }

    /**
     * Change permissions for Player when he changes world.
     *
     * @param event the PlayerChangedWorldEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangeWorld(final PlayerChangedWorldEvent event) {
        this.plugin.getManager().applyWorldPermissions(event.getPlayer());
    }
}
