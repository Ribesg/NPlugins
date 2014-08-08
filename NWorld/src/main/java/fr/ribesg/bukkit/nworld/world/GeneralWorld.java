/***************************************************************************
 * Project file:    NPlugins - NWorld - GeneralWorld.java                  *
 * Full Class name: fr.ribesg.bukkit.nworld.world.GeneralWorld             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.world;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.WorldUtil;
import fr.ribesg.bukkit.nworld.NWorld;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Ribesg
 */
public abstract class GeneralWorld implements Comparable<GeneralWorld> {

    public enum WorldType {
        STOCK,
        STOCK_NETHER,
        STOCK_END,
        ADDITIONAL,
        ADDITIONAL_SUB_NETHER,
        ADDITIONAL_SUB_END,
        UNKNOWN;

        public static boolean isStock(final GeneralWorld world) {
            switch (world.getType()) {
                case STOCK:
                case STOCK_NETHER:
                case STOCK_END:
                    return true;
                default:
                    return false;
            }
        }

        public static boolean isAdditional(final GeneralWorld world) {
            switch (world.getType()) {
                case ADDITIONAL:
                case ADDITIONAL_SUB_NETHER:
                case ADDITIONAL_SUB_END:
                    return true;
                default:
                    return false;
            }
        }
    }

    protected final NWorld plugin;

    protected String    worldName;
    protected NLocation spawnLocation;
    protected String    requiredPermission;
    protected boolean   enabled;
    protected boolean   hidden;
    protected WorldType type;

    public GeneralWorld(final NWorld instance, final String worldName, final NLocation spawnLocation, final String requiredPermission, final boolean enabled, final boolean hidden) {
        this.plugin = instance;
        this.worldName = worldName;
        this.spawnLocation = spawnLocation;
        this.requiredPermission = requiredPermission;
        this.enabled = enabled;
        this.hidden = hidden;
        this.type = WorldType.UNKNOWN;
        if (!this.plugin.getWorlds().containsKey(worldName)) {
            this.plugin.getWorlds().put(worldName, this);
        }
    }

    /**
     * Constructor for sub-classes, to be allowed to have the time to compute
     * the name of AdditionalSubWorlds based on parentWorld's name
     * <p/>
     * Don't forget to initialize fields after the call to super()
     */
    protected GeneralWorld(final NWorld instance) {
        this.plugin = instance;
    }

    public World create() {
        return this.create(org.bukkit.WorldType.NORMAL);
    }

    public World create(org.bukkit.WorldType type) {
        if (type == null) {
            type = org.bukkit.WorldType.NORMAL;
        }
        try {
            if (WorldUtil.isLoaded(this.worldName) != null || WorldUtil.exists(this.worldName) != null) {
                throw new IllegalStateException("World already exists");
            }
        } catch (final IOException e) {
            return null;
        }
        final WorldCreator creator = new WorldCreator(this.worldName);
        creator.seed(this.getSeed());
        creator.type(type);
        switch (this.type) {
            case ADDITIONAL:
            case STOCK:
                creator.environment(World.Environment.NORMAL);
                break;
            case ADDITIONAL_SUB_NETHER:
            case STOCK_NETHER:
                creator.environment(World.Environment.NETHER);
                break;
            case ADDITIONAL_SUB_END:
            case STOCK_END:
                creator.environment(World.Environment.THE_END);
                break;
            default:
                throw new IllegalStateException("Incorrect world type: " + this.type);
        }
        final World result = creator.createWorld();
        this.setSpawnLocation(result.getSpawnLocation());
        return result;
    }

    public boolean isLoaded() {
        return WorldUtil.isLoaded(this.worldName) != null;
    }

    public World load() {
        try {
            if (this.isLoaded()) {
                throw new IllegalStateException("World already loaded");
            } else if (WorldUtil.exists(this.worldName) == null) {
                throw new IllegalStateException("World does not exists");
            }
        } catch (final IOException e) {
            return null;
        }
        final WorldCreator creator = new WorldCreator(this.worldName);
        final World result = creator.createWorld();
        if (this.spawnLocation == null) {
            this.setSpawnLocation(result.getSpawnLocation());
        }
        this.setEnabled(true);
        return result;
    }

    public void unload() {
        if (!this.isLoaded()) {
            throw new IllegalStateException("World not loaded");
        }

        // Teleport players to another world
        final Location spawn = this.plugin.getWorlds().get(Bukkit.getWorlds().get(0).getName()).spawnLocation.toBukkitLocation();
        for (final Player p : Bukkit.getWorld(this.worldName).getPlayers()) {
            this.plugin.sendMessage(p, MessageId.world_teleportedBecauseOfWorldUnload);
            p.teleport(spawn);
        }

        // Unload the world
        Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.unloadWorld(fr.ribesg.bukkit.nworld.world.GeneralWorld.this.getWorldName(), true);
            }
        }, 1L);

        this.setEnabled(false);
    }

    public boolean exists() {
        try {
            return WorldUtil.exists(this.worldName) != null;
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return Null if the world doesn't exist / isn't loaded
     */
    public World getBukkitWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    public abstract long getSeed();

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    public NLocation getSpawnLocation() {
        return this.spawnLocation;
    }

    public void setSpawnLocation(final NLocation spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public void setSpawnLocation(final Location spawnLocation) {
        this.setSpawnLocation(new NLocation(spawnLocation));
    }

    public String getRequiredPermission() {
        return this.requiredPermission;
    }

    public void setRequiredPermission(final String requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public WorldType getType() {
        return this.type;
    }

    public void setType(final WorldType type) {
        this.type = type;
    }

    @Override
    public int compareTo(final GeneralWorld o) {
        return this.worldName.compareTo(o.worldName);
    }

    public boolean isMalformed() {
        return false;
    }
}
