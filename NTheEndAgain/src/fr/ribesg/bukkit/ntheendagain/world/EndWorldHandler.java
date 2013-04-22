package fr.ribesg.bukkit.ntheendagain.world;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.tasks.RespawnTask;
import fr.ribesg.bukkit.ntheendagain.tasks.UnexpectedDragonDeathHandlerTask;

public class EndWorldHandler {

    private final static Random                        rand = new Random();

    private final String                               camelCaseWorldName;

    @Getter private final NTheEndAgain                 plugin;
    @Getter private final World                        endWorld;
    @Getter private final EndChunks                    chunks;
    @Getter private final Config                       config;
    @Getter private final Map<UUID, Map<String, Long>> dragons;
    @Getter private final Set<UUID>                    loadedDragons;
    @Getter private final Set<BukkitTask>              tasks;

    @Getter private int                                numberOfAliveEnderDragons;

    public EndWorldHandler(final NTheEndAgain instance, final World world) {
        plugin = instance;
        endWorld = world;
        camelCaseWorldName = Utils.toLowerCamelCase(endWorld.getName());
        chunks = new EndChunks(plugin);
        config = new Config(plugin, endWorld.getName());
        dragons = new HashMap<UUID, Map<String, Long>>();
        loadedDragons = new HashSet<UUID>();
        tasks = new HashSet<BukkitTask>();

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
            respawnDragons();
        }

        final BukkitScheduler scheduler = plugin.getServer().getScheduler();
        final UnexpectedDragonDeathHandlerTask deathTask = new UnexpectedDragonDeathHandlerTask(this);
        tasks.add(scheduler.runTaskTimer(plugin, deathTask, 0L, 20L));

        if (config.getRespawnTimerMax() != 0) {
            final long t = config.getLastTaskExecTime();
            long initialDelay = 0;
            if (t != 0) {
                initialDelay = config.getRespawnTimerMin() - (System.currentTimeMillis() - t);
                if (initialDelay < 0) {
                    initialDelay = 0;
                }
            }

            int randomRespawnTimer = rand.nextInt(config.getRespawnTimerMax() - config.getRespawnTimerMin()) + config.getRespawnTimerMin();
            final RespawnTask task = new RespawnTask(this);
            tasks.add(scheduler.runTaskTimer(plugin, task, initialDelay, randomRespawnTimer * 20));
        }

    }

    public void unload() {
        for (BukkitTask t : tasks) {
            t.cancel();
        }
        tasks.clear();
        if (getConfig().getRespawnOnBoot() == 1 && getConfig().getRespawnTimerMax() == 0 && getConfig().getRegenOnRespawn() == 1) {
            hardRegen();
        }
        try {
            // Reload-friendly lastExecTime storing in config file
            final long lastExecTime = getConfig().getLastTaskExecTime();
            loadConfig();
            getConfig().setLastTaskExecTime(getConfig().getRespawnTimerMax() == 0 ? 0 : lastExecTime);
            saveConfig();
        } catch (final IOException e) {
            plugin.getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            plugin.getLogger().severe("This error occured when NTheEndAgain tried to save " + e.getMessage() + ".yml");
        }
        try {
            saveChunks();
        } catch (final IOException e) {
            plugin.getLogger().severe("An error occured, stacktrace follows:");
            e.printStackTrace();
            plugin.getLogger().severe("This error occured when NTheEndAgain tried to save " + e.getMessage() + ".yml");
            plugin.getLogger().severe("/!\\ THIS MEANS THAT PROTECTED CHUNKS COULD BE REGENERATED ON NEXT REGEN IN THIS WORLD /!\\");
        }
    }

    public void playerHitED(final UUID enderDragonID, final String playerName, final long dmg) {
        Map<String, Long> dragonMap;
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
        int respawning = 0;
        for (int i = getNumberOfAliveEnderDragons(); i < config.getNbEnderDragons(); i++) {
            respawnDragon();
            respawning++;
        }
        if (respawning >= 1) {
            plugin.broadcastMessage(MessageId.theEndAgain_respawned, Integer.toString(respawning), endWorld.getName());
        }
        return respawning;
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

    private void hardRegen() {
        plugin.getLogger().info("Regenerating End world \"" + endWorld.getName() + "\"...");
        regen();
        for (final EndChunk c : chunks) {
            if (c.hasToBeRegen()) {
                endWorld.regenerateChunk(c.getX(), c.getZ());
                c.setToBeRegen(false);
            }
        }
        plugin.getLogger().info("Done.");
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
        // Create a random location near the center
        final int x = rand.nextInt(81) - 40; // [-40;40]
        final int y = 100 + rand.nextInt(21); // [100;120]
        final int z = rand.nextInt(81) - 40; // [-40;40]
        final Location loc = new Location(endWorld, x, y, z);

        final Chunk chunk = loc.getChunk();
        final EndChunk endChunk = chunks.getChunk(endWorld.getName(), chunk.getX(), chunk.getZ());
        if (endChunk != null && endChunk.hasToBeRegen()) {
            endWorld.regenerateChunk(chunk.getX(), chunk.getZ());
            endChunk.setToBeRegen(false);
        }

        Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

            @Override
            public void run() {
                endWorld.spawnEntity(loc, EntityType.ENDER_DRAGON);
            }
        }, 2L);
    }
}
