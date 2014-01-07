/***************************************************************************
 * Project file:    NPlugins - NCore - NCore.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.NCore                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.common.updater.FileDescription;
import fr.ribesg.bukkit.ncore.common.updater.Updater;
import fr.ribesg.bukkit.ncore.config.Config;
import fr.ribesg.bukkit.ncore.event.NEventsListener;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ncore.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * The Core of the N Plugin Suite
 *
 * @author Ribesg
 */
public class NCore extends JavaPlugin {

	private final static Logger LOGGER = Logger.getLogger(NCore.class.getName());

	private Map<String, Node> nodes;
	private Metrics           metrics;
	private Config            pluginConfig;
	private Updater           updater;

	@Override
	public void onEnable() {
		try {
			metrics = new Metrics(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// Config
		try {
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			LOGGER.severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			LOGGER.severe("This error occured when NCore tried to load config.yml");
		}

		this.nodes = new HashMap<>();

		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new BukkitRunnable() {

			@Override
			public void run() {
				afterNodesLoad();
			}
		}, 5 * 20L /* ~5 seconds */);

		Bukkit.getPluginManager().registerEvents(new NEventsListener(this), this);
	}

	@Override
	public void onDisable() {
		// Nothing yet
	}

	private void afterNodesLoad() {
		boolean noNodeFound = true;
		final Metrics.Graph nodesUsedGraph = metrics.createGraph("Nodes used");
		final List<JavaPlugin> plugins = new LinkedList<>();
		plugins.add(this);

		if (get(Node.CUBOID) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.CUBOID) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
			plugins.add((JavaPlugin) get(Node.CUBOID));
		}

		if (get(Node.ENCHANTING_EGG) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.ENCHANTING_EGG) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
			plugins.add((JavaPlugin) get(Node.ENCHANTING_EGG));
		}

		if (get(Node.GENERAL) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.GENERAL) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
			plugins.add((JavaPlugin) get(Node.GENERAL));
		}

		if (get(Node.PLAYER) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.PLAYER) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
			plugins.add((JavaPlugin) get(Node.PLAYER));
		}

		if (get(Node.TALK) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.TALK) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
			plugins.add((JavaPlugin) get(Node.TALK));
		}

		if (get(Node.THE_END_AGAIN) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.THE_END_AGAIN) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
			plugins.add((JavaPlugin) get(Node.THE_END_AGAIN));
		}

		if (get(Node.WORLD) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.WORLD) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
			plugins.add((JavaPlugin) get(Node.WORLD));
		}

		metrics.start();

		if (noNodeFound) {
			final FrameBuilder frame = new FrameBuilder();
			frame.addLine("This plugin can be safely removed", FrameBuilder.Option.CENTER);
			frame.addLine("It seems that you are using this plugin, NCore, while note using any");
			frame.addLine("node of the NPlugins suite. Maybe you forgot to add the Node(s) you");
			frame.addLine("wanted to use, or you forgot to remove NCore after removing all nodes.");
			frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);

			for (final String s : frame.build()) {
				LOGGER.severe(s);
			}

			getPluginLoader().disablePlugin(this);
		} else {
			checkForUpdates(plugins.toArray(new JavaPlugin[plugins.size()]));
		}
	}

	public Node get(final String nodeName) {
		return this.nodes.get(nodeName);
	}

	public void set(final String nodeName, final Node node) {
		if (this.nodes.containsKey(nodeName)) {
			throw new IllegalStateException("Registering the same node twice!");
		} else {
			this.nodes.put(nodeName, node);
		}
	}

	public void checkForUpdates(final JavaPlugin... plugins) {
		if (this.updater != null) {
			this.updater = new Updater('v' + getDescription().getVersion(), null);
		}
		Bukkit.getScheduler().runTaskAsynchronously(this, new BukkitRunnable() {

			@Override
			public void run() {
				for (final JavaPlugin plugin : plugins) {
					if (plugin != null && VersionUtils.isRelease(plugin.getDescription().getVersion())) {
						Boolean result = null;
						FileDescription latestFile = null;
						try {
							if (!updater.isUpToDate(plugin.getName(), 'v' + plugin.getDescription().getVersion())) {
								latestFile = updater.getLatestVersion(plugin.getName());
								result = false;
							} else {
								result = true;
							}
						} catch (final IOException e) {
							e.printStackTrace();
						}

						final Boolean finalResult = result;
						final FileDescription finalLatestFile = latestFile;
						Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								checkedForUpdates(plugin, finalResult, finalLatestFile);
								return null;
							}
						});
					}
				}
			}
		});
	}

	public void checkedForUpdates(final JavaPlugin plugin, final Boolean result, final FileDescription fileDescription) {
		if (result == null) {
			LOGGER.warning("Failed to check for updates for plugin " + plugin.getName());
		} else if (!result) {
			LOGGER.warning("A new version of " + plugin.getName() + " is available!");
			LOGGER.warning("Current version:   v" + plugin.getDescription().getVersion());
			LOGGER.warning("Available version: " + fileDescription.getVersion());
			LOGGER.warning("Find all updated from the NCore homepage!");
			LOGGER.warning("http://dev.bukkit.org/bukkit-plugins/ncore/");
		} else {
			LOGGER.info(plugin.getName() + " is up to date (v" + plugin.getDescription().getVersion() + ")");
		}
	}
}
