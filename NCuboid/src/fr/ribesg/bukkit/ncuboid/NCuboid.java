package fr.ribesg.bukkit.ncuboid;

import java.io.IOException;

import lombok.Getter;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import fr.ribesg.bukkit.ncore.lang.AbstractMessages.MessageId;
import fr.ribesg.bukkit.ncore.nodes.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.commands.MainCommandExecutor;
import fr.ribesg.bukkit.ncuboid.lang.Messages;
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

/**
 * TODO
 * 
 * @author Ribesg
 */
public class NCuboid extends CuboidNode {
    
    // Configs
    @Getter private Messages messages;
    @Getter private Config   pluginConfig;
    
    // Useful Nodes
    // // None
    
    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#onNodeEnable()
     */
    @Override
    protected boolean onNodeEnable() {
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
            getLogger().severe("This error occured when NCuboid tried to load messages.yml");
            return false;
        }
        
        // AbstractConfig
        try {
            pluginConfig = new Config();
            pluginConfig.loadConfig(this);
        } catch (final IOException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NCuboid tried to load config.yml");
            return false;
        }
        
        // Create the CuboidDB // TODO Load/Save from/to file
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
        
        return true;
    }
    
    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#linkCore()
     */
    @Override
    protected void linkCore() {
        getCore().setCuboidNode(this);
    }
    
    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#handleOtherNodes()
     */
    @Override
    protected void handleOtherNodes() {
        // Nothing to do here for now
    }
    
    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#onNodeDisable()
     */
    @Override
    protected void onNodeDisable() {
        // TODO Save CuboidDB, do other things eventually (stop tasks etc)
    }
    
    /**
     * Send a message with arguments
     * TODO <b>This may be moved<b>
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
}
