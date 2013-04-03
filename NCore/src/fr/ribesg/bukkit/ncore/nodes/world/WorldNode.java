package fr.ribesg.bukkit.ncore.nodes.world;

import fr.ribesg.bukkit.ncore.nodes.NPlugin;

/**
 * Represents the NWorld plugin
 * 
 * @author Ribesg
 */
public abstract class WorldNode extends NPlugin {

    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#linkCore()
     */
    @Override
    protected void linkCore() {
        getCore().setWorldNode(this);
    }
}
