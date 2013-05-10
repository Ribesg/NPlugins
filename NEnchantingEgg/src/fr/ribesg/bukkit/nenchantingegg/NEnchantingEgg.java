package fr.ribesg.bukkit.nenchantingegg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.nodes.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.lang.Messages;

public class NEnchantingEgg extends EnchantingEggNode {

    @Getter private static NEnchantingEgg  instance;

    // Configs
    @Getter private Messages               messages;
    @Getter private Config                 pluginConfig;

    // Useful Nodes
    // // None

    // Actual plugin data
    @Getter private Map<ChunkCoord, Altar> altarMap;

    @Override
    protected String getMinCoreVersion() {
        return "0.1.0";
    }

    @Override
    public boolean onNodeEnable() {
        instance = this;

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
            getLogger().severe("This error occured when NEnchantingEgg tried to load messages.yml");
            return false;
        }

        altarMap = new HashMap<ChunkCoord, Altar>();

        // Config
        try {
            pluginConfig = new Config(this);
            pluginConfig.loadConfig();
        } catch (final IOException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NEnchantingEgg tried to load config.yml");
            return false;
        }

        // Listener
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new NListener(this), this);

        // Commands
        //getCommand("theCommand").setExecutor(new NCommandExecutor(this));

        return true;
    }

    @Override
    public void onNodeDisable() {
        try {
            getPluginConfig().writeConfig();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        instance = null;
    }

    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#handleOtherNodes()
     */
    @Override
    protected void handleOtherNodes() {
        // Nothing to do here for now
    }

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
