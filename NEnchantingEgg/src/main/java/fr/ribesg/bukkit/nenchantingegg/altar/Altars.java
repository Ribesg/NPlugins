/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Altars.java                *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.Altars           *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Altars {

    private final NEnchantingEgg plugin;

    private final Map<String, Set<Altar>> perWorld;
    private final Map<ChunkCoord, Altar>  perChunk;

    public Altars(final NEnchantingEgg plugin) {
        this.plugin = plugin;

        this.perWorld = new HashMap<>();
        this.perChunk = new HashMap<>();
    }

    public void onEnable() {
        this.plugin.entering(this.getClass(), "onEnable");

        for (final Altar altar : this.getAltars()) {
            altar.setState(AltarState.INACTIVE);
        }

        this.plugin.exiting(this.getClass(), "onEnable");
    }

    public void onDisable() {
        this.plugin.entering(this.getClass(), "onDisable");

        for (final Altar altar : this.getAltars()) {
            altar.hardResetToInactive(false);
        }

        this.plugin.exiting(this.getClass(), "onDisable");
    }

    public Set<Altar> getAltars() {
        return new HashSet<>(this.perChunk.values());
    }

    public boolean canAdd(final Altar altar, final double minDistance) {
        this.plugin.entering(this.getClass(), "canAdd");

        boolean result = true;
        final NLocation l = altar.getCenterLocation();
        this.plugin.debug("Trying to add Altar at location " + l);

        if (this.perWorld.containsKey(l.getWorldName())) {
            final double minDistanceSquared = minDistance * minDistance;
            if (this.plugin.isDebugEnabled()) {
                this.plugin.debug("There are already altars in this world");
                this.plugin.debug("Required minimal distance: " + minDistance + " (squared: " + minDistanceSquared + ')');
            }
            final Set<Altar> set = this.perWorld.get(l.getWorldName());
            for (final Altar other : set) {
                final double distanceSquared = l.distance2DSquared(other.getCenterLocation());
                if (this.plugin.isDebugEnabled()) {
                    this.plugin.debug("Distance (squared) with " + other.getCenterLocation() + ": " + distanceSquared);
                }
                if (distanceSquared < minDistanceSquared) {
                    this.plugin.debug("Too close, can't add");
                    result = false;
                    break;
                }
            }
        }

        this.plugin.exiting(this.getClass(), "canAdd");
        return result;
    }

    public void add(final Altar altar) {
        this.plugin.entering(this.getClass(), "add");

        final World w = altar.getCenterLocation().getWorld();
        final Set<Altar> set;
        if (this.perWorld.containsKey(w.getName())) {
            set = this.perWorld.get(w.getName());
        } else {
            set = new HashSet<>();
        }
        set.add(altar);
        this.perWorld.put(w.getName(), set);
        for (final ChunkCoord c : altar.getChunks()) {
            this.perChunk.put(c, altar);
        }

        this.plugin.exiting(this.getClass(), "add");
    }

    public void remove(final Altar altar) {
        this.plugin.entering(this.getClass(), "remove");

        final World w = altar.getCenterLocation().getWorld();
        if (this.perWorld.containsKey(w.getName())) {
            final Set<Altar> set = this.perWorld.get(w.getName());
            set.remove(altar);
            if (set.isEmpty()) {
                this.perWorld.remove(w.getName());
            }
        }
        for (final ChunkCoord c : altar.getChunks()) {
            this.perChunk.remove(c);
        }

        this.plugin.exiting(this.getClass(), "remove");
    }

    public Altar get(final ChunkCoord coord) {
        return this.perChunk.get(coord);
    }

    public void time(final String worldName, final MinecraftTime currentTime) {
        this.plugin.entering(this.getClass(), "time");

        if (this.perWorld.containsKey(worldName)) {
            if (currentTime == MinecraftTime.NIGHT) {
                for (final Altar a : this.perWorld.get(worldName)) {
                    if (a.getState() == AltarState.INACTIVE) {
                        this.plugin.debug("Activating altar at location " + a.getCenterLocation());
                        this.plugin.getInactiveToActiveTransition().doTransition(a);
                    }
                }
            } else {
                for (final Altar a : this.perWorld.get(worldName)) {
                    if (a.getState() == AltarState.ACTIVE) {
                        this.plugin.debug("Hard-reseting altar at location " + a.getCenterLocation());
                        a.hardResetToInactive(false);
                    } else if (a.getState() == AltarState.LOCKED) {
                        this.plugin.debug("Unlocking altar at location " + a.getCenterLocation());
                        a.setState(AltarState.INACTIVE);
                    }
                }
            }
        }

        this.plugin.exiting(this.getClass(), "time");
    }

    public void chunkLoad(final ChunkCoord c) {
        this.plugin.entering(this.getClass(), "chunkLoad");

        if (this.perChunk.containsKey(c)) {
            this.plugin.debug("Chunk contains an Altar");
            final World world = Bukkit.getWorld(c.getWorldName());
            final Altar altar = this.perChunk.get(c);
            if (altar.getState() == AltarState.INACTIVE && MinecraftTime.isNightTime(world.getTime())) {
                this.plugin.debug("Activating altar at location " + altar.getCenterLocation());
                this.plugin.getInactiveToActiveTransition().doTransition(altar);
            } else if (altar.getState() != AltarState.INACTIVE && MinecraftTime.isNightTime(world.getTime())) {
                this.plugin.debug("Hard-reseting altar at location " + altar.getCenterLocation());
                altar.hardResetToInactive(false);
            }
        }

        this.plugin.exiting(this.getClass(), "chunkLoad");
    }
}
