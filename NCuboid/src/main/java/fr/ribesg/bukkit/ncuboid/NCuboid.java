/***************************************************************************
 * Project file:    NPlugins - NCuboid - NCuboid.java                      *
 * Full Class name: fr.ribesg.bukkit.ncuboid.NCuboid                       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.RegionDbPersistenceHandler;
import fr.ribesg.bukkit.ncuboid.beans.WorldRegion;
import fr.ribesg.bukkit.ncuboid.commands.MainCommandExecutor;
import fr.ribesg.bukkit.ncuboid.config.Config;
import fr.ribesg.bukkit.ncuboid.dynmap.DynmapBridge;
import fr.ribesg.bukkit.ncuboid.jail.JailHandler;
import fr.ribesg.bukkit.ncuboid.lang.Messages;
import fr.ribesg.bukkit.ncuboid.listeners.EventExtensionListener;
import fr.ribesg.bukkit.ncuboid.listeners.PlayerStickListener;
import fr.ribesg.bukkit.ncuboid.listeners.WorldLoadingListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.*;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.List;

/**
 * TODO
 *
 * @author Ribesg
 */
public class NCuboid extends NPlugin implements CuboidNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;

	// Useful Nodes
	// // None

	// Cuboids base
	private RegionDb db;

	// Jail handling
	private JailHandler jailHandler;

	// Dynmap!
	private DynmapBridge dynmapBridge;

	@Override
	protected String getMinCoreVersion() {
		return "0.5.1";
	}

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#onNodeEnable() */
	@Override
	protected boolean onNodeEnable() {
		// Messages first !
		try {
			if (!getDataFolder().isDirectory()) {
				getDataFolder().mkdir();
			}
			messages = new Messages();
			messages.loadMessages(this);
		} catch (final IOException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NCuboid tried to load messages.yml");
			return false;
		}

		// Config
		try {
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NCuboid tried to load config.yml");
			return false;
		}

		// Dynmap Bridge! Before loading Regions!
		this.dynmapBridge = new DynmapBridge();

		// Create the RegionDb
		try {
			db = RegionDbPersistenceHandler.loadDb(this);
		} catch (final IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NCuboid tried to load cuboidDB.yml");
			return false;
		}

		// Handle jail system
		jailHandler = new JailHandler(this);
		jailHandler.loadJails();

		// Listeners
		final PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new EventExtensionListener(this), this);
		pm.registerEvents(new PlayerStickListener(this), this);
		pm.registerEvents(new WorldLoadingListener(this), this);

		// Flag Listeners
		pm.registerEvents(new BoosterFlagListener(this), this);
		pm.registerEvents(new BuildFlagListener(this), this);
		pm.registerEvents(new ChatFlagListener(this), this);
		pm.registerEvents(new ChestFlagListener(this), this);
		pm.registerEvents(new ClosedFlagListener(this), this);
		pm.registerEvents(new CreativeFlagListener(this), this);
		pm.registerEvents(new DropFlagListener(this), this);
		pm.registerEvents(new EndermanGriefFlagListener(this), this);
		pm.registerEvents(new ExplosionFlagListener(this), this);
		pm.registerEvents(new FarmFlagListener(this), this);
		pm.registerEvents(new FireFlagListener(this), this);
		pm.registerEvents(new GodFlagListener(this), this);
		pm.registerEvents(new InvisibleFlagListener(this), this);
		pm.registerEvents(new MobFlagListener(this), this);
		pm.registerEvents(new PassFlagListener(this), this);
		pm.registerEvents(new PvpFlagListener(this), this);
		pm.registerEvents(new SnowFlagListener(this), this);
		pm.registerEvents(new TeleportFlagListener(this), this);
		pm.registerEvents(new UseFlagListener(this), this);
		pm.registerEvents(new WarpgateFlagListener(this), this);

		// Command
		getCommand("cuboid").setExecutor(new MainCommandExecutor(this));

		// Dynmap Bridge! After loading Regions!
		this.dynmapBridge.initialize(this.db);

		// Metrics - Number of Regions
		final Metrics.Graph g = getMetrics().createGraph("Amount of Regions");
		g.addPlotter(new Metrics.Plotter() {

			@Override
			public int getValue() {
				return getDb().size();
			}
		});

		return true;
	}

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes() */
	@Override
	protected void handleOtherNodes() {
		// See if there are new worlds
		for (final World world : getServer().getWorlds()) {
			if (db.getByWorld(world.getName()) == null) {
				db.addByWorld(new WorldRegion(world.getName()));
			}
		}
	}

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#onNodeDisable() */
	@Override
	protected void onNodeDisable() {
		try {
			RegionDbPersistenceHandler.saveDb(this, getDb());
		} catch (final IOException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NCuboid tried to save cuboidDB.yml");
		}
	}

	public RegionDb getDb() {
		return db;
	}

	public void setDb(final RegionDb db) {
		this.db = db;
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	public DynmapBridge getDynmapBridge() {
		return dynmapBridge;
	}

	// API for other nodes

	@Override
	public String getNodeName() {
		return CUBOID;
	}

	@Override
	public boolean isJailed(final String playerName) {
		return jailHandler.isJailed(playerName);
	}

	@Override
	public boolean jail(final String playerName, final String jailName) {
		return jailHandler.jail(playerName, jailName);
	}

	@Override
	public boolean unJail(final String playerName) {
		return jailHandler.unJail(playerName);
	}

	@Override
	public List<String> getJailList() {
		return jailHandler.getJailList();
	}
}
