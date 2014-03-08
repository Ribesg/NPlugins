/***************************************************************************
 * Project file:    NPlugins - NCuboid - Attribute.java                    *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Attribute               *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

public enum Attribute {

	// String
	FAREWELL_MESSAGE,
	WELCOME_MESSAGE,

	// Integer
	EXPLOSION_BLOCK_DROP,
	FEED_AMOUNT,
	FEED_MAX_FOOD,
	FEED_MIN_FOOD,
	FEED_TIMER,
	HEAL_AMOUNT,
	HEAL_MAX_HEALTH,
	HEAL_MIN_HEALTH,
	HEAL_TIMER,

	// Location
	EXTERNAL_POINT,
	INTERNAL_POINT,

	// Vector
	BOOSTER_VECTOR;

	public static Attribute get(final String val) {
		final String in = val.toUpperCase();
		Attribute fa;
		try {
			fa = Attribute.valueOf(in);
		} catch (final IllegalArgumentException e) {
			switch (in) {
				case "WM":
				case "W_M":
				case "WMES":
				case "W_MES":
				case "WMESS":
				case "W_MESS":
				case "WELCOMEMES":
				case "WELCOME_MES":
				case "WELCOMEMESS":
				case "WELCOME_MESS":
				case "WELCOMEMESSAGE":
					fa = WELCOME_MESSAGE;
					break;
				case "FM":
				case "F_M":
				case "FMES":
				case "F_MES":
				case "FMESS":
				case "F_MESS":
				case "FAREWELLMES":
				case "FAREWELL_MES":
				case "FAREWELLMESS":
				case "FAREWELL_MESS":
				case "FAREWELLMESSAGE":
					fa = FAREWELL_MESSAGE;
					break;
				case "HA":
				case "HAMOUNT":
				case "H_AMOUNT":
					fa = HEAL_AMOUNT;
					break;
				case "HT":
				case "HTIMER":
				case "H_TIMER":
					fa = HEAL_TIMER;
					break;
				case "HMIN":
				case "HEALMIN":
					fa = HEAL_MIN_HEALTH;
					break;
				case "HMAX":
				case "HEALMAX":
					fa = HEAL_MAX_HEALTH;
					break;
				case "FA":
				case "FAMOUNT":
				case "F_AMOUNT":
					fa = FEED_AMOUNT;
					break;
				case "FT":
				case "FTIMER":
				case "F_TIMER":
					fa = FEED_TIMER;
					break;
				case "FMIN":
				case "FEEDMIN":
					fa = FEED_MIN_FOOD;
					break;
				case "FMAX":
				case "FEEDMAX":
					fa = FEED_MAX_FOOD;
					break;
				case "EXPLOSION_DROP":
				case "EXPLOSIONDROP":
				case "EXP_BLOCK_DROP":
				case "EXPBLOCKDROP":
				case "EXP_DROP":
				case "EXPDROP":
					fa = EXPLOSION_BLOCK_DROP;
					break;
				case "EP":
				case "EXTERNALPOINT":
				case "EXTPOINT":
				case "EXT_POINT":
					fa = EXTERNAL_POINT;
					break;
				case "IP":
				case "INTERNALPOINT":
				case "INTPOINT":
				case "INT_POINT":
					fa = INTERNAL_POINT;
					break;
				case "BV":
				case "BVECTOR":
				case "B_VECTOR":
				case "BOOSTERVECTOR":
					fa = BOOSTER_VECTOR;
					break;
				default:
					fa = null;
					break;
			}
		}

		return fa;
	}

	public static boolean isStringAttribute(final Attribute att) {
		return att != null && FAREWELL_MESSAGE.compareTo(att) <= 0 && WELCOME_MESSAGE.compareTo(att) >= 0;
	}

	public static boolean isIntegerAttribute(final Attribute att) {
		return att != null && EXPLOSION_BLOCK_DROP.compareTo(att) <= 0 && HEAL_TIMER.compareTo(att) >= 0;
	}

	public static boolean isLocationAttribute(final Attribute att) {
		return att != null && EXTERNAL_POINT.compareTo(att) <= 0 && INTERNAL_POINT.compareTo(att) >= 0;
	}

	public static boolean isVectorAttribute(final Attribute att) {
		return att != null && att == BOOSTER_VECTOR;
	}
}
