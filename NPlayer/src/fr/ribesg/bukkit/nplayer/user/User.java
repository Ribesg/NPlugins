package fr.ribesg.bukkit.nplayer.user;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

	private final LoggedOutUserHandler handler;

	private final String userName;
	private       String passwordHash;

	private final List<String> knownIps;
	private       String       lastIp;

	private final Date firstJoin;
	private       Date lastSeen;

	private Location home;

	private boolean loggedIn;
	private boolean autoLogout;

	public User(LoggedOutUserHandler handler, String userName, String passwordHash, String currentIp, Date firstJoin) {
		this.handler = handler;
		this.userName = userName;
		this.passwordHash = passwordHash;
		this.knownIps = new ArrayList<>();
		this.knownIps.add(currentIp);
		this.lastIp = currentIp;
		this.firstJoin = firstJoin;
		this.lastSeen = firstJoin;
		this.loggedIn = false;
		this.autoLogout = true;
		this.home = null;
	}

	public User(LoggedOutUserHandler handler,
	            String lastIp,
	            Date firstJoin,
	            List<String> knownIps,
	            Date lastSeen,
	            String passwordHash,
	            String userName,
	            boolean autoLogout,
	            Location home) {
		this.handler = handler;
		this.lastIp = lastIp;
		this.firstJoin = firstJoin;
		this.knownIps = knownIps;
		this.lastSeen = lastSeen;
		this.loggedIn = false;
		this.passwordHash = passwordHash;
		this.userName = userName;
		this.autoLogout = autoLogout;
		this.home = home;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void newIp(String currentIp) {
		this.lastIp = currentIp;
		if (!this.knownIps.contains(currentIp)) {
			this.knownIps.add(currentIp);
		}
	}

	public Date getFirstJoin() {
		return firstJoin;
	}

	public List<String> getKnownIps() {
		return knownIps;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
		if (loggedIn) {
			handler.notifyLogin(this);
		} else {
			handler.notifyLogout(this);
		}
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getUserName() {
		return userName;
	}

	public boolean hasAutoLogout() {
		return autoLogout;
	}

	public void setAutoLogout(boolean autoLogout) {
		this.autoLogout = autoLogout;
	}

	public Location getHome() {
		return home;
	}

	public void setHome(Location home) {
		this.home = home;
	}
}
