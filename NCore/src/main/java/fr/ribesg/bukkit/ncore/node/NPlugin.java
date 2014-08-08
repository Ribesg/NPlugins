/***************************************************************************
 * Project file:    NPlugins - NCore - NPlugin.java                        *
 * Full Class name: fr.ribesg.bukkit.ncore.node.NPlugin                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.VersionUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.mcstats.Metrics;

/**
 * This class represents a plugin node of the N plugin suite
 *
 * @author Ribesg
 */
public abstract class NPlugin extends JavaPlugin implements Node {

    private static final String CORE          = "NCore";
    private static final String NCORE_WEBSITE = "http://www.ribesg.fr/";

    private final Logger logger = this.getLogger();

    private NCore   core;
    private boolean enabled;
    private boolean debugEnabled;

    private Metrics metrics;

    @Override
    public void onEnable() {
        final FrameBuilder frame;
        this.core = (NCore)Bukkit.getPluginManager().getPlugin(CORE);
        if (this.badCoreVersion()) {

            frame = new FrameBuilder();
            frame.addLine("This plugin requires " + CORE + " v" + this.getMinCoreVersion(), FrameBuilder.Option.CENTER);
            frame.addLine(CORE + " plugin was found but the");
            frame.addLine("current version (v" + this.getCoreVersion() + ") is too low.");
            frame.addLine("See " + NCORE_WEBSITE);
            frame.addLine("Disabling plugin...");

            for (final String s : frame.build()) {
                this.error(s);
            }

            this.getPluginLoader().disablePlugin(this);
        } else /* Everything's ok */ {
            this.debugEnabled = this.core.getPluginConfig().isDebugEnabled(this.getName());
            if (this.debugEnabled) {
                this.info("DEBUG MODE ENABLED!");
            }
            try {
                this.metrics = new Metrics(this);
                this.metrics.start();
            } catch (final IOException e) {
                this.error("Failed to initialize Metrics", e);
            }
            try {
                this.loadMessages();
            } catch (final IOException e) {
                this.error("An error occured when N" + this.getNodeName() + " tried to load messages.yml", e);
            }
            final boolean activationResult = this.onNodeEnable();
            if (activationResult) {
                this.enabled = true;
                this.afterEnable();
            } else {
                // TODO Emergency mode
                this.getLogger().severe("Disabling plugin...");
                this.getPluginLoader().disablePlugin(this);
            }
        }
    }

    /**
     * Loads the Messages used by this Node.
     *
     * @throws IOException if it fails to load this Node's Messages
     */
    protected abstract void loadMessages() throws IOException;

    /**
     * Replace the normal {@link JavaPlugin#onEnable()} method in a normal plugin.
     *
     * @return If we should disable the plugin immediatly because we got a problem
     */
    protected abstract boolean onNodeEnable();

    /**
     * Call {@link #handleOtherNodes()} after plugin initialization tick.
     */
    private void afterEnable() {
        new BukkitRunnable() {

            @Override
            public void run() {
                fr.ribesg.bukkit.ncore.node.NPlugin.this.handleOtherNodes();
            }
        }.runTask(this);
    }

    /**
     * Connect this Node to other Nodes if needed
     * Called after every plugins has been loaded
     */
    protected abstract void handleOtherNodes();

    @Override
    public void onDisable() {
        if (this.enabled) {
            this.onNodeDisable();
        }
    }

    /**
     * Associate commands to their executors with a nullcheck.
     *
     * @param commandName the name of the command
     * @param executor    the executor
     *
     * @return if the command was successfully registered
     */
    public boolean setCommandExecutor(final String commandName, final CommandExecutor executor) {
        this.debug("- Registering command " + commandName);
        final PluginCommand cuboidCmd = this.getCommand(commandName);
        if (cuboidCmd != null) {
            cuboidCmd.setExecutor(executor);
            return true;
        } else {
            this.error("Command registered by another plugin: " + commandName);
            return false;
        }
    }

    /**
     * Replace the normal {@link JavaPlugin#onDisable()} method in a normal plugin.
     * Only here for compliance
     */
    protected abstract void onNodeDisable();

    private boolean badCoreVersion() {
        this.linkCore();
        return VersionUtil.compare(this.getCoreVersion(), this.getMinCoreVersion()) < 0;
    }

    public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
        final String[] m = this.getMessages().get(messageId, args);
        to.sendMessage(m);
    }

    public void broadcastMessage(final MessageId messageId, final String... args) {
        final String[] m = this.getMessages().get(messageId, args);
        for (final String mes : m) {
            this.getServer().broadcastMessage(mes);
        }
    }

    public void broadcastExcluding(final Player player, final MessageId messageId, final String... args) {
        final String[] m = this.getMessages().get(messageId, args);
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p != player) {
                p.sendMessage(m);
            }
        }
    }

    public abstract AbstractMessages getMessages();

    /**
     * Call the Core's Setter for this Node type
     * Basically: core.set[THIS]Node(this);
     */
    private void linkCore() {
        this.core.set(this.getNodeName(), this);
    }

    protected abstract String getMinCoreVersion();

    private String getCoreVersion() {
        return this.core.getDescription().getVersion();
    }

    public NCore getCore() {
        return this.core;
    }

    protected Metrics getMetrics() {
        return this.metrics;
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
