package fr.ribesg.bukkit.ncuboid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    public static final Charset                                  CHARSET = Charset.defaultCharset();

    @Getter @Setter(AccessLevel.PRIVATE) private static Material selectionItemMaterial;

    public static void loadConfig(final Path pathConfig) throws IOException {
        if (!Files.exists(pathConfig)) {
            Files.createFile(pathConfig);
            writeConfig(pathConfig);
        } else {
            final YamlConfiguration config = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(pathConfig, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine() + '\n');
                }
                config.loadFromString(s.toString());
            } catch (final Exception e) {
                e.printStackTrace();
            }

            // selectionItemMaterial. Default : Stick/280
            final Material m = Material.getMaterial(config.getInt("selectionItemMaterial", 280));
            setSelectionItemMaterial(m == null ? Material.STICK : m);

            // Rewrite the config to "clean" it
            writeConfig(pathConfig);
        }
    }

    public static void writeConfig(final Path pathConfig) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(pathConfig, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            final StringBuilder content = new StringBuilder();

            // Header
            content.append("################################################################################\n");
            content.append("# Config file for NCuboid plugin. If you don't understand something, please    #\n");
            content.append("# ask on dev.bukkit.org or on forum post.                               Ribesg #\n");
            content.append("################################################################################\n\n");

            // selectionItemMaterial. Default : Stick/280
            content.append("# The tool used to select points and get informations about blocks protection. Default : 180 (Stick)\n");
            content.append("selectionItemMaterial: " + getSelectionItemMaterial().getId() + "\n\n");

            writer.write(content.toString());
        }
    }
}
