package fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.utils.SignUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ReceiverSign {

	private static final Pattern ACCEPTED_MATERIALS_REGEX = Pattern.compile("^\\*|\\d+(:\\d+)?(;\\d+(:\\d+)?)*$");

	private static Set<Material> chestMaterials;

	private static Set<Material> getChestMaterials() {
		if (chestMaterials == null) {
			chestMaterials = new HashSet<>();
			chestMaterials.add(Material.CHEST);
			chestMaterials.add(Material.TRAPPED_CHEST);
		}
		return chestMaterials;
	}

	private final NLocation         location;
	private final Set<Material>     acceptedMaterials;
	private final Set<MaterialData> acceptedMaterialDatas;
	private final boolean           acceptsAll;

	private List<Block> chests;
	private long        lastUpdateDate;

	/**
	 * Builds a new ReceiverSign that accepts only some types of Materials.
	 *
	 * @param location      the Location of the ReceiverSign
	 * @param acceptsString the Materials that this ReceiverSign accepts
	 */
	public ReceiverSign(final NLocation location, final String acceptsString) {
		this.location = location;

		if (!ACCEPTED_MATERIALS_REGEX.matcher(acceptsString).matches()) {
			throw new IllegalArgumentException();
		}
		if ("*".equals(acceptsString)) {
			this.acceptsAll = true;
			this.acceptedMaterials = null;
			this.acceptedMaterialDatas = null;
		} else {
			this.acceptsAll = false;
			this.acceptedMaterials = new HashSet<>();
			this.acceptedMaterialDatas = new HashSet<>();
			for (final String s : acceptsString.split(";")) {
				if (s.contains(":")) {
					acceptedMaterialDatas.add(new MaterialData(Integer.parseInt(s.split(":")[0]),
					                                           (byte) Integer.parseInt(s.split(":")[1])));
				} else {
					acceptedMaterials.add(Material.getMaterial(Integer.parseInt(s)));
				}
			}
		}

		this.chests = SignUtils.getBlocksForLocation(this.location.toBukkitLocation(), getChestMaterials());
		this.lastUpdateDate = System.currentTimeMillis();
	}

	public boolean accepts(final MaterialData matData) {
		return this.acceptsAll ||
		       this.acceptedMaterials != null && this.acceptedMaterials.contains(matData.getItemType()) ||
		       this.acceptedMaterialDatas != null && this.acceptedMaterialDatas.contains(matData);
	}

	public boolean accepts(final Material mat) {
		return this.acceptsAll || this.acceptedMaterials != null && this.acceptedMaterials.contains(mat);
	}

	public boolean acceptsAll() {
		return this.acceptsAll;
	}

	public boolean send(final ItemStack is) {
		if ((System.currentTimeMillis() - lastUpdateDate) > 1000L) {
			this.chests = SignUtils.getBlocksForLocation(this.location.toBukkitLocation(), getChestMaterials());
		}

		for (final Block b : this.chests) {
			final InventoryHolder holder = (InventoryHolder) b.getState();
			if (holder.getInventory().addItem(is).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	public List<ItemStack> send(final List<ItemStack> items) {
		if ((System.currentTimeMillis() - lastUpdateDate) > 1000L) {
			this.chests = SignUtils.getBlocksForLocation(this.location.toBukkitLocation(), getChestMaterials());
		}

		List<ItemStack> result = new ArrayList<>(items);

		for (final Block b : this.chests) {
			final InventoryHolder holder = (InventoryHolder) b.getState();
			result = new ArrayList<>(holder.getInventory().addItem(result.toArray(new ItemStack[result.size()])).values());
			if (result.isEmpty()) {
				break;
			}
		}

		return result;
	}

	public NLocation getLocation() {
		return this.location;
	}

	public String getAcceptsString() {
		if (acceptsAll) {
			return "*";
		} else {
			final StringBuilder result = new StringBuilder();
			if (acceptedMaterials != null) {
				for (Material m : acceptedMaterials) {
					result.append(m.getId()).append(';');
				}
			}
			if (acceptedMaterialDatas != null) {
				for (MaterialData md : acceptedMaterialDatas) {
					result.append(md.getItemTypeId()).append(':').append(md.getData()).append(';');
				}
			}
			final String finalResult = result.toString();
			return finalResult.substring(0, finalResult.length() - 1);
		}
	}
}
