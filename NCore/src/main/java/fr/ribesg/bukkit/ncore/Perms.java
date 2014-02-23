/***************************************************************************
 * Project file:    NPlugins - NCore - Perms.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.Perms                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import org.bukkit.entity.Player;

public class Perms {
        private static final String UPDATER_NOTICE                 = "ncore.updater.notice";
    
        public static boolean hasUpdateNotice(final Player player) {
		return player.isOp() || player.hasPermission(UPDATER_NOTICE);
	}
}
