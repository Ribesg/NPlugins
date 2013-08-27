package fr.ribesg.bukkit.ncore.node;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.common.FrameBuilder;
import fr.ribesg.bukkit.ncore.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

/**
 * This class represents a plugin node of the N plugin suite
 *
 * @author Ribesg
 */
public abstract class NPlugin extends JavaPlugin {

	private static final String NCORE         = "NCore";
	private static final String NCORE_WEBSITE = "http://www.ribesg.fr/";

	private Metrics metrics;

	private NCore core;
	private boolean enabled = false;

	@Override
	public void onEnable() {
		FrameBuilder frame;
		if (isCoreMissing()) {
			frame = new FrameBuilder();
			frame.addLine("This plugin requires NCore", FrameBuilder.Option.CENTER);
			frame.addLine("It is an additional Plugin you");
			frame.addLine("should put in you /plugins folder.");
			frame.addLine("See " + NCORE_WEBSITE);
			frame.addLine("Disabling plugin...");

			for (final String s : frame.build()) {
				getLogger().severe(s);
			}

			getPluginLoader().disablePlugin(this);
		} else if (badCoreVersion()) {

			frame = new FrameBuilder();
			frame.addLine("This plugin requires NCore v" + getMinCoreVersion(), FrameBuilder.Option.CENTER);
			frame.addLine("NCore plugin was found but the");
			frame.addLine("current version (v" + getCoreVersion() + ") is too low.");
			frame.addLine("See " + NCORE_WEBSITE);
			frame.addLine("Disabling plugin...");

			for (final String s : frame.build()) {
				getLogger().severe(s);
			}

			getPluginLoader().disablePlugin(this);
		} else {
			try {
				metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
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

	/**
	 * Check if the Core exists, if yes, connect to it.
	 *
	 * @return If the Core was found
	 */
	private boolean isCoreMissing() {
		if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
			return true;
		} else {
			core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
			linkCore();
			return false;
		}
	}

	private boolean badCoreVersion() {
		return getCoreVersion().compareTo(getMinCoreVersion()) < 0;
	}

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
