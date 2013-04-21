package fr.ribesg.bukkit.ntheendagain.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;

public class EndChunks implements Iterable<EndChunk> {

    private final static Charset                CHARSET = StandardCharsets.UTF_8;

    private final Logger                        log;
    private final HashMap<ChunkCoord, EndChunk> chunks;

    public EndChunks(final NTheEndAgain plugin) {
        log = plugin.getLogger();
        chunks = new HashMap<ChunkCoord, EndChunk>();
    }

    public EndChunk addChunk(final Chunk bukkitChunk) {
        EndChunk res = new EndChunk(bukkitChunk);
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
            if (config.isList("chunks")) {
                EndChunk ec;
                for (final String s : config.getStringList("chunks")) {
                    ec = EndChunk.fromString(s);
                    if (ec == null) {
                        log.warning("Error loading config: incorrect chunk format !");
                        log.warning("Incorrect format: " + s);
                    } else {
                        this.addChunk(ec);
                    }
                }
            } else {
                log.severe("Error loading config: 'chunks' list not found");
                throw new IOException("Error loading config");
            }
        }
    }

    public void write(final Path pathEndChunks) throws IOException {
        if (!Files.exists(pathEndChunks)) {
            Files.createFile(pathEndChunks);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(pathEndChunks, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            final YamlConfiguration config = new YamlConfiguration();
            final List<String> endChunks = new ArrayList<String>();
            for (final EndChunk ec : this) {
                endChunks.add(ec.toString());
            }
            config.set("chunks", endChunks);
            writer.write(config.saveToString());
        }
    }

    @Override
    public Iterator<EndChunk> iterator() {
        return chunks.values().iterator();
    }
}
