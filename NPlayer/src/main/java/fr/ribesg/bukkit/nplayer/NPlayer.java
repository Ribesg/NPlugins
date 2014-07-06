/***************************************************************************
 * Project file:    NPlugins - NPlayer - NPlayer.java                      *
 * Full Class name: fr.ribesg.bukkit.nplayer.NPlayer                       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.node.player.PlayerNode;
import fr.ribesg.bukkit.nplayer.lang.Messages;
import fr.ribesg.bukkit.nplayer.punishment.Jail;
import fr.ribesg.bukkit.nplayer.punishment.Punishment;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentDb;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentListener;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentType;
import fr.ribesg.bukkit.nplayer.punishment.TemporaryPunishmentCleanerTask;
import fr.ribesg.bukkit.nplayer.user.LoggedOutUserHandler;
import fr.ribesg.bukkit.nplayer.user.UserDb;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.mcstats.Metrics;

import java.io.IOException;

public class NPlayer extends NPlugin implements PlayerNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;

	// Useful Nodes
	private CuboidNode cuboidNode;

	// Plugin Data
	private UserDb               userDb;
	private LoggedOutUserHandler loggedOutUserHandler;
	private PunishmentDb         punishmentDb;

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

		getCore().getFilterManager().addDenyFilter(new LoginRegisterFilter());

		debug("Loading plugin config...");
		try {
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NPlayer tried to load config.yml", e);
			return false;
		}

		debug("Initializing LoggedOutUserHandler...");
		loggedOutUserHandler = new LoggedOutUserHandler(this);

		debug("Creating UserDb...");
		userDb = new UserDb(this);

		debug("Loading UserDb...");
		try {
			userDb.loadConfig();
		} catch (IOException | InvalidConfigurationException e) {
			error("An error occured when NPlayer tried to load userDB.yml", e);
			return false;
		}

		debug("Creating PunishmentDb...");
		punishmentDb = new PunishmentDb(this);

		debug("Loading PunishmentDb...");
		try {
			punishmentDb.loadConfig();
		} catch (IOException | InvalidConfigurationException e) {
			error("An error occured when NPlayer tried to load punishmentDB.yml", e);
			return false;
		}

		debug("Launching temporary punishments cleaner task...");
		new TemporaryPunishmentCleanerTask(this).runTaskTimer(this, 10L, 10L);

		debug("Creating and Registering Listeners...");
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(loggedOutUserHandler, this);
		pm.registerEvents(new PunishmentListener(this), this);

		debug("Creating PlayerCommandHandler and registering commands...");
		final PlayerCommandHandler playerCommandHandler = new PlayerCommandHandler(this);
		setCommandExecutor("nplayer", playerCommandHandler);
		setCommandExecutor("login", playerCommandHandler);
		setCommandExecutor("register", playerCommandHandler);
		setCommandExecutor("logout", playerCommandHandler);
		// TODO setCommandExecutor("info", playerCommandHandler);
		setCommandExecutor("home", playerCommandHandler);
		setCommandExecutor("sethome", playerCommandHandler);
		setCommandExecutor("forcelogin", playerCommandHandler);

		debug("Creating PunishmentCommandHandler and registering commands...");
		final PunishmentCommandHandler punishmentCommandHandler = new PunishmentCommandHandler(this);
		setCommandExecutor("ban", punishmentCommandHandler);
		setCommandExecutor("banip", punishmentCommandHandler);
		setCommandExecutor("mute", punishmentCommandHandler);
		setCommandExecutor("jail", punishmentCommandHandler);
		setCommandExecutor("unban", punishmentCommandHandler);
		setCommandExecutor("unbanip", punishmentCommandHandler);
		setCommandExecutor("unmute", punishmentCommandHandler);
		setCommandExecutor("unjail", punishmentCommandHandler);
		setCommandExecutor("kick", punishmentCommandHandler);

		debug("Registering CommandHandler's Listeners...");
		pm.registerEvents(playerCommandHandler, this);

		debug("Initializing Metrics...");
		final Metrics.Graph g = getMetrics().createGraph("Amount of Players");
		g.addPlotter(new Metrics.Plotter("Registered") {

			@Override
			public int getValue() {
				return getUserDb().size();
			}
		});
		g.addPlotter(new Metrics.Plotter("Played in the last 2 weeks") {

			@Override
			public int getValue() {
				return getUserDb().recurrentSize();
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
		this.cuboidNode = getCore().getCuboidNode();
		if (this.cuboidNode == null) {
			info("NCuboid not found, Jail feature disabled");
		} else {
			info("NCuboid found, Jail feature enabled");
			for (final Punishment p : punishmentDb.getAllPunishments()) {
				if (p.getType() == PunishmentType.JAIL) {
					final Jail jail = (Jail) p;
					if (!cuboidNode.jail(UuidDb.getId(Node.PLAYER, jail.getPunished()), jail.getJailPointName())) {
						error("Failed to jail player '" + jail.getPunished() + "' in NCuboid!");
					}
				}
			}
		}
	}

	@Override
	public void onNodeDisable() {
		entering(getClass(), "onNodeDisable");

		debug("Saving config.yml...");
		try {
			getPluginConfig().writeConfig();
		} catch (final IOException e) {
			error("An error occured when NPlayer tried to save config.yml", e);
		}

		debug("Saving userDB.yml...");
		try {
			userDb.saveConfig();
		} catch (final IOException e) {
			error("An error occured when NPlayer tried to save userDB.yml", e);
		}

		debug("Saving punishmentDB.yml");
		try {
			punishmentDb.saveConfig();
		} catch (final IOException e) {
			error("An error occured when NPlayer tried to save punishmentDB.yml", e);
		}

		exiting(getClass(), "onNodeDisable");
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	public CuboidNode getCuboidNode() {
		return cuboidNode;
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

	// API for other nodes

	@Override
	public String getNodeName() {
		return PLAYER;
	}
}
