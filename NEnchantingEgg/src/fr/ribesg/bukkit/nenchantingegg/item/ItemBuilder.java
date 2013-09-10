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

	private static Random        rand              = new Random();
	private static Set<Material> possibleMainItems = null;

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

	private int getBaseRessourceAmount(Material material) {
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
		final Material mat = mainItem.getType();
		final short maxDurability = mat.getMaxDurability();

		if (ITEMBUILDER_DEBUG) {
			System.out.println("MaxDurability=" + maxDurability);
		}

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

		if (ITEMBUILDER_DEBUG) {
			System.out.println("RepairCount=" + repairCount);
		}

		if (ITEMBUILDER_DEBUG) {
			System.out.println("Applying item cost boost...");
		}

		repairCount *= getBaseRessourceAmount(mat);

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
