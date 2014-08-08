/***************************************************************************
 * Project file:    NPlugins - NPlayer - LoginRegisterFilter.java          *
 * Full Class name: fr.ribesg.bukkit.nplayer.LoginRegisterFilter           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.common.logging.DenyFilter;

/**
 * @author Ribesg
 */
public class LoginRegisterFilter implements DenyFilter {

	@Override
	public boolean denies(final String message) {
		return message != null && (message.contains(" issued server command: /login") || message.contains(" issued server command: /register"));
	}
}
