/***************************************************************************
 * Project file:    NPlugins - NPlayer - User.java                         *
 * Full Class name: fr.ribesg.bukkit.nplayer.user.User                     *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class User {

    private final LoggedOutUserHandler handler;

    private final UUID   userId;
    private       String passwordHash;

    private final List<String> knownIps;
    private       String       lastIp;

    private Location home;

    private boolean loggedIn;
    private boolean autoLogout;

    public User(final LoggedOutUserHandler handler, final UUID userId, final String passwordHash, final String currentIp) {
        this.handler = handler;
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.knownIps = new ArrayList<>();
        this.knownIps.add(currentIp);
        this.lastIp = currentIp;
        this.loggedIn = false;
        this.autoLogout = true;
        this.home = null;
    }

    public User(final LoggedOutUserHandler handler, final String lastIp, final List<String> knownIps, final String passwordHash, final UUID userId, final boolean autoLogout, final Location home) {
        this.handler = handler;
        this.lastIp = lastIp;
        this.knownIps = knownIps;
        this.loggedIn = false;
        this.passwordHash = passwordHash;
        this.userId = userId;
        this.autoLogout = autoLogout;
        this.home = home;
    }

    public String getLastIp() {
        return this.lastIp;
    }

    public void newIp(final String currentIp) {
        this.lastIp = currentIp;
        if (!this.knownIps.contains(currentIp)) {
            this.knownIps.add(currentIp);
        }
    }

    public List<String> getKnownIps() {
        return this.knownIps;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
        if (this.handler != null) {
            if (loggedIn) {
                this.handler.notifyLogin(Bukkit.getPlayer(this.userId));
            } else {
                this.handler.notifyLogout(Bukkit.getPlayer(this.userId));
            }
        }
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public boolean hasAutoLogout() {
        return this.autoLogout;
    }

    public void setAutoLogout(final boolean autoLogout) {
        this.autoLogout = autoLogout;
    }

    public Location getHome() {
        return this.home;
    }

    public void setHome(final Location home) {
        this.home = home;
    }
}
