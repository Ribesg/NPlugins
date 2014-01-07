/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - ItemBuilder.java           *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.item.ItemBuilder       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.item;

import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/** Based on a main item and some ingredients, builds a new boosted item. */
public class ItemBuilder {

	private static final Random        rand              = new Random();
	private static       Set<Material> possibleMainItems = null;

	private static final double[][] boostValues = new double[][] {
			{
					1.3d,
					-0.05d
			},
			{
					1.1d,
					-0.15d
			},
			{
					1.2d,
					-0.37d
			},
			{
					1.4d,
					-0.68d
			},
			{
					1.7d,
					-1.09d
			},
			{
					1.9d,
					-1.42d
			}
	};
	private static final double     enchReduce  = 0.1f;

	/** List of items that can be boosted */
	private static Set<Material> getPossibleMainItems() {
		if (possibleMainItems == null) {
			possibleMainItems = new HashSet<>();
			possibleMainItems.add(Material.IRON_SPADE);
			possibleMainItems.add(Material.IRON_PICKAXE);
			possibleMainItems.add(Material.IRON_AXE);
			possibleMainItems.add(Material.BOW);
			possibleMainItems.add(Material.IRON_SWORD);
			possibleMainItems.add(Material.WOOD_SWORD);
			possibleMainItems.add(Material.WOOD_SPADE);
			possibleMainItems.add(Material.WOOD_PICKAXE);
			possibleMainItems.add(Material.WOOD_AXE);
			possibleMainItems.add(Material.STONE_SWORD);
			possibleMainItems.add(Material.STONE_SPADE);
			possibleMainItems.add(Material.STONE_PICKAXE);
			possibleMainItems.add(Material.STONE_AXE);
			possibleMainItems.add(Material.DIAMOND_SWORD);
			possibleMainItems.add(Material.DIAMOND_SPADE);
			possibleMainItems.add(Material.DIAMOND_PICKAXE);
			possibleMainItems.add(Material.DIAMOND_AXE);
			possibleMainItems.add(Material.GOLD_SWORD);
			possibleMainItems.add(Material.GOLD_SPADE);
			possibleMainItems.add(Material.GOLD_PICKAXE);
			possibleMainItems.add(Material.GOLD_AXE);
			possibleMainItems.add(Material.WOOD_HOE);
			possibleMainItems.add(Material.STONE_HOE);
			possibleMainItems.add(Material.IRON_HOE);
			possibleMainItems.add(Material.DIAMOND_HOE);
			possibleMainItems.add(Material.GOLD_HOE);
			possibleMainItems.add(Material.LEATHER_HELMET);
			possibleMainItems.add(Material.LEATHER_CHESTPLATE);
			possibleMainItems.add(Material.LEATHER_LEGGINGS);
			possibleMainItems.add(Material.LEATHER_BOOTS);
			possibleMainItems.add(Material.CHAINMAIL_HELMET);
			possibleMainItems.add(Material.CHAINMAIL_CHESTPLATE);
			possibleMainItems.add(Material.CHAINMAIL_LEGGINGS);
			possibleMainItems.add(Material.CHAINMAIL_BOOTS);
			possibleMainItems.add(Material.IRON_HELMET);
			possibleMainItems.add(Material.IRON_CHESTPLATE);
			possibleMainItems.add(Material.IRON_LEGGINGS);
			possibleMainItems.add(Material.IRON_BOOTS);
			possibleMainItems.add(Material.DIAMOND_HELMET);
			possibleMainItems.add(Material.DIAMOND_CHESTPLATE);
			possibleMainItems.add(Material.DIAMOND_LEGGINGS);
			possibleMainItems.add(Material.DIAMOND_BOOTS);
			possibleMainItems.add(Material.GOLD_HELMET);
			possibleMainItems.add(Material.GOLD_CHESTPLATE);
			possibleMainItems.add(Material.GOLD_LEGGINGS);
			possibleMainItems.add(Material.GOLD_BOOTS);
		}
		return possibleMainItems;
	}

