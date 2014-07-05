/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - NEnchantment.java          *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.enchantment.NEnchantment
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.enchantment;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public abstract class NEnchantment implements Listener {

	protected final NEnchantingEgg plugin;

	protected final String      name;
	protected final String      code;
	protected final Enchantment requiredEnchantment;

	protected NEnchantment(final NEnchantingEgg instance, final String name, final Enchantment requiredEnchantment) {
		if (name.length() < 5) {
			throw new IllegalArgumentException("Name is too short");
		}
		this.plugin = instance;
		this.name = name;
		final StringBuilder codeBuilder = new StringBuilder();
		for (final char c : Integer.toHexString(this.name.hashCode()).substring(0, 4).toCharArray()) {
			codeBuilder.append(ChatColor.COLOR_CHAR).append(c);
		}
		this.code = codeBuilder.toString() + ChatColor.RESET + ChatColor.GRAY + ChatColor.BOLD + this.name + " I";
		this.requiredEnchantment = requiredEnchantment;
	}

	public abstract boolean canEnchant(final ItemStack is);

	public ItemStack enchant(final ItemStack is) {
		final ItemMeta meta = is.getItemMeta();
		final List<String> lore;
		if (meta.hasLore()) {
			lore = meta.getLore();
		} else {
			lore = new LinkedList<>();
		}
		lore.add(this.code);
		meta.setLore(lore);
		is.setItemMeta(meta);
		return is;
	}

	protected boolean hasEnchantment(final ItemStack is) {
		return is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(this.code);
	}
}
