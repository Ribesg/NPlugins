package fr.ribesg.bukkit.ncore.nodes;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ribesg.bukkit.ncore.NCore;

/**
 * This class represents a plugin node of the N plugin suite
 * 
 * @author Ribesg
 */
public abstract class NPlugin extends JavaPlugin {

    private static final String NCORE         = "NCore";
    private static final String NCORE_WEBSITE = "http://www.ribesg.fr/bukkit/NCore.html";

    @Getter private NCore       core;
    private boolean             enabled       = false;

    @Override
    public void onEnable() {
        if (!checkCore()) {
            getLogger().severe("This plugin requires " + NCORE + " to work.");
            getLogger().severe("See " + NCORE_WEBSITE);
            getLogger().severe("Disabling plugin...");
            getPluginLoader().disablePlugin(this);
        }
        if (onNodeEnable()) {
            enabled = true;
            afterEnable();
        } else {
            getLogger().severe("Disabling plugin...");
            getPluginLoader().disablePlugin(this);
        }
    }

    /**
     * Replace the normal {@link org.bukkit.plugin.java.JavaPlugin#onEnable()} method in a normal plugin.
     * 
     * @return If we should disable the plugin immediatly because we got a problem
     */
    protected abstract boolean onNodeEnable();

    /**
     * Call {@link #handleOtherNodes()} after plugin initialization tick.
     */
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
    private boolean checkCore() {
        if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
            return false;
        } else {
            core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
            linkCore();
            return true;
        }
    }

    /**
     * Call the Core's Setter for this Node type
     * Basically: core.set[THIS]Node(this);
     */
    protected abstract void linkCore();

}
