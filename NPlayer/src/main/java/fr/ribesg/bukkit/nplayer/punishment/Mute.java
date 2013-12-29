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

	public Mute(final String muted, final String reason) {
		super(muted, PunishmentType.MUTE, reason);
	}

	public Mute(final String muted, final String reason, final long endDate) {
		super(muted, PunishmentType.MUTE, reason, endDate);
	}
}
