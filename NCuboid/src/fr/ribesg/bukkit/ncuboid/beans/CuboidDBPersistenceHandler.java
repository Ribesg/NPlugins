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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid.CuboidType;

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
        
        final ConfigurationSection worldCuboidsSection = config.createSection("worldCuboids");
        final Iterator<WorldCuboid> worldCuboidIterator = db.worldCuboidIterator();
        while (worldCuboidIterator.hasNext()) {
            final WorldCuboid cuboid = worldCuboidIterator.next();
            writeWorldCuboid(worldCuboidsSection, cuboid);
            
        }
        
        final ConfigurationSection playerCuboidsSection = config.createSection("playerCuboids");
        final Iterator<PlayerCuboid> playerCuboidIterator = db.playerCuboidIterator();
        while (playerCuboidIterator.hasNext()) {
            final PlayerCuboid cuboid = playerCuboidIterator.next();
            writePlayerCuboid(playerCuboidsSection, cuboid);
        }
        
        final String configString = config.saveToString();
        try (BufferedWriter writer = Files.newBufferedWriter(cuboidDBConfigFile, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            writer.write(configString);
        }
    }
    
    private static void writeWorldCuboid(final ConfigurationSection parent, final WorldCuboid cuboid) {
        final ConfigurationSection sec = parent.createSection(cuboid.getWorld().getName());
        sec.set("priority", cuboid.getPriority());
        writeFlags(sec, cuboid);
        writeFlagAtts(sec, cuboid);
        writeRights(sec, cuboid);
        
    }
    
    private static void writePlayerCuboid(final ConfigurationSection parent, final PlayerCuboid cuboid) {
        final ConfigurationSection sec = parent.createSection(cuboid.getCuboidName());
        
        // TODO Set everything else here
        sec.set("priority", cuboid.getPriority());
        
        if (cuboid.getType() == CuboidType.RECT) {
            // Set coords etc
        } else {
            // Hello, future
        }
        
        writeFlags(sec, cuboid);
        writeFlagAtts(sec, cuboid);
        writeRights(sec, cuboid);
    }
    
    private static void writeFlags(ConfigurationSection parent, final GeneralCuboid cuboid) {
        List<String> flags = new ArrayList<String>();
        for (Flag f : Flag.values()) {
            if (cuboid.getFlag(f)) {
                flags.add(f.toString());
            }
        }
        parent.set("flags", flags);
    }
    
    private static void writeFlagAtts(ConfigurationSection parent, final GeneralCuboid cuboid) {
        final ConfigurationSection sec = parent.createSection(cuboid.getWorld().getName());
        
        for (FlagAtt f : FlagAtt.values()) {
            if (FlagAtt.isIntFlagAtt(f)) {
                Integer theInteger = cuboid.getIntFlagAtt(f);
                if (theInteger != null) {
                    sec.set(f.toString(), theInteger);
                }
            } else if (FlagAtt.isLocFlagAtt(f)) {
                Location theLocation = cuboid.getLocFlagAtt(f);
                if (theLocation != null) {
                    sec.set(f.toString(), Utils.toStringPlus(theLocation));
                }
            } else if (FlagAtt.isVectFlagAtt(f)) {
                Vector theVector = cuboid.getVectFlagAtt(f);
                if (theVector != null) {
                    sec.set(f.toString(), Utils.toString(theVector));
                }
            } else {
                // Hello, future
            }
        }
    }
    
    private static void writeRights(ConfigurationSection parent, final GeneralCuboid cuboid) {
        ConfigurationSection sec = parent.createSection("rights");
        
        Set<String> allowedPlayers = cuboid.getAllowedPlayers();
        if (allowedPlayers != null) {
            List<String> allowedPlayersStringList = new ArrayList<String>(allowedPlayers);
            sec.set("disallowedPlayers", allowedPlayersStringList);
        }
        
        Set<String> allowedGroups = cuboid.getAllowedGroups();
        if (allowedGroups != null) {
            List<String> allowedGroupsStringList = new ArrayList<String>(allowedGroups);
            sec.set("allowedGroups", allowedGroupsStringList);
        }
        
        Set<String> disallowedCommands = cuboid.getDisallowedCommands();
        if (disallowedCommands != null) {
            List<String> disallowedCommandsStringList = new ArrayList<String>(disallowedCommands);
            sec.set("disallowedCommands", disallowedCommandsStringList);
        }
    }
}
