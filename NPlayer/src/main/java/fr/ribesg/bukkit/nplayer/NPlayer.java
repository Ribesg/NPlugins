package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.node.player.PlayerNode;
import fr.ribesg.bukkit.nplayer.lang.Messages;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentDb;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentListener;
import fr.ribesg.bukkit.nplayer.user.LoggedOutUserHandler;
import fr.ribesg.bukkit.nplayer.user.UserDb;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.mcstats.Metrics;

import java.io.IOException;

public class NPlayer extends PlayerNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;

	// Useful Nodes
	// // None

	// Plugin Data
	private UserDb               userDb;
	private LoggedOutUserHandler loggedOutUserHandler;
	private PunishmentDb         punishmentDb;

	@Override
	protected String getMinCoreVersion() {
		return "0.4.0";
	}

	@Override
	public boolean onNodeEnable() {
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
			getLogger().severe("This error occured when NPlayer tried to load messages.yml");
			return false;
		}

		// Config
		try {
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NPlayer tried to load config.yml");
			return false;
		}

		loggedOutUserHandler = new LoggedOutUserHandler(this);

		userDb = new UserDb(this);
		try {
			userDb.loadConfig();
		} catch (IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NPlayer tried to load userDB.yml");
			return false;
		}

		punishmentDb = new PunishmentDb(this);
		try {
			punishmentDb.loadConfig();
		} catch (IOException | InvalidConfigurationException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NPlayer tried to load punishmentDB.yml");
			return false;
		}

		// Listener
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(loggedOutUserHandler, this);
		pm.registerEvents(new PunishmentListener(this), this);

		// Commands

		final PlayerCommandHandler playerCommandHandler = new PlayerCommandHandler(this);
		getCommand("login").setExecutor(playerCommandHandler);
		getCommand("register").setExecutor(playerCommandHandler);
		getCommand("logout").setExecutor(playerCommandHandler);
		// TODO getCommand("info").setExecutor(playerCommandHandler);
		getCommand("home").setExecutor(playerCommandHandler);
		getCommand("sethome").setExecutor(playerCommandHandler);

		final PunishmentCommandHandler punishmentCommandHandler = new PunishmentCommandHandler(this);
		getCommand("ban").setExecutor(punishmentCommandHandler);
		getCommand("banip").setExecutor(punishmentCommandHandler);
		getCommand("mute").setExecutor(punishmentCommandHandler);
		getCommand("jail").setExecutor(punishmentCommandHandler);
		getCommand("unban").setExecutor(punishmentCommandHandler);
		getCommand("unbanip").setExecutor(punishmentCommandHandler);
		getCommand("unmute").setExecutor(punishmentCommandHandler);
		getCommand("unjail").setExecutor(punishmentCommandHandler);
		getCommand("kick").setExecutor(punishmentCommandHandler);

		// CommandHandler's Listeners
		pm.registerEvents(playerCommandHandler, this);

		// Metrics
		final Metrics.Graph g1 = getMetrics().createGraph("Number of registered Players");
		g1.addPlotter(new Metrics.Plotter() {

			@Override
			public int getValue() {
				return getUserDb().size();
			}
		});
		final Metrics.Graph g2 = getMetrics().createGraph("Number of recurrent registered Players");
		g2.addPlotter(new Metrics.Plotter() {

			@Override
			public int getValue() {
				return getUserDb().recurrentSize();
			}
		});

		return true;
	}

	@Override
	public void onNodeDisable() {
		try {
			getPluginConfig().writeConfig();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		try {
			userDb.saveConfig();
		} catch (IOException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NPlayer tried to save userDB.yml");
		}
		try {
			punishmentDb.saveConfig();
		} catch (IOException e) {
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NPlayer tried to save punishmentDB.yml");
		}
	}

	@Override
	protected void linkCore() {
		getCore().setPlayerNode(this);
	}

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes() */
	@Override
	protected void handleOtherNodes() {
		// Nothing to do here for now
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	public UserDb getUserDb() {
		return userDb;
	}

	public LoggedOutUserHandler getLoggedOutUserHandler() {
		return loggedOutUserHandler;
	}

	public PunishmentDb getPunishmentDb() {
		return punishmentDb;
	}
}
