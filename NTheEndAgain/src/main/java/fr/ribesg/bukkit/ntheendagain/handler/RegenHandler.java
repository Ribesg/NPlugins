/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - RegenHandler.java            *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.handler.RegenHandler     *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain.handler;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.task.SlowSoftRegeneratorTaskHandler;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Ribesg
 */
public class RegenHandler {

    /**
     * The End spawn Location is always [100;50;0]
     */
    private static final int END_SPAWN_CHUNK_X = 100 >> 4;
    /**
     * The End spawn Location is always [100;50;0]
     */
    private static final int END_SPAWN_CHUNK_Z = 0;

    private final EndWorldHandler worldHandler;
    private final NTheEndAgain    plugin;

    public RegenHandler(final EndWorldHandler worldHandler) {
        this.worldHandler = worldHandler;
        this.plugin = worldHandler.getPlugin();
    }

    public void regen() {
        this.plugin.entering(this.getClass(), "regen");

        this.regen(this.worldHandler.getConfig().getRegenMethod());

        this.plugin.exiting(this.getClass(), "regen");
    }

    public void regen(final int type) {
        this.plugin.entering(this.getClass(), "regen(int)");

        this.plugin.debug("Kicking players out of the world/server...");
        this.kickPlayers();

        this.plugin.debug("Schedule regen task in " + EndWorldHandler.KICK_TO_REGEN_DELAY + " ticks");
        Bukkit.getScheduler().runTaskLater(this.worldHandler.getPlugin(), new BukkitRunnable() {

            @Override
            public void run() {
                fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.plugin.entering(this.getClass(), "run", "task from regen(int)");

                switch (type) {
                    case 0:
                        fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.plugin.debug("Hard regen...");
                        fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.hardRegen(false);
                        break;
                    case 1:
                        fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.plugin.debug("Soft regen...");
                        fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.softRegen(false);
                        break;
                    case 2:
                        fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.plugin.debug("Crystal regen...");
                        fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.crystalRegen();
                        break;
                    default:
                        break;
                }

                fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.plugin.exiting(this.getClass(), "run", "task from regen(int)");
            }
        }, EndWorldHandler.KICK_TO_REGEN_DELAY);

        this.plugin.exiting(this.getClass(), "regen(int)");
    }

    public void hardRegenOnStop() {
        this.plugin.entering(this.getClass(), "hardRegenOnStop");

        this.hardRegen(true);

        this.plugin.exiting(this.getClass(), "hardRegenOnStop");
    }

    /*package*/ void regenThenRespawn() {
        this.plugin.entering(this.getClass(), "regenThenRespawn");

        this.regen();

        this.plugin.debug("Scheduling respawn task in " + EndWorldHandler.REGEN_TO_RESPAWN_DELAY + "ticks");
        Bukkit.getScheduler().runTaskLater(this.worldHandler.getPlugin(), new BukkitRunnable() {

            @Override
            public void run() {
                fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.plugin.entering(this.getClass(), "run", "task from regenThenRespawn");

                fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.worldHandler.getRespawnHandler().respawnNoRegen();

                fr.ribesg.bukkit.ntheendagain.handler.RegenHandler.this.plugin.exiting(this.getClass(), "run", "task from regenThenRespawn");
            }
        }, EndWorldHandler.REGEN_TO_RESPAWN_DELAY);

        this.plugin.exiting(this.getClass(), "regenThenRespawn");
    }

    private void hardRegen(final boolean pluginDisabled) {
        this.plugin.entering(this.getClass(), "hardRegen");

        final NTheEndAgain plugin = this.worldHandler.getPlugin();
        final World endWorld = this.worldHandler.getEndWorld();
        final EndChunks chunks = this.worldHandler.getChunks();

        final String prefix = "[REGEN " + endWorld.getName() + "] ";
        plugin.info(prefix + "Regenerating End world...");

        plugin.debug("Kicking players out of the world/server...");
        this.kickPlayers();

        plugin.debug("Calling softRegen to set all chunks to toBeRegen...");
        this.softRegen(pluginDisabled);

        final long totalChunks = chunks.size();
        long i = 0, regen = 0;
        long lastTime = System.currentTimeMillis();

        plugin.debug("Starting regeneration...");
        for (final EndChunk c : chunks) {
            if (System.currentTimeMillis() - lastTime > 500) {
                plugin.info(prefix + regen + " chunks regenerated (" + i * 100 / totalChunks + "% done)");
                lastTime = System.currentTimeMillis();
            }
            if (c.hasToBeRegen()) {
                c.cleanCrystalLocations();
                c.resetSavedDragons();
                for (final Entity e : endWorld.getChunkAt(c.getX(), c.getZ()).getEntities()) {
                    if (e.getType() == EntityType.ENDER_DRAGON) {
                        this.worldHandler.getDragons().remove(e.getUniqueId());
                        this.worldHandler.getLoadedDragons().remove(e.getUniqueId());
                    }
                    e.remove();
                }
                endWorld.regenerateChunk(c.getX(), c.getZ());
                c.setToBeRegen(false);
                regen++;
            }
            i++;
        }
        plugin.info(prefix + "Done.");

        plugin.exiting(this.getClass(), "hardRegen");
    }

    private void softRegen(final boolean pluginDisabled) {
        this.plugin.entering(this.getClass(), "softRegen");

        this.plugin.debug("Calling softRegen on chunks...");
        this.worldHandler.getChunks().softRegen();

		/*
         * Instantly regen the spawn chunk to prevent NPE when an Entity
		 * tries to teleport here and we regen the chunk on Chunk Load.
		 */
        this.plugin.debug("Regenerating spawn chunks...");
        this.worldHandler.getEndWorld().getChunkAt(END_SPAWN_CHUNK_X, END_SPAWN_CHUNK_Z).load(true);

        if (!pluginDisabled) {
            // Launch Slow Soft Regen task
            this.plugin.debug("Not disabling plugin, launch slow regen task...");
            this.worldHandler.setSlowSoftRegeneratorTaskHandler(new SlowSoftRegeneratorTaskHandler(this.worldHandler));
            this.worldHandler.getSlowSoftRegeneratorTaskHandler().run();
        }

        this.plugin.exiting(this.getClass(), "softRegen");
    }

    private void crystalRegen() {
        this.plugin.entering(this.getClass(), "crystalRegen");

        this.worldHandler.getChunks().crystalRegen();

        this.plugin.exiting(this.getClass(), "crystalRegen");
    }

    private void kickPlayers() {
        this.plugin.entering(this.getClass(), "kickPlayers");

        final Config config = this.worldHandler.getConfig();
        final NTheEndAgain plugin = this.worldHandler.getPlugin();
        final World endWorld = this.worldHandler.getEndWorld();

        switch (config.getRegenAction()) {
            case 0:
                plugin.debug("Kicking players...");
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
                break;
            case 1:
                plugin.debug("Teleporting players...");
                final World world = Bukkit.getWorlds().get(0);
                final WorldNode worldNode = plugin.getCore().getWorldNode();
                final Location spawnLoc;
                if (worldNode == null) {
                    spawnLoc = world.getSpawnLocation();
                } else {
                    spawnLoc = worldNode.getWorldSpawnLocation(world.getName());
                }
                for (final Player p : endWorld.getPlayers()) {
                    p.teleport(spawnLoc);
                    plugin.sendMessage(p, MessageId.theEndAgain_worldRegenerating);
                }
                break;
            case 2:
                // Do nothing
                break;
            default:
                throw new IllegalStateException("Invalid configuration value regenAction found at Runtime, please report this!");
        }

        plugin.exiting(this.getClass(), "kickPlayers");
    }
}
