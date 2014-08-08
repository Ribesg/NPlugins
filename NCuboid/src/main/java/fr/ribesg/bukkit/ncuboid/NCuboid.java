/***************************************************************************
 * Project file:    NPlugins - NCuboid - NCuboid.java                      *
 * Full Class name: fr.ribesg.bukkit.ncuboid.NCuboid                       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import fr.ribesg.bukkit.ncuboid.beans.Jail;
import fr.ribesg.bukkit.ncuboid.beans.Jails;
import fr.ribesg.bukkit.ncuboid.beans.RegionDb;
import fr.ribesg.bukkit.ncuboid.beans.RegionDbPersistenceHandler;
import fr.ribesg.bukkit.ncuboid.beans.WorldRegion;
import fr.ribesg.bukkit.ncuboid.commands.MainCommandExecutor;
import fr.ribesg.bukkit.ncuboid.config.Config;
import fr.ribesg.bukkit.ncuboid.dynmap.DynmapBridge;
import fr.ribesg.bukkit.ncuboid.lang.Messages;
import fr.ribesg.bukkit.ncuboid.listeners.EventExtensionListener;
import fr.ribesg.bukkit.ncuboid.listeners.PlayerStickListener;
import fr.ribesg.bukkit.ncuboid.listeners.WorldLoadingListener;
import fr.ribesg.bukkit.ncuboid.listeners.attribute.MessageListener;
import fr.ribesg.bukkit.ncuboid.listeners.attribute.RespawnListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.BoosterFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.BuildFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ChatFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ChestFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ClosedFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.CreativeFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.DropFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.EndermanGriefFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ExplosionFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.FarmFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.FireFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.GodFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.InvisibleFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.JailFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.MobFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.PassFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.PvpFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.SnowFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.TeleportFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.UseFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.WarpgateFlagListener;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import org.mcstats.Metrics;

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

	// Jails
	private Jails jails;

	// Dynmap!
	private DynmapBridge dynmapBridge;

	@Override
	protected String getMinCoreVersion() {
		return "0.6.9";
	}

	@Override
	public void loadMessages() throws IOException {
		this.debug("Loading plugin Messages...");
		if (!this.getDataFolder().isDirectory()) {
			this.getDataFolder().mkdir();
		}

		final Messages messages = new Messages();
		messages.loadMessages(this);

		this.messages = messages;
	}

	@Override
	protected boolean onNodeEnable() {
		this.entering(this.getClass(), "onNodeEnable");

		this.debug("Loading plugin configuration...");
		try {
			this.pluginConfig = new Config(this);
			this.pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			this.error("An error occured when NCuboid tried to load config.yml", e);
			return false;
		}

		this.debug("Creating Dynmap Bridge...");
		this.dynmapBridge = new DynmapBridge();

		this.debug("Creating Jail system...");
		this.jails = new Jails(this);

		this.debug("Loading Regions...");
		try {
			this.db = RegionDbPersistenceHandler.loadDb(this);
		} catch (final IOException | InvalidConfigurationException e) {
			this.error("An error occured when NCuboid tried to load cuboidDB.yml", e);
			return false;
		}

		this.debug("Creating and registering listeners...");
		final PluginManager pm = this.getServer().getPluginManager();

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
		pm.registerEvents(new JailFlagListener(this), this);
		pm.registerEvents(new MobFlagListener(this), this);
		pm.registerEvents(new PassFlagListener(this), this);
		pm.registerEvents(new PvpFlagListener(this), this);
		pm.registerEvents(new SnowFlagListener(this), this);
		pm.registerEvents(new TeleportFlagListener(this), this);
		pm.registerEvents(new UseFlagListener(this), this);
		pm.registerEvents(new WarpgateFlagListener(this), this);

		// Attribute listeners
		pm.registerEvents(new MessageListener(this), this);
		pm.registerEvents(new RespawnListener(this), this);

		this.debug("Registering command...");
		this.setCommandExecutor("ncuboid", new MainCommandExecutor(this));

		this.debug("Initializing Dynmap bridge...");
		this.dynmapBridge.initialize(this.db);

		this.debug("Initializing Metrics...");
		final Metrics.Graph g = this.getMetrics().createGraph("Amount of Regions");
		g.addPlotter(new Metrics.Plotter() {

			@Override
			public int getValue() {
				return fr.ribesg.bukkit.ncuboid.NCuboid.this.getDb().size();
			}
		});

		this.exiting(this.getClass(), "onNodeEnable");
		return true;
	}

	@Override
	protected void handleOtherNodes() {
		this.entering(this.getClass(), "handleOtherNodes");

		this.debug("Seeking new worlds...");
		for (final World world : this.getServer().getWorlds()) {
			if (this.db.getByWorld(world.getName()) == null) {
				this.debug("  New world found: " + world.getName());
				this.db.addByWorld(new WorldRegion(world.getName()));
			}
		}

		this.exiting(this.getClass(), "handleOtherNodes");
	}

	@Override
	protected void onNodeDisable() {
		this.entering(this.getClass(), "onNodeDisable");

		this.debug("Saving Regions...");
		try {
			RegionDbPersistenceHandler.saveDb(this, this.db);
		} catch (final IOException e) {
			this.error("An error occured when NCuboid tried to save cuboidDB.yml", e);
		}

		this.exiting(this.getClass(), "onNodeDisable");
	}

	public RegionDb getDb() {
		return this.db;
	}

	public void setDb(final RegionDb db) {
		this.db = db;
	}

	@Override
	public Messages getMessages() {
		return this.messages;
	}

	public Config getPluginConfig() {
		return this.pluginConfig;
	}

	public DynmapBridge getDynmapBridge() {
		return this.dynmapBridge;
	}

	public Jails getJails() {
		return this.jails;
	}

	public boolean shouldShow(final Player player) {
		final GeneralNode general = this.getCore().getGeneralNode();
		return general == null || !general.isSpy(player.getUniqueId());
	}

	// API for other nodes

	@Override
	public String getNodeName() {
		return CUBOID;
	}

	@Override
	public boolean isJailed(final UUID id) {
		this.entering(this.getClass(), "isJailed", "id=" + id);
		final boolean result = this.jails.isJailed(id);
		this.exiting(this.getClass(), "isJailed", "result=" + result);
		return result;
	}

	@Override
	public boolean jail(final UUID id, final String jailName) {
		if (this.isDebugEnabled()) {
			this.entering(this.getClass(), "jail", "id=" + id + ";jailName=" + jailName);
		}
		final boolean result = this.jails.jail(id, jailName);
		this.exiting(this.getClass(), "jail", "result=" + result);
		return result;
	}

	@Override
	public boolean unJail(final UUID id) {
		this.entering(this.getClass(), "unJail", "id=" + id);
		final boolean result = this.jails.unJail(id);
		this.exiting(this.getClass(), "unJail", "result=" + result);
		return result;
	}

	@Override
	public Set<String> getJailsSet() {
		return this.jails.getJailNames();
	}

	@Override
	public NLocation getJailLocation(final String jailName) {
		this.entering(this.getClass(), "getJailLocation", "jailName=" + jailName);

		final Jail jail = this.jails.getByName(jailName);
		if (jail != null) {
			this.exiting(this.getClass(), "getJailLocation");
			return jail.getLocation();
		} else {
			this.exiting(this.getClass(), "getJailLocation", "Failed: unknown jail '" + jailName + '\'');
			return null;
		}
	}

	@Override
	public boolean isInInvisibleRegion(final Player player) {
		for (final GeneralRegion r : this.db.getAllByLocation(player.getLocation())) {
			if (r.getFlag(Flag.INVISIBLE)) {
				return true;
			}
		}
		return false;
	}
}
