package com.github.ribesg.ngeneral;

import lombok.Getter;

import org.bukkit.Bukkit;

import com.github.ribesg.ncore.NCore;
import com.github.ribesg.ncore.nodes.general.GeneralNode;

public class NGeneral extends GeneralNode {

    // Core plugin
    public static final String NCORE           = "NCore";
    @Getter public NCore       core;

    // Useful Nodes
    // // None

    // Set to true by afterEnable() call
    // Prevent multiple calls to afterEnable
    private boolean            loadingComplete = false;

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

                }
            });
        }
    }

    @Override
    public void onDisable() {

    }

    public void setCore(final NCore core) {
        this.core = core;
        core.setGeneralNode(this);
    }

}
