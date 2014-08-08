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
import java.util.UUID;

import org.bukkit.entity.Player;

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
     * @param id the Player's UUID
     *
     * @return true if the Player is jailed, false otherwise
     */
    public boolean isJailed(final UUID id);

    /**
     * Jails the Player if he's not jailed and if the provided jail name
     * exists.
     *
     * @param id       the Player's UUID
     * @param jailName the Jail name
     *
     * @return true if the Player can be, and is now considered, jailed
     */
    public boolean jail(final UUID id, final String jailName);

    /**
     * Unjails a Player
     *
     * @param id the Player's UUID
     *
     * @return true if the Player was jailed
     */
    public boolean unJail(final UUID id);

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

    /**
     * Checks if a Player is in a Region with INVISIBLE Flag.
     *
     * @param player the player
     *
     * @return true if the Player is in a Region with INVISIBLE Flag,
     * false otherwise
     */
    public boolean isInInvisibleRegion(final Player player);
}
