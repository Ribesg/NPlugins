package fr.ribesg.bukkit.ntheendagain.handler;

import fr.ribesg.bukkit.ncore.utils.Utils;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.task.RegenTask;
import fr.ribesg.bukkit.ntheendagain.task.RespawnTask;
import fr.ribesg.bukkit.ntheendagain.task.UnexpectedDragonDeathHandlerTask;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EndWorldHandler {

    static final long KICK_TO_REGEN_DELAY    = 2L;
    static final long REGEN_TO_RESPAWN_DELAY = KICK_TO_REGEN_DELAY + 8L;

    private final String camelCaseWorldName;

    private final NTheEndAgain                 plugin;
    private final World                        endWorld;
    private final EndChunks                    chunks;
    private final Config                       config;
    private final Map<UUID, Map<String, Long>> dragons;
    private final Set<UUID>                    loadedDragons;
    private final Set<BukkitTask>              tasks;
    private final RespawnHandler               respawnHandler;
    private final RegenHandler                 regenHandler;

    /**
     * Class constructor
     * - Initialize all variables
     * <p/>
     * First thing to do after call to constructor is config load
     *
     * @param instance the Plugin instance
     * @param world    the related World
     */
    public EndWorldHandler(final NTheEndAgain instance, final World world) {
        plugin = instance;
        endWorld = world;
        camelCaseWorldName = Utils.toLowerCamelCase(endWorld.getName());
        chunks = new EndChunks(world.getName());
        config = new Config(plugin, endWorld.getName());
        dragons = new HashMap<>();
        loadedDragons = new HashSet<>();
        tasks = new HashSet<>();
        respawnHandler = new RespawnHandler(this);
        regenHandler = new RegenHandler(this);

        // Config is not yet loaded here
    }

    public void loadConfig() throws IOException {
        config.loadConfig(camelCaseWorldName + "Config.yml");
    }

    public void loadChunks() throws IOException {
        chunks.load(plugin.getConfigFilePath(camelCaseWorldName + "Chunks"));
    }

    public void saveConfig() throws IOException {
        config.writeConfig(camelCaseWorldName + "Config.yml");
    }

    public void saveChunks() throws IOException {
        chunks.write(plugin.getConfigFilePath(camelCaseWorldName + "Chunks"));
    }

    /**
     * Post-config-load initialization method
     * - Count existing EDs
     * - Respawn Dragons if needed
     * - Schedule respawn and regen tasks
     */
    public void init() {
        // Config is now loaded

        countEntities();

        if (config.getRespawnType() == 3) {
            respawnHandler.respawnNoRegen();
        }

        tasks.add(new UnexpectedDragonDeathHandlerTask(this).schedule(getPlugin()));

        if (config.getRespawnTimerMax() != 0 && (config.getRespawnType() == 4 || config.getRespawnType() == 5)) {
            tasks.add(new RespawnTask(this).schedule(getPlugin()));
        }

        if (config.getRegenTimer() != 0 && (config.getRegenType() == 2 || config.getRegenType() == 3)) {
            tasks.add(new RegenTask(this).schedule(getPlugin()));
        }

    }

    /**
     * To be called in plugin's onDisable() method or
     * when the world is unloaded.
     * - Cancel all tasks
     * - Hard regen if needed and if plugin disable
     * - Make scheduled tasks persistent
     * - Save configs
     */
    public void unload(boolean pluginDisabled) {
        for (final BukkitTask t : tasks) {
            t.cancel();
        }
        tasks.clear();
        if (pluginDisabled && config.getHardRegenOnStop() == 1) {
            getRegenHandler().regen();
        }
        try {
            // Reload-friendly lastExecTime storing in config file
            final long nextRegenExecTime = getConfig().getNextRegenTaskTime();
            final long nextRespawnExecTime = getConfig().getNextRespawnTaskTime();
            loadConfig();
            getConfig().setNextRegenTaskTime(getConfig().getRegenTimer() == 0 ? 0 : nextRegenExecTime);
            getConfig().setNextRespawnTaskTime(getConfig().getRespawnTimerMax() == 0 ? 0 : nextRespawnExecTime);
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

    /**
     * Counts:
     * - EnderDragons
     * - EnderCrystals
     */
    private void countEntities() {
        plugin.getLogger().info("Counting existing EDs in " + endWorld.getName() + "...");
        for (final EndChunk c : getChunks()) {
            if (endWorld.isChunkLoaded(c.getX(), c.getZ())) {
                final Chunk chunk = endWorld.getChunkAt(c.getX(), c.getZ());
                for (final Entity e : chunk.getEntities()) {
                    if (e.getType() == EntityType.ENDER_DRAGON) {
                        final EnderDragon ed = (EnderDragon) e;
                        if (!getDragons().containsKey(ed.getUniqueId())) {
                            ed.setMaxHealth(getConfig().getEdHealth());
                            ed.setHealth(ed.getMaxHealth());
                            getDragons().put(ed.getUniqueId(), new HashMap<String, Long>());
                            getLoadedDragons().add(ed.getUniqueId());
                        }
                    } else if (e.getType() == EntityType.ENDER_CRYSTAL) {
                        c.addCrystalLocation(e);
                    }
                }
            } else {
                endWorld.loadChunk(c.getX(), c.getZ());
                c.resetSavedDragons();
                endWorld.unloadChunkRequest(c.getX(), c.getZ());
            }
        }
        plugin.getLogger().info("Done, " + getNumberOfAliveEnderDragons() + " EnderDragon(s) found.");
    }

    /**
     * Called when a Player hits an EnderDragon
     *
     * @param enderDragonID the EnderDragon's ID
     * @param playerName    the Player's name
     * @param dmg           the amount of damages done
     */
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

    public EndChunks getChunks() {
        return chunks;
    }

    public Config getConfig() {
        return config;
    }

    public Map<UUID, Map<String, Long>> getDragons() {
        return dragons;
    }

    public World getEndWorld() {
        return endWorld;
    }

    public Set<UUID> getLoadedDragons() {
        return loadedDragons;
    }

    public int getNumberOfAliveEnderDragons() {
        return getLoadedDragons().size() + getChunks().getTotalSavedDragons();
    }

    public NTheEndAgain getPlugin() {
        return plugin;
    }

    public Set<BukkitTask> getTasks() {
        return tasks;
    }

    public RespawnHandler getRespawnHandler() {
        return respawnHandler;
    }

    public RegenHandler getRegenHandler() {
        return regenHandler;
    }

    public String getCamelCaseWorldName() {
        return camelCaseWorldName;
    }
}
