/***************************************************************************
 * Project file:    NPlugins - NCore - DateUtil.java                       *
 * Full Class name: fr.ribesg.bukkit.ncore.util.DateUtil                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;
import java.util.Date;

/** @author Ribesg */
public class DateUtil {

	public static Date now() {
		return new Date();
	}

	public static String formatDate(final Date date) {
		return String.format("%tFT%<tRZ", date).replace(':', 'h');
	}

	public static String formatDate(final long date) {
		return formatDate(new Date(date));
	}

	public static String formatNow() {
		return formatDate(now());
	}
}
