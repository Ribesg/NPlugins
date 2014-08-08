/***************************************************************************
 * Project file:    NPlugins - NGeneral - ItemNetworkHandlerTask.java      *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.ItemNetworkHandlerTask
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.common.collection.pairlist.PairList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ItemNetworkHandlerTask extends BukkitRunnable {

	private static final Random RANDOM = new Random();

	private final ItemNetwork network;
	private       BukkitTask  task;

	public ItemNetworkHandlerTask(final ItemNetwork network) {
		super();
		this.network = network;
	}

	public void initialize() {
		this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.network.getFeature().getPlugin(), this, RANDOM.nextInt(5 * 20), 5L);
	}

	public void terminate() {
		if (this.task != null) {
			this.task.cancel();
		}
	}

	@Override
	public void run() {

		// If there is something to send
		if (!this.network.getBuffer().isEmpty()) {

			// This map will link origin locations to itemstack to send and their possible destinations
			final Map<NLocation, PairList<ItemStack, List<ReceiverSign>>> toBeSent = new HashMap<>(this.network.getBuffer().size());

			// This map will contain what cannot be sent over this Network
			final Map<NLocation, List<ItemStack>> notSendable = new HashMap<>();

			// It's a do-while loop as we know that there is at least one InventoryContent in the Buffer
			do {

				// Map of what will be sent and the possible destinations
				final PairList<ItemStack, List<ReceiverSign>> contentMap = new PairList<>();

				// Get the InventoryContent to sent
				final InventoryContent content = this.network.getBuffer().poll();

				// Where those items come from
				final NLocation origin = content.getOrigin();

				// The items to send
				final List<ItemStack> items = content.getItems();

				// For each of these items
				final Iterator<ItemStack> it = items.iterator();
				while (it.hasNext()) {
					final ItemStack is = it.next();
					final List<ReceiverSign> receivers = new ArrayList<>();

					// Find all possible destinations
					for (final ReceiverSign rs : this.network.getReceivers()) {
						if (rs.acceptsAll()) {
							receivers.add(rs);
						} else if (rs.accepts(is.getType()) || rs.accepts(is.getData())) {
							receivers.add(0, rs);
						}
					}

					// Remove the ItemStack from the list and add it to what needs to be sent
					if (!receivers.isEmpty()) {
						it.remove();
						contentMap.put(is, receivers);
					}
				}

				// Add to the list of items to be sent
				toBeSent.put(origin, contentMap);

				// Remaining items cannot be sent
				if (!items.isEmpty()) {
					notSendable.put(origin, items);
				}
			} while (!this.network.getBuffer().isEmpty());

			// Send everything
			Bukkit.getScheduler().callSyncMethod(this.network.getFeature().getPlugin(), new Callable() {

				@Override
				public Object call() throws Exception {
					ItemNetworkHandlerTask.this.network.send(toBeSent, notSendable);
					return null;
				}
			});
		}
	}
}
