/***************************************************************************
 * Project file:    NPlugins - NCuboid - Jails.java                        *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Jails                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Jails {

	private final NCuboid plugin;

	private final Map<String, Jail>             byName;
	private final Map<GeneralRegion, Set<Jail>> byRegion;

	private final Map<UUID, Jail> jailed;

	public Jails(final NCuboid instance) {
		this.plugin = instance;
		this.byName = new HashMap<>();
		this.byRegion = new HashMap<>();
		this.jailed = new HashMap<>();
	}

	// Config handling

	public void saveJails(final ConfigurationSection parent) {
		final ConfigurationSection jailsSection = parent.createSection("jails");
		for (final Jail jail : byName.values()) {
			final ConfigurationSection jailSection = jailsSection.createSection(jail.getName());
			jailSection.set("location", jail.getLocation().toStringPlus());
			jailSection.set("region", jail.getRegion().getRegionName());
		}
	}

	public void loadJails(final RegionDb db, final ConfigurationSection parent) {
		if (parent.isConfigurationSection("jails")) {
			final ConfigurationSection jailsSection = parent.getConfigurationSection("jails");
			for (final String jailName : jailsSection.getKeys(false)) {
				if (!jailsSection.isConfigurationSection(jailName)) {
					plugin.error("Malformed configuration value for jail '" + jailName + "' in regionDB.yml, ignoring jail");
				} else {
					final ConfigurationSection jailSection = jailsSection.getConfigurationSection(jailName);
					final NLocation location = NLocation.toNLocation(jailSection.getString("location"));
					final String regionName = jailSection.getString("region");
					final GeneralRegion region = db.getByName(regionName);
					if (location == null) {
						plugin.error("Malformed location for jail '" + jailName + "' in regionDB.yml, ignoring jail");
					} else if (region == null) {
						plugin.error("Unknown region '" + regionName + "' for jail '" + jailName + "' in regionDB.yml, ignoring jail");
					} else {
						add(new Jail(jailName, location, region));
					}
				}
			}
		}
	}

	// Jails handling

	public boolean add(final Jail jail) {
		plugin.entering(getClass(), "add", "jail=" + jail);

		if (containsName(jail.getName())) {
			plugin.exiting(getClass(), "add", "Failed: jail already exists with same name");
			return false;
		} else {
			this.byName.put(jail.getName(), jail);
			Set<Jail> regionJails = this.byRegion.get(jail.getRegion());
			if (regionJails == null) {
				regionJails = new HashSet<>();
				this.byRegion.put(jail.getRegion(), regionJails);
			}
			regionJails.add(jail);

			plugin.exiting(getClass(), "add");
			return true;
		}
	}

	public boolean remove(final String jailName) {
		plugin.entering(getClass(), "remove", "jailName=" + jailName);

		final Jail jail = getByName(jailName);
		if (jail != null) {
			final Set<Jail> jails = getByRegion(jail.getRegion());
			if (jails.size() == 1) {
				this.byRegion.remove(jail.getRegion());
			} else {
				jails.remove(jail);
			}
			this.byName.remove(jailName.toLowerCase());

			plugin.exiting(getClass(), "remove");
			return true;
		} else {
			plugin.exiting(getClass(), "remove", "Failed: unknown jail");
			return false;
		}
	}

	public Jail getByName(final String jailName) {
		return this.byName.get(jailName.toLowerCase());
	}

	public boolean containsName(final String jailName) {
		return getByName(jailName) != null;
	}

	public Set<Jail> getByRegion(final GeneralRegion region) {
		return this.byRegion.get(region);
	}

	public boolean containsRegion(final GeneralRegion region) {
		return getByRegion(region) != null;
	}

	public Set<String> getJailNames() {
		return this.byName.keySet();
	}

	// Jailed player handling

	public boolean jail(final UUID id, final String jailName) {
		if (plugin.isDebugEnabled()) {
			plugin.entering(getClass(), "jail", "id=" + id + ";jailName=" + jailName);
		}
		if (!isJailed(id) && containsName(jailName)) {
			this.jailed.put(id, getByName(jailName));

			plugin.exiting(getClass(), "jail");
			return true;
		} else {
			if (plugin.isDebugEnabled()) {
				plugin.exiting(getClass(), "jail", "Failed: " + (isJailed(id) ? "player already jailed" : "unknown jail"));
			}
			return false;
		}
	}

	public boolean unJail(final UUID id) {
		return this.jailed.remove(id) != null;
	}

	public boolean isJailed(final UUID id) {
		return this.jailed.containsKey(id);
	}

	public Jail getJailForPlayer(final UUID id) {
		return this.jailed.get(id);
	}
}
