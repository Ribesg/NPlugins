/***************************************************************************
 * Project file:    NPlugins - NCore - NPlugin.java                        *
 * Full Class name: fr.ribesg.bukkit.ncore.node.NPlugin                    *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.lang.AbstractMessages;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

import java.io.IOException;

/**
 * This class represents a plugin node of the N plugin suite
 *
 * @author Ribesg
 */
public abstract class NPlugin extends JavaPlugin {

	private static final String CORE          = "Core";
	private static final String NCORE_WEBSITE = "http://www.ribesg.fr/";

	private NCore core;
	private boolean enabled = false;

	private Metrics metrics;

	@Override
	public void onEnable() {
		FrameBuilder frame;
		core = (NCore) Bukkit.getPluginManager().getPlugin(CORE);
		if (badCoreVersion()) {

			frame = new FrameBuilder();
			frame.addLine("This plugin requires " + CORE + " v" + getMinCoreVersion(), FrameBuilder.Option.CENTER);
			frame.addLine(CORE + " plugin was found but the");
			frame.addLine("current version (v" + getCoreVersion() + ") is too low.");
			frame.addLine("See " + NCORE_WEBSITE);
			frame.addLine("Disabling plugin...");

			for (final String s : frame.build()) {
				getLogger().severe(s);
			}

			getPluginLoader().disablePlugin(this);
		} else /* Everything's ok */ {
			try {
				metrics = new Metrics(this);
				metrics.start();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			boolean activationResult = onNodeEnable();
			if (activationResult) {
				enabled = true;
				afterEnable();
			} else {
				// TODO Emergency mode
				getLogger().severe("Disabling plugin...");
				getPluginLoader().disablePlugin(this);
			}
		}
	}

	/**
	 * Replace the normal {@link org.bukkit.plugin.java.JavaPlugin#onEnable()} method in a normal plugin.
	 *
	 * @return If we should disable the plugin immediatly because we got a problem
	 */
	protected abstract boolean onNodeEnable();

	/** Call {@link #handleOtherNodes()} after plugin initialization tick. */
	private void afterEnable() {
		new BukkitRunnable() {

			@Override
			public void run() {
				handleOtherNodes();
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
		if (enabled) {
			onNodeDisable();
		}
	}

	/**
	 * Replace the normal {@link org.bukkit.plugin.java.JavaPlugin#onDisable()} method in a normal plugin.
	 * Only here for compliance
	 */
	protected abstract void onNodeDisable();

	private boolean badCoreVersion() {
		linkCore();
		return getCoreVersion().compareTo(getMinCoreVersion()) < 0;
	}

	public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
		final String[] m = getMessages().get(messageId, args);
		to.sendMessage(m);
	}

	public void broadcastMessage(final MessageId messageId, final String... args) {
		final String[] m = getMessages().get(messageId, args);
		for (final String mes : m) {
			getServer().broadcastMessage(mes);
		}
	}

	public void broadcastExcluding(final Player player, final MessageId messageId, final String... args) {
		final String[] m = getMessages().get(messageId, args);
		for (final Player p : Bukkit.getOnlinePlayers()) {
			if (p != player) {
				for (final String mes : m) {
					getServer().broadcastMessage(mes);
				}
			}
		}
	}

	public abstract AbstractMessages getMessages();

	/**
	 * Call the Core's Setter for this Node type
	 * Basically: core.set[THIS]Node(this);
	 */
	protected abstract void linkCore();

	protected abstract String getMinCoreVersion();

	private String getCoreVersion() {
		return getCore().getDescription().getVersion();
	}

	protected NCore getCore() {
		return core;
	}

	protected Metrics getMetrics() {
		return metrics;
	}
}
