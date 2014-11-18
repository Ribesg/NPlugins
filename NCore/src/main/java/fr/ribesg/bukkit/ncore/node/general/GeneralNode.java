/***************************************************************************
 * Project file:    NPlugins - NCore - GeneralNode.java                    *
 * Full Class name: fr.ribesg.bukkit.ncore.node.general.GeneralNode        *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.node.general;

import fr.ribesg.bukkit.ncore.info.InfoCommandHandler;
import fr.ribesg.bukkit.ncore.node.Node;

import java.util.UUID;

/**
 * API for the NGeneral plugin.
 * Want something to be added here? Just ask me!
 *
 * @author Ribesg
 */
public interface GeneralNode extends Node, InfoCommandHandler {

    /**
     * Checks if a Player is in Spy Mode.
     *
     * @param playerId the player's UUID
     *
     * @return true if the Player is in Spy Mode, false otherwise
     */
    public boolean isSpy(final UUID playerId);
}
