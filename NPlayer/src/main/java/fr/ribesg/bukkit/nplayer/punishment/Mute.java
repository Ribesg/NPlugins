/***************************************************************************
 * Project file:    NPlugins - NPlayer - Mute.java                         *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.Mute               *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;
public class Mute extends Punishment {

	public Mute(String muted, String reason) {
		super(muted, PunishmentType.MUTE, reason);
	}

	public Mute(String muted, String reason, long endDate) {
		super(muted, PunishmentType.MUTE, reason, endDate);
	}
}
