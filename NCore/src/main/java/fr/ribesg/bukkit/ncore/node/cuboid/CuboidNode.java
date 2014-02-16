/***************************************************************************
 * Project file:    NPlugins - NCore - CuboidNode.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node.cuboid;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.node.Node;

import java.util.Set;

/**
 * API for the NCuboid plugin.
 * Want something to be added here? Just ask me!
 *
 * @author Ribesg
 */
public interface CuboidNode extends Node {

	/**
	 * Checks if a Player is jailed. Not case sensitive.
	 *
	 * @param playerName the Player's name
	 *
	 * @return true if the Player is jailed, false otherwise
	 */
	public boolean isJailed(String playerName);

	/**
	 * Jails the Player if he's not jailed and if the provided jail name
	 * exists.
	 *
	 * @param playerName the Player's name
	 * @param jailName   the Jail name
	 *
	 * @return true if the Player can be, and is now considered, jailed
	 */
	public boolean jail(String playerName, String jailName);

	/**
	 * Unjails a Player
	 *
	 * @param playerName the Player's name
	 *
	 * @return true if the Player was jailed
	 */
	public boolean unJail(String playerName);

	/**
	 * Gets a set of all existing jails.
	 *
	 * @return a set of all existing jails
	 */
	public Set<String> getJailsSet();

	/**
	 * Gets the Location of a Jail.
	 *
	 * @param jailName the Jail name
	 *
	 * @return the Location of the jail, or null if it does not exist
	 */
	public NLocation getJailLocation(final String jailName);
}
