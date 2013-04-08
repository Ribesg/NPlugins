package fr.ribesg.bukkit.ntheendagain.world;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.tasks.RespawnTask;

public class EndWorldHandler {

    private final static Random                                rand = new Random();

    private final String                                       camelCaseWorldName;

    private final NTheEndAgain                                 plugin;
    @Getter private final World                                endWorld;
    @Getter private final EndChunks                            chunks;
    @Getter private final Config                               config;
    @Getter private final HashMap<UUID, HashMap<String, Long>> dragons;

    @Getter @Setter private int                                numberOfAliveEDs;

    public EndWorldHandler(final NTheEndAgain instance, final World world) {
        plugin = instance;
        endWorld = world;
        camelCaseWorldName = Utils.toLowerCamelCase(endWorld.getName());
        chunks = new EndChunks(plugin);
        config = new Config(plugin, endWorld.getName());
        dragons = new HashMap<UUID, HashMap<String, Long>>();

        if (config.getRespawnOnBoot() == 1) {
            respawnDragons();
        } else {
            updateNumberOfAliveEDs();
        }
    }

    public void loadConfigs() throws IOException {
        chunks.load(plugin.getConfigFilePath(camelCaseWorldName + "Chunks"));
        config.loadConfig(plugin, camelCaseWorldName + "Config.yml");
    }

    public void saveChunks() throws IOException {
        chunks.write(plugin.getConfigFilePath(camelCaseWorldName + "Chunks"));
    }

    public void init() {
        if (config.getRespawnTimer() != 0) {
            long t = config.getLastTaskExecTime();
            long initialDelay = 0;
            if (t != 0) {
                initialDelay = config.getRespawnTimer() - (System.currentTimeMillis() - t);
                if (initialDelay < 0) {
                    initialDelay = 0;
                }
            }
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            RespawnTask task = new RespawnTask(this);
            scheduler.runTaskTimer(plugin, task, initialDelay, config.getRespawnTimer());
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
        updateNumberOfAliveEDs();
        int respawned = 0;
        for (int i = numberOfAliveEDs; i <= config.getNbEnderDragons(); i++) {
            respawnDragon();
            respawned++;
        }
        if (respawned == 1) {
            // TODO plugin.broadcastMessage(TODO) 1 spawned
            plugin.getServer().broadcastMessage("An EnderDragon has been respawned");
        } else if (respawned > 1) {
            // TODO plugin.broadcastMessage(TODO) x spawned
            plugin.getServer().broadcastMessage(respawned + " EnderDragons has been respawned");
        }
        return respawned;
    }

    public void regen() {
        switch (config.getActionOnRegen()) {
            case 0:
                for (final Player p : endWorld.getPlayers()) {
                    p.kickPlayer("End World Regenarating"); // TODO Messages
                }
            case 1:
                for (final Player p : endWorld.getPlayers()) {
                    // TODO Future: Use spawn point defined by NWorld, when NWorld will do it
                    //              and if NWorld is enabled of course
                    p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                    p.sendMessage("End World Regenerating"); // TODO Messages
                }
            default:
                // Not possible.
                break;
        }
        for (final EnderDragon e : endWorld.getEntitiesByClass(EnderDragon.class)) {
            e.remove();
        }
        dragons.clear();
        numberOfAliveEDs = 0;
        chunks.softRegen();

        // TODO Messages
        // TODO Broadcast message
        plugin.getServer().broadcastMessage("End world " + endWorld.getName() + " regenerated");
    }

    private void respawnDragon() {
        final int x = rand.nextInt(41) - 20; // [-20;20]
        final int y = 70 + rand.nextInt(21); // [70;90]
        final int z = rand.nextInt(41) - 20; // [-20;20]
        final Location loc = new Location(endWorld, x, y, z);
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load(true);
        }
        endWorld.spawnEntity(loc, EntityType.ENDER_DRAGON);
    }

    private void updateNumberOfAliveEDs() {
        numberOfAliveEDs = endWorld.getEntitiesByClass(EnderDragon.class).size();
    }
}