	/** The amount of base material needed based on the type of item */
	private int getBaseRessourceAmount(final Material material) {
		switch (material) {
			case LEATHER_CHESTPLATE:
			case CHAINMAIL_CHESTPLATE:
			case IRON_CHESTPLATE:
			case DIAMOND_CHESTPLATE:
			case GOLD_CHESTPLATE:
				return 8;
			case LEATHER_LEGGINGS:
			case CHAINMAIL_LEGGINGS:
			case IRON_LEGGINGS:
			case DIAMOND_LEGGINGS:
			case GOLD_LEGGINGS:
				return 7;
			case LEATHER_HELMET:
			case CHAINMAIL_HELMET:
			case IRON_HELMET:
			case DIAMOND_HELMET:
			case GOLD_HELMET:
				return 5;
			case LEATHER_BOOTS:
			case CHAINMAIL_BOOTS:
			case IRON_BOOTS:
			case DIAMOND_BOOTS:
			case GOLD_BOOTS:
				return 4;
			case IRON_PICKAXE:
			case IRON_AXE:
			case WOOD_PICKAXE:
			case WOOD_AXE:
			case STONE_PICKAXE:
			case STONE_AXE:
			case DIAMOND_PICKAXE:
			case DIAMOND_AXE:
			case GOLD_PICKAXE:
			case GOLD_AXE:
				return 3;
			case IRON_SWORD:
			case WOOD_SWORD:
			case STONE_SWORD:
			case DIAMOND_SWORD:
			case GOLD_SWORD:
			case WOOD_HOE:
			case STONE_HOE:
			case IRON_HOE:
			case DIAMOND_HOE:
			case GOLD_HOE:
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

	/** Handles the reception of an item */
	public void addItem(final ItemStack is) {
		if (getPossibleMainItems().contains(is.getType()) && is.getEnchantments().size() != 0) {
			mainItem = is;
			if (altar.getEggLocation().getBlock().getType() != Material.DRAGON_EGG) {
				items.clear();
				altar.hardResetToInactive(true);
			} else {
				plugin.getEggProvidedToItemProvidedTransition().doTransition(altar);
			}
		} else {
			items.add(is);
		}
	}

	public void computeItem() {
		if (!items.isEmpty() && mainItem != null) {
			// Step 1: repair
			repair();

			// Step 2: boost
			boost();

			// TODO: Other steps

			// Output the item
			altar.buildItem(mainItem, items);
		} else {
			altar.buildItem(mainItem, items);
			plugin.getItemProvidedToLockedTransition().doTransition(altar);
		}
	}

	private void repair() {
		final Material mat = mainItem.getType();
		final short maxDurability = mat.getMaxDurability();

		// Get the total durability points sacrificed, in %
		double repairCount = 0;
		final Iterator<ItemStack> it = items.iterator();
		ItemStack is;
		while (it.hasNext()) {
			is = it.next();
			if (is.getType() == mat) {
				repairCount += is.getAmount() * ((maxDurability - is.getDurability()) / (double) maxDurability);
				it.remove();
			}
		}

		repairCount *= getBaseRessourceAmount(mat);

		// Get the number of enchantment levels
		int totalEnchantmentLevel = 0;
		for (final Integer i : mainItem.getEnchantments().values()) {
			totalEnchantmentLevel += i;
		}

		// Compute base durability boost
		final double configurableCoef = plugin.getPluginConfig().getRepairBoostMultiplier();
		double boost = configurableCoef * repairCount / totalEnchantmentLevel;

		// Add some randomness: boost = 80%*boost + [0-40%]*boost; => boost = [80-120%]*boost;
		boost = boost - 0.2 * boost + rand.nextFloat() * 0.4 * boost;

		// Apply durability
		double finalDurability = mainItem.getDurability() - boost * maxDurability;
		if (finalDurability < 0) {
			finalDurability = 0;
		}

		mainItem.setDurability((short) finalDurability);
	}

	private void boost() {
		// Count the amount of Magma Cream and Eye of Ender sacrificed
		int magmaCream = 0;
		int eyeOfEnder = 0;
		final Iterator<ItemStack> it = items.iterator();
		ItemStack is;
		while (it.hasNext()) {
			is = it.next();
			if (is.getType() == Material.MAGMA_CREAM) {
				magmaCream += is.getAmount();
				it.remove();
			} else if (is.getType() == Material.EYE_OF_ENDER) {
				eyeOfEnder += is.getAmount();
				it.remove();
			}
		}

		// Reduce amounts to max allowed quantity
		if (magmaCream > 64) {
			magmaCream = 64;
		}
		if (eyeOfEnder > 64) {
			eyeOfEnder = 64;
		}

		// We do nothing if there's none
		if (magmaCream != 0 || eyeOfEnder != 0) {
			// Get the amount of enchantment levels
			double enchantments = -1f;
			for (final Integer i : mainItem.getEnchantments().values()) {
				enchantments += 0.75f + 0.25f * i;
			}

			// Get the total amount of ingredients
			final int total = magmaCream + eyeOfEnder;

			// Get a ratio between 0 and 1 of the total of ingredients
			double ratio = magmaCream == 0 ? 0 : eyeOfEnder / magmaCream;
			if (ratio > 1) {
				ratio = 1 / ratio;
			}

			// Weight the total with the ratio
			final double weightedTotal = total / 2 + ratio * (total / 2);

			// Get a coef between 0 and 1 from the weightedTotal (Math.exp(something between -1 and 0))
			final double coef = Math.exp(-(1f - (weightedTotal / 128f)));

			// Compute probabilities
			final double[] probabilities = new double[] {
					coef * boostValues[0][0] + boostValues[0][1] - enchReduce * enchantments,
					coef * boostValues[1][0] + boostValues[1][1] - enchReduce * enchantments,
					coef * boostValues[2][0] + boostValues[2][1] - enchReduce * enchantments,
					coef * boostValues[3][0] + boostValues[3][1] - enchReduce * enchantments,
					coef * boostValues[4][0] + boostValues[4][1] - enchReduce * enchantments,
					coef * boostValues[5][0] + boostValues[5][1] - enchReduce * enchantments
			};

			// Apply configurable coef and fix out-of-scope values
			final double configurableCoef = plugin.getPluginConfig().getEnchantmentBoostMultiplier();
			for (int i = 0; i < probabilities.length; i++) {
				probabilities[i] *= configurableCoef;
				if (probabilities[i] > 1) {
					probabilities[i] = 1;
				} else if (probabilities[i] < 0) {
					probabilities[i] = 0;
				}
			}

			// Roll dice
			final Map<Enchantment, Integer> newEnchantmentsMap = new HashMap<>();
			for (final Map.Entry<Enchantment, Integer> e : mainItem.getEnchantments().entrySet()) {
				int result = 0;
				for (int i = 6; i > 0; i--) {
					if (rand.nextFloat() <= probabilities[i - 1]) {
						result = i;
						break;
					}
				}
				newEnchantmentsMap.put(e.getKey(), Math.min(10, e.getValue() + result));
			}

			// Clear enchantments
			for (final Enchantment e : Enchantment.values()) {
				mainItem.removeEnchantment(e);
			}

			// Apply enchantments
			mainItem.addUnsafeEnchantments(newEnchantmentsMap);
		}
	}
}
