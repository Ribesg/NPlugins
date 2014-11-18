/***************************************************************************
 * Project file:    NPlugins - NCore - InfoCommandHandler.java             *
 * Full Class name: fr.ribesg.bukkit.ncore.info.InfoCommandHandler         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.info;

import org.bukkit.command.CommandSender;

public interface InfoCommandHandler {

    /**
     * Used for /info command.
     *
     * @param sender     the command sender
     * @param query      the query of the info command
     * @param infoObject the Info object
     */
    public void populateInfo(final CommandSender sender, final String query, final Info infoObject);
}
