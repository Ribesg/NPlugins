/***************************************************************************
 * Project file:    NPlugins - NCore - NCore.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.NCore                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.common.logging.FilterManager;
import fr.ribesg.bukkit.ncore.config.Config;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.event.NEventsListener;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.node.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ncore.node.permissions.PermissionsNode;
import fr.ribesg.bukkit.ncore.node.player.PlayerNode;
import fr.ribesg.bukkit.ncore.node.talk.TalkNode;
import fr.ribesg.bukkit.ncore.node.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import fr.ribesg.bukkit.ncore.updater.Updater;
import fr.ribesg.bukkit.ncore.updater.UpdaterListener;
import fr.ribesg.bukkit.ncore.util.ColorUtil;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Core of the N Plugin Suite
 *
 * @author Ribesg
 */
public class NCore extends JavaPlugin {

	private Logger        logger;
	private FilterManager filterManager;

	private Map<String, Node> nodes;
	private Metrics           metrics;
	private Config            pluginConfig;
	private Updater           updater;
	private boolean debugEnabled = false;

	@Override
	public void onEnable() {
		this.logger = this.getLogger();
		this.filterManager = new FilterManager();

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
			logger.log(Level.SEVERE, "An error occured when NCore tried to load config.yml", e);
		}

		if (pluginConfig.getDebugEnabled().contains(getName())) {
			this.debugEnabled = true;
			info("DEBUG MODE ENABLED!");
		}

		try {
			new UuidDb(this).loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			logger.log(Level.SEVERE, "An error occured when NCore tried to load uuidDb.yml", e);
		}

