package com.github.ribesg.npunisher;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ribesg.ncore.NCore;
import com.github.ribesg.ncore.nodes.cuboid.CuboidNode;
import com.github.ribesg.npunisher.api.NPunisherAPI;

public class NPunisher extends JavaPlugin {

	// Core plugin related
	public static final String	NCORE			= "NCore";
	@Getter public NCore		core;
	public NPunisherAPI			api;

	// Useful Nodes
	public static final String	NCUBOID			= "NCuboid";
	@Getter public CuboidNode	cuboidNode;

	// Set to true by afterEnable() call
	// Prevent multiple calls to afterEnable
	private boolean				loadingComplete	= false;

	@Override
	public void onEnable() {
		if (linkCore()) {
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
					}
				}
			});
		}
	}

	@Override
	public void onDisable() {

	}

	public boolean linkCore() {
		if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
			return false;
		} else {
			core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
			api = new NPunisherAPI(this);
			core.setPunisherNode(api);
			return true;
		}
	}

}
