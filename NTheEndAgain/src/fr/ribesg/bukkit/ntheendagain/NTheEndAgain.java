package fr.ribesg.bukkit.ntheendagain;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ntheendagain.api.NTheEndAgainAPI;
import fr.ribesg.bukkit.ntheendagain.lang.Messages;
import fr.ribesg.bukkit.ntheendagain.lang.Messages.MessageId;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;

public class NTheEndAgain extends JavaPlugin {

    // Constants
    public static final String NCORE           = "NCore";
    public static final String F_MESSAGES      = "messages.yml";
    public static final String F_CONFIG        = "config.yml";
    public static final String F_ENDCHUNKS     = "endChunksDB.yml";

    // Core plugin related
    @Getter public NCore       core;
    public NTheEndAgainAPI     api;

    // Useful Nodes
    // // None

    // Files
    @Getter private Path       pathConfig;
    @Getter private Path       pathMessages;
    @Getter private Path       pathEndChunks;

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

        if (linkCore()) {
            afterEnable();
        }

        // EndChunks
        try {
            pathEndChunks = Paths.get(getDataFolder().getPath(), F_ENDCHUNKS);
            new EndChunks(this);
            EndChunks.load(pathEndChunks);
        } catch (final IOException e) {
            e.printStackTrace();
            sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, F_ENDCHUNKS);
            getServer().getPluginManager().disablePlugin(this);
        }

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
        // EndChunks
        try {
            EndChunks.write(pathEndChunks);
        } catch (final IOException e) {
            e.printStackTrace();
            sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, F_ENDCHUNKS); // TODO Messages WHILE SAVING
        }
    }

    private boolean linkCore() {
        if (!Bukkit.getPluginManager().isPluginEnabled(NCORE)) {
            return false;
        } else {
            core = (NCore) Bukkit.getPluginManager().getPlugin(NCORE);
            api = new NTheEndAgainAPI(this);
            core.setTheEndAgainNode(api);
            return true;
        }
    }

    public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
        final String[] m = Messages.get(messageId, args);
        to.sendMessage(m);
    }

}
