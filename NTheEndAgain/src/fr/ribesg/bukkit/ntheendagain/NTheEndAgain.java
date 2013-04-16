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

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.nodes.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ntheendagain.lang.Messages;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

public class NTheEndAgain extends TheEndAgainNode {

    // Configs
    @Getter private Messages                 messages;
    @Getter private Config                   pluginConfig;

    // Useful Nodes
    // // None

    // Actual plugin data
    private HashMap<String, EndWorldHandler> worldHandlers;

    @Override
    protected String getMinCoreVersion() {
        return "0.1.0";
    }

    @Override
    public boolean onNodeEnable() {
        // Messages first !
        try {
            if (!getDataFolder().isDirectory()) {
                getDataFolder().mkdir();
            }
            messages = new Messages();
            messages.loadMessages(this);
        } catch (final IOException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NTheEndAgain tried to load messages.yml");
            return false;
        }

        getServer().getPluginManager().registerEvents(new NListener(this), this);

        // Load End worlds configs and chunks data
        worldHandlers = new HashMap<String, EndWorldHandler>();
        for (final World w : Bukkit.getWorlds()) {
            if (w.getEnvironment() == Environment.THE_END) {
                final EndWorldHandler handler = new EndWorldHandler(this, w);
                try {
                    handler.loadConfig();
                    handler.loadChunks();
                    worldHandlers.put(Utils.toLowerCamelCase(w.getName()), handler);
                    handler.init();
                } catch (final IOException e) {
                    getLogger().severe("An error occured, stacktrace follows:");
                    e.printStackTrace();
                    getLogger().severe("This error occured when NTheEndAgain tried to load " + e.getMessage() + ".yml");
                    return false;
                }
            }
        }

        getLogger().setFilter(new MovedTooQuicklyFilter(this));

        getCommand("end").setExecutor(new NCommandExecutor(this));

        return true;
    }

    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#handleOtherNodes()
     */
    @Override
    protected void handleOtherNodes() {
        // Nothing to do here for now
    }

    @Override
    public void onNodeDisable() {
        for (final EndWorldHandler handler : worldHandlers.values()) {
            handler.stop();
            try {
                // Reload-friendly lastExecTime storing in config file
                final long lastExecTime = handler.getConfig().getLastTaskExecTime();
                handler.loadConfig();
                handler.getConfig().setLastTaskExecTime(lastExecTime);
                handler.saveConfig();
            } catch (final IOException e) {
                getLogger().severe("An error occured, stacktrace follows:");
                e.printStackTrace();
                getLogger().severe("This error occured when NTheEndAgain tried to save " + e.getMessage() + ".yml");
            }
            try {
                handler.saveChunks();
            } catch (final IOException e) {
                getLogger().severe("An error occured, stacktrace follows:");
                e.printStackTrace();
                getLogger().severe("This error occured when NTheEndAgain tried to save " + e.getMessage() + ".yml");
                getLogger().severe("/!\\ THIS MEANS THAT PROTECTED CHUNKS COULD BE REGENERATED ON NEXT REGEN IN THIS WORLD /!\\");
            }
        }
    }

    public Path getConfigFilePath(final String fileName) {
        return Paths.get(getDataFolder().getPath(), fileName + ".yml");
    }

    /**
     * @param lowerCamelCaseWorldName
     *            Key
     * @return Value
     */
    public EndWorldHandler getHandler(final String lowerCamelCaseWorldName) {
        return worldHandlers.get(lowerCamelCaseWorldName);
    }

    /**
     * Send a message with arguments TODO <b>This may be moved<b>
     * 
     * @param to
     *            Receiver
     * @param messageId
     *            The Message Id
     * @param args
     *            The arguments
     */
    public void sendMessage(final CommandSender to, final MessageId messageId, final String... args) {
        final String[] m = messages.get(messageId, args);
        to.sendMessage(m);
    }

    public void broadcastMessage(final MessageId messageId, final String... args) {
        final String[] m = messages.get(messageId, args);
        for (final String mes : m) {
            getServer().broadcastMessage(mes);
        }
    }

}
