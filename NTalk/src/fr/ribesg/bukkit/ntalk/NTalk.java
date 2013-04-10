package fr.ribesg.bukkit.ntalk;

import java.io.IOException;

import lombok.Getter;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.nodes.chat.TalkNode;
import fr.ribesg.bukkit.ntalk.format.Formater;
import fr.ribesg.bukkit.ntalk.lang.Messages;

public class NTalk extends TalkNode {

    // Configs
    @Getter private Messages messages;
    @Getter private Config   pluginConfig;

    // Useful Nodes
    // // None

    // Formater
    @Getter private Formater formater;

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
            getLogger().severe("This error occured when NTalk tried to load messages.yml");
            return false;
        }

        // Config
        try {
            pluginConfig = new Config(this);
            pluginConfig.loadConfig(this);
        } catch (final IOException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NTalk tried to load config.yml");
            return false;
        }
        formater = new Formater(this);

        // Listeners
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new NListener(this), this);

        // Command
        getCommand("pm").setExecutor(new NCommandExecutor(this));
        getCommand("pr").setExecutor(new NCommandExecutor(this));
        getCommand("nick").setExecutor(new NCommandExecutor(this));

        return true;
    }

    @Override
    public void onNodeDisable() {
        try {
            getPluginConfig().writeConfig(this);
        } catch (final IOException e) {
            e.printStackTrace();
        }
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
}
