/***************************************************************************
 * Project file:    NPlugins - NCore - TheEndAgainNode.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.node.theendagain.TheEndAgainNode*
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
