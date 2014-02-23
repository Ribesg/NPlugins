/***************************************************************************
 * Project file:    NPlugins - NCore - Updater.java                        *
 * Full Class name: fr.ribesg.bukkit.ncore.updater.Updater                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.updater;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.utils.TimeUtils;
import fr.ribesg.bukkit.ncore.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.logging.Level;

/**
 * This class checks for updates for every used nodes.
 *
 * @author Ribesg
 */
public class Updater {

	public static final String PREFIX = "§0[§c§lN§6Updater§0] §f";

	private static final DecimalFormat FORMAT = new DecimalFormat("#00.00");

	private static final String API_HOST  = "https://api.curseforge.com";
	private static final String API_QUERY = "/servermods/files?projectIds=";

	/**
	 * IDs of the Curse projects corresponding to the NPlugins suite
	 */
	private static final Map<String, Integer> CURSE_IDS;

	static {
		final Map<String, Integer> map = new TreeMap<>();
		map.put("NCore".toLowerCase(), 55525);
		map.put("NCuboid".toLowerCase(), 69596);
		map.put("NEnchantingEgg".toLowerCase(), 46694);
		map.put("NGeneral".toLowerCase(), 69595);
		map.put("NPlayer".toLowerCase(), 69598);
		map.put("NTalk".toLowerCase(), 35825);
		map.put("NTheEndAgain".toLowerCase(), 35931);
		map.put("NWorld".toLowerCase(), 62972);
		CURSE_IDS = Collections.unmodifiableMap(map);
	}

	private final NCore                   plugin;
	private final Map<String, JavaPlugin> plugins;

	private final String pluginVersion;
	private final Proxy  proxy;
	private final String apiKey;

	private final Map<String, Collection<FileDescription>> cache;
	private final Map<String, Long>                        cacheTimeout;
	private final long                                     cacheDuration;

	// NodeName, New Version
	private final SortedMap<String, String> updateAvailable;

	public Updater(final NCore instance, final String pluginVersion, @Nullable final Proxy proxy, @Nullable final String apiKey) {
		this(instance, pluginVersion, proxy, apiKey, "15min");
	}

	public Updater(final NCore instance, final String pluginVersion, @Nullable final Proxy proxy, @Nullable final String apiKey, final String cacheDuration) {
		this.plugin = instance;
		this.plugins = new HashMap<>();
		this.pluginVersion = pluginVersion;
		this.proxy = proxy;
		this.apiKey = apiKey;
		this.cache = new HashMap<>();
		this.cacheTimeout = new HashMap<>();
		this.cacheDuration = TimeUtils.getInMilliseconds(cacheDuration);
		this.updateAvailable = new TreeMap<>();

		this.plugins.put(plugin.getName().toLowerCase(), plugin);
		for (final String nodeName : CURSE_IDS.keySet()) {
			final String shortNodeName = nodeName.substring(1);
			if (plugin.get(shortNodeName) != null) {
				this.plugins.put(nodeName.toLowerCase(), (JavaPlugin) plugin.get(shortNodeName));
			}
		}
	}

	public Map<String, JavaPlugin> getPlugins() {
		return plugins;
	}

	public void startTask() {
		new UpdaterTask(this).runTaskTimer(plugin, 20L, plugin.getPluginConfig().getUpdateCheckInterval() * 20L);
	}

	public void checkForUpdates() {
		checkForUpdates(null);
	}

	public void checkForUpdates(@Nullable final CommandSender sender) {
		checkForUpdates(sender, null);
	}

