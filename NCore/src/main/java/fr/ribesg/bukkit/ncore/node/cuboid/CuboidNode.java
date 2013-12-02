/***************************************************************************
 * Project file:    NPlugins - NCore - CuboidNode.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node.cuboid;

import fr.ribesg.bukkit.ncore.node.NPlugin;

import java.util.List;

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

	public abstract boolean isJailed(String playerName);

	public abstract boolean jail(String playerName, String jailName);

	public abstract boolean unJail(String playerName);

	public abstract List<String> getJailList();
}
