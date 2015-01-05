/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - NEnchantingEgg.java        *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg         *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.nenchantingegg.altar.Altars;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.ActiveToEggProvidedTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.EggProvidedToItemProvidedTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.InactiveToActiveTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.ItemProvidedToLockedTransition;
import fr.ribesg.bukkit.nenchantingegg.enchantment.Arboricide;
import fr.ribesg.bukkit.nenchantingegg.lang.Messages;
import fr.ribesg.bukkit.nenchantingegg.listener.ItemListener;
import fr.ribesg.bukkit.nenchantingegg.listener.PlayerListener;
import fr.ribesg.bukkit.nenchantingegg.listener.WorldListener;
import fr.ribesg.bukkit.nenchantingegg.task.TimeListenerTask;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

import org.mcstats.Metrics;

public class NEnchantingEgg extends NPlugin implements EnchantingEggNode {

    // Configs
    private Messages messages;
    private Config   pluginConfig;

    // Useful Nodes
    // // None

    // Actual plugin data
    private Altars altars;

    // Transitions
    private InactiveToActiveTransition          inactiveToActiveTransition;
    private ActiveToEggProvidedTransition       activeToEggProvidedTransition;
    private EggProvidedToItemProvidedTransition eggProvidedToItemProvidedTransition;
    private ItemProvidedToLockedTransition      itemProvidedToLockedTransition;

    // Listeners
    private WorldListener  worldListener;
    private ItemListener   itemListener;
    private PlayerListener playerListener;

    // Enchantments
    private Arboricide arboricide;

    @Override
    protected String getMinCoreVersion() {
        return "0.6.9";
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

        this.debug("Initializing transitions...");
        this.inactiveToActiveTransition = new InactiveToActiveTransition(this);
        this.activeToEggProvidedTransition = new ActiveToEggProvidedTransition(this);
        this.eggProvidedToItemProvidedTransition = new EggProvidedToItemProvidedTransition(this);
        this.itemProvidedToLockedTransition = new ItemProvidedToLockedTransition(this);

        this.debug("Creating altars handler...");
        this.altars = new Altars(this);

        this.debug("Loading plugin config...");
        try {
            this.pluginConfig = new Config(this);
            this.pluginConfig.loadConfig();
        } catch (final IOException | InvalidConfigurationException e) {
            this.error("An error occured when NEnchantingEgg tried to load config.yml", e);
            return false;
        }

        this.debug("Enabling altars...");
        this.altars.onEnable();

        final PluginManager pm = this.getServer().getPluginManager();
        this.debug("Creating listeners...");
        this.worldListener = new WorldListener(this);
        this.itemListener = new ItemListener(this);
        this.playerListener = new PlayerListener(this);
        this.debug("Registering listeners...");
        pm.registerEvents(this.worldListener, this);
        pm.registerEvents(this.itemListener, this);
        pm.registerEvents(this.playerListener, this);

        this.debug("Creating enchantments...");
        this.arboricide = new Arboricide(this);
        this.debug("Registering enchantments...");
        pm.registerEvents(this.arboricide, this);

        // debug("Registering commands...");
        // getCommand("theCommand").setExecutor(new NCommandExecutor(this));

        this.debug("Starting TimeListenerTask...");
        Bukkit.getScheduler().runTaskTimer(this, new TimeListenerTask(this), 100L, 50);

        this.debug("Handling Metrics...");
        final Metrics.Graph g = this.getMetrics().createGraph("Amount of Altars");
        g.addPlotter(new Metrics.Plotter() {

            @Override
            public int getValue() {
                return fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg.this.getAltars().getAltars().size();
            }
        });

        this.exiting(this.getClass(), "onNodeEnable");
        return true;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if ("nenchantingegg".equals(cmd.getName())) {
            if (args.length < 1) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                case "rld":
                    if (Perms.hasReload(sender)) {
                        if (args.length != 2) {
                            return false;
                        }
                        switch (args[1].toLowerCase()) {
                            case "messages":
                            case "mess":
                            case "mes":
                                try {
                                    this.loadMessages();
                                    this.sendMessage(sender, MessageId.cmdReloadMessages);
                                } catch (final IOException e) {
                                    this.error("An error occured when NEnchantingEgg tried to load messages.yml", e);
                                    this.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
                                }
                                return true;
                            default:
                                return false;
                        }
                    } else {
                        this.sendMessage(sender, MessageId.noPermissionForCommand);
                        return true;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void handleOtherNodes() {
        // Nothing to do here for now
    }

    @Override
    public void onNodeDisable() {
        this.entering(this.getClass(), "onNodeDisable");

        this.debug("Cancelling tasks...");
        Bukkit.getScheduler().cancelTasks(this);

        this.debug("Disbaling altars...");
        this.altars.onDisable();

        this.debug("Saving plugin config...");
        try {
            this.pluginConfig.writeConfig();
        } catch (final IOException e) {
            this.error("An error occured when NEnchantingEgg tried to save config.yml", e);
        }

        this.altars = null;
        this.exiting(this.getClass(), "onNodeDisable");
    }

    public ActiveToEggProvidedTransition getActiveToEggProvidedTransition() {
        return this.activeToEggProvidedTransition;
    }

    public Altars getAltars() {
        return this.altars;
    }

    public EggProvidedToItemProvidedTransition getEggProvidedToItemProvidedTransition() {
        return this.eggProvidedToItemProvidedTransition;
    }

    public InactiveToActiveTransition getInactiveToActiveTransition() {
        return this.inactiveToActiveTransition;
    }

    public ItemProvidedToLockedTransition getItemProvidedToLockedTransition() {
        return this.itemProvidedToLockedTransition;
    }

    public WorldListener getWorldListener() {
        return this.worldListener;
    }

    public ItemListener getItemListener() {
        return this.itemListener;
    }

    public PlayerListener getPlayerListener() {
        return this.playerListener;
    }

    public Arboricide getArboricide() {
        return this.arboricide;
    }

    @Override
    public Messages getMessages() {
        return this.messages;
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    // API for other nodes

    @Override
    public String getNodeName() {
        return ENCHANTING_EGG;
    }
}
