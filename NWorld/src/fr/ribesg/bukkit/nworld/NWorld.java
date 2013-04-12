package fr.ribesg.bukkit.nworld;

import java.io.IOException;
import java.util.HashMap;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.nodes.world.WorldNode;
import fr.ribesg.bukkit.nworld.lang.Messages;

/**
 * The main plugin class.
 * 
 * @author Ribesg
 */
public class NWorld extends WorldNode {

    // Configs
    @Getter private Messages                  messages;
    @Getter private Config                    pluginConfig;

    // Useful Nodes
    // // None

    /**
     * Map linking lowercased World Names as keys to a boolean saying if they are open to teleportation or not
     */
    @Getter private HashMap<String, Boolean>  worldMap;

    /**
     * Map linking lowercased World Names as keys to their spawn location, here uncluding Yaw and Pitch
     */
    @Getter private HashMap<String, Location> spawnMap;

    @Override
    protected String getMinCoreVersion() {
        return "0.0.6";
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
            getLogger().severe("This error occured when NWorld tried to load messages.yml");
            return false;
        }

        worldMap = new HashMap<String, Boolean>();
        spawnMap = new HashMap<String, Location>();

        // Config
        try {
            pluginConfig = new Config(this);
            pluginConfig.loadConfig(this);
        } catch (final IOException e) {
            getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            getLogger().severe("This error occured when NWorld tried to load config.yml");
            return false;
        }

        for (final World w : getServer().getWorlds()) {
            if (!worldMap.containsKey(w.getName())) {
                worldMap.put(w.getName(), false);
            }
            if (!spawnMap.containsKey(w.getName())) {

                // Anti stuck in the wall
                Location loc = w.getSpawnLocation();
                loc.setX(loc.getBlockX() + 0.5);
                loc.setY(loc.getBlockY() + 0.4);
                loc.setZ(loc.getBlockZ() + 0.5);
                while (loc.getBlock().getType() != Material.AIR || loc.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR) {
                    loc.add(0, 1, 0);
                }

                // Anti burn in lava for the Nether (TODO Make this deactivable ?)
                final Location loc2 = loc.clone();
                while (loc2.getBlock().getType() == Material.AIR) {
                    loc2.add(0, -1, 0);
                }
                if (loc2.getBlock().getType() == Material.STATIONARY_LAVA || loc2.getBlock().getType() == Material.LAVA) {
                    loc2.getBlock().setType(Material.COBBLESTONE);
                }

                loc = loc2.clone().add(0, 1, 0);

                spawnMap.put(w.getName(), loc);
            }
        }

        // Listener
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new NListener(this), this);

        // Commands
        getCommand("nworld").setExecutor(new NCommandExecutor(this));
        getCommand("spawn").setExecutor(new NCommandExecutor(this));
        getCommand("setspawn").setExecutor(new NCommandExecutor(this));

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

    public void broadcastMessage(final MessageId messageId, final String... args) {
        final String[] m = messages.get(messageId, args);
        for (final String mes : m) {
            getServer().broadcastMessage(mes);
        }
    }
}
