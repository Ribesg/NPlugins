package fr.ribesg.bukkit.ncuboid;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncuboid.api.NCuboidAPI;
import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.commands.MainCommandExecutor;
import fr.ribesg.bukkit.ncuboid.lang.Messages;
import fr.ribesg.bukkit.ncuboid.lang.Messages.MessageId;
import fr.ribesg.bukkit.ncuboid.listeners.EventExtensionListener;
import fr.ribesg.bukkit.ncuboid.listeners.PlayerStickListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.BoosterFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.BuildFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ChatFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ChestFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ClosedFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.CreativeFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.DropFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.EndermanGriefFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.ExplosionFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.FarmFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.FireFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.GodFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.InvisibleFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.MobFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.PVPFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.PassFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.SnowFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.TeleportFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.UseFlagListener;
import fr.ribesg.bukkit.ncuboid.listeners.flag.WarpgateFlagListener;

public class NCuboid extends JavaPlugin {
    // Constants
    public static final String NCORE           = "NCore";
    public static final String F_MESSAGES      = "messages.yml";
    public static final String F_CONFIG        = "config.yml";
    public static final String F_CUBOIDS       = "cuboidDB.yml";

    // Core plugin related
    @Getter public NCore       core;
    public NCuboidAPI          api;

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

        // Dependencies handling
        if (linkCore()) {
            afterEnable();
        }
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

    public boolean linkCore() {
        if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
            return false;
        } else {
            core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
            api = new NCuboidAPI(this);
            core.setCuboidNode(api);
            return true;
        }
    }

    public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
        final String[] m = Messages.get(messageId, args);
        to.sendMessage(m);
    }
}
