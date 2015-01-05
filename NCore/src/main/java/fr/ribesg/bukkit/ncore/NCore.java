/***************************************************************************
 * Project file:    NPlugins - NCore - NCore.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.NCore                           *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.common.logging.FilterManager;
import fr.ribesg.bukkit.ncore.config.Config;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.event.NEventsListener;
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
import fr.ribesg.bukkit.ncore.util.FrameBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.mcstats.Metrics;

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
    private UuidDb            uuidDb;
    private Updater           updater;
    private boolean           debugEnabled;

    @Override
    public void onEnable() {
        this.logger = this.getLogger();
        this.filterManager = FilterManager.create();

        try {
            this.metrics = new Metrics(this);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Config
        try {
            this.pluginConfig = new Config(this);
            this.pluginConfig.loadConfig();
        } catch (final IOException | InvalidConfigurationException e) {
            this.logger.log(Level.SEVERE, "An error occured when NCore tried to load config.yml", e);
        }

        if (this.pluginConfig.getDebugEnabled().contains(this.getName())) {
            this.debugEnabled = true;
            this.info("DEBUG MODE ENABLED!");
        }

        try {
            this.uuidDb = new UuidDb(this);
            this.uuidDb.loadConfig();
        } catch (final IOException | InvalidConfigurationException e) {
            this.logger.log(Level.SEVERE, "An error occured when NCore tried to load uuidDb.yml", e);
        }

        this.nodes = new HashMap<>();

        Bukkit.getScheduler().runTaskLaterAsynchronously(this, new BukkitRunnable() {

            @Override
            public void run() {
                NCore.this.afterNodesLoad();
            }
        }, 5 * 20L /* ~5 seconds */);

        Bukkit.getPluginManager().registerEvents(new NEventsListener(this), this);
        Bukkit.getPluginManager().registerEvents(new UpdaterListener(this), this);

        new NCommandExecutor(this);
    }

    @Override
    public void onDisable() {
        try {
            this.uuidDb.writeConfig();
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "An error occured when NCore tried to save uuidDb.yml", e);
        }
    }

    private void afterNodesLoad() {
        boolean noNodeFound = true;
        final Metrics.Graph nodesUsedGraph = this.metrics.createGraph("Nodes used");

        if (this.get(Node.CUBOID) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.CUBOID.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        if (this.get(Node.ENCHANTING_EGG) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.ENCHANTING_EGG.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        if (this.get(Node.GENERAL) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.GENERAL.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        if (this.get(Node.PLAYER) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.PLAYER.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        if (this.get(Node.PERMISSIONS) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.PERMISSIONS.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        if (this.get(Node.TALK) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.TALK.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        if (this.get(Node.THE_END_AGAIN) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.THE_END_AGAIN.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        if (this.get(Node.WORLD) != null) {
            nodesUsedGraph.addPlotter(new Metrics.Plotter(Node.WORLD.substring(1)) {

                @Override
                public int getValue() {
                    return 1;
                }
            });
            noNodeFound = false;
        }

        this.metrics.start();

        if (noNodeFound) {
            final FrameBuilder frame = new FrameBuilder();
            frame.addLine("This plugin can be safely removed", FrameBuilder.Option.CENTER);
            frame.addLine("It seems that you are using this plugin, NCore, while note using any");
            frame.addLine("node of the NPlugins suite. Maybe you forgot to add the Node(s) you");
            frame.addLine("wanted to use, or you forgot to remove NCore after removing all nodes.");
            frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);

            for (final String s : frame.build()) {
                this.logger.severe(s);
            }

            this.getPluginLoader().disablePlugin(this);
        } else if (this.pluginConfig.isUpdateCheck()) {
            this.updater = new Updater(this, 'v' + this.getDescription().getVersion(), this.pluginConfig.getProxy(), this.pluginConfig.getApiKey());
            if (this.pluginConfig.getUpdateCheckInterval() > 0) {
                this.updater.startTask();
            }
        }
    }

    public Node get(final String nodeName) {
        return this.nodes.get(nodeName.toLowerCase());
    }

    public CuboidNode getCuboidNode() {
        return (CuboidNode)this.get(Node.CUBOID);
    }

    public EnchantingEggNode getEnchantingEggNode() {
        return (EnchantingEggNode)this.get(Node.ENCHANTING_EGG);
    }

    public GeneralNode getGeneralNode() {
        return (GeneralNode)this.get(Node.GENERAL);
    }

    public PlayerNode getPlayerNode() {
        return (PlayerNode)this.get(Node.PLAYER);
    }

    public PermissionsNode getPermissionsNode() {
        return (PermissionsNode)this.get(Node.PERMISSIONS);
    }

    public TalkNode getTalkNode() {
        return (TalkNode)this.get(Node.TALK);
    }

    public TheEndAgainNode getTheEndAgainNode() {
        return (TheEndAgainNode)this.get(Node.THE_END_AGAIN);
    }

    public WorldNode getWorldNode() {
        return (WorldNode)this.get(Node.WORLD);
    }

    public void set(final String nodeName, final Node node) {
        if (this.nodes.containsKey(nodeName.toLowerCase())) {
            throw new IllegalStateException("Registering the same node twice!");
        } else {
            this.nodes.put(nodeName.toLowerCase(), node);
        }
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    public Updater getUpdater() {
        return this.updater;
    }

    public FilterManager getFilterManager() {
        return this.filterManager;
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
        this.logger.log(level, message);
    }

    public void info(final String message) {
        this.log(Level.INFO, message);
    }

    public void entering(final Class clazz, final String methodName) {
        if (this.debugEnabled) {
            this.log(Level.INFO, "DEBUG >>> '" + methodName + "' in " + this.shortNPluginPackageName(clazz.getName()));
        }
    }

    public void entering(final Class clazz, final String methodName, final String comment) {
        if (this.debugEnabled) {
            this.log(Level.INFO, "DEBUG >>> '" + methodName + "' in " + this.shortNPluginPackageName(clazz.getName()) + " (" + comment + ')');
        }
    }

    public void exiting(final Class clazz, final String methodName) {
        if (this.debugEnabled) {
            this.log(Level.INFO, "DEBUG <<< '" + methodName + "' in " + this.shortNPluginPackageName(clazz.getName()));
        }
    }

    public void exiting(final Class clazz, final String methodName, final String comment) {
        if (this.debugEnabled) {
            this.log(Level.INFO, "DEBUG <<< '" + methodName + "' in " + this.shortNPluginPackageName(clazz.getName()) + " (" + comment + ')');
        }
    }

    private String shortNPluginPackageName(final String packageName) {
        return packageName.substring(17);
    }

    public void debug(final String message) {
        if (this.debugEnabled) {
            this.log(Level.INFO, "DEBUG         " + message);
        }
    }

    public void debug(final String message, final Throwable e) {
        if (this.debugEnabled) {
            this.logger.log(Level.SEVERE, "DEBUG         " + message, e);
        }
    }

    public void error(final String message) {
        this.error(Level.SEVERE, message);
    }

    public void error(final Level level, final String message) {
        this.log(level, message);
    }

    public void error(final String message, final Throwable e) {
        this.error(Level.SEVERE, message, e);
    }

    public void error(final Level level, final String message, final Throwable e) {
        this.logger.log(level, message, e);
    }
}
