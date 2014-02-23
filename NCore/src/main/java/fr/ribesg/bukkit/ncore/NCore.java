/***************************************************************************
 * Project file:    NPlugins - NCore - NCore.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.NCore                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.config.Config;
import fr.ribesg.bukkit.ncore.event.NEventsListener;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.node.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ncore.node.player.PlayerNode;
import fr.ribesg.bukkit.ncore.node.talk.TalkNode;
import fr.ribesg.bukkit.ncore.node.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import fr.ribesg.bukkit.ncore.updater.Updater;
import fr.ribesg.bukkit.ncore.updater.UpdaterListener;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
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

	private static Logger logger;

	private Map<String, Node> nodes;
	private Metrics           metrics;
	private Config            pluginConfig;
	private Updater           updater;

	@Override
	public void onEnable() {
		logger = this.getLogger();

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
			if (args.length < 1 || args.length > 2) {
				return false;
			} else {
				final String header = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "DEBUG " + ChatColor.RESET;
				final String nodeName = args[args.length - 1];
				final Plugin plugin = Bukkit.getPluginManager().getPlugin(nodeName);
				if (plugin == null || !(plugin instanceof NPlugin)) {
					sender.sendMessage(header + ChatColor.RED + "'" + nodeName + "' is unknown or unloaded!");
				} else {
					final NPlugin nPlugin = (NPlugin) plugin;
					final boolean value;
					if (args.length == 1) {
						value = !nPlugin.isDebugEnabled();
					} else {
						value = Boolean.parseBoolean(args[0]);
					}
					nPlugin.setDebugEnabled(value);
					sender.sendMessage(header + ChatColor.GREEN + "'" + nodeName + "' now has debug mode " + ChatColor.GOLD +
					                   (value ? "enabled" : "disabled") + ChatColor.GREEN + "!");
					try {
						final List<String> debugEnabledList = pluginConfig.getDebugEnabled();
						if (value) {
							debugEnabledList.add(nPlugin.getName());
						} else {
							debugEnabledList.remove(nPlugin.getName());
						}
						pluginConfig.loadConfig();
						pluginConfig.setDebugEnabled(debugEnabledList);
						pluginConfig.writeConfig();
					} catch (InvalidConfigurationException | IOException ignored) {
						// Not a real problem
					}
				}
				return true;
			}
		} else if (cmd.getName().equals("updater")) {
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
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.CUBOID) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.ENCHANTING_EGG) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.ENCHANTING_EGG) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.GENERAL) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.GENERAL) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.PLAYER) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.PLAYER) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.TALK) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.TALK) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.THE_END_AGAIN) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.THE_END_AGAIN) {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		if (get(Node.WORLD) != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.WORLD) {

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
			this.updater.startTask();
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
        
	public Updater getPluginUpdater() {
		return updater;
	}
}
