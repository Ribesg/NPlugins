/***************************************************************************
 * Project file:    NPlugins - NGeneral - ItemNetworkFeature.java          *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.itemnetwork.ItemNetworkFeature
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.itemnetwork;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.feature.Feature;
import fr.ribesg.bukkit.ngeneral.feature.FeatureType;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetwork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;

public class ItemNetworkFeature extends Feature {

    private final Map<String, ItemNetwork> networks;
    private final Set<NLocation>           lockedChestLocations;

    public ItemNetworkFeature(final NGeneral instance) {
        super(instance, FeatureType.ITEM_NETWORK, instance.getPluginConfig().hasItemNetworkFeature());
        this.networks = new HashMap<>();
        this.lockedChestLocations = new HashSet<>();
    }

    @Override
    public void initialize() {
        final ItemNetworkListener listener = new ItemNetworkListener(this);
        final ItemNetworkCommandExecutor executor = new ItemNetworkCommandExecutor(this);

        for (final ItemNetwork in : this.networks.values()) {
            in.initialize();
        }

        Bukkit.getPluginManager().registerEvents(listener, this.getPlugin());
        this.plugin.setCommandExecutor("itemnetwork", executor);
    }

    public void lock(final NLocation loc) {
        this.lockedChestLocations.add(loc);
        final World w = loc.getWorld();

        final Material chest = w.getBlockAt(loc.toBukkitLocation()).getType();

        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();

        if (w.getBlockAt(x - 1, y, z).getType() == chest) {
            this.lockedChestLocations.add(new NLocation(w.getName(), x - 1, y, z));
            return;
        }

        if (w.getBlockAt(x + 1, y, z).getType() == chest) {
            this.lockedChestLocations.add(new NLocation(w.getName(), x + 1, y, z));
            return;
        }

        if (w.getBlockAt(x, y, z - 1).getType() == chest) {
            this.lockedChestLocations.add(new NLocation(w.getName(), x, y, z - 1));
            return;
        }

        if (w.getBlockAt(x, y, z + 1).getType() == chest) {
            this.lockedChestLocations.add(new NLocation(w.getName(), x, y, z + 1));
        }
    }

    public void unlock(final NLocation loc) {
        if (this.lockedChestLocations.remove(loc)) {

            final World w = loc.getWorld();

            w.playEffect(loc.toBukkitLocation().add(0.5, 0.5, 0.5), Effect.ENDER_SIGNAL, 0);

            final Material chest = w.getBlockAt(loc.toBukkitLocation()).getType();

            final int x = loc.getBlockX();
            final int y = loc.getBlockY();
            final int z = loc.getBlockZ();

            if (w.getBlockAt(x - 1, y, z).getType() == chest) {
                this.lockedChestLocations.remove(new NLocation(w.getName(), x - 1, y, z));
                w.playEffect(w.getBlockAt(x - 1, y, z).getLocation().add(0.5, 0.5, 0.5), Effect.ENDER_SIGNAL, 0);
                return;
            }

            if (w.getBlockAt(x + 1, y, z).getType() == chest) {
                this.lockedChestLocations.remove(new NLocation(w.getName(), x + 1, y, z));
                w.playEffect(w.getBlockAt(x + 1, y, z).getLocation().add(0.5, 0.5, 0.5), Effect.ENDER_SIGNAL, 0);
                return;
            }

            if (w.getBlockAt(x, y, z - 1).getType() == chest) {
                this.lockedChestLocations.remove(new NLocation(w.getName(), x, y, z - 1));
                w.playEffect(w.getBlockAt(x, y, z - 1).getLocation().add(0.5, 0.5, 0.5), Effect.ENDER_SIGNAL, 0);
                return;
            }

            if (w.getBlockAt(x, y, z + 1).getType() == chest) {
                this.lockedChestLocations.remove(new NLocation(w.getName(), x, y, z + 1));
                w.playEffect(w.getBlockAt(x, y, z + 1).getLocation().add(0.5, 0.5, 0.5), Effect.ENDER_SIGNAL, 0);
            }
        }
    }

    public boolean isLocked(final NLocation loc) {
        return this.lockedChestLocations.contains(loc);
    }

    public Map<String, ItemNetwork> getNetworks() {
        return this.networks;
    }
}
