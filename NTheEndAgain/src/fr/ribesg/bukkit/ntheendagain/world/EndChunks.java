package fr.ribesg.bukkit.ntheendagain.world;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EndChunks implements Iterable<EndChunk> {

    private final static Charset CHARSET = StandardCharsets.UTF_8;

    private final HashMap<ChunkCoord, EndChunk> chunks;

    public EndChunks() {
        chunks = new HashMap<ChunkCoord, EndChunk>();
    }

    public EndChunk addChunk(final Chunk bukkitChunk) {
        final EndChunk res = new EndChunk(bukkitChunk);
        addChunk(res);
        return res;
    }

    private void addChunk(final EndChunk endChunk) {
        chunks.put(endChunk.getCoords(), endChunk);
    }

    public EndChunk getChunk(final String world, final int x, final int z) {
        return chunks.get(new ChunkCoord(x, z, world));
    }

    public void softRegen() {
        for (final EndChunk ec : this) {
            ec.setToBeRegen(!ec.isProtected());
        }
    }

    public void crystalRegen() {
        Chunk bukkitChunk;
        for (final EndChunk c : chunks.values()) {
            if (c.containsCrystal()) {
                final World w = Bukkit.getWorld(c.getWorldName());
                bukkitChunk = w.getChunkAt(c.getX(), c.getZ());
                final Set<Location> locs = c.getCrystalLocations();
                for (final Entity e : bukkitChunk.getEntities()) {
                    if (e.getType() == EntityType.ENDER_CRYSTAL) {
                        e.remove();
                    }
                }
                for (final Location loc : locs) {
                    w.spawnEntity(loc.clone().add(0, -1, 0), EntityType.ENDER_CRYSTAL);
                    loc.getBlock().getRelative(BlockFace.DOWN).setType(Material.BEDROCK);
                }
            }
        }
    }

    public void load(final Path pathEndChunks) throws IOException {
        if (!Files.exists(pathEndChunks)) {
            return;
        } else {
            final YamlConfiguration config = new YamlConfiguration();
            try (BufferedReader reader = Files.newBufferedReader(pathEndChunks, CHARSET)) {
                final StringBuilder s = new StringBuilder();
                while (reader.ready()) {
                    s.append(reader.readLine() + '\n');
                }
                config.loadFromString(s.toString());
            } catch (final Exception e) {
                e.printStackTrace();
            }

            for (final String chunkCoordString : config.getKeys(false)) {
                final ConfigurationSection sec = config.getConfigurationSection(chunkCoordString);
                if (sec != null) {
                    final EndChunk ec = EndChunk.rebuild(sec);
                    addChunk(ec);
                }
            }
        }
    }

    public void write(final Path pathEndChunks) throws IOException {
        if (!Files.exists(pathEndChunks)) {
            Files.createFile(pathEndChunks);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(pathEndChunks,
                                                             CHARSET,
                                                             StandardOpenOption.TRUNCATE_EXISTING,
                                                             StandardOpenOption.WRITE)) {
            final YamlConfiguration config = new YamlConfiguration();
            for (final EndChunk c : chunks.values()) {
                c.store(config);
            }
            writer.write(config.saveToString());
        }
    }

    @Override
    public Iterator<EndChunk> iterator() {
        return chunks.values().iterator();
    }
}
