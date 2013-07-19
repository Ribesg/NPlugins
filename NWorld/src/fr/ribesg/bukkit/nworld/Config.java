package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map.Entry;

public class Config extends AbstractConfig<NWorld> {

    private final static String LEVEL1_SEPARATOR = ";;";
    private final static String LEVEL2_SEPARATOR = ";";

    private int broadcastOnWorldCreate;
    private int broadcastOnWorldLoad;
    private int broadcastOnWorldUnload;

    public Config(final NWorld instance) {
        super(instance);
    }

    /** @see AbstractConfig#setValues(YamlConfiguration) */
    @Override
    protected void setValues(final YamlConfiguration config) {

        // broadcastOnWorldCreate. Default: 0.
        // Possible values: 0,1
        setBroadcastOnWorldCreate(config.getInt("broadcastOnWorldCreate", 0));
        if (getBroadcastOnWorldCreate() < 0 || getBroadcastOnWorldCreate() > 1) {
            setBroadcastOnWorldCreate(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(),
                               MessageId.incorrectValueInConfiguration,
                               "config.yml",
                               "broadcastOnWorldCreate",
                               "0");
        }

        // broadcastOnWorldLoad. Default: 0.
        // Possible values: 0,1
        setBroadcastOnWorldLoad(config.getInt("broadcastOnWorldLoad", 0));
        if (getBroadcastOnWorldLoad() < 0 || getBroadcastOnWorldLoad() > 1) {
            setBroadcastOnWorldLoad(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(),
                               MessageId.incorrectValueInConfiguration,
                               "config.yml",
                               "broadcastOnWorldLoad",
                               "0");
        }

        // broadcastOnWorldUnload. Default: 0.
        // Possible values: 0,1
        setBroadcastOnWorldUnload(config.getInt("broadcastOnWorldUnload", 0));
        if (getBroadcastOnWorldUnload() < 0 || getBroadcastOnWorldUnload() > 1) {
            setBroadcastOnWorldUnload(0);
            plugin.sendMessage(plugin.getServer().getConsoleSender(),
                               MessageId.incorrectValueInConfiguration,
                               "config.yml",
                               "broadcastOnWorldUnload",
                               "0");
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
            final Location loc = buildLocation(w, split[2]);
            plugin.getWorldMap().put(worldName, availableForTeleportation);
            plugin.getSpawnMap().put(worldName, loc == null ? w.getSpawnLocation() : loc);
        }
    }

    /** @see AbstractConfig#getConfigString() */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        FrameBuilder frame;

        // Header
        frame = new FrameBuilder();
        frame.addLine("Config file for NWorld plugin", FrameBuilder.Option.CENTER);
        frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
        frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
        for (final String line : frame.build()) {
            content.append(line + '\n');
        }

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

    Location buildLocation(final World world, final String string) {
        final String[] location = string.split(LEVEL2_SEPARATOR);
        try {
            final double x = Integer.parseInt(location[0]) + 0.5;
            final double y = Integer.parseInt(location[1]) + 0.25;
            final double z = Integer.parseInt(location[2]) + 0.5;
            final double yaw = Float.parseFloat(location[3]);
            final double pitch = Float.parseFloat(location[4]);
            return new Location(world, x, y, z, (float) yaw, (float) pitch);
        } catch (final Exception e) {
            plugin.getLogger().severe("Unable to load spawnpoint of world " + world.getName());
            return null;
        }
    }

    public void loadWorldFromConfig(final World world) {
        final Path path = Paths.get(plugin.getDataFolder().toPath().toAbsolutePath().toString() + File.separator + "config.yml");
        if (!Files.exists(path)) {
            plugin.getWorldMap().put(world.getName(), false);
            plugin.getSpawnMap().put(world.getName(), world.getSpawnLocation());
        } else {
            final YamlConfiguration config = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine() + '\n');
                }
                config.loadFromString(s.toString());
            } catch (final Exception e) {
                e.printStackTrace();
            }

            // Load known worlds list
            final List<String> worldList = config.getStringList("worlds");
            for (final String code : worldList) {
                final String[] split = code.split(LEVEL1_SEPARATOR);
                final String worldName = split[0];
                if (worldName.equals(world.getName())) {
                    final boolean availableForTeleportation = Integer.parseInt(split[1]) == 1;
                    final Location loc = buildLocation(world, split[2]);
                    plugin.getWorldMap().put(world.getName(), availableForTeleportation);
                    plugin.getSpawnMap().put(world.getName(), loc == null ? world.getSpawnLocation() : loc);
                    return;
                }
            }
            plugin.getWorldMap().put(world.getName(), false);
            plugin.getSpawnMap().put(world.getName(), world.getSpawnLocation());
        }
    }

    public int getBroadcastOnWorldCreate() {
        return broadcastOnWorldCreate;
    }

    private void setBroadcastOnWorldCreate(int broadcastOnWorldCreate) {
        this.broadcastOnWorldCreate = broadcastOnWorldCreate;
    }

    public int getBroadcastOnWorldLoad() {
        return broadcastOnWorldLoad;
    }

    private void setBroadcastOnWorldLoad(int broadcastOnWorldLoad) {
        this.broadcastOnWorldLoad = broadcastOnWorldLoad;
    }

    public int getBroadcastOnWorldUnload() {
        return broadcastOnWorldUnload;
    }

    private void setBroadcastOnWorldUnload(int broadcastOnWorldUnload) {
        this.broadcastOnWorldUnload = broadcastOnWorldUnload;
    }
}
