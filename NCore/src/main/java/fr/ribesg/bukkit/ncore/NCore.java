/***************************************************************************
 * Project file:    NPlugins - NCore - NCore.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.NCore                           *
 *                                                                         *
 *                Copyright (c) 2014 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.common.event.NEventsListener;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Core of the N Plugin Suite
 *
 * @author Ribesg
 */
public class NCore extends JavaPlugin {

	private Map<String, Node> nodes;

	private Metrics metrics;

	@Override
	public void onEnable() {
		try {
			metrics = new Metrics(this);
		} catch (final IOException e) {
			e.printStackTrace();
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
				getLogger().severe(s);
			}

			getPluginLoader().disablePlugin(this);
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
}