		this.nodes = new HashMap<>();

		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new BukkitRunnable() {

			@Override
			public void run() {
				afterNodesLoad();
			}
		}, 5 * 20L /* ~5 seconds */);

		Bukkit.getPluginManager().registerEvents(new NEventsListener(this), this);
		Bukkit.getPluginManager().registerEvents(new UpdaterListener(this), this);
	}

	@Override
	public void onDisable() {
		// Nothing yet
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (cmd.getName().equals("debug")) {
			if (!Perms.hasDebug(sender)) {
				sender.sendMessage(ColorUtil.colorize("&cYou do not have the permission to use that command"));
				return true;
			}
			if (args.length < 1 || args.length > 2) {
				return false;
			} else {
				final String header = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "DEBUG " + ChatColor.RESET;
				final String nodeName = args[args.length - 1];
				final Plugin plugin = Bukkit.getPluginManager().getPlugin(nodeName);
				if (plugin == null || (!(plugin instanceof NPlugin) && plugin != this)) {
					sender.sendMessage(header + ChatColor.RED + "'" + nodeName + "' is unknown or unloaded!");
				} else {
					final boolean value;
					if (plugin == this) {
						if (args.length == 1) {
							value = !isDebugEnabled();
						} else {
							value = Boolean.parseBoolean(args[0]);
						}
						setDebugEnabled(value);
					} else {
						final NPlugin nPlugin = (NPlugin) plugin;
						if (args.length == 1) {
							value = !nPlugin.isDebugEnabled();
						} else {
							value = Boolean.parseBoolean(args[0]);
						}
						nPlugin.setDebugEnabled(value);
					}
					sender.sendMessage(header + ChatColor.GREEN + "'" + nodeName + "' now has debug mode " + ChatColor.GOLD +
					                   (value ? "enabled" : "disabled") + ChatColor.GREEN + "!");
					try {
						final List<String> debugEnabledList = pluginConfig.getDebugEnabled();
						if (value) {
							debugEnabledList.add(plugin.getName());
						} else {
							debugEnabledList.remove(plugin.getName());
						}
						pluginConfig.loadConfig();
						pluginConfig.setDebugEnabled(debugEnabledList);
						pluginConfig.writeConfig();
					} catch (final InvalidConfigurationException | IOException ignored) {
						// Not a real problem
					}
				}
				return true;
			}
		} else if (cmd.getName().equals("updater")) {
			if (!Perms.hasUpdater(sender)) {
				sender.sendMessage(ColorUtil.colorize("&cYou do not have the permission to use that command"));
				return true;
			}
			if (updater == null) {
				sender.sendMessage(Updater.PREFIX + ChatColor.RED + "Updater is disabled in config");
			} else if (args.length != 2) {
				return false;
			} else {
				final String action = args[0].toLowerCase();
				final String nodeName = args[1];
				final boolean all = args[1].equalsIgnoreCase("all");
				if (!all && updater.getPlugins().get(nodeName.toLowerCase()) == null) {
					sender.sendMessage(Updater.PREFIX + ChatColor.RED + "Unknown Node: " + nodeName);
				} else {
					switch (action) {
						case "check":
						case "status":
							updater.checkForUpdates(sender, all ? null : nodeName);
							break;
						case "download":
						case "dl":
							if (all) {
								sender.sendMessage(Updater.PREFIX + ChatColor.RED + "Please select a specific Node to download");
							} else {
								updater.downloadUpdate(sender, nodeName);
							}
							break;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private void afterNodesLoad() {
		boolean noNodeFound = true;
		final Metrics.Graph nodesUsedGraph = metrics.createGraph("Nodes used");

		if (get(Node.CUBOID) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.CUBOID.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.ENCHANTING_EGG) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.ENCHANTING_EGG.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.GENERAL) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.GENERAL.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.PLAYER) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.PLAYER.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.PERMISSIONS) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.PERMISSIONS.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.TALK) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.TALK.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.THE_END_AGAIN) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.THE_END_AGAIN.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.WORLD) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.WORLD.substring(1)) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
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
				logger.severe(s);
			}

			getPluginLoader().disablePlugin(this);
		} else if (pluginConfig.isUpdateCheck()) {
			this.updater = new Updater(this, 'v' + getDescription().getVersion(), pluginConfig.getProxy(), pluginConfig.getApiKey());
			if (pluginConfig.getUpdateCheckInterval() > 0) {
				this.updater.startTask();
			}
		}
	}

	public Node get(final String nodeName) {
		return this.nodes.get(nodeName.toLowerCase());
	}

	public CuboidNode getCuboidNode() {
		return (CuboidNode) get(Node.CUBOID);
	}

	public EnchantingEggNode getEnchantingEggNode() {
		return (EnchantingEggNode) get(Node.ENCHANTING_EGG);
	}

	public GeneralNode getGeneralNode() {
		return (GeneralNode) get(Node.GENERAL);
	}

	public PlayerNode getPlayerNode() {
		return (PlayerNode) get(Node.PLAYER);
	}

	public PermissionsNode getPermissionsNode() {
		return (PermissionsNode) get(Node.PERMISSIONS);
	}

	public TalkNode getTalkNode() {
		return (TalkNode) get(Node.TALK);
	}

	public TheEndAgainNode getTheEndAgainNode() {
		return (TheEndAgainNode) get(Node.THE_END_AGAIN);
	}

	public WorldNode getWorldNode() {
		return (WorldNode) get(Node.WORLD);
	}

	public void set(final String nodeName, final Node node) {
		if (this.nodes.containsKey(nodeName.toLowerCase())) {
			throw new IllegalStateException("Registering the same node twice!");
		} else {
			this.nodes.put(nodeName.toLowerCase(), node);
		}
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	public Updater getUpdater() {
		return updater;
	}

	public FilterManager getFilterManager() {
		return filterManager;
	}

	// ##################### //
	// ## Debugging stuff ## //
	// ##################### //

	public void setDebugEnabled(final boolean value) {
		this.debugEnabled = value;
	}

	public boolean isDebugEnabled() {
		return this.debugEnabled;
	}

	public void log(final Level level, final String message) {
		logger.log(level, message);
	}

	public void info(final String message) {
		log(Level.INFO, message);
	}

	public void entering(final Class clazz, final String methodName) {
		if (this.debugEnabled) {
			log(Level.INFO, "DEBUG >>> '" + methodName + "' in " + shortNPluginPackageName(clazz.getName()));
		}
	}

	public void entering(final Class clazz, final String methodName, final String comment) {
		if (this.debugEnabled) {
			log(Level.INFO, "DEBUG >>> '" + methodName + "' in " + shortNPluginPackageName(clazz.getName()) + " (" + comment + ')');
		}
	}

	public void exiting(final Class clazz, final String methodName) {
		if (this.debugEnabled) {
			log(Level.INFO, "DEBUG <<< '" + methodName + "' in " + shortNPluginPackageName(clazz.getName()));
		}
	}

	public void exiting(final Class clazz, final String methodName, final String comment) {
		if (this.debugEnabled) {
			log(Level.INFO, "DEBUG <<< '" + methodName + "' in " + shortNPluginPackageName(clazz.getName()) + " (" + comment + ')');
		}
	}

	private String shortNPluginPackageName(final String packageName) {
		return packageName.substring(17);
	}

	public void debug(final String message) {
		if (this.debugEnabled) {
			log(Level.INFO, "DEBUG         " + message);
		}
	}

	public void debug(final String message, final Throwable e) {
		if (this.debugEnabled) {
			logger.log(Level.SEVERE, "DEBUG         " + message, e);
		}
	}

	public void error(final String message) {
		error(Level.SEVERE, message);
	}

	public void error(final Level level, final String message) {
		log(level, message);
	}

	public void error(final String message, final Throwable e) {
		error(Level.SEVERE, message, e);
	}

	public void error(final Level level, final String message, final Throwable e) {
		logger.log(level, message, e);
	}
}
