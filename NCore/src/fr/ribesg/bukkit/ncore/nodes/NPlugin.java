package fr.ribesg.bukkit.ncore.nodes;

import static fr.ribesg.bukkit.ncore.Utils.frame;
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
    private static final String NCORE_WEBSITE = "http://www.ribesg.fr/bukkit/NPlugins";

    @Getter private NCore       core;
    private boolean             enabled       = false;

    @Override
    public void onEnable() {
        if (!checkCore()) {
            final String[] messages = new String[4];
            messages[0] = "/!\\ This plugin requires NCore to work. /!\\";
            messages[1] = "It is an additional Plugin you should put in you /plugins folder.";
            messages[2] = "See " + NCORE_WEBSITE + " for more informations.";
            messages[3] = "Disabling plugin...";

            for (final String s : frame(messages)) {
                getLogger().severe(s);
            }

            getPluginLoader().disablePlugin(this);
        }
        else if (!checkCoreVersion()) {
            final String[] messages = new String[4];
            messages[0] = "/!\\ This plugin requires NCore v" + getMinCoreVersion() + "to work. /!\\";
            messages[1] = "NCore plugin was found but the current version (v" + getCoreVersion() + ") is too low.";
            messages[2] = "See " + NCORE_WEBSITE + " for more informations.";
            messages[3] = "Disabling plugin...";

            for (final String s : frame(messages)) {
                getLogger().severe(s);
            }

            getPluginLoader().disablePlugin(this);
        }
        else if (onNodeEnable()) {
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

    private boolean checkCoreVersion() {
        return getCoreVersion().compareTo(getMinCoreVersion()) > 0;
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

}
