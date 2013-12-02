/***************************************************************************
 * Project file:    NPlugins - NGeneral - InventoryContent.java            *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.itemnetwork.beans.InventoryContent
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
