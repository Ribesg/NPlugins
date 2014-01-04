/***************************************************************************
 * Project file:    NPlugins - NCuboid - Flag.java                         *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Flag                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

public enum Flag {
	BOOSTER,
	BUILD,
	CHAT,
	CHEST,
	CLOSED,
	CREATIVE,
	DROP,
	ENDERMANGRIEF,
	EXPLOSION_BLOCK,
	EXPLOSION_PLAYER,
	EXPLOSION_ITEM,
	FARM,
	FEED,
	FIRE,
	GOD,
	HEAL,
	HIDDEN,
	INVISIBLE,
	JAIL,
	MOB,
	PASS,
	PERMANENT,
	PICKUP,
	PVP,
	SNOW,
	TELEPORT,
	USE,
	WARPGATE;

	public static Flag get(final String val) {
		final String in = val.toUpperCase();
		Flag f;
		try {
			f = Flag.valueOf(in);
		} catch (final IllegalArgumentException e) {
			switch (in) {
				case "BOOST":
					f = BOOSTER;
					break;
				case "SPEAK":
					f = CHAT;
					break;
				case "EXPLOSIONBLOCK":
				case "EXP_BLOCK":
				case "EXPBLOCK":
					f = EXPLOSION_BLOCK;
					break;
				case "EXPLOSIONPLAYER":
				case "EXP_PLAYER":
				case "EXPPLAYER":
					f = EXPLOSION_PLAYER;
					break;
				case "EXPLOSIONITEM":
				case "EXP_ITEM":
				case "EXPITEM":
					f = EXPLOSION_ITEM;
					break;
				case "ENDERMAN":
					f = ENDERMANGRIEF;
					break;
				case "HIDE":
					f = HIDDEN;
					break;
				case "PERM":
					f = PERMANENT;
					break;
				case "TEL":
					f = TELEPORT;
					break;
				case "WG":
				case "WARP":
					f = WARPGATE;
					break;
				default:
					f = null;
					break;
			}
		}
		return f;
	}
}
