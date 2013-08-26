package fr.ribesg.bukkit.ncore.node.theendagain;

import fr.ribesg.bukkit.ncore.node.NPlugin;

/**
 * Represents the NTheEngAgain plugin
 *
 * @author Ribesg
 */
public abstract class TheEndAgainNode extends NPlugin {

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#linkCore() */
	@Override
	protected void linkCore() {
		getCore().setTheEndAgainNode(this);
	}

}
