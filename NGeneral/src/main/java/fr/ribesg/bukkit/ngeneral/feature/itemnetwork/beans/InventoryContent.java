package fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans;
import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryContent {

	private final NLocation       origin;
	private final List<ItemStack> items;

	public InventoryContent(final NLocation origin, final List<ItemStack> items) {
		this.origin = origin;
		this.items = items;
	}

	public NLocation getOrigin() {
		return origin;
	}

	public List<ItemStack> getItems() {
		return items;
	}
}
