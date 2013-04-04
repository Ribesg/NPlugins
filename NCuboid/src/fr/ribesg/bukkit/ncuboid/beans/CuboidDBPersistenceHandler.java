package fr.ribesg.bukkit.ncuboid.beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ncuboid.NCuboid;

public class CuboidDBPersistenceHandler {

    public static final Charset CHARSET     = StandardCharsets.UTF_8;
    private final static String DB_FILENAME = "cuboidDB.yml";

    public static CuboidDB loadDB(final NCuboid plugin) throws IOException, InvalidConfigurationException {
        final CuboidDB db = new CuboidDB(plugin);

        final Path pluginFolder = plugin.getDataFolder().toPath();
        if (!Files.exists(pluginFolder)) {
            Files.createDirectories(pluginFolder);
        }

        final Path cuboidDBConfigFile = Paths.get(pluginFolder.toString(), DB_FILENAME);
        if (!Files.exists(cuboidDBConfigFile)) {
            return db;
        } else {
            String configString = null;

            try (BufferedReader reader = Files.newBufferedReader(cuboidDBConfigFile, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine() + '\n');
                }
                configString = s.toString();
            }

            final YamlConfiguration config = new YamlConfiguration();
            config.loadFromString(configString);

            if (config.isConfigurationSection("worlds")) {
                final ConfigurationSection sec = config.getConfigurationSection("worlds");
                for (final String key : sec.getKeys(false)) {
                    final WorldCuboid cuboid = readWorldCuboid(sec, key);
                    db.addByWorld(cuboid);
                }
            }

            if (config.isConfigurationSection("players")) {
                final ConfigurationSection sec = config.getConfigurationSection("players");
                for (final String key : sec.getKeys(false)) {
                    final PlayerCuboid cuboid = readPlayerCuboid(sec, key);
                    db.add(cuboid);
                }
            }

            return db;
        }
    }

    private static WorldCuboid readWorldCuboid(final ConfigurationSection sec, final String name) {

        // TODO

        return null;
    }

    private static PlayerCuboid readPlayerCuboid(final ConfigurationSection sec, final String name) {

        // TODO

        return null;
    }

    public static void saveDB(final NCuboid plugin, final CuboidDB db) throws IOException {
        final Path pluginFolder = plugin.getDataFolder().toPath();
        if (!Files.exists(pluginFolder)) {
            Files.createDirectories(pluginFolder);
        }

        final Path cuboidDBConfigFile = Paths.get(pluginFolder.toString(), DB_FILENAME);
        if (!Files.exists(cuboidDBConfigFile)) {
            Files.createFile(cuboidDBConfigFile);
        }

        final YamlConfiguration config = new YamlConfiguration();

        final ConfigurationSection worldCuboids = config.createSection("worldCuboids");
        final Iterator<WorldCuboid> worldCuboidIterator = db.worldCuboidIterator();
        while (worldCuboidIterator.hasNext()) {
            final WorldCuboid cuboid = worldCuboidIterator.next();
            writeConfigurationSection(worldCuboids, cuboid);

        }

        final ConfigurationSection playerCuboids = config.createSection("playerCuboids");
        final Iterator<PlayerCuboid> playerCuboidIterator = db.playerCuboidIterator();
        while (playerCuboidIterator.hasNext()) {
            final PlayerCuboid cuboid = playerCuboidIterator.next();
            writeConfigurationSection(playerCuboids, cuboid);
        }

        final String configString = config.saveToString();
        try (BufferedWriter writer = Files.newBufferedWriter(cuboidDBConfigFile, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            writer.write(configString);
        }

    }

    private static void writeConfigurationSection(final ConfigurationSection parent, final WorldCuboid cuboid) {
        // final ConfigurationSection sec = parent.createSection(cuboid.getWorld().getName());

        // TODO
    }

    private static void writeConfigurationSection(final ConfigurationSection parent, final PlayerCuboid cuboid) {
        // final ConfigurationSection sec = parent.createSection(cuboid.getCuboidName());

        // TODO
    }
}
