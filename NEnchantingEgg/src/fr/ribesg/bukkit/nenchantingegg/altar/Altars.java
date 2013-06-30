package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.ncore.utils.Time;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Altars {

    private final NEnchantingEgg plugin;

    private final Map<String, Set<Altar>> perWorld;
    private final Map<ChunkCoord, Altar>  perChunk;

    public Altars(NEnchantingEgg plugin) {
        this.plugin = plugin;

        perWorld = new HashMap<>();
        perChunk = new HashMap<>();
    }

    public boolean canAdd(final Altar altar, final double minDistance) {
        final Location l = altar.getCenterLocation();
        final World w = l.getWorld();
        if (perWorld.containsKey(w.getName())) {
            final double minDistanceSquared = minDistance * minDistance;
            final Set<Altar> set = perWorld.get(w);
            for (final Altar other : set) {
                if (l.distanceSquared(other.getCenterLocation()) < minDistanceSquared) {
                    return false;
                }
            }
        }
        return true;
    }

    public void add(final Altar altar) {
        final World w = altar.getCenterLocation().getWorld();
        Set<Altar> set;
        if (perWorld.containsKey(w.getName())) {
            set = perWorld.get(w.getName());
        } else {
            set = new HashSet<>();
        }
        set.add(altar);
        perWorld.put(w.getName(), set);
        for (final ChunkCoord c : altar.getChunks()) {
            perChunk.put(c, altar);
        }
    }

    public void remove(final Altar altar) {
        final World w = altar.getCenterLocation().getWorld();
        if (perWorld.containsKey(w.getName())) {
            final Set<Altar> set = perWorld.get(w.getName());
            set.remove(altar);
            if (set.size() == 0) {
                perWorld.remove(w.getName());
            }
        }
        for (final ChunkCoord c : altar.getChunks()) {
            perChunk.remove(c);
        }
    }

    public Altar get(final ChunkCoord coord) {
        return perChunk.get(coord);
    }

    public void timeChange(final String worldName, final Time fromTime, final Time toTime) {
        System.out.println("Time change");
        if (perWorld.containsKey(worldName)) {
            System.out.println("\tWorld contains Altars");
            int i = 0;
            if (fromTime == Time.DAY && toTime == Time.NIGHT) {
                for (final Altar a : perWorld.get(worldName)) {
                    System.out.println("\t\tAltar " + ++i);
                    if (a.getState() == AltarState.INACTIVE) {
                        System.out.println("\t\t\tTransition");
                        plugin.getInactiveToActiveTransition().doTransition(a);
                    }
                }
            } else if (fromTime == Time.NIGHT && toTime == Time.DAY) {
                for (final Altar a : perWorld.get(worldName)) {
                    System.out.println("\t\tAltar " + ++i);
                    if (a.getState() == AltarState.ACTIVE) {
                        System.out.println("\t\t\tTransition");
                        plugin.getItemProvidedToLockedTransition().doTransition(a, true);
                    } else if (a.getState() == AltarState.LOCKED) {
                        a.setState(AltarState.INACTIVE);
                    }
                }
            }
        }
    }
}
