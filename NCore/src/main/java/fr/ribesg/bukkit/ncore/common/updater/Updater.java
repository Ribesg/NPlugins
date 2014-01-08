/***************************************************************************
 * Project file:    NPlugins - NCore - Updater.java                        *
 * Full Class name: fr.ribesg.bukkit.ncore.common.updater.Updater          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.updater;

import fr.ribesg.bukkit.ncore.utils.TimeUtils;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class checks for updates for every used nodes.
 *
 * @author Ribesg
 */
public class Updater {

	private static final String API_HOST  = "https://api.curseforge.com";
	private static final String API_QUERY = "/servermods/files?projectIds=";

	/** IDs of the Curse projects corresponding to the NPlugins suite */
	private static final Map<String, Integer> CURSE_IDS;

	static {
		final Map<String, Integer> map = new TreeMap<>();
		map.put("NCore", 55525);
		map.put("NCuboid", 69596);
		map.put("NEnchantingEgg", 46694);
		map.put("NGeneral", 69595);
		map.put("NPlayer", 69598);
		map.put("NTalk", 35825);
		map.put("NTheEndAgain", 35931);
		map.put("NWorld", 62972);
		CURSE_IDS = Collections.unmodifiableMap(map);
	}

	private final String pluginVersion;
	private final Proxy  proxy;
	private final String apiKey;

	private final Map<String, Collection<FileDescription>> cache;
	private final Map<String, Long>                        cacheTimeout;
	private final long                                     cacheDuration;

	public Updater(final String pluginVersion, @Nullable final Proxy proxy, @Nullable final String apiKey) {
		this(pluginVersion, proxy, apiKey, "15min");
	}

	public Updater(final String pluginVersion, @Nullable final Proxy proxy, @Nullable final String apiKey, final String cacheDuration) {
		this.pluginVersion = pluginVersion;
		this.proxy = proxy;
		this.apiKey = apiKey;
		this.cache = new HashMap<>();
		this.cacheTimeout = new HashMap<>();
		this.cacheDuration = TimeUtils.getInMilliseconds(cacheDuration);
	}

	/**
	 * Gets all informations about the latest file for the provided plugin.
	 *
	 * @param pluginName the plugin to get file information for
	 *
	 * @return the informations about the latest plugin file
	 *
	 * @throws IOException if an error occur while trying to contact Curse API
	 */
	public FileDescription getLatestVersion(final String pluginName) throws IOException {
		if (CURSE_IDS.containsKey(pluginName)) {
			return getNPluginLatestVersion(pluginName);
		} else {
			throw new UnsupportedOperationException("Non-NPlugin not yet supported");
		}
	}

	private FileDescription getNPluginLatestVersion(final String pluginName) throws IOException {
		final Collection<FileDescription> files = getFiles(pluginName);
		if (files.isEmpty()) {
			return null;
		} else {
			return files.iterator().next();
		}
	}

	/**
	 * Checks if a plugin is up to date.
	 *
	 * @param pluginName     the name of the plugin, handles only NPlugins for now.
	 * @param currentVersion the used version of the plugin
	 *
	 * @return true if the plugin is up to date, false otherwise
	 *
	 * @throws IOException if an error occur while trying to contact Curse API
	 */
	public boolean isUpToDate(final String pluginName, final String currentVersion) throws IOException {
		final FileDescription latestFile = getLatestVersion(pluginName);
		return latestFile.getVersion().compareTo(currentVersion) <= 0;
	}

	/**
	 * Gets the files associated with the provided plugin name from Curse
	 * servers. The plugin name can only be one of those contained in the
	 * internal CURSE_IDS map, i.e. the N plugin suite nodes.
	 *
	 * @param pluginName the name of the plugin to get Files for
	 *
	 * @return an ordered Collection of FileDescription, higher versions first
	 *
	 * @throws IOException if there is an error while retrieving files
	 */
	public Collection<FileDescription> getFiles(final String pluginName) throws IOException {
		try {
			final Integer projectId = CURSE_IDS.get(pluginName);
			if (projectId == null) {
				throw new IllegalArgumentException("Unknown plugin '" + pluginName + "'");
			}
			return getFiles(projectId);
		} catch (final IOException e) {
			throw new IOException("Unable to get files associated to plugin '" + pluginName + "': " + e.getMessage(), e);
		}
	}

	/**
	 * Gets the files associated with the provided plugin id from Curse
	 * servers.
	 *
	 * @param projectId the ID of the plugin to get Files for
	 *
	 * @return an ordered Collection of FileDescription, higher versions first
	 *
	 * @throws IOException if there is an error while retrieving files
	 */
	public Collection<FileDescription> getFiles(final int... projectId) throws IOException {
		try {
			final String projectIds = getProjectIdsString(projectId);
			final Long timeout = this.cacheTimeout.get(projectIds);
			if (timeout != null) {
				if (System.currentTimeMillis() < timeout) {
					return this.cache.get(projectIds);
				} else {
					this.cache.remove(projectIds);
					this.cacheTimeout.remove(projectIds);
				}
			}
			final URL url = buildUrl(projectIds);
			final URLConnection connection = openConnection(url, proxy);
			final String jsonString = getJson(connection);
			final Collection<FileDescription> files = FileDescription.parse(jsonString).values();
			this.cache.put(projectIds, files);
			this.cacheTimeout.put(projectIds, System.currentTimeMillis() + this.cacheDuration);
			return files;
		} catch (final IOException e) {
			throw new IOException("Unable to get files associated to id(s) " + Arrays.toString(projectId) + ": " + e.getMessage(), e);
		}
	}

	private String getJson(final URLConnection connection) throws IOException {
		try (final InputStreamReader isReader = new InputStreamReader(connection.getInputStream());
		     final BufferedReader reader = new BufferedReader(isReader)) {
			return reader.readLine();
		} catch (final IOException e) {
			throw new IOException("Unable to read response from '" + connection.getURL().getHost() + "': " + e.getMessage(), e);
		}
	}

	private URLConnection openConnection(final URL url, @Nullable final Proxy proxy) throws IOException {
		try {
			final URLConnection connection;
			if (proxy == null) {
				connection = url.openConnection();
			} else {
				connection = url.openConnection(proxy);
			}
			connection.setConnectTimeout(1337); // "Low enough" timeout in case somebody has the good idea to call this sync...
			if (this.apiKey != null) {
				connection.addRequestProperty("X-API-Key", apiKey);
			}
			connection.addRequestProperty("User-Agent", "NPlugins v" + this.pluginVersion + " Updater by Ribesg");
			connection.setDoOutput(true);
			return connection;
		} catch (final IOException e) {
			throw new IOException("Unable to reach '" + url.getHost() + "': " + e.getMessage(), e);
		}
	}

	private URL buildUrl(final String projectIds) {
		try {
			return new URL(API_HOST + API_QUERY + projectIds);
		} catch (final MalformedURLException ex) {
			throw new IllegalArgumentException("Invalid projectsIds parameter", ex);
		}
	}

	private String getProjectIdsString(final int... projectId) {
		if (projectId.length < 1) {
			throw new IllegalArgumentException("projectId should contain at least one id!");
		}

		final StringBuilder builder = new StringBuilder(Integer.toString(projectId[0]));
		if (projectId.length > 1) {
			for (int i = 1; i < projectId.length; i++) {
				builder.append(',').append(Integer.toString(projectId[i]));
			}
		}

		return builder.toString();
	}
}
