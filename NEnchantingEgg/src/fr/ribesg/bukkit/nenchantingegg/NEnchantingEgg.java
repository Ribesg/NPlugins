package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.nodes.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.nenchantingegg.altar.Altars;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.ActiveToEggProvidedTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.EggProvidedToItemProvidedTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.InactiveToActiveTransition;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.ItemProvidedToLockedTransition;
import fr.ribesg.bukkit.nenchantingegg.lang.Messages;
import fr.ribesg.bukkit.nenchantingegg.listener.BlockListener;
import fr.ribesg.bukkit.nenchantingegg.listener.EnchantingEggListener;
import fr.ribesg.bukkit.nenchantingegg.listener.ItemListener;
import fr.ribesg.bukkit.nenchantingegg.listener.PlayerListener;
import fr.ribesg.bukkit.nenchantingegg.task.TimeListenerTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;

public class NEnchantingEgg extends EnchantingEggNode {

    // Configs
    private Messages messages;
    private Config   pluginConfig;

    // Useful Nodes
    // // None

    // Actual plugin data
    private Altars altars;

    // Transitions
    private InactiveToActiveTransition          inactiveToActiveTransition;
    private ActiveToEggProvidedTransition       activeToEggProvidedTransition;
    private EggProvidedToItemProvidedTransition eggProvidedToItemProvidedTransition;
    private ItemProvidedToLockedTransition      itemProvidedToLockedTransition;

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
            getLogger().severe("This error occured when NEnchantingEgg tried to load messages.yml");
            return false;
        }

        inactiveToActiveTransition = new InactiveToActiveTransition(this);
        activeToEggProvidedTransition = new ActiveToEggProvidedTransition(this);
        eggProvidedToItemProvidedTransition = new EggProvidedToItemProvidedTransition(this);
        itemProvidedToLockedTransition = new ItemProvidedToLockedTransition(this);

        altars = new Altars(this);

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
        pm.registerEvents(new EnchantingEggListener(this), this);
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new ItemListener(this), this);
        pm.registerEvents(new BlockListener(this), this);

        // Commands
        //getCommand("theCommand").setExecutor(new NCommandExecutor(this));

        // Tasks
        Bukkit.getScheduler().runTaskTimer(this, new TimeListenerTask(this), 0, 20);

        return true;
    }

    @Override
    public void onNodeDisable() {
        try {
            getPluginConfig().writeConfig();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        altars = null;

        Bukkit.getScheduler().cancelTasks(this);
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

    public ActiveToEggProvidedTransition getActiveToEggProvidedTransition() {
        return activeToEggProvidedTransition;
    }

    public Altars getAltars() {
        return altars;
    }

    public EggProvidedToItemProvidedTransition getEggProvidedToItemProvidedTransition() {
        return eggProvidedToItemProvidedTransition;
    }

    public InactiveToActiveTransition getInactiveToActiveTransition() {
        return inactiveToActiveTransition;
    }

    public ItemProvidedToLockedTransition getItemProvidedToLockedTransition() {
        return itemProvidedToLockedTransition;
    }

    public Messages getMessages() {
        return messages;
    }

    public Config getPluginConfig() {
        return pluginConfig;
    }
}
