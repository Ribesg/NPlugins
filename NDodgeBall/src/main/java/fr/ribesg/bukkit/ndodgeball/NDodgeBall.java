/***************************************************************************
 * Project file:    NPlugins - NPlugins - NDodgeBall.java                  *
 * Full Class name: fr.ribesg.bukkit.ndodgeball.NDodgeBall                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ndodgeball;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ndodgeball.api.NDodgeBallAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NDodgeBall extends JavaPlugin {

	// Core plugin
	public static final String NCORE = "NCore";
	@Getter
	public NCore         core;
	public NDodgeBallAPI api;

	// Useful Nodes
	public static final String NCUBOID = "NCuboid";
	@Getter
	public CuboidNode cuboidNode;

	// Set to true by afterEnable() call
	// Prevent multiple calls to afterEnable
	private boolean loadingComplete = false;

	@Override
	public void onEnable() {
		if (linkCore()) {
			afterEnable();
		} else {
			// TODO Fails : this plugin requires NCuboid
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

	public boolean linkCore() {
		if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
			return false;
		} else {
			core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
			api = new NDodgeBallAPI(this);
			core.setDodgeBallNode(api);
			return true;
		}
	}

}
