package fr.ribesg.bukkit.nworld;

import java.util.List;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.lang.MessageId;

public class Config extends AbstractConfig {

    private final static String                      LEVEL1_SEPARATOR = ";;";
    private final static String                      LEVEL2_SEPARATOR = ";";

    private final NWorld                             plugin;

    @Getter @Setter(AccessLevel.PRIVATE) private int broadcastOnWorldCreate;
    @Getter @Setter(AccessLevel.PRIVATE) private int broadcastOnWorldLoad;
    @Getter @Setter(AccessLevel.PRIVATE) private int broadcastOnWorldUnload;

    public Config(final NWorld instance) {
        plugin = instance;

    }

    /**
     * @see AbstractConfig#setValues(YamlConfiguration)
     */
    @Override
    protected void setValues(final YamlConfiguration config) {

        // broadcastOnWorldCreate. Default: 0.
        // Possible values: 0,1
        setBroadcastOnWorldCreate(config.getInt("broadcastOnWorldCreate", 0));
        if (getBroadcastOnWorldCreate() < 0 || getBroadcastOnWorldCreate() > 1) {
            setBroadcastOnWorldCreate(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, "config.yml", "broadcastOnWorldCreate", "0");
        }

        // broadcastOnWorldLoad. Default: 0.
        // Possible values: 0,1
        setBroadcastOnWorldLoad(config.getInt("broadcastOnWorldLoad", 0));
        if (getBroadcastOnWorldLoad() < 0 || getBroadcastOnWorldLoad() > 1) {
            setBroadcastOnWorldLoad(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, "config.yml", "broadcastOnWorldLoad", "0");
        }

        // broadcastOnWorldUnload. Default: 0.
        // Possible values: 0,1
        setBroadcastOnWorldUnload(config.getInt("broadcastOnWorldUnload", 0));
        if (getBroadcastOnWorldUnload() < 0 || getBroadcastOnWorldUnload() > 1) {
            setBroadcastOnWorldUnload(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(), MessageId.incorrectValueInConfiguration, "config.yml", "broadcastOnWorldUnload", "0");
        }

        // Load known worlds list
        final List<String> worldList = config.getStringList("worlds");
        for (final String code : worldList) {
            final String[] split = code.split(LEVEL1_SEPARATOR);
            final String worldName = split[0];
            World w = plugin.getServer().getWorld(worldName);
            if (w == null) {
                w = plugin.getServer().createWorld(new WorldCreator(worldName));
            }
            final boolean availableForTeleportation = Integer.parseInt(split[1]) == 1;
            final String[] location = split[2].split(LEVEL2_SEPARATOR);
            Location loc = null;
            try {
                final double x = Integer.parseInt(location[0]) + 0.5;
                final double y = Integer.parseInt(location[1]) + 0.25;
                final double z = Integer.parseInt(location[2]) + 0.5;
                final double yaw = Float.parseFloat(location[3]);
                final double pitch = Float.parseFloat(location[4]);
                loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
            } catch (final Exception e) {
                plugin.getLogger().severe("Unable to load spawnpoint of world " + worldName);
            }
            plugin.getWorldMap().put(worldName, availableForTeleportation);
            plugin.getSpawnMap().put(worldName, loc == null ? w.getSpawnLocation() : loc);
        }
    }

    /**
     * @see AbstractConfig#getConfigString()
     */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();

        // Header
        content.append("################################################################################\n");
        content.append("# Config file for NWorld plugin. If you don't understand something, please ask #\n");
        content.append("# on dev.bukkit.org or on forum post.                                   Ribesg #\n");
        content.append("################################################################################\n\n");

        // Broadcast on world creation
        content.append("# Do we broadcast a message on World creation. Possible values: 0,1\n");
        content.append("# Default : 0\n");
        content.append("broadcastOnWorldCreate: " + getBroadcastOnWorldCreate() + "\n\n");

        // Broadcast on world load
        content.append("# Do we broadcast a message on World load. Possible values: 0,1\n");
        content.append("# Default : 0\n");
        content.append("broadcastOnWorldLoad: " + getBroadcastOnWorldLoad() + "\n\n");

        // Broadcast on world unload
        content.append("# Do we broadcast a message on World unload. Possible values: 0,1\n");
        content.append("# Default : 0\n");
        content.append("broadcastOnWorldUnload: " + getBroadcastOnWorldUnload() + "\n\n");

        // Save known worlds to load them later
        content.append("# Known worlds, DO NOT MODIFY\n");
        content.append("worlds:\n");
        for (final Entry<String, Boolean> e : plugin.getWorldMap().entrySet()) {
            content.append("  - \"");
            content.append(e.getKey());
            content.append(LEVEL1_SEPARATOR);
            content.append(e.getValue() ? "1" : "0");
            content.append(LEVEL1_SEPARATOR);
            final Location spawnLoc = plugin.getSpawnMap().get(e.getKey());
            content.append(spawnLoc.getBlockX());
            content.append(LEVEL2_SEPARATOR);
            content.append(spawnLoc.getBlockY());
            content.append(LEVEL2_SEPARATOR);
            content.append(spawnLoc.getBlockZ());
            content.append(LEVEL2_SEPARATOR);
            content.append(spawnLoc.getYaw());
            content.append(LEVEL2_SEPARATOR);
            content.append(spawnLoc.getPitch());
            content.append("\"\n");
        }

        return content.toString();
    }
}
