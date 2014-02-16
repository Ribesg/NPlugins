/***************************************************************************
 * Project file:    NPlugins - NPlayer - Ban.java                          *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.Ban                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;
public class Ban extends Punishment {

	public Ban(final String banned, final String reason) {
		super(banned, PunishmentType.BAN, reason);
	}

	public Ban(final String punished, final String reason, final long endDate) {
		super(punished, PunishmentType.BAN, reason, endDate);
	}

	@Override
	public String toString() {
		return "Ban{" +
		       "punished='" + punished + '\'' +
		       ", type=" + type +
		       ", endDate=" + endDate +
		       ", reason='" + reason + '\'' +
		       '}';
	}
}
