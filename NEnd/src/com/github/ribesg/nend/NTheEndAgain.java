package com.github.ribesg.nend;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ribesg.ncore.NCore;
import com.github.ribesg.nend.api.NTheEndAgainAPI;

public class NTheEndAgain extends JavaPlugin {

	// Core plugin
	public static final String	NCORE			= "NCore";
	@Getter public NCore		core;
	public NTheEndAgainAPI		api;

	// Useful Nodes
	// // None

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
			api = new NTheEndAgainAPI(this);
			core.setTheEndAgainNode(api);
			return true;
		}
	}

}
