package fr.ribesg.bukkit.nenchantingegg.api;


import fr.ribesg.bukkit.ncore.nodes.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;

public class NEnchantingEggAPI extends EnchantingEggNode {

	private final NEnchantingEgg	plugin;

	public NEnchantingEggAPI(final NEnchantingEgg instance) {
		plugin = instance;
	}
}
