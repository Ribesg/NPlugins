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
     * Load the config containing messages
     * Creates a new config if it does not exists
     * Fix the config after parsing
     * 
     * @param plugin
     *            The plugin
     * @throws IOException
     *             If there is an error reading / writing file
     */
    public void loadConfig(JavaPlugin plugin) throws IOException {
        Path path = Paths.get(
                        plugin.getDataFolder().toPath().toAbsolutePath().toString()
                                        + File.separator
                                        + "config.yml");
        if (!Files.exists(path)) {
            Files.createFile(path);
            writeConfig(plugin);
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
            writeConfig(plugin);
        }
    }
    
    private void writeConfig(JavaPlugin plugin) throws IOException {
        Path path = Paths.get(
                        plugin.getDataFolder().toPath().toAbsolutePath().toString()
                                        + File.separator
                                        + "config.yml");
        writeConfig(path);
    }
    
    private void writeConfig(Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET, StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
            writer.write(getConfigString());
        }
    }
    
    /**
     * Set the values in the config to there current values
     * 
     * @param config
     *            The config where to set values
     */
    protected abstract void setValues(YamlConfiguration config);
    
    /**
     * @return the String to be written to file
     */
    protected abstract String getConfigString();
}
