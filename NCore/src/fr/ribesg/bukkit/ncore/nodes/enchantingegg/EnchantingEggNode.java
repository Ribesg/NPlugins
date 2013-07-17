package fr.ribesg.bukkit.ncore.nodes.enchantingegg;

import fr.ribesg.bukkit.ncore.nodes.NPlugin;

/**
 * Represents the NEnchantingEgg plugin
 *
 * @author Ribesg
 */
public abstract class EnchantingEggNode extends NPlugin {

    /** @see fr.ribesg.bukkit.ncore.nodes.NPlugin#linkCore() */
    @Override
    protected void linkCore() {
        getCore().setEnchantingEggNode(this);
    }
}
