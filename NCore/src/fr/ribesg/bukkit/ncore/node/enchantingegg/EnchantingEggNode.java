package fr.ribesg.bukkit.ncore.node.enchantingegg;

import fr.ribesg.bukkit.ncore.node.NPlugin;

/**
 * Represents the NEnchantingEgg plugin
 *
 * @author Ribesg
 */
public abstract class EnchantingEggNode extends NPlugin {

    /** @see fr.ribesg.bukkit.ncore.node.NPlugin#linkCore() */
    @Override
    protected void linkCore() {
        getCore().setEnchantingEggNode(this);
    }
}
