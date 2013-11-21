package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class InventoryUtils {

	public static List<ItemStack> sortAndStack(final Collection<ItemStack> items) {
		final Inventory tmpInventory = Bukkit.createInventory(null, 6);
		final Map<Integer, ItemStack> remaining = tmpInventory.addItem(items.toArray(new ItemStack[items.size()]));
		if (!remaining.isEmpty()) {
			throw new IllegalArgumentException("Too much ItemStacks");
		} else {
			return Arrays.asList(sort(tmpInventory.getContents()));
		}
	}

	public static ItemStack[] sort(ItemStack[] items) {
		Arrays.sort(items, new Comparator<ItemStack>() {

			@Override
			public int compare(ItemStack o1, ItemStack o2) {
				if (o1 == null) {
					if (o2 == null) {
						return 0;
					} else {
						return 1;
					}
				} else {
					if (o2 == null) {
						return -1;
					} else {
						int r = Integer.compare(o1.getTypeId(), o2.getTypeId());
						if (r != 0) {
							r = Short.compare(o1.getDurability(), o2.getDurability());
						}
						return r;
					}
				}
			}
		});
		return items;
	}
}
