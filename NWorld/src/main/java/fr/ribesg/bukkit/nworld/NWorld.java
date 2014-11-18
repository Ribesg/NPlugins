/***************************************************************************
 * Project file:    NPlugins - NWorld - NWorld.java                        *
 * Full Class name: fr.ribesg.bukkit.nworld.NWorld                         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.info.Info;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import fr.ribesg.bukkit.nworld.config.Config;
import fr.ribesg.bukkit.nworld.lang.Messages;
import fr.ribesg.bukkit.nworld.warp.Warps;
import fr.ribesg.bukkit.nworld.world.AdditionalWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld.WorldType;
import fr.ribesg.bukkit.nworld.world.StockWorld;
import fr.ribesg.bukkit.nworld.world.Worlds;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

import org.mcstats.Metrics;

/**
 * The main plugin class.
 *
 * @author Ribesg
 */
public class NWorld extends NPlugin implements WorldNode {

    // Configs
    private Messages messages;
    private Config   pluginConfig;

    // Useful Nodes
    // // None

    // Actual plugin data
    private Worlds worlds;
    private Warps  warps;

    @Override
    protected String getMinCoreVersion() {
        return "0.6.9";
    }

    @Override
    public void loadMessages() throws IOException {
        this.debug("Loading plugin Messages...");
        if (!this.getDataFolder().isDirectory()) {
            this.getDataFolder().mkdir();
        }

        final Messages messages = new Messages();
        messages.loadMessages(this);

        this.messages = messages;
    }

    @Override
    public boolean onNodeEnable() {
        this.worlds = new Worlds();
        this.warps = new Warps();

        // Config
        try {
            this.pluginConfig = new Config(this);
            this.pluginConfig.loadConfig();
        } catch (final IOException | InvalidConfigurationException e) {
            this.getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            this.getLogger().severe("This error occured when NWorld tried to load config.yml");
            return false;
        }

        this.worlds = this.pluginConfig.getWorlds();
        this.warps = this.pluginConfig.getWarps();

        // This loop will detect newly created worlds
        // - Default main World, at first plugin start
        // - Nether & End when activated
        for (final World w : Bukkit.getWorlds()) {
            this.warps.worldEnabled(w.getName());
            if (!this.worlds.containsKey(w.getName())) {
                WorldType type = null;
                switch (w.getEnvironment()) {
                    case NORMAL:
                        type = WorldType.STOCK;
                        break;
                    case NETHER:
                        type = WorldType.STOCK_NETHER;
                        break;
                    case THE_END:
                        type = WorldType.STOCK_END;
                        break;
                }
                final StockWorld world = new StockWorld(this, w.getName(), type, new NLocation(w.getSpawnLocation()), this.pluginConfig.getDefaultRequiredPermission(), true, false);
                this.worlds.put(w.getName(), world);
            }
        }

        // This loop will create/load additional worlds
        for (final AdditionalWorld w : this.worlds.getAdditional().values()) {
            if (w.isEnabled()) {
                // Create (Load) the world
                final WorldCreator creator = new WorldCreator(w.getWorldName());
                creator.environment(World.Environment.NORMAL);
                creator.seed(w.getSeed());
                final World world = Bukkit.createWorld(creator);

                // Re-set spawn location
                final NLocation loc = w.getSpawnLocation();
                world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

                // Check if some warps should be enabled
                this.warps.worldEnabled(w.getWorldName());

                // Load Nether if needed
                if (w.hasNether()) {
                    final String netherName = w.getWorldName() + "_nether";

                    // Create (Load) the world
                    final WorldCreator netherCreator = new WorldCreator(netherName);
                    netherCreator.environment(World.Environment.NETHER);
                    netherCreator.seed(w.getSeed());
                    Bukkit.createWorld(netherCreator);

                    // Check if some warps should be enabled
                    this.warps.worldEnabled(netherName);
                }
                // Load End if needed
                if (w.hasEnd()) {
                    final String endName = w.getWorldName() + "_the_end";

                    // Create (Load) the world
                    final WorldCreator endCreator = new WorldCreator(endName);
                    endCreator.environment(World.Environment.THE_END);
                    endCreator.seed(w.getSeed());
                    Bukkit.createWorld(endCreator);

                    // Check if some warps should be enabled
                    this.warps.worldEnabled(endName);
                }
            }
        }

        // Listener
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new NListener(this), this);

        // Commands
        final WorldCommandExecutor executor = new WorldCommandExecutor(this);
        this.setCommandExecutor("nworld", executor);
        this.setCommandExecutor("spawn", executor);
        this.setCommandExecutor("setspawn", executor);
        this.setCommandExecutor("warp", executor);
        this.setCommandExecutor("setwarp", executor);
        this.setCommandExecutor("delwarp", executor);

        // Metrics - Worlds
        final Metrics.Graph g1 = this.getMetrics().createGraph("Amount of Worlds");
        g1.addPlotter(new Metrics.Plotter("Normal") {

            @Override
            public int getValue() {
                return fr.ribesg.bukkit.nworld.NWorld.this.getWorlds().sizeNormal();
            }
        });
        g1.addPlotter(new Metrics.Plotter("Nether") {

            @Override
            public int getValue() {
                return fr.ribesg.bukkit.nworld.NWorld.this.getWorlds().sizeNether();
            }
        });
        g1.addPlotter(new Metrics.Plotter("End") {

            @Override
            public int getValue() {
                return fr.ribesg.bukkit.nworld.NWorld.this.getWorlds().sizeEnd();
            }
        });

        // Metrics - Warps
        final Metrics.Graph g2 = this.getMetrics().createGraph("Amount of Warps");
        g2.addPlotter(new Metrics.Plotter() {

            @Override
            public int getValue() {
                return fr.ribesg.bukkit.nworld.NWorld.this.getWarps().size();
            }
        });

        return true;
    }

    @Override
    protected void handleOtherNodes() {
        // Nothing to do here for now
    }

    @Override
    public void onNodeDisable() {
        try {
            this.pluginConfig.writeConfig();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Messages getMessages() {
        return this.messages;
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    private boolean isMainWorld(final World world) {
        return world != null && this.isMainWorld(world.getName());
    }

    private boolean isMainWorld(final String worldName) {
        return Bukkit.getWorlds().get(0).getName().equals(worldName);
    }

    public Warps getWarps() {
        return this.warps;
    }

    public Worlds getWorlds() {
        return this.worlds;
    }

    // API for other nodes

    @Override
    public String getNodeName() {
        return WORLD;
    }

    @Override
    public void populateInfo(final CommandSender sender, final String query, final Info infoObject) {
        // TODO Implement method
    }

    @Override
    public Location getWorldSpawnLocation(final String worldName) {
        final GeneralWorld world = this.worlds.get(worldName);
        return world == null ? null : world.getSpawnLocation().toBukkitLocation();
    }
}
