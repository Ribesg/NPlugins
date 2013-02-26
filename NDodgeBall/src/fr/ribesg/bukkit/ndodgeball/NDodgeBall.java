package fr.ribesg.bukkit.ndodgeball;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.nodes.cuboid.CuboidNode;
import fr.ribesg.bukkit.ndodgeball.api.NDodgeBallAPI;

public class NDodgeBall extends JavaPlugin {

    // Core plugin
    public static final String NCORE           = "NCore";
    @Getter public NCore       core;
    public NDodgeBallAPI       api;

    // Useful Nodes
    public static final String NCUBOID         = "NCuboid";
    @Getter public CuboidNode  cuboidNode;

    // Set to true by afterEnable() call
    // Prevent multiple calls to afterEnable
    private boolean            loadingComplete = false;

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
