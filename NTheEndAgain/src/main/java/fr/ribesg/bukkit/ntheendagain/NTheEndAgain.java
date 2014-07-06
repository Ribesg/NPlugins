/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - NTheEndAgain.java            *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.NTheEndAgain             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import fr.ribesg.bukkit.ntheendagain.lang.Messages;
import fr.ribesg.bukkit.ntheendagain.listener.ChunkListener;
import fr.ribesg.bukkit.ntheendagain.listener.DamageListener;
import fr.ribesg.bukkit.ntheendagain.listener.EnderDragonListener;
import fr.ribesg.bukkit.ntheendagain.listener.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.InvalidConfigurationException;
import org.mcstats.Metrics;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class NTheEndAgain extends NPlugin implements TheEndAgainNode {

	// Configs
	private Messages messages;

	// Useful Nodes
	// // None

	// Actual plugin data
	private HashMap<String, EndWorldHandler> worldHandlers;

	@Override
	protected String getMinCoreVersion() {
		return "0.6.6";
	}

	@Override
	protected void loadMessages() throws IOException {
		debug("Loading plugin Messages...");
		if (!getDataFolder().isDirectory()) {
			getDataFolder().mkdir();
		}

		final Messages messages = new Messages();
		messages.loadMessages(this);

		this.messages = messages;
	}

	@Override
	public boolean onNodeEnable() {
		entering(getClass(), "onNodeEnable");

		debug("Loading End world config and Chunk data...");
		worldHandlers = new HashMap<>();
		boolean res = true;

		debug("Analysing all worlds...");
		for (final World w : Bukkit.getWorlds()) {
			debug("World " + w.getName() + " is of type " + w.getEnvironment());
			if (w.getEnvironment() == Environment.THE_END) {
				try {
					debug("Trying to load world " + w.getName());
					res = loadWorld(w);
					if (!res) {
						debug("Load of world " + w.getName() + " failed!");
						break;
					}
				} catch (final InvalidConfigurationException e) {
					error("An error occured when NTheEndAgain tried to load \"" + w.getName() + "\"'s config file.", e);
					break;
				}
			}
		}
		if (!res) {
			error("Failed to load a configuration, please triple-check them. Disabling plugin...");
			for (final EndWorldHandler handler : worldHandlers.values()) {
				handler.cancelTasks();
			}
			return false;
		}

		debug("Activating filter if needed...");
		activateFilter();

		debug("Registering event handlers...");
		getServer().getPluginManager().registerEvents(new WorldListener(this), this);
		getServer().getPluginManager().registerEvents(new ChunkListener(this), this);
		getServer().getPluginManager().registerEvents(new EnderDragonListener(this), this);
		getServer().getPluginManager().registerEvents(new DamageListener(this), this);

		debug("Registering command...");
		setCommandExecutor("nend", new TheEndAgainCommandExecutor(this));

		debug("Handling Metrics...");
		final Metrics.Graph g1 = getMetrics().createGraph("Amount of End Worlds handled");
		g1.addPlotter(new Metrics.Plotter() {

			@Override
			public int getValue() {
				return getWorldHandlers().size();
			}
		});

		// Metrics - Type of regeneration
		int disabled = 0;
		int hard = 0;
		int soft = 0;
		int crystal = 0;
		for (final EndWorldHandler h : getWorldHandlers().values()) {
			if (h.getConfig().getRegenType() == 0) {
				disabled++;
			} else {
				switch (h.getConfig().getRegenMethod()) {
					case 0:
						hard++;
						break;
					case 1:
						soft++;
						break;
					case 2:
						crystal++;
						break;
					default:
						break;
				}
			}
		}
		final int finalDisabled = disabled;
		final int finalHard = hard;
		final int finalSoft = soft;
		final int finalCrystal = crystal;
		final Metrics.Graph g2 = getMetrics().createGraph("Regeneration Method");
		g2.addPlotter(new Metrics.Plotter("Disabled") {

			@Override
			public int getValue() {
				return finalDisabled;
			}
		});
		g2.addPlotter(new Metrics.Plotter("Hard Regen") {

			@Override
			public int getValue() {
				return finalHard;
			}
		});
		g2.addPlotter(new Metrics.Plotter("Soft Regen") {

			@Override
			public int getValue() {
				return finalSoft;
			}
		});
		g2.addPlotter(new Metrics.Plotter("Crystals Only") {

			@Override
			public int getValue() {
				return finalCrystal;
			}
		});

		// Metrics - Regeneration on Stop
		int regenOnStop = 0;
		for (final EndWorldHandler h : getWorldHandlers().values()) {
			if (h.getConfig().getHardRegenOnStop() == 1) {
				regenOnStop++;
			}
		}
		final int finalRegenOnStop = regenOnStop;
		final Metrics.Graph g3 = getMetrics().createGraph("Hard Regeneration on Stop");
		g3.addPlotter(new Metrics.Plotter("Enabled") {

			@Override
			public int getValue() {
				return finalRegenOnStop;
			}
		});
		g3.addPlotter(new Metrics.Plotter("Disabled") {

			@Override
			public int getValue() {
				return getWorldHandlers().size() - finalRegenOnStop;
			}
		});

		exiting(getClass(), "onNodeEnable");
		return true;
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes()
	 */
	@Override
	protected void handleOtherNodes() {
		// Nothing to do here for now
	}

	@Override
	public void onNodeDisable() {
		entering(getClass(), "onNodeDisable");

		for (final EndWorldHandler handler : worldHandlers.values()) {
			try {
				handler.unload(true);
			} catch (final InvalidConfigurationException e) {
				error("Unable to disable \"" + handler.getEndWorld().getName() + "\"'s world handler. Server should be " +
				      "stopped now (Were you reloading?)", e);
			}
		}

		exiting(getClass(), "onNodeDisable");
	}

	public Path getConfigFilePath(final String fileName) {
		return Paths.get(getDataFolder().getPath(), fileName + ".yml");
	}

	public boolean loadWorld(final World endWorld) throws InvalidConfigurationException {
		final EndWorldHandler handler = new EndWorldHandler(this, endWorld);
		try {
			handler.loadConfig();
			handler.loadChunks();
			worldHandlers.put(handler.getCamelCaseWorldName(), handler);
			handler.initLater();
			return true;
		} catch (final IOException e) {
			error("This error occured when NTheEndAgain tried to load " + e.getMessage() + ".yml", e);
			return false;
		}
	}

	/**
	 * @param lowerCamelCaseWorldName Key
	 *
	 * @return Value
	 */
	public EndWorldHandler getHandler(final String lowerCamelCaseWorldName) {
		return worldHandlers.get(lowerCamelCaseWorldName);
	}

	/**
	 * Activate the "Moved too quickly!" messages filter if at least one
	 * End world require it
	 */
	public void activateFilter() {
		boolean filterActivated = false;
		for (final EndWorldHandler handler : worldHandlers.values()) {
			if (handler.getConfig().getFilterMovedTooQuicklySpam() == 1) {
				debug("Filter needs to be actiavted for world " + handler.getEndWorld().getName());
				filterActivated = true;
				break;
			}
		}
		if (filterActivated) {
			debug("Filter activated!");
			getCore().getFilterManager().addDenyFilter(new MovedTooQuicklyDenyFilter(this));
		} else {
			debug("Filter was not activated");
		}
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public HashMap<String, EndWorldHandler> getWorldHandlers() {
		return worldHandlers;
	}

	// API for other nodes

	@Override
	public String getNodeName() {
		return THE_END_AGAIN;
	}
}
