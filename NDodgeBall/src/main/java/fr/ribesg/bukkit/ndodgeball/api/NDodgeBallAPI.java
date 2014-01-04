/***************************************************************************
 * Project file:    NPlugins - NPlugins - NDodgeBallAPI.java               *
 * Full Class name: fr.ribesg.bukkit.ndodgeball.api.NDodgeBallAPI          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ndodgeball.api;

import fr.ribesg.bukkit.ncore.node.dodgeball.DodgeBallNode;
import fr.ribesg.bukkit.ndodgeball.NDodgeBall;

public class NDodgeBallAPI extends DodgeBallNode {

	private final NDodgeBall plugin;

	public NDodgeBallAPI(final NDodgeBall instance) {
		plugin = instance;
	}
}
