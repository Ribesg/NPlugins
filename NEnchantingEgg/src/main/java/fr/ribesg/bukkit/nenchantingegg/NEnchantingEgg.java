/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - NEnchantingEgg.java        *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.nenchantingegg.altar.Altars;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.ActiveToEggProvidedTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.EggProvidedToItemProvidedTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.InactiveToActiveTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.ItemProvidedToLockedTransition;
import fr.ribesg.bukkit.nenchantingegg.lang.Messages;
import fr.ribesg.bukkit.nenchantingegg.listener.ItemListener;
import fr.ribesg.bukkit.nenchantingegg.listener.PlayerListener;
import fr.ribesg.bukkit.nenchantingegg.listener.WorldListener;
import fr.ribesg.bukkit.nenchantingegg.task.TimeListenerTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.mcstats.Metrics;

import java.io.IOException;

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

	@Override
	protected String getMinCoreVersion() {
		return "0.5.1";
	}

	@Override
	public boolean onNodeEnable() {
		entering(getClass(), "onNodeEnable");

		debug("Loading plugin Messages...");
		try {
			if (!getDataFolder().isDirectory()) {
				getDataFolder().mkdir();
			}
			messages = new Messages();
			messages.loadMessages(this);
		} catch (final IOException e) {
			error("An error occured when NEnchantingEgg tried to load messages.yml", e);
			return false;
		}

		debug("Initializing transitions...");
		inactiveToActiveTransition = new InactiveToActiveTransition(this);
		activeToEggProvidedTransition = new ActiveToEggProvidedTransition(this);
		eggProvidedToItemProvidedTransition = new EggProvidedToItemProvidedTransition(this);
		itemProvidedToLockedTransition = new ItemProvidedToLockedTransition(this);

		debug("Creating altars handler...");
		altars = new Altars(this);

		debug("Loading plugin config...");
		try {
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NEnchantingEgg tried to load config.yml", e);
			return false;
		}

		debug("Enabling altars...");
		altars.onEnable();

		final PluginManager pm = getServer().getPluginManager();
		debug("Creating listeners...");
		worldListener = new WorldListener(this);
		itemListener = new ItemListener(this);
		playerListener = new PlayerListener(this);
		debug("Registering listeners...");
		pm.registerEvents(worldListener, this);
		pm.registerEvents(itemListener, this);
		pm.registerEvents(playerListener, this);

		// debug("Registering commands...");
		// getCommand("theCommand").setExecutor(new NCommandExecutor(this));

		debug("Starting TimeListenerTask...");
		Bukkit.getScheduler().runTaskTimer(this, new TimeListenerTask(this), 100L, 50);

		debug("Handling Metrics...");
		final Metrics.Graph g = getMetrics().createGraph("Amount of Altars");
		g.addPlotter(new Metrics.Plotter() {

			@Override
			public int getValue() {
				return getAltars().getAltars().size();
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

		debug("Cancelling tasks...");
		Bukkit.getScheduler().cancelTasks(this);

		debug("Disbaling altars...");
		altars.onDisable();

		debug("Saving plugin config...");
		try {
			getPluginConfig().writeConfig();
		} catch (final IOException e) {
			error("An error occured when NEnchantingEgg tried to save config.yml", e);
		}

		altars = null;
		exiting(getClass(), "onNodeDisable");
	}

	public ActiveToEggProvidedTransition getActiveToEggProvidedTransition() {
		return activeToEggProvidedTransition;
	}

	public Altars getAltars() {
		return altars;
	}

	public EggProvidedToItemProvidedTransition getEggProvidedToItemProvidedTransition() {
		return eggProvidedToItemProvidedTransition;
	}

	public InactiveToActiveTransition getInactiveToActiveTransition() {
		return inactiveToActiveTransition;
	}

	public ItemProvidedToLockedTransition getItemProvidedToLockedTransition() {
		return itemProvidedToLockedTransition;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public ItemListener getItemListener() {
		return itemListener;
	}

	public PlayerListener getPlayerListener() {
		return playerListener;
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	// API for other nodes

	@Override
	public String getNodeName() {
		return ENCHANTING_EGG;
	}
}
