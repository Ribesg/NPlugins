package fr.ribesg.bukkit.ncore.node.cuboid;

import fr.ribesg.bukkit.ncore.node.NPlugin;

/**
 * Represents the NCuboid plugin
 *
 * @author Ribesg
 */
public abstract class CuboidNode extends NPlugin {

    /** @see fr.ribesg.bukkit.ncore.node.NPlugin#linkCore() */
    @Override
    protected void linkCore() {
        getCore().setCuboidNode(this);
    }
}
