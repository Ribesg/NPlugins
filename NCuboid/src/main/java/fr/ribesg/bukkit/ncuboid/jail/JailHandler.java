package fr.ribesg.bukkit.ncuboid.jail;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 *
 * @author Ribesg
 */
public class JailHandler {

	private final NCuboid plugin;

	private final Set<String> jails;

	// PlayerName, JailName
	private final Map<String, String> jailedMap;

	// JailName, Region
	private final Map<String, GeneralRegion> jailsMap;

	public JailHandler(NCuboid instance) {
		this.plugin = instance;
		jailedMap = new HashMap<>();
		jailsMap = new HashMap<>();
		jails = new HashSet<>();
	}

	public void loadJails() {
		for (GeneralRegion cuboid : plugin.getDb()) {
			// TODO Fill jails & jailsMap
		}
	}

	public void loadJailed(final Map<String, String> jailedMap) {
		this.jailedMap.putAll(jailedMap);
	}

	public boolean isJailed(String playerName) {
		return jailedMap.containsKey(playerName);
	}

	public boolean jail(String playerName, String jailName) {
		return false;  // TODO Implement method
	}

	public boolean unJail(String playerName) {
		return jailedMap.remove(playerName) != null;
	}

	public List<String> getJailList() {
		return null;  // TODO Implement method
	}
}
