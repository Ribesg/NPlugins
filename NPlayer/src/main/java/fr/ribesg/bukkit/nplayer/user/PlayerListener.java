/***************************************************************************
 * Project file:    NPlugins - NPlayer - PlayerListener.java               *
 * Full Class name: fr.ribesg.bukkit.nplayer.user.PlayerListener           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.user;

import fr.ribesg.bukkit.ncore.event.PlayerJoinedEvent;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.nplayer.NPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Ribesg
 */
public class PlayerListener implements Listener {

    private final NPlayer              plugin;
    private final LoggedOutUserHandler loggedOutUserHandler;
    private final int                  authenticationMode;

    public PlayerListener(final NPlayer instance, final LoggedOutUserHandler loggedOutUserHandler) {
        this.plugin = instance;
        this.loggedOutUserHandler = loggedOutUserHandler;
        this.authenticationMode = this.plugin.getPluginConfig().getAuthenticationMode();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoined(final PlayerJoinedEvent event) {
        final Player player = event.getPlayer();
        final String playerIp = player.getAddress().getAddress().getHostAddress();
        if (this.authenticationMode != 0) {
            this.loggedOutUserHandler.notifyConnect(player);
        }
        final User user = this.plugin.getUserDb().get(player.getUniqueId());
        if (this.authenticationMode == 0) {
            // Authentication disabled
            if (user == null) {
                this.plugin.getUserDb().newUser(player.getUniqueId(), null, playerIp);
            } else if (!user.getLastIp().equals(playerIp)) {
                user.newIp(playerIp);
            }
        } else if (this.authenticationMode == 1) {
            // Authentication enabled
            if (user == null || user.getPasswordHash() == null) {
                // Unknown, should /register
                this.plugin.sendMessage(player, MessageId.player_pleaseRegister);
            } else if (user.getLastIp().equals(playerIp) && !user.hasAutoLogout()) {
                // Auto-login
                user.setLoggedIn(true);
                this.plugin.sendMessage(player, MessageId.player_autoLogged);
            } else {
                // Should /login
                this.plugin.sendMessage(player, MessageId.player_pleaseLogin);
            }
        } else {
            // Authentication optional
            if (user == null) {
                this.plugin.getUserDb().newUser(player.getUniqueId(), null, playerIp).setLoggedIn(true);
            } else if (user.getPasswordHash() != null) {
                // Has a password, should login
                this.plugin.sendMessage(player, MessageId.player_pleaseLogin);
            } else if (!user.getLastIp().equals(playerIp)) {
                user.setLoggedIn(true);
                user.newIp(playerIp);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final User user = this.plugin.getUserDb().get(player.getUniqueId());
        if (user != null && user.hasAutoLogout()) {
            user.setLoggedIn(false);
            if (this.loggedOutUserHandler != null) {
                this.loggedOutUserHandler.notifyLogout(player);
            }
        }
        if (this.loggedOutUserHandler != null) {
            this.loggedOutUserHandler.notifyDisconnect(player);
        }
    }
}
