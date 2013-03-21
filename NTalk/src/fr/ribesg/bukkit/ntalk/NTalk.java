package fr.ribesg.bukkit.ntalk;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ntalk.api.NTalkAPI;
import fr.ribesg.bukkit.ntalk.lang.Messages;
import fr.ribesg.bukkit.ntalk.lang.Messages.MessageId;
import fr.ribesg.bukkit.ntalk.listeners.PlayerChatListener;

public class NTalk extends JavaPlugin {

    // Constants
    public static final String NCORE           = "NCore";
    public static final String F_MESSAGES      = "messages.yml";
    public static final String F_CONFIG        = "config.yml";

    // Core plugin related
    @Getter public NCore       core;
    public NTalkAPI            api;

    // Useful Nodes
    // // None

    // Formater
    @Getter private Formater   formater;

    // Files
    @Getter private Path       pathConfig;
    @Getter private Path       pathMessages;

    // Set to true by afterEnable() call
    // Prevent multiple calls to afterEnable
    private boolean            loadingComplete = false;

    @Override
    public void onEnable() {
        // AbstractMessages first !
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

        // AbstractConfig
        formater = new Formater();
        try {
            pathConfig = Paths.get(getDataFolder().getPath(), F_CONFIG);
            formater.load(pathConfig);
        } catch (final IOException e) {
            e.printStackTrace();
            sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, F_CONFIG);
            getServer().getPluginManager().disablePlugin(this);
        }

        // Listeners
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerChatListener(this), this);

        // Command
        //getCommand("command").setExecutor(new MyCommandExecutor(this));

        // Dependencies handling
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
            api = new NTalkAPI(this);
            core.setChatNode(api);
            return true;
        }
    }

    public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
        final String[] m = Messages.get(messageId, args);
        to.sendMessage(m);
    }
}
