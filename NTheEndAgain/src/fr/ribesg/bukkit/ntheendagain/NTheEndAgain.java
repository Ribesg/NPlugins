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

import fr.ribesg.bukkit.ncore.lang.AbstractMessages.MessageId;
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

        // Load End worlds configs and chunks data
        worldHandlers = new HashMap<String, EndWorldHandler>();
        for (final World w : Bukkit.getWorlds()) {
            if (w.getEnvironment() == Environment.THE_END) {
                final EndWorldHandler handler = new EndWorldHandler(this, w);
                try {
                    handler.loadConfigs();
                    worldHandlers.put(w.getName(), handler);
                } catch (final IOException e) {
                    getLogger().severe("An error occured, stacktrace follows:");
                    e.printStackTrace();
                    getLogger().severe("This error occured when NTheEndAgain tried to load " + e.getMessage() + ".yml");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#linkCore()
     */
    @Override
    protected void linkCore() {
        getCore().setTheEndAgainNode(this);
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

    public Path getConfigFilePath(final String fileName) {
        return Paths.get(getDataFolder().getPath(), fileName + ".yml");
    }

}
