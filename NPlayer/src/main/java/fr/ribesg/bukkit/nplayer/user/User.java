/***************************************************************************
 * Project file:    NPlugins - NPlayer - User.java                         *
 * Full Class name: fr.ribesg.bukkit.nplayer.user.User                     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.user;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
		return lastIp;
	}

	public void newIp(final String currentIp) {
		this.lastIp = currentIp;
		if (!this.knownIps.contains(currentIp)) {
			this.knownIps.add(currentIp);
		}
	}

	public List<String> getKnownIps() {
		return knownIps;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(final boolean loggedIn) {
		this.loggedIn = loggedIn;
		if (loggedIn) {
			handler.notifyLogin(Bukkit.getPlayerExact(UuidDb.getName(getUserId()))); // TODO Change to getPlayer(UUID)
		} else {
			handler.notifyLogout(Bukkit.getPlayerExact(UuidDb.getName(getUserId()))); // TODO Change to getPlayer(UUID)
		}
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(final String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public UUID getUserId() {
		return userId;
	}

	public boolean hasAutoLogout() {
		return autoLogout;
	}

	public void setAutoLogout(final boolean autoLogout) {
		this.autoLogout = autoLogout;
	}

	public Location getHome() {
		return home;
	}

	public void setHome(final Location home) {
		this.home = home;
	}
}
