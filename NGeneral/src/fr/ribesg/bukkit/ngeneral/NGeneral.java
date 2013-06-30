package fr.ribesg.bukkit.ngeneral;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ngeneral.api.NGeneralAPI;

public class NGeneral extends JavaPlugin {

    // Core plugin related
    public static final String NCORE           = "NCore";
     public NCore       core;
    public NGeneralAPI         api;

    // Useful Nodes
    // // None

    // Set to true by afterEnable() call
    // Prevent multiple calls to afterEnable
    private boolean            loadingComplete = false;

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
            api = new NGeneralAPI(this);
            core.setGeneralNode(api);
            return true;
        }
    }

}
