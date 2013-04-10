package fr.ribesg.bukkit.ncore.nodes.theendagain;

import fr.ribesg.bukkit.ncore.nodes.NPlugin;

/**
 * Represents the NTheEngAgain plugin
 * 
 * @author Ribesg
 */
public abstract class TheEndAgainNode extends NPlugin {

    /**
     * @see fr.ribesg.bukkit.ncore.nodes.NPlugin#linkCore()
     */
    @Override
    protected void linkCore() {
        getCore().setTheEndAgainNode(this);
    }

}
