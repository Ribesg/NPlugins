package fr.ribesg.bukkit.ncore.nodes.cuboid;

import fr.ribesg.bukkit.ncore.nodes.NPlugin;

/**
 * Represents the NCuboid plugin
 * 
 * @author Ribesg
 */
public abstract class CuboidNode extends NPlugin {

    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#linkCore()
     */
    @Override
    protected void linkCore() {
        getCore().setCuboidNode(this);
    }
}
