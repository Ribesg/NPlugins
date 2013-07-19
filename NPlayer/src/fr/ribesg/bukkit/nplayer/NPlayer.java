package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.nodes.player.PlayerNode;
import fr.ribesg.bukkit.nplayer.lang.Messages;
import fr.ribesg.bukkit.nplayer.user.LoggedOutUserHandler;
import fr.ribesg.bukkit.nplayer.user.UserDB;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;

public class NPlayer extends PlayerNode {

    // Configs
    private Messages messages;
    private Config   pluginConfig;

    // Useful Nodes
    // // None

    // Plugin Data
    private UserDB               userDb;
    private LoggedOutUserHandler loggedOutUserHandler;

    @Override
    protected String getMinCoreVersion() {
        return "0.3.0";
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
            getLogger().severe("This error occured when NPlayer tried to load messages.yml");
            return false;
        }

        // Config
        try {
            pluginConfig = new Config(this);
            pluginConfig.loadConfig();
        } catch (final IOException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NPlayer tried to load config.yml");
            return false;
        }

        // Commands
        PlayerCommandExecutor executor = new PlayerCommandExecutor(this);
        getCommand("login").setExecutor(executor);
        getCommand("register").setExecutor(executor);
        getCommand("logout").setExecutor(executor);
        //getCommand("info").setExecutor(executor);
        getCommand("home").setExecutor(executor);
        getCommand("sethome").setExecutor(executor);

        loggedOutUserHandler = new LoggedOutUserHandler(this);

        // Listener
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(loggedOutUserHandler, this);
        pm.registerEvents(executor, this);

        userDb = new UserDB(this);
        try {
            userDb.loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NPlayer tried to load userDB.yml");
            return false;
        }

        return true;
    }

    @Override
    public void onNodeDisable() {
        try {
            getPluginConfig().writeConfig();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            userDb.saveConfig();
        } catch (IOException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NPlayer tried to save userDB.yml");
        }
    }

    @Override
    protected void linkCore() {
        getCore().setPlayerNode(this);
    }

    /** @see fr.ribesg.bukkit.ncore.nodes.NPlugin#handleOtherNodes() */
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

    public Messages getMessages() {
        return messages;
    }

    public Config getPluginConfig() {
        return pluginConfig;
    }

    public UserDB getUserDb() {
        return userDb;
    }

    public LoggedOutUserHandler getLoggedOutUserHandler() {
        return loggedOutUserHandler;
    }
}
