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
import fr.ribesg.bukkit.ncore.info.Info;
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
import fr.ribesg.bukkit.nplayer.user.PlayerListener;
import fr.ribesg.bukkit.nplayer.user.UserDb;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

import org.mcstats.Metrics;

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
        return "0.6.10";
    }

    @Override
    protected void loadMessages() throws IOException {
        this.debug("Loading plugin Messages...");
        if (!this.getDataFolder().isDirectory()) {
            this.getDataFolder().mkdir();
        }

        final Messages messages = new Messages();
        messages.loadMessages(this);

        this.messages = messages;
    }

    @Override
    public boolean onNodeEnable() {
        this.entering(this.getClass(), "onNodeEnable");

        this.debug("Loading plugin config...");
        try {
            this.pluginConfig = new Config(this);
            this.pluginConfig.loadConfig();
        } catch (final IOException | InvalidConfigurationException e) {
            this.error("An error occured when NPlayer tried to load config.yml", e);
            return false;
        }

        if (this.pluginConfig.getAuthenticationMode() != 1) {
            this.debug("Registering LoginRegisterFilter...");
            this.getCore().getFilterManager().addDenyFilter(new LoginRegisterFilter());

            this.debug("Initializing LoggedOutUserHandler...");
            this.loggedOutUserHandler = new LoggedOutUserHandler(this);
        } else {
            this.loggedOutUserHandler = null;
        }

        this.debug("Creating UserDb...");
        this.userDb = new UserDb(this);

        this.debug("Loading UserDb...");
        try {
            this.userDb.loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            this.error("An error occured when NPlayer tried to load userDB.yml", e);
            return false;
        }

        this.debug("Creating PunishmentDb...");
        this.punishmentDb = new PunishmentDb(this);

        this.debug("Loading PunishmentDb...");
        try {
            this.punishmentDb.loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            this.error("An error occured when NPlayer tried to load punishmentDB.yml", e);
            return false;
        }

        this.debug("Launching temporary punishments cleaner task...");
        new TemporaryPunishmentCleanerTask(this).runTaskTimer(this, 10L, 10L);

        this.debug("Creating and Registering Listeners...");
        final PluginManager pm = this.getServer().getPluginManager();
        if (this.loggedOutUserHandler != null) {
            pm.registerEvents(this.loggedOutUserHandler, this);
        }
        pm.registerEvents(new PlayerListener(this, this.loggedOutUserHandler), this);
        pm.registerEvents(new PunishmentListener(this), this);

        this.debug("Creating PlayerCommandHandler and registering commands...");
        final PlayerCommandHandler playerCommandHandler = new PlayerCommandHandler(this);
        this.setCommandExecutor("nplayer", playerCommandHandler);
        this.setCommandExecutor("login", playerCommandHandler);
        this.setCommandExecutor("register", playerCommandHandler);
        this.setCommandExecutor("logout", playerCommandHandler);
        // TODO setCommandExecutor("info", playerCommandHandler);
        this.setCommandExecutor("home", playerCommandHandler);
        this.setCommandExecutor("sethome", playerCommandHandler);
        this.setCommandExecutor("forcelogin", playerCommandHandler);

        this.debug("Creating PunishmentCommandHandler and registering commands...");
        final PunishmentCommandHandler punishmentCommandHandler = new PunishmentCommandHandler(this);
        this.setCommandExecutor("ban", punishmentCommandHandler);
        this.setCommandExecutor("banip", punishmentCommandHandler);
        this.setCommandExecutor("mute", punishmentCommandHandler);
        this.setCommandExecutor("jail", punishmentCommandHandler);
        this.setCommandExecutor("unban", punishmentCommandHandler);
        this.setCommandExecutor("unbanip", punishmentCommandHandler);
        this.setCommandExecutor("unmute", punishmentCommandHandler);
        this.setCommandExecutor("unjail", punishmentCommandHandler);
        this.setCommandExecutor("kick", punishmentCommandHandler);

        this.debug("Registering CommandHandler's Listeners...");
        pm.registerEvents(playerCommandHandler, this);

        this.debug("Initializing Metrics...");
        final Metrics.Graph g = this.getMetrics().createGraph("Amount of Players");
        g.addPlotter(new Metrics.Plotter("Registered") {

            @Override
            public int getValue() {
                return fr.ribesg.bukkit.nplayer.NPlayer.this.getUserDb().size();
            }
        });
        g.addPlotter(new Metrics.Plotter("Played in the last 2 weeks") {

            @Override
            public int getValue() {
                return fr.ribesg.bukkit.nplayer.NPlayer.this.getUserDb().recurrentSize();
            }
        });

        this.exiting(this.getClass(), "onNodeEnable");
        return true;
    }

    @Override
    protected void handleOtherNodes() {
        this.cuboidNode = this.getCore().getCuboidNode();
        if (this.cuboidNode == null) {
            this.info("NCuboid not found, Jail feature disabled");
        } else {
            this.info("NCuboid found, Jail feature enabled");
            for (final Punishment p : this.punishmentDb.getAllPunishments()) {
                if (p.getType() == PunishmentType.JAIL) {
                    final Jail jail = (Jail)p;
                    if (!this.cuboidNode.jail(UuidDb.getId(Node.PLAYER, jail.getPunished()), jail.getJailPointName())) {
                        this.error("Failed to jail player '" + jail.getPunished() + "' in NCuboid!");
                    }
                }
            }
        }
    }

    @Override
    public void onNodeDisable() {
        this.entering(this.getClass(), "onNodeDisable");

        this.debug("Saving config.yml...");
        try {
            this.pluginConfig.writeConfig();
        } catch (final IOException e) {
            this.error("An error occured when NPlayer tried to save config.yml", e);
        }

        this.debug("Saving userDB.yml...");
        try {
            this.userDb.saveConfig();
        } catch (final IOException e) {
            this.error("An error occured when NPlayer tried to save userDB.yml", e);
        }

        this.debug("Saving punishmentDB.yml");
        try {
            this.punishmentDb.saveConfig();
        } catch (final IOException e) {
            this.error("An error occured when NPlayer tried to save punishmentDB.yml", e);
        }

        this.exiting(this.getClass(), "onNodeDisable");
    }

    @Override
    public Messages getMessages() {
        return this.messages;
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    public CuboidNode getCuboidNode() {
        return this.cuboidNode;
    }

    public UserDb getUserDb() {
        return this.userDb;
    }

    public LoggedOutUserHandler getLoggedOutUserHandler() {
        return this.loggedOutUserHandler;
    }

    public PunishmentDb getPunishmentDb() {
        return this.punishmentDb;
    }

    // API for other nodes

    @Override
    public String getNodeName() {
        return PLAYER;
    }

    @Override
    public void populateInfo(final CommandSender sender, final String query, final Info infoObject) {
        // TODO Implement method
    }
}
