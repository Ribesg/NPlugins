/***************************************************************************
 * Project file:    NPlugins - NCore - AbstractConfig.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.config.AbstractConfig           *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.config;

import fr.ribesg.bukkit.ncore.util.DateUtil;

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

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a config file
 *
 * @param <T> The Node type
 *
 * @author Ribesg
 */
public abstract class AbstractConfig<T extends JavaPlugin> {

    /**
     * The Charset used for reading/writing files
     */
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * The Plugin linked to this config
     */
    protected final T plugin;

    /**
     * Constructor
     *
     * @param instance Linked plugin instance
     */
    public AbstractConfig(final T instance) {
        this.plugin = instance;
    }

    /**
     * Default fileName to config.yml in call to {@link #loadConfig(String)}
     *
     * @throws IOException                   If there is an error reading / writing file
     * @throws InvalidConfigurationException If the configuration is malformed
     */
    public void loadConfig() throws IOException, InvalidConfigurationException {
        this.loadConfig("config.yml");
    }

    /**
     * Load the config containing messages
     * Creates a new config if it does not exists
     * Fix the config after parsing
     *
     * @param fileName The name of the file to load
     *
     * @throws IOException                   If there is an error reading / writing file
     * @throws InvalidConfigurationException If the configuration is malformed
     */
    public void loadConfig(final String fileName) throws IOException, InvalidConfigurationException {
        final Path path = Paths.get(this.plugin.getDataFolder().toPath().toAbsolutePath() + File.separator + fileName);
        if (!Files.exists(path)) {
            path.toFile().getParentFile().mkdirs();
            Files.createFile(path);
            this.writeConfig(path);
        } else {
            final YamlConfiguration config = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine()).append('\n');
                }
                config.loadFromString(s.toString());
            }

            try {
                this.handleValues(config);
            } catch (final Throwable t) {
                // Make a backup copy of the just-read file in case something REALLY wrong happened
                Files.copy(path, Paths.get(this.plugin.getDataFolder().toPath().toAbsolutePath().toString(), fileName + '.' + DateUtil.formatNow() + ".bak"));
                throw t;
            }

            // Rewrite the config to "clean" it
            this.writeConfig(path);
        }
    }

    public void writeConfig() throws IOException {
        this.writeConfig("config.yml");
    }

    public void writeConfig(final String fileName) throws IOException {
        final Path path = Paths.get(this.plugin.getDataFolder().toPath().toAbsolutePath() + File.separator + fileName);
        this.writeConfig(path);
    }

    private void writeConfig(final Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            writer.write(this.getConfigString());
        }
    }

    protected void wrongValue(final String fileName, final String key, final Object incorrectValue, final Object valueSet) {
        final StringBuilder message1 = new StringBuilder();
        message1.append("Incorrect value '").append(incorrectValue);
        message1.append("' found in config file ").append(fileName);
        message1.append(" for key '").append(key).append('\'');

        final StringBuilder message2 = new StringBuilder();
        message2.append("The value of config key '").append(key);
        message2.append("' as been reset to '").append(valueSet);
        message2.append("' in file ").append(fileName);

        this.plugin.getLogger().warning(message1.toString());
        this.plugin.getLogger().info(message2.toString());
    }

    protected boolean match(final long value, final long min, final long max) {
        return min <= value && value <= max;
    }

    protected boolean match(final double value, final double min, final double max) {
        return min <= value && value <= max;
    }

    /**
     * Read the values in the YamlConfiguration instance and handles them correctly (Ex: saves them to Config class fields)
     *
     * @param config The config where to set values
     *
     * @throws InvalidConfigurationException If the configuration is malformed
     */
    protected abstract void handleValues(final YamlConfiguration config) throws InvalidConfigurationException;

    /**
     * @return the String to be written to file
     */
    protected abstract String getConfigString();
}
