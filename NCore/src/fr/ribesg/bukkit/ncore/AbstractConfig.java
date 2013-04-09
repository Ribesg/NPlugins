package fr.ribesg.bukkit.ncore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a config file
 * 
 * @author ribes
 */
public abstract class AbstractConfig {

    /**
     * The Charset used for reading/writing files
     */
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Default fileName to config.yml in call to {@link #loadConfig(JavaPlugin, String)}
     * 
     * @param plugin
     *            The plugin
     * @throws IOException
     *             If there is an error reading / writing file
     */
    public void loadConfig(final JavaPlugin plugin) throws IOException {
        loadConfig(plugin, "config.yml");
    }

    /**
     * Load the config containing messages
     * Creates a new config if it does not exists
     * Fix the config after parsing
     * 
     * @param plugin
     *            The plugin
     * @param fileName
     *            The name of the file to load
     * @throws IOException
     *             If there is an error reading / writing file
     */
    public void loadConfig(final JavaPlugin plugin, final String fileName) throws IOException {
        final Path path = Paths.get(plugin.getDataFolder().toPath().toAbsolutePath().toString() + File.separator + fileName);
        if (!Files.exists(path)) {
            Files.createFile(path);
            writeConfig(path);
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

            setValues(config);

            // Rewrite the config to "clean" it
            writeConfig(path);
        }
    }

    public void writeConfig(final JavaPlugin plugin) throws IOException {
        final Path path = Paths.get(plugin.getDataFolder().toPath().toAbsolutePath().toString() + File.separator + "config.yml");
        writeConfig(path);
    }

    private void writeConfig(final Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            writer.write(getConfigString());
        }
    }

    /**
     * Set the values in the config to there current values
     * 
     * @param config
     *            The config where to set values
     */
    protected abstract void setValues(final YamlConfiguration config);

    /**
     * @return the String to be written to file
     */
    protected abstract String getConfigString();
}
