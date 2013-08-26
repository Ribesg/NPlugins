package fr.ribesg.bukkit.nenchantingegg.item;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ItemBuilder {

	private static final boolean ITEMBUILDER_DEBUG = false;

	private static Random       rand              = new Random();
	private static Set<Integer> possibleMainItems = null;

	private static Set<Integer> getPossibleMainItems() {
		if (possibleMainItems == null) {
			possibleMainItems = new HashSet<>();
			for (int i = 256; i <= 258; i++) {
				possibleMainItems.add(i);
			}
			possibleMainItems.add(261);
			for (int i = 267; i <= 279; i++) {
				possibleMainItems.add(i);
			}
			for (int i = 283; i <= 286; i++) {
				possibleMainItems.add(i);
			}
			for (int i = 290; i <= 294; i++) {
				possibleMainItems.add(i);
			}
			for (int i = 298; i <= 317; i++) {
				possibleMainItems.add(i);
			}
		}
		return possibleMainItems;
	}

	private int getBaseRessourceAmount(int id) {
		switch (id) {
			case 299:
			case 303:
			case 307:
			case 311:
			case 315:
				return 8;
			case 300:
			case 304:
			case 308:
			case 312:
			case 316:
				return 7;
			case 298:
			case 302:
			case 306:
			case 310:
			case 314:
				return 5;
			case 301:
			case 305:
			case 309:
			case 313:
			case 317:
				return 4;
			case 257:
			case 258:
			case 270:
			case 271:
			case 274:
			case 275:
			case 278:
			case 279:
			case 285:
			case 286:
				return 3;
			case 267:
			case 268:
			case 272:
			case 276:
			case 283:
			case 290:
			case 291:
			case 292:
			case 293:
			case 294:
				return 2;
			default:
				return 1;
		}
	}

	private final NEnchantingEgg plugin;

	private       ItemStack       mainItem;
	private final List<ItemStack> items;
	private final Altar           altar;

	public ItemBuilder(final Altar altar) {
		this.altar = altar;
		this.plugin = altar.getPlugin();
		items = new ArrayList<>();
	}

	public void addItem(final ItemStack is) {
		if (getPossibleMainItems().contains(is.getTypeId()) && is.getEnchantments().size() != 0) {
			mainItem = is;
			plugin.getEggProvidedToItemProvidedTransition().doTransition(altar);
		} else {
			items.add(is);
		}
	}

	public void computeItem() {
		if (items != null && mainItem != null) {
			// Step 1: repair
			repair();

			// TODO: Other steps

			// Output the item
			altar.buildItem(mainItem);
		} else {
			plugin.getItemProvidedToLockedTransition().doTransition(altar);
		}
	}

	private void repair() {
		final int id = mainItem.getTypeId();
		final short maxDurability = Material.getMaterial(id).getMaxDurability();

		if (ITEMBUILDER_DEBUG) {
			System.out.println("MaxDurability=" + maxDurability);
		}

		// Get the total durability points sacrificed, in %
		double repairCount = 0;
		final Iterator<ItemStack> it = items.iterator();
		ItemStack is;
		while (it.hasNext()) {
			is = it.next();
			if (is.getTypeId() == id) {
				repairCount += is.getAmount() * ((maxDurability - is.getDurability()) / (double) maxDurability);
				it.remove();
			}
		}

		if (ITEMBUILDER_DEBUG) {
			System.out.println("RepairCount=" + repairCount);
		}

		if (ITEMBUILDER_DEBUG) {
			System.out.println("Applying item cost boost...");
		}

		repairCount *= getBaseRessourceAmount(id);

		if (ITEMBUILDER_DEBUG) {
			System.out.println("RepairCount=" + repairCount);
		}

		// Get the number of enchantment levels
		int totalEnchantmentLevel = 0;
		for (final Integer i : mainItem.getEnchantments().values()) {
			totalEnchantmentLevel += i;
		}

		if (ITEMBUILDER_DEBUG) {
			System.out.println("TotalEnchantmentLevel=" + totalEnchantmentLevel);
		}

		// Compute base durability boost
		double coef = plugin.getPluginConfig().getRepairBoostMultiplier();
		double boost = coef * repairCount / totalEnchantmentLevel;

		if (ITEMBUILDER_DEBUG) {
			System.out.println("Boost=" + boost);
		}

		// Add some randomness: boost = 80%*boost + [0-40%]*boost; => boost = [80-120%]*boost;
		boost = boost - 0.2 * boost + rand.nextFloat() * 0.4 * boost;

		if (ITEMBUILDER_DEBUG) {
			System.out.println("Boost=" + boost + " (Random)");
		}

		// Apply durability
		double finalDurability = mainItem.getDurability() - boost * maxDurability;
		if (finalDurability < 0) {
			finalDurability = 0;
		}

		if (ITEMBUILDER_DEBUG) {
			System.out.println("FinalDurability=" + finalDurability);
		}

		mainItem.setDurability((short) finalDurability);
	}
}
