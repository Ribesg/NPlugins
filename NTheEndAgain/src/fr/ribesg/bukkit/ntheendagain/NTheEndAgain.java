package fr.ribesg.bukkit.ntheendagain;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ntheendagain.api.NTheEndAgainAPI;
import fr.ribesg.bukkit.ntheendagain.lang.Messages;
import fr.ribesg.bukkit.ntheendagain.lang.Messages.MessageId;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

public class NTheEndAgain extends JavaPlugin {

    // Constants
    public static final String               NCORE           = "NCore";
    public static final String               F_MESSAGES      = "messages.yml";

    // Core plugin related
    @Getter public NCore                     core;
    public NTheEndAgainAPI                   api;

    // Useful Nodes
    // // None

    // Files
    @Getter private Path                     pathMessages;

    // Set to true by afterEnable() call
    // Prevent multiple calls to afterEnable
    private boolean                          loadingComplete = false;

    // Actual plugin data
    private HashMap<String, EndWorldHandler> worldHandlers;

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

        // Load End worlds configs and chunks data
        worldHandlers = new HashMap<String, EndWorldHandler>();
        for (final World w : Bukkit.getWorlds()) {
            if (w.getEnvironment() == Environment.THE_END) {
                final EndWorldHandler handler = new EndWorldHandler(this, w);
                try {
                    handler.loadConfigs();
                    worldHandlers.put(w.getName(), handler);
                } catch (final IOException e) {
                    e.printStackTrace();
                    sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, e.getMessage());
                }
            }
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
        for (final EndWorldHandler handler : worldHandlers.values()) {
            try {
                handler.saveChunks();
            } catch (final IOException e) {
                e.printStackTrace();
                sendMessage(getServer().getConsoleSender(), MessageId.errorWhileLoadingConfiguration, e.getMessage());
            }
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

    public Path getConfigFilePath(final String fileName) {
        return Paths.get(getDataFolder().getPath(), fileName + ".yml");
    }

}
