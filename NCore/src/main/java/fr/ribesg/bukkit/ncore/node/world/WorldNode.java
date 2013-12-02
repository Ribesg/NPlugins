/***************************************************************************
 * Project file:    NPlugins - NCore - WorldNode.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.node.world.WorldNode            *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node.world;

import fr.ribesg.bukkit.ncore.node.NPlugin;

/**
 * Represents the NWorld plugin
 *
 * @author Ribesg
 */
public abstract class WorldNode extends NPlugin {

	/** @see fr.ribesg.bukkit.ncore.node.NPlugin#linkCore() */
	@Override
	protected void linkCore() {
		getCore().setWorldNode(this);
	}
}