	public void checkForUpdates(@Nullable final CommandSender sender, @Nullable final String nodeName) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {

			@Override
			public void run() {
				for (final JavaPlugin plugin : plugins.values()) {
					if (plugin != null && (nodeName == null || plugin.getName().equalsIgnoreCase(nodeName)) && VersionUtils.isRelease('v' + plugin.getDescription().getVersion())) {
						final String version = VersionUtils.getVersion('v' + plugin.getDescription().getVersion());
						Boolean result = null;
						FileDescription latestFile = null;
						try {
							if (!isUpToDate(plugin.getName(), version)) {
								latestFile = getLatestVersion(plugin.getName());
								result = false;
							} else {
								result = true;
							}
						} catch (final IOException e) {
							plugin.getLogger().log(Level.SEVERE, "Failed to check for update for node " + plugin.getName() + ": " + e.getMessage());
						}

						final Boolean finalResult = result;
						final FileDescription finalLatestFile = latestFile;
						Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								checkedForUpdates(sender == null ? Bukkit.getConsoleSender() : sender, plugin, finalResult, finalLatestFile);
								return null;
							}
						});
					}
				}
			}
		});
	}

	private void checkedForUpdates(final CommandSender sender, final JavaPlugin plugin, final Boolean result, final FileDescription fileDescription) {
		if (result == null) {
			sender.sendMessage(PREFIX + ChatColor.RED + "Failed to check for updates for plugin " + plugin.getName());
		} else if (!result) {
			newUpdateAvailable(plugin.getName(), fileDescription.getVersion());
			sender.sendMessage(PREFIX + ChatColor.GREEN + "A new version of " + ChatColor.GOLD + plugin.getName() + ChatColor.GREEN + " is available!");
			sender.sendMessage(PREFIX + ChatColor.GREEN + "Current version:   " + ChatColor.GOLD + "v" + plugin.getDescription().getVersion());
			sender.sendMessage(PREFIX + ChatColor.GREEN + "Available version: " + ChatColor.GOLD + fileDescription.getVersion());
			sender.sendMessage(PREFIX + ChatColor.GREEN + "Download the update from BukkitDev or with");
			sender.sendMessage(PREFIX + ChatColor.GOLD + "/updater download " + plugin.getName());
		} else {
			sender.sendMessage(PREFIX + ChatColor.GOLD + plugin.getName() + ChatColor.GREEN + " is up to date (latest: " + ChatColor.GOLD + "v" + plugin.getDescription().getVersion() + ChatColor.GREEN + ")");
		}
	}

	private void newUpdateAvailable(final String pluginName, final String newVersion) {
		this.updateAvailable.put(pluginName.toLowerCase(), newVersion);
	}

	public void downloadUpdate(final CommandSender sender, final String nodeName) {
		if (updateAvailable.containsKey(nodeName.toLowerCase())) {
			try {
				if (nodeName != null) {
					final FileDescription desc = getLatestVersion(nodeName);
					if (desc != null) {
						new BukkitRunnable() {

							@Override
							public void run() {
								try {
									downloadFile(sender, desc.getFileName(), desc.getDownloadUrl());
								} catch (final DownloadFailedException e) {
									sender.sendMessage(PREFIX + ChatColor.RED + "Failed to download file");
								}
							}
						}.runTaskAsynchronously(plugin);
					} else {
						sender.sendMessage(PREFIX + ChatColor.RED + "Failed to get latest file informations");
					}
				} else {
					for (final String name : updateAvailable.keySet()) {
						final FileDescription desc = getLatestVersion(name);
						if (desc != null) {
							new BukkitRunnable() {

								@Override
								public void run() {
									try {
										downloadFile(sender, desc.getFileName(), desc.getDownloadUrl());
									} catch (final DownloadFailedException e) {
										sender.sendMessage(PREFIX + ChatColor.RED + "Failed to download file");
									}
								}
							}.runTaskAsynchronously(plugin);
						} else {
							sender.sendMessage(PREFIX + ChatColor.RED + "Failed to get latest file informations");
						}
					}
				}
			} catch (final IOException e) {
				sender.sendMessage(PREFIX + ChatColor.RED + e.getMessage());
			}
		} else {
			sender.sendMessage(PREFIX + ChatColor.GREEN + "No update available for " + nodeName);
		}
	}

	private void downloadFile(final CommandSender sender, final String file, final String link) throws DownloadFailedException {
		// Update folder
		if (!Bukkit.getUpdateFolderFile().exists()) {
			Bukkit.getUpdateFolderFile().mkdir();
		}

		// Build URL object
		final URL url;
		try {
			url = new URL(link);
		} catch (final MalformedURLException e) {
			throw new DownloadFailedException("Malformed url", e);
		}

		// Determine file length
		final int fileLength;
		try {
			fileLength = url.openConnection().getContentLength();
		} catch (final IOException e) {
			throw new DownloadFailedException("Failed to open connection", e);
		}

		// Download the file
		try (
				BufferedInputStream in = new BufferedInputStream(url.openStream());
				FileOutputStream fout = new FileOutputStream(Bukkit.getUpdateFolderFile().getAbsolutePath() + File.separator + file)
		) {
			final byte[] data = new byte[256];
			int chunk;
			long downloaded = 0, lastTime = 0;
			while ((chunk = in.read(data, 0, 256)) != -1) {
				downloaded += chunk;
				fout.write(data, 0, chunk);
				if (lastTime < System.currentTimeMillis() - 100) {
					sender.sendMessage(PREFIX + ChatColor.GREEN + "Downloading...      " + FORMAT.format(downloaded * 100.0 / fileLength) + '%');
					lastTime = System.currentTimeMillis();
				}
			}
			sender.sendMessage(PREFIX + ChatColor.GREEN + "Download complete! 100.00%");
		} catch (final IOException e) {
			throw new DownloadFailedException("Failed while downloading the file", e);
		}
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
		if (CURSE_IDS.containsKey(pluginName.toLowerCase())) {
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
			final Integer projectId = CURSE_IDS.get(pluginName.toLowerCase());
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
			connection.setConnectTimeout(1337); // "Low enough" timeout in case somebody has the "good" idea to call this sync...
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
        
        public SortedMap<String, String> getUpdateAvailable() {
            return updateAvailable;
        }
        
        public String getMessagePrefix() {
            return PREFIX;
        }
}
