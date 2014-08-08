/***************************************************************************
 * Project file:    NPlugins - NGeneral - ItemNetwork.java                 *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetwork
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.common.collection.pairlist.Pair;
import fr.ribesg.bukkit.ncore.common.collection.pairlist.PairList;
import fr.ribesg.bukkit.ngeneral.feature.itemnetwork.ItemNetworkFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class ItemNetwork {

    private final ItemNetworkFeature      feature;
    private final String                  name;
    private final Set<ReceiverSign>       receivers;
    private final Queue<InventoryContent> buffer;
    private final String                  creator;

    private final ItemNetworkHandlerTask task;

    public ItemNetwork(final ItemNetworkFeature feature, final String networkName, final String creatorName) {
        this.feature = feature;
        this.name = networkName;
        this.receivers = Collections.newSetFromMap(new ConcurrentHashMap<ReceiverSign, Boolean>());
        this.buffer = new ConcurrentLinkedQueue<>();
        this.task = new ItemNetworkHandlerTask(this);
        this.creator = creatorName;
    }

    public void initialize() {
        this.task.initialize();
    }

    public void terminate() {
        this.task.terminate();
    }

    /**
     * Items passed to this method will be added to a concurrent buffer.
     * An asynchronous task will then handle them ASAP and call the
     * {@link #send(Map, Map)} method with every needed
     * informations.
     * <p/>
     * Note: Items passed to this method should no longer be in the origin
     * Inventory.
     *
     * @param items the items to send over the network
     */
    public void queue(final InventoryContent items) {
        this.buffer.add(items);
    }

    /**
     * This method will be called synchronously once the asynchronous task
     * have determined where which ItemStack should go.
     * It will try to send all the ItemStack to their destination, then it
     * will replace all non-sent items in the original chest.
     *
     * @param toBeSent the map of Items
     */
    /* package */ void send(final Map<NLocation, PairList<ItemStack, List<ReceiverSign>>> toBeSent, final Map<NLocation, List<ItemStack>> notSendable) {
        for (final Entry<NLocation, PairList<ItemStack, List<ReceiverSign>>> nLocationPairListEntry : toBeSent.entrySet()) {
            final PairList<ItemStack, List<ReceiverSign>> items = nLocationPairListEntry.getValue();
            for (final Pair<ItemStack, List<ReceiverSign>> p : items) {
                boolean sent = false;
                ItemStack toSend = p.getKey();
                for (final ReceiverSign rs : p.getValue()) {
                    if ((toSend = rs.send(toSend)) == null) {
                        sent = true;
                        break;
                    }
                }
                if (!sent) {
                    List<ItemStack> notSent = notSendable.get(nLocationPairListEntry.getKey());
                    if (notSent == null) {
                        notSent = new ArrayList<>();
                        notSendable.put(nLocationPairListEntry.getKey(), notSent);
                    }
                    notSent.add(p.getKey());
                }
            }
        }

        for (final Entry<NLocation, List<ItemStack>> nLocationListEntry : notSendable.entrySet()) {
            final List<ItemStack> notSent = nLocationListEntry.getValue();
            final Block block = nLocationListEntry.getKey().toBukkitLocation().getBlock();
            if (block.getType() == Material.CHEST) {
                final Chest chest = (Chest)block.getState();
                final Map<Integer, ItemStack> remaining = chest.getInventory().addItem(notSent.toArray(new ItemStack[notSent.size()]));
                if (!remaining.isEmpty()) {
                    final Location loc = block.getLocation().add(0.5, 0.5, 0.5);
                    for (final ItemStack is : remaining.values()) {
                        loc.getWorld().dropItem(loc, is);
                    }
                }
            } else {
                final Location loc = block.getLocation().add(0.5, 0.5, 0.5);
                for (final ItemStack is : notSent) {
                    loc.getWorld().dropItem(loc, is);
                }
            }
        }

        for (final NLocation origin : toBeSent.keySet()) {
            this.feature.unlock(origin);
        }
        for (final NLocation origin : notSendable.keySet()) {
            this.feature.unlock(origin);
        }
    }

    /**
     * Checks if the privided location is too far from other signs to be a
     * valid Sign location.
     *
     * @param loc the location to check
     *
     * @return true if the location is too far, false otherwise
     */
    public boolean isTooFar(final Location loc) {
        final int maxDistance = this.feature.getPlugin().getPluginConfig().getItemNetworkMaxDistance();
        final int squaredMaxDistance = maxDistance * maxDistance;
        final NLocation nLoc = new NLocation(loc);
        try {
            for (final ReceiverSign r : this.receivers) {
                if (r.getLocation().distance2DSquared(nLoc) > squaredMaxDistance) {
                    return true;
                }
            }
        } catch (final IllegalArgumentException e) {
            return true;
        }
        return false;
    }

    public ItemNetworkFeature getFeature() {
        return this.feature;
    }

    public String getName() {
        return this.name;
    }

    public Set<ReceiverSign> getReceivers() {
        return this.receivers;
    }

    public String getCreator() {
        return this.creator;
    }

    /* package */ Queue<InventoryContent> getBuffer() {
        return this.buffer;
    }
}
