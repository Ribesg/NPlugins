package com.github.ribesg.ncuboid;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import com.github.ribesg.ncore.NCore;
import com.github.ribesg.ncore.nodes.cuboid.CuboidNode;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.commands.MainCommandExecutor;
import com.github.ribesg.ncuboid.lang.Messages;
import com.github.ribesg.ncuboid.lang.Messages.MessageId;
import com.github.ribesg.ncuboid.listeners.EventExtensionListener;
import com.github.ribesg.ncuboid.listeners.PlayerStickListener;
import com.github.ribesg.ncuboid.listeners.flag.BoosterFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.BuildFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.ChatFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.ChestFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.ClosedFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.CreativeFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.DropFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.EndermanGriefFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.ExplosionFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.FarmFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.FireFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.GodFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.InvisibleFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.MobFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.PVPFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.PassFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.SnowFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.TeleportFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.UseFlagListener;
import com.github.ribesg.ncuboid.listeners.flag.WarpgateFlagListener;

public class NCuboid extends CuboidNode {
    // Constants
    public static final String NCORE           = "NCore";
    public static final String F_MESSAGES      = "messages.yml";
    public static final String F_CONFIG        = "config.yml";
    public static final String F_CUBOIDS       = "cuboidDB.yml";

    // Core plugin
    @Getter public NCore       core;

    // Useful Nodes
    // // None

    // Files
    @Getter private Path       pathConfig;
    @Getter private Path       pathMessages;

    // Set to true by afterEnable() call
    // Prevent multiple calls to afterEnable
    private boolean            loadingComplete = false;

    @Override
    public void onEnable() {
        // Messages first !
        try {
            if (!getDataFolder().isDirectory()) {
                getDataFolder().mkdir();
            }
            pathMessages = Paths.get(getDataFolder().getPath(), F_MESSAGES);
            Messages.loadConfig(pathMessages);
        } catch (final IOException e) {
            e.printStackTrace();
            sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, F_MESSAGES);
            getServer().getPluginManager().disablePlugin(this);
        }

        // Config
        try {
            pathConfig = Paths.get(getDataFolder().getPath(), F_CONFIG);
            Config.loadConfig(pathConfig);
        } catch (final IOException e) {
            e.printStackTrace();
            sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, F_CONFIG);
            getServer().getPluginManager().disablePlugin(this);
        }

        // Dependencies handling
        core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);

        // Create the CuboidDB
        new CuboidDB(this);

        // Listeners
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EventExtensionListener(this), this);
        pm.registerEvents(new PlayerStickListener(this), this);
        // Flag Listeners
        pm.registerEvents(new BoosterFlagListener(this), this);
        pm.registerEvents(new BuildFlagListener(this), this);
        pm.registerEvents(new ChatFlagListener(this), this);
        pm.registerEvents(new ChestFlagListener(this), this);
        pm.registerEvents(new ClosedFlagListener(this), this);
        pm.registerEvents(new CreativeFlagListener(this), this);
        pm.registerEvents(new DropFlagListener(this), this);
        pm.registerEvents(new EndermanGriefFlagListener(this), this);
        pm.registerEvents(new ExplosionFlagListener(this), this);
        pm.registerEvents(new FarmFlagListener(this), this);
        pm.registerEvents(new FireFlagListener(this), this);
        pm.registerEvents(new GodFlagListener(this), this);
        pm.registerEvents(new InvisibleFlagListener(this), this);
        pm.registerEvents(new MobFlagListener(this), this);
        pm.registerEvents(new PassFlagListener(this), this);
        pm.registerEvents(new PVPFlagListener(this), this);
        pm.registerEvents(new SnowFlagListener(this), this);
        pm.registerEvents(new TeleportFlagListener(this), this);
        pm.registerEvents(new UseFlagListener(this), this);
        pm.registerEvents(new WarpgateFlagListener(this), this);

        // Command
        getCommand("cuboid").setExecutor(new MainCommandExecutor(this));

        afterEnable();
    }

    private void afterEnable() {
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
        core.setCuboidNode(this);
    }

    public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
        final String[] m = Messages.get(messageId, args);
        to.sendMessage(m);
    }
}
