/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Arboricide.java            *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.enchantment.Arboricide *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.enchantment;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Arboricide extends NEnchantment {

	public Arboricide(final NEnchantingEgg instance) {
		super(instance, "Arboricide", Enchantment.DIG_SPEED);
	}

	@Override
	public boolean canEnchant(final ItemStack is) {
		final Material type = is.getType();
		return !this.hasEnchantment(is) && is.getEnchantmentLevel(this.requiredEnchantment) == 10 &&
		       (type == Material.DIAMOND_AXE || type == Material.GOLD_AXE || type == Material.IRON_AXE ||
		        type == Material.STONE_AXE || type == Material.WOOD_AXE);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void handleEvent(final BlockBreakEvent event) {
		if (this.hasEnchantment(event.getPlayer().getItemInHand())) {
			final Block block = event.getBlock();
			final int x = block.getX(), y = block.getY(), z = block.getZ();
			final World world = block.getWorld();
			final Material type = block.getType();
			if (type == Material.LOG || type == Material.LOG_2) {
				this.plugin.debug("Destroying " + NLocation.toString(block.getLocation()));
				if (world.getBlockAt(x - 1, y, z).getType() != type &&
				    world.getBlockAt(x + 1, y, z).getType() != type &&
				    world.getBlockAt(x, y, z - 1).getType() != type &&
				    world.getBlockAt(x, y, z + 1).getType() != type) {
					final List<Set<NLocation>> toBeDestroyed = new LinkedList<>();
					final Set<NLocation> allBlocksSet = new HashSet<>();
					Set<NLocation> previousSet = new HashSet<>();
					previousSet.add(new NLocation(world.getName(), x, y, z));

					boolean found = true;
					while (found) {
						found = false;
						final Set<NLocation> nextSet = new HashSet<>();
						for (final NLocation l : previousSet) {
							for (int by = l.getBlockY(); by <= l.getBlockY() + 1; by++) {
								for (int bx = l.getBlockX() - 1; bx <= l.getBlockX() + 1; bx++) {
									for (int bz = l.getBlockZ() - 1; bz <= l.getBlockZ() + 1; bz++) {
										final NLocation newLoc = new NLocation(world.getName(), bx, by, bz);
										if (!allBlocksSet.contains(newLoc) && world.getBlockAt(bx, by, bz).getType() == type) {
											nextSet.add(newLoc);
											allBlocksSet.add(newLoc);
											found = true;
										}
									}
								}
							}
						}
						if (found) {
							toBeDestroyed.add(nextSet);
							previousSet = nextSet;
						}
					}
					if (!toBeDestroyed.isEmpty()) {
						this.launchDestroyTask(toBeDestroyed);
					}
				}
			}
		}
	}

	private void launchDestroyTask(final List<Set<NLocation>> toBeDestroyed) {
		new BukkitRunnable() {

			final Random random = new Random();
			final World world = toBeDestroyed.get(0).iterator().next().getWorld();
			int i;

			@Override
			public void run() {
				if (this.i < toBeDestroyed.size()) {
					final Set<NLocation> locs = toBeDestroyed.get(this.i);
					final Iterator<NLocation> it = locs.iterator();
					NLocation loc;
					while (it.hasNext()) {
						loc = it.next();
						if (this.random.nextFloat() > 0.75) {
							this.world.playSound(loc.toBukkitLocation(), Sound.DIG_WOOD, 1f, 1f);
							this.world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).breakNaturally();
							it.remove();
						}
					}
					if (locs.isEmpty()) {
						this.i++;
					}
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(this.plugin, 1, 1);
	}
}
