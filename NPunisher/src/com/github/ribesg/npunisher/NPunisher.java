package com.github.ribesg.npunisher;

import lombok.Getter;

import org.bukkit.Bukkit;

public class NPunisher extends PunisherNode {

	// Core plugin
	public static final String	NCORE			= "NCore";
	@Getter public NCore		core;

	// Useful Nodes
	public static final String	NCUBOID			= "NCuboid";
	@Getter public CuboidNode	cuboidNode;

	// Set to true by afterEnable() call
	// Prevent multiple calls to afterEnable
	private boolean				loadingComplete	= false;

	@Override
	public void onEnable() {
		if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
			// TODO
		} else {
			core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
			// TODO
			afterEnable();
		}
	}

	public void afterEnable() {
		if (!loadingComplete) {
			loadingComplete = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run() {
					// Interact with other Nodes here
					if (!Bukkit.getPluginManager().isPluginEnabled(NCUBOID)) {
						// TODO
					} else {
						cuboidNode = (CuboidNode) Bukkit.getPluginManager().getPlugin(NCUBOID);
						// TODO
						afterEnable();
					}
				}
			});
		}
	}

	@Override
	public void onDisable() {

	}

	public void setCore(final NCore core) {
		this.core = core;
		core.setPunisherNode(this);
	}

}
