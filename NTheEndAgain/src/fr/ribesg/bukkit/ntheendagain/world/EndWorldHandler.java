package fr.ribesg.bukkit.ntheendagain.world;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.tasks.RespawnTask;
import fr.ribesg.bukkit.ntheendagain.tasks.UnexpectedDragonDeathHandlerTask;

public class EndWorldHandler {

    private final static Random                                rand = new Random();

    private final String                                       camelCaseWorldName;

    private final NTheEndAgain                                 plugin;
    @Getter private final World                                endWorld;
    @Getter private final EndChunks                            chunks;
    @Getter private final Config                               config;
    @Getter private final HashMap<UUID, HashMap<String, Long>> dragons;
    @Getter private final HashSet<UUID>                        loadedDragons;

    @Getter private int                                        numberOfAliveEnderDragons;

    public EndWorldHandler(final NTheEndAgain instance, final World world) {
        plugin = instance;
        endWorld = world;
        camelCaseWorldName = Utils.toLowerCamelCase(endWorld.getName());
        chunks = new EndChunks(plugin);
        config = new Config(plugin, endWorld.getName());
        dragons = new HashMap<UUID, HashMap<String, Long>>();
        loadedDragons = new HashSet<UUID>();

        // Config is not yet loaded here
    }

    public void loadConfig() throws IOException {
        config.loadConfig(plugin, camelCaseWorldName + "Config.yml");
    }

    public void loadChunks() throws IOException {
        chunks.load(plugin.getConfigFilePath(camelCaseWorldName + "Chunks"));
    }

    public void saveConfig() throws IOException {
        config.writeConfig(plugin, camelCaseWorldName + "Config.yml");
    }

    public void saveChunks() throws IOException {
        chunks.write(plugin.getConfigFilePath(camelCaseWorldName + "Chunks"));
    }

    public void init() {
        // Config is now loaded

        numberOfAliveEnderDragons = 0;

        plugin.getLogger().info("Counting existing EDs in " + endWorld.getName() + "...");
        for (final EndChunk c : getChunks()) {
            if (endWorld.isChunkLoaded(c.getX(), c.getZ())) {
                final Chunk chunk = endWorld.getChunkAt(c.getX(), c.getZ());
                for (final Entity e : chunk.getEntities()) {
                    if (e.getType() == EntityType.ENDER_DRAGON) {
                        final EnderDragon ed = (EnderDragon) e;
                        if (!getDragons().containsKey(ed.getUniqueId())) {
                            ed.setMaxHealth(getConfig().getEnderDragonHealth());
                            ed.setHealth(ed.getMaxHealth());
                            getDragons().put(ed.getUniqueId(), new HashMap<String, Long>());
                            getLoadedDragons().add(ed.getUniqueId());
                            incrementDragonCount();
                        }
                    }
                }
            } else {
                endWorld.loadChunk(c.getX(), c.getZ());
                endWorld.unloadChunkRequest(c.getX(), c.getZ());
            }
        }
        plugin.getLogger().info("Done, " + numberOfAliveEnderDragons + " EnderDragon(s) found.");

        if (config.getRespawnOnBoot() == 1) {
            if (getConfig().getRegenOnRespawn() == 1) {
                regen();
            }
            respawnDragons();
        }

        final BukkitScheduler scheduler = plugin.getServer().getScheduler();
        final UnexpectedDragonDeathHandlerTask deathTask = new UnexpectedDragonDeathHandlerTask(this);
        scheduler.runTaskTimer(plugin, deathTask, 0L, 20L);

        if (config.getRespawnTimer() != 0) {
            final long t = config.getLastTaskExecTime();
            long initialDelay = 0;
            if (t != 0) {
                initialDelay = config.getRespawnTimer() - (System.currentTimeMillis() - t);
                if (initialDelay < 0) {
                    initialDelay = 0;
                }
            }
            final RespawnTask task = new RespawnTask(this);
            scheduler.runTaskTimer(plugin, task, initialDelay, config.getRespawnTimer() * 20);
        }

    }

    public void playerHitED(final UUID enderDragonID, final String playerName, final long dmg) {
        HashMap<String, Long> dragonMap;
        if (!dragons.containsKey(enderDragonID)) {
            dragonMap = new HashMap<String, Long>();
            dragons.put(enderDragonID, dragonMap);
        } else {
            dragonMap = dragons.get(enderDragonID);
        }
        if (dragonMap.containsKey(playerName)) {
            dragonMap.put(playerName, dragonMap.get(playerName) + dmg);
        } else {
            dragonMap.put(playerName, dmg);
        }
    }

    public int respawnDragons() {
        int respawned = 0;
        for (int i = getNumberOfAliveEnderDragons(); i < config.getNbEnderDragons(); i++) {
            respawnDragon();
            respawned++;
        }
        if (respawned >= 1) {
            plugin.broadcastMessage(MessageId.theEndAgain_respawned, Integer.toString(respawned), endWorld.getName());
        }
        return respawned;
    }

    public void regen() {
        switch (config.getActionOnRegen()) {
            case 0:
                final String[] lines = plugin.getMessages().get(MessageId.theEndAgain_worldRegenerating);
                final StringBuilder messageBuilder = new StringBuilder(lines[0]);
                for (int i = 1; i < lines.length; i++) {
                    messageBuilder.append('\n');
                    messageBuilder.append(lines[i]);
                }
                final String message = messageBuilder.toString();
                for (final Player p : endWorld.getPlayers()) {
                    p.kickPlayer(message);
                }
            case 1:
                for (final Player p : endWorld.getPlayers()) {
                    // TODO Future: Use spawn point defined by NWorld, when NWorld will do it
                    //              and if NWorld is enabled of course
                    p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                    plugin.sendMessage(p, MessageId.theEndAgain_worldRegenerating);
                }
            default:
                // Not possible.
                break;
        }
        chunks.softRegen();
    }

    public void incrementDragonCount() {
        numberOfAliveEnderDragons++;
    }

    public void decrementDragonCount() {
        if (--numberOfAliveEnderDragons < 0) {
            numberOfAliveEnderDragons = 0;
        }
    }

    private void respawnDragon() {
        final int x = rand.nextInt(81) - 40; // [-40;40]
        final int y = 100 + rand.nextInt(21); // [100;120]
        final int z = rand.nextInt(81) - 40; // [-40;40]
        final Location loc = new Location(endWorld, x, y, z);
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load(true);
        }
        endWorld.spawnEntity(loc, EntityType.ENDER_DRAGON);
    }
}
