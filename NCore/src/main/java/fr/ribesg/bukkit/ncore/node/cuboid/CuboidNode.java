/***************************************************************************
 * Project file:    NPlugins - NCore - CuboidNode.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node.cuboid;

import java.util.List;

/**
 * API for the NCuboid plugin.
 * Want something to be added here? Just ask me!
 *
 * @author Ribesg
 */
public interface CuboidNode {

	public boolean isJailed(String playerName);

	public boolean jail(String playerName, String jailName);

	public boolean unJail(String playerName);

	public List<String> getJailList();
}
