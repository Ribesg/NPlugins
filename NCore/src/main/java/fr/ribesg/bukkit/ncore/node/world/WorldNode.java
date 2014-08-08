/***************************************************************************
 * Project file:    NPlugins - NCore - WorldNode.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.node.world.WorldNode            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node.world;

import fr.ribesg.bukkit.ncore.node.Node;

import org.bukkit.Location;

/**
 * API for the NWorld plugin.
 * Want something to be added here? Just ask me!
 *
 * @author Ribesg
 */
public interface WorldNode extends Node {

    /**
     * Gets the Location of the provided world's spawn point.
     *
     * @param worldName the world name
     *
     * @return the spawn location or null if no world found
     */
    public Location getWorldSpawnLocation(final String worldName);
}
